package ru.education.memory_leak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MemoryLeak {


    public static void main(String[] args) {
        SpringApplication.run(MemoryLeak.class, args);

    }
}
