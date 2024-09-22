### Основные паттерны

#### 1. Interface
Интерфейс определяет контракт, который должен соблюдаться классами, его реализующими.

```java
// Пример интерфейса
public interface Animal {
    void makeSound();
}

// Реализация интерфейса
public class Dog implements Animal {
    @Override
    public void makeSound() {
        System.out.println("Bark");
    }
}

public class Cat implements Animal {
    @Override
    public void makeSound() {
        System.out.println("Meow");
    }
}
```

#### 2. Marker Interface
Маркерный интерфейс используется для сигнализации компилятору или среде выполнения о том, что класс или объект должны обрабатывать особым образом.

```java
// Пример маркерного интерфейса
public interface Serializable {
}

// Класс, реализующий маркерный интерфейс
public class User implements Serializable {
    private String name;

    // Конструктор, геттеры и сеттеры
}
```

#### 3. Delegation
Делегирование — это паттерн, при котором один объект передает выполнение части своей работы другому объекту (делегату).

```java
// Делегатор
public class Printer {
    public void print(String message) {
        System.out.println(message);
    }
}

// Класс, который делегирует печать
public class Message {
    private Printer printer = new Printer();

    public void printMessage(String message) {
        printer.print(message);
    }
}
```

---

### Порождающие паттерны

#### 1. Factory Method
Паттерн предоставляет интерфейс для создания объектов, оставляя поддержку подклассам для того, чтобы решать, какой класс инстанцировать.

```java
// Продукт
public interface Product {
    void doSomething();
}

// Конкретные продукты
public class ConcreteProductA implements Product {
    @Override
    public void doSomething() {
        System.out.println("Product A");
    }
}

public class ConcreteProductB implements Product {
    @Override
    public void doSomething() {
        System.out.println("Product B");
    }
}

// Фабрика
public abstract class Creator {
    public abstract Product factoryMethod();
}

public class CreatorA extends Creator {
    @Override
    public Product factoryMethod() {
        return new ConcreteProductA();
    }
}

public class CreatorB extends Creator {
    @Override
    public Product factoryMethod() {
        return new ConcreteProductB();
    }
}
```

#### 2. Singleton
Обеспечивает наличие только одного экземпляра класса и предоставляет глобальную точку доступа к нему.

```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

#### 3. Prototype
Позволяет создавать новые объекты путем клонирования существующих.

```java
public abstract class Prototype implements Cloneable {
    public abstract Prototype clone();
}

public class ConcretePrototype extends Prototype {
    @Override
    public Prototype clone() {
        try {

System.out.println("Loading " + filename);
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

class ProxyImage implements Image {
    private RealImage realImage;
    private String filename;

    public ProxyImage(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}
```

---

### Поведенческие паттерны

#### 1. Command
Инкапсулирует запрос как объект, позволяя параметризовать клиенты с различными запросами.

```java
interface Command {
    void execute();
}

class Light {
    public void turnOn() {
        System.out.println("Light is ON");
    }
    public void turnOff() {
        System.out.println("Light is OFF");
    }
}

class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}

class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOff();
    }
}

class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }
    
    public void pressButton() {
        command.execute();
    }
}
```

#### 2. Observer (наблюдатель) - (издатель — подписчик)
Определяет зависимость "один ко многим" между объектами, так что при изменении состояния одного объекта все его зависимые уведомляются.

```java
import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String message);
}

class ConcreteObserver implements Observer {
    private String name;

    public ConcreteObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " received message: " + message);
    }
}

class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
```

Это примеры некоторых из основных, порождающих, структурных и поведенческих паттернов проектирования на Java. Эти паттерны помогают улучшить структуру кода, увеличить его переиспользуемость и читаемость.