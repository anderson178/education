package ru.education.solid.isp;

public class BeforeISP {
    interface Animal {
        void eat();
        void fly();
    }

    static class Dog implements Animal {
        @Override
        public void eat() {
            System.out.println("Dog eats");
        }

        @Override
        public void fly() {
            throw new UnsupportedOperationException("Dog can't fly!");
        }
    }
}
