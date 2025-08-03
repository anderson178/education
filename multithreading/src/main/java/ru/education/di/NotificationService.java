package ru.education.di;

@Component
public class NotificationService {

    @Autowired
    private UserRepository userRepository; // будет внедрён

    public void notifyUser(String userId) {
       userRepository.findEmailById(userId);
    }
}
