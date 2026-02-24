package ru.practice.mth.chapter1;

public class MyThreadTest extends Thread {
    private final String name;

    public MyThreadTest(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " " + name + " " + i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Main {
    public static void main(String[] args) {
        MyThreadTest t1 = new MyThreadTest("Alpha");
        MyThreadTest t2 = new MyThreadTest("Beta");
        t1.start();
        t2.start();
    }
}
