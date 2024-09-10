package ru.education.memory_leak;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/api")
    public void test() {
        Leak leak = new Leak();
        leak.addNumber();
    }
}
