## Общие сведения:
В Java аннотации (annotations) — это метаданные, которые могут быть добавлены к коду для предоставления дополнительной информации о классах, методах, полях и других элементах. Аннотации могут быть обработаны на этапе компиляции или выполнения программы.

### Обработка аннотаций в runtime
Через reflection
```java
import java.lang.annotation.*;

// Определяем аннотацию с RetentionPolicy.RUNTIME
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAnnotation {
    String value() default "Default Value";
    int priority() default 1;
}
```

### Обработка аннотаций на этапе compile time.

Для обработки аннотаций на этапе компиляции можно использовать Java Compiler API или создавать custom annotation processors с помощью APT (Annotation Processing Tool). Это позволяет генерировать код или выполнять проверки на основе аннотаций.

Сгенерированный код не попадает в байт-код, то есть он существует только во время компиляции

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Определяем аннотацию с RetentionPolicy.SOURCE - значит, не сохраняется в .class файле.
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Loggable {
    String message() default "Executing method";
}
```

### Обработка аннотаций создание исходников
На этапе компиляции через аннотации генерируется код как отдельный java-файл попадая в исходный код

Пример создания исходника для dto с добавлением методов get/set

Аннотация
```java
// src/main/java/com/dto/annotation/GenerateAccessors.java
package com.dto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) // нужна только на этапе компиляции
@Target(ElementType.TYPE)         // применяется к классам
public @interface GenerateAccessors {
}
```

Dto класс
```java
// src/main/java/com/dto/model/UserDto.java
package com.dto.model;

import com.dto.annotation.GenerateAccessors;

@GenerateAccessors
public class UserDto {
    private String name;
    private int age;
    private String email;
}
```

Annotation Processor (генерация геттеров и сеттеров)
```java
// src/main/java/com/dto/processor/AccessorsProcessor.java
package com.dto.processor;

import com.dto.annotation.GenerateAccessors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("com.dto.annotation.GenerateAccessors")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AccessorsProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateAccessors.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        "@GenerateAccessors can only be applied to classes", element);
                continue;
            }

            TypeElement typeElement = (TypeElement) element;
            try {
                generateAccessorClass(typeElement);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        "Failed to generate accessors: " + e.getMessage(), element);
            }
        }
        return true; // "consume" аннотацию
    }

    private void generateAccessorClass(TypeElement classElement) throws IOException {
        String className = classElement.getSimpleName().toString();
        String packageName = elementUtils.getPackageOf(classElement).getQualifiedName().toString();

        // Генерируем тот же класс, но с геттерами и сеттерами
        JavaFileObject fileObject = processingEnv.getFiler()
                .createSourceFile(packageName + "." + className);

        try (PrintWriter out = new PrintWriter(fileObject.openWriter())) {
            // Пишем пакет
            if (!packageName.isEmpty()) {
                out.println("package " + packageName + ";");
                out.println();
            }

            // Начинаем класс
            out.println("public class " + className + " {");

            // Обрабатываем все поля
            for (VariableElement field : getFields(classElement)) {
                String fieldType = field.asType().toString();
                String fieldName = field.getSimpleName().toString();
                String capitalizedFieldName = capitalize(fieldName);

                // Поле
                out.println("    private " + fieldType + " " + fieldName + ";");

                // Геттер
                out.println();
                out.println("    public " + fieldType + " get" + capitalizedFieldName + "() {");
                out.println("        return this." + fieldName + ";");
                out.println("    }");

                // Сеттер
                out.println();
                out.println("    public void set" + capitalizedFieldName + "(" + fieldType + " " + fieldName + ") {");
                out.println("        this." + fieldName + " = " + fieldName + ";");
                out.println("    }");
            }

            // Закрываем класс
            out.println("}");
        }

        messager.printMessage(Diagnostic.Kind.NOTE,
                "Generated accessors for: " + className);
    }

    private Iterable<VariableElement> getFields(TypeElement typeElement) {
        return typeElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(VariableElement.class::cast)
                .toList();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
```


