package com.quizapp.quizapp.service;

import com.quizapp.quizapp.model.Role;
import com.quizapp.quizapp.model.User;
import com.quizapp.quizapp.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) { this.repo = repo; }

    public User register(String name, String email, String password, Role role) {
        return repo.save(new User(name, email, password, role));
    }

    public User get(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Object all() { return repo.findAll(); }
}

