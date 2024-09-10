package ru.education.memory_leak;

import java.util.ArrayList;
import java.util.List;

public class Leak {
    private static final List<Double> list = new ArrayList<>();


    public void addNumber() {
        for (int i = 0; i < 5_000_000; i++) {
            list.add(Math.random());
        }
    }
}
