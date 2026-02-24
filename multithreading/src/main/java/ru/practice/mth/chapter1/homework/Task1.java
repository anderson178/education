package ru.practice.mth.chapter1.homework;

public class Task1 {

    static class MyThread extends Thread {
        private final String name;

        MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            commonMethod(name);
        }
    }

    static class MyThreadRunnable implements Runnable {
        private final String name;

        MyThreadRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            commonMethod(name);
        }
    }

    private static void commonMethod(String name) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Привет от " + name + "! Итерация " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyThreadRunnable("T1"));
        Thread t2 = new Thread(() -> commonMethod(Thread.currentThread().getName()), "T2");
        Thread t3 = new MyThread("T3");
        t1.start();
        t2.start();
        t3.start();
    }
}
