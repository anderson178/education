package ru.education.threads.stepik;

public class SimpleThreads {
    public static void main(String[] args) throws InterruptedException {
        Runnable task1 =()-> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        };

        Runnable task2 =()-> {
            for (int i = 10; i < 20; i++) {
                System.out.println(i);
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();


        main2(args);
    }

    public static void print() {
        int i = 5;
        System.out.println(i);
        i += 10;
        System.out.println(i);
    }

    public static void main2(String[] args) throws InterruptedException {
        Thread t1 = new Thread(SimpleThreads::print);
        Thread t2 = new Thread(SimpleThreads::print);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
