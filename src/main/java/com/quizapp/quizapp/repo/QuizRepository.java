package com.quizapp.quizapp.repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizapp.quizapp.model.Quiz;
import com.quizapp.quizapp.model.User;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCreatedBy(User user);
}
