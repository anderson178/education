package ru.education.threads.stepik;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        Runnable task = () -> {
            int result = 12*15;
            System.out.println(result);
        };

        executor.submit(task);
    }
}
