package ru.education.di;

@Component
public class UserRepository {
    public String findEmailById(String id) {
        return "user" + id + "@example.com";
    }
}
