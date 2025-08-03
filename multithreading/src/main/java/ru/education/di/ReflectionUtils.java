package ru.education.di;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtils {
    public static Set<Class<?>> findAnnotatedClasses(String packageName, Class<?> annotationClass) {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.getFile());
                scanDirectory(file, packageName, annotationClass, classes);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    private static void scanDirectory(File directory, String packageName,
                                      Class<?> annotationClass, Set<Class<?>> classes)
            throws ClassNotFoundException {
        if (!directory.exists()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), annotationClass, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent((Class<? extends Annotation>) annotationClass)) {
                    classes.add(clazz);
                }
            }
        }
    }
}
