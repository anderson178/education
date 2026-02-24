package ru.practice.mth.chapter1.homework;

public class Task2 {

    public static class UnsafeCounter {
        private int count = 0;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    public static void main(String[] args) {
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                unsafeCounter.increment();
                System.out.println("Current value - " + unsafeCounter.getCount() + "Thread " + Thread.currentThread().getName());
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    unsafeCounter.increment();
                    Thread.sleep(50);
                    System.out.println("Current value - " + unsafeCounter.getCount() + "Thread " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t1.start();
        t2.start();
    }
}
