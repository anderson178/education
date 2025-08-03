### Reentrant Lock (Реентерабельная блокировка)

ReentrantLock — это явная блокировка из java.util.concurrent.locks, которая позволяет потоку повторно захватывать ту же блокировку, если он уже её держит.

Когда использовать?
- Когда нужна гибкость, которой нет у synchronized (таймауты, попытки, условия)
- Для реализации справедливой блокировки (fair locking)
- Когда нужна попытка захвата с таймаутом


```java
public class Counter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock(); // захватываем блокировку
        try {
            count++;
            log("Incremented to " + count);
            // Можно вызвать другой метод, использующий ту же блокировку
            internalWork();
        } finally {
            lock.unlock(); // обязательно в finally!
        }
    }

    private void internalWork() {
        lock.lock(); // ✅ Можно — это тот же поток
        try {
            log("Doing internal work...");
        } finally {
            lock.unlock();
        }
    }

    private void log(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
```

### Pessimistic Lock (Пессимистическая блокировка)

Блокировка, при которой ресурс блокируется заранее, на всё время использования. Предполагается, что конфликты возможны — поэтому лучше перестраховаться.

Где используется?
- В базах данных (например, SELECT ... FOR UPDATE)
- В многопоточных приложениях с высокой конкуренцией
- Когда стоимость конфликта высока

Плюсы:
- Гарантирует отсутствие конфликтов
- Простота логики

Минусы:
- Может привести к низкой параллельности
 -Риск взаимоблокировок (deadlock)

```java
synchronized (account) {
// Работаем с аккаунтом — никто другой не может войти
updateBalance(account, amount);
}
```

### Optimistic Lock (Оптимистическая блокировка)

Не блокирует ресурс заранее. Вместо этого — проверяет, не изменился ли он с момента чтения. Если изменился — операция откатывается.
К примеру у hibernate есть аннотация @Version, которая при update будет проверять это поле, если версия изменилась, то откат иначе апдейт

Когда использовать?
- Когда конфликты редки
- Для высокой производительности
- В веб-приложениях (например, редактирование профиля)

Плюсы:
- Высокая параллельность
- Нет блокировок

Минусы:
- Возможны повторные попытки (retry)
- Нужно обрабатывать OptimisticLockException

```java
public class OptimisticCounter {
    private volatile int value = 0;
    private volatile int version = 0;

    public boolean updateIfUnchanged(int expectedVersion, int newValue) {
        synchronized (this) {
            if (version == expectedVersion) {
                value = newValue;
                version++;
                return true;
            }
            return false;
        }
    }

    public int getValue() {
        return value;
    }

    public int getVersion() {
        return version;
    }
}
```

```java
OptimisticCounter counter = new OptimisticCounter();

// Поток 1
int ver = counter.getVersion();
int newVal = counter.getValue() + 10;

// Делаем что-то долгое...
if (!counter.updateIfUnchanged(ver, newVal)) {
    System.out.println("Conflict! Someone else updated the counter.");
}
```
