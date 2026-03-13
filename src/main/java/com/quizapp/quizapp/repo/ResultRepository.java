package com.quizapp.quizapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizapp.quizapp.model.StudentQuizResult;

public interface ResultRepository extends JpaRepository<StudentQuizResult, Long> {

    // ✅ Find results for logged-in student
    List<StudentQuizResult> findByStudentId(Long studentId);
}


