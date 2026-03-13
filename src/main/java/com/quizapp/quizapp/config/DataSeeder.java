package com.quizapp.quizapp.config;

import com.quizapp.quizapp.model.Role;
import com.quizapp.quizapp.model.User;
import com.quizapp.quizapp.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User teacher = new User("Teacher One", "teacher@example.com", "pass", Role.TEACHER);
            User student = new User("Student One", "student@example.com", "pass", Role.STUDENT);
            userRepository.save(teacher);
            userRepository.save(student);
        }
    }
}
