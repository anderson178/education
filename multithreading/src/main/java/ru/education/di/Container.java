package ru.education.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Container {
    private final Map<String, Object> beans = new ConcurrentHashMap<>();
    private final Map<String, Class<?>> beanClasses = new ConcurrentHashMap<>();

    /**
     * Сканируем пакет и регистрируем все @Component
     */
    public void scan(String packageName) throws Exception {
        Set<Class<?>> componentClasses = ReflectionUtils.findAnnotatedClasses(packageName, Component.class);
        for (Class<?> clazz : componentClasses) {
            String beanName = extractBeanName(clazz);
            beanClasses.put(beanName, clazz);
            // Пока не создаём экземпляр — ленивая инициализация
        }

        // Создаём бины (в правильном порядке, если нужно)
        for (String beanName : beanClasses.keySet()) {
            getBean(beanName); // lazy init + inject
        }
    }

    private String extractBeanName(Class<?> clazz) {
        Component annotation = clazz.getAnnotation(Component.class);
        String value = annotation.value();
        return value.isEmpty() ? toLowerFirstChar(clazz.getSimpleName()) : value;
    }

    private String toLowerFirstChar(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * Получить бин по имени (создаёт, если ещё не создан)
     */
    public <T> T getBean(String name) {
        if (beans.containsKey(name)) {
            return (T) beans.get(name);
        }

        Class<?> clazz = beanClasses.get(name);
        if (clazz == null) {
            throw new RuntimeException("No bean named '" + name + "' found");
        }

        try {
            Object instance = createBean(clazz);
            beans.put(name, instance);
            return (T) instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean: " + name, e);
        }
    }

    /**
     * Создаёт экземпляр и внедряет зависимости
     */
    private Object createBean(Class<?> clazz) throws Exception {
        Constructor<?> constructor = findAutowiredConstructor(clazz);
        Object instance;

        if (constructor != null) {
            // Внедряем через конструктор
            Object[] args = Arrays.stream(constructor.getParameterTypes())
                    .map(paramType -> findBeanByType(paramType))
                    .toArray();
            instance = constructor.newInstance(args);
        } else {
            // Без конструктора — дефолтный конструктор
            instance = clazz.getDeclaredConstructor().newInstance();
        }

        // Внедряем зависимости в поля
        injectFields(instance);

        return instance;
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            if (c.isAnnotationPresent(Autowired.class)) {
                return c;
            }
        }
        return null;
    }

    private void injectFields(Object instance) throws Exception {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Object dependency = findBeanByType(field.getType());
                field.set(instance, dependency);
            }
        }
    }

    private Object findBeanByType(Class<?> type) {
        // Простой поиск: по имени типа (в реальности может быть сложнее)
        return beans.values().stream()
                .filter(type::isInstance)
                .findFirst()
                .orElseGet(() -> {
                    // Если нет — попробуем создать
                    String beanName = toLowerFirstChar(type.getSimpleName());
                    if (beanClasses.containsKey(beanName)) {
                        try {
                            return getBean(beanName);
                        } catch (Exception e) {
                            throw new RuntimeException("Cannot create bean of type: " + type, e);
                        }
                    }
                    throw new RuntimeException("No qualifying bean of type: " + type);
                });
    }
}
