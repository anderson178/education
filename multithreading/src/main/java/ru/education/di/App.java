package ru.education.di;

public class App {
    public static void main(String[] args) throws Exception {
        Container container = new Container();
        container.scan("service");   // сканируем service
        container.scan("repository"); // и repository

        NotificationService ns = container.getBean("notificationService");
        ns.notifyUser("123");
    }
}
