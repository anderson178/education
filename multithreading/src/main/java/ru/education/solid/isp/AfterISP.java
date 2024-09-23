package ru.education.solid.isp;

public class AfterISP {
    interface Eatable {
        void eat();
    }

    interface Flyable {
        void fly();
    }

    static class Dog implements Eatable {
        @Override
        public void eat() {
            System.out.println("Dog eats");
        }
    }
}
