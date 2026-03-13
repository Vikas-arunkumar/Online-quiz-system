package com.quizapp.quizapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizapp.quizapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

