package ru.practice.mth.chapter1;

public class Task implements Runnable {
    private final String id;

    public Task(String id) { this.id = id; }

    @Override
    public void run() {
        System.out.println("Задача " + id + "выполняется в потоке " + Thread.currentThread().getName());
    }
}

class Main1 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Task("A"), "Worker-1");
        Thread t2 = new Thread(() -> System.out.println("Лямбда в " +
                Thread.currentThread().getName()), "Worker-2");
        t1.start();
        t2.start();
    }
}