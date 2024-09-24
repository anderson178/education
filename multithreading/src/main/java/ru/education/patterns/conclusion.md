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

### Структурные паттерны

#### 1. Фасад (Facade) - упрощает интерфейс сложной системы

Паттерн Фасад предоставляет упрощенный интерфейс для сложной подсистемы, скрывая ее сложность от клиента.

#### Пример:

Предположим, у нас есть система для работы с компьютером, состоящая из различных компонентов: процессора, жесткого диска и оперативной памяти.

```java
// Подсистемы
class CPU {
    public void freeze() { System.out.println("CPU is frozen."); }
    public void jump(long position) { System.out.println("Jump to: " + position); }
    public void execute() { System.out.println("CPU is executing."); }
}

class Memory {
    public void load(long position, byte[] data) { 
        System.out.println("Loading data into memory at position: " + position); 
    }
}

class HardDrive {
    public byte[] read(long lba, int size) { 
        System.out.println("Reading data from hard drive - LBA: " + lba + ", Size: " + size);
        return new byte[size]; // Возвращаем пустой массив
    }
}

// Фасад
class Computer {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;

    public Computer(CPU cpu, Memory memory, HardDrive hardDrive) {
        this.cpu = cpu;
        this.memory = memory;
        this.hardDrive = hardDrive;
    }

    public void startComputer() {
        cpu.freeze();
        memory.load(0, hardDrive.read(0, 1024));
        cpu.jump(0);
        cpu.execute();
    }
}

// Использование
public class FacadeExample {
    public static void main(String[] args) {
        CPU cpu = new CPU();
        Memory memory = new Memory();
        HardDrive hardDrive = new HardDrive();
        Computer computer = new Computer(cpu, memory, hardDrive);
        
        computer.startComputer();
    }
}
```

#### 2. Декоратор (Decorator) - добавляет новые функциональные возможности к объектам динамически

Паттерн Декоратор позволяет добавлять новые функциональные возможности объектам, не изменяя их структуру.

#### Пример:

Предположим, у нас есть интерфейс кофе, и мы хотим добавлять различные вкусы к кофе.

```java
// Компонент
interface Coffee {
    String getDescription();
    double cost();
}

// Конкретный компонент
class SimpleCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "Simple coffee";
    }

    @Override
    public double cost() {
        return 1.0;
    }
}

// Декоратор
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

// Конкретные декораторы
class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", milk";
    }

    @Override
    public double cost() {
        return coffee.cost() + 0.5;
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", sugar";
    }

    @Override
    public double cost() {
        return coffee.cost() + 0.2;
    }
}

// Использование
public class DecoratorExample {
    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " $" + coffee.cost());

        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " $" + coffee.cost());

        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " $" + coffee.cost());
    }
}
```

#### 3. Прокси (proxy) - управляет доступом к объектам, добавляя уровень контроля и защищенности

#### Основные компоненты Proxy паттерна

1. **Субъект** (Subject) — интерфейс или абстрактный класс, который определяет общие операции для проксируемого объекта и прокси.

2. **Конкретный Субъект** (Real Subject) — класс, реализующий интерфейс Subject и представляющий реальный объект, к которому будет осуществляться доступ через прокси.

3. **Прокси** (Proxy) — класс, реализующий интерфейс Subject и содержащий ссылку на объект Real Subject. Он может добавлять дополнительную функциональность (например, логику безопасности, управление доступом и так далее) перед или после обращения к реальному объекту.

### Пример внедрения паттерна Proxy

Давайте рассмотрим простой пример использования паттерна Proxy. Мы создадим интерфейс `Image`, который будет представлять изображения, и реализуем прокси для загружаемого изображения.

#### 1. Interface (Subject)

```java
// Subject
public interface Image {
    void display();
}
```

#### 2. Реальный Субъект (Real Subject)

```java
// Real Subject
class RealImage implements Image {
    private String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadImageFromDisk();
    }

    private void loadImageFromDisk() {
        System.out.println("Loading " + filename);
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}
```

#### 3. Прокси (Proxy)

```java
// Proxy
class ProxyImage implements Image {
    private RealImage realImage;
    private String filename;

    public ProxyImage(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        // Ленивая загрузка
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}
```

#### 4. Пример Использования

```java
public class ProxyPatternExample {
    public static void main(String[] args) {
        Image image1 = new ProxyImage("image1.jpg");
        Image image2 = new ProxyImage("image2.jpg");

        // Загрузка и отображение изображения
        image1.display(); // Загрузка произойдет здесь
        image1.display(); // Загрузка не произойдет, т.к. уже загружено

        // Загрузка и отображение другого изображения
        image2.display(); // Загрузка произойдет здесь
    }
}
```
