package com.quizapp.quizapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizapp.quizapp.model.StudentQuizResult;

public interface StudentQuizResultRepository extends JpaRepository<StudentQuizResult, Long> {

    // ✅ Fetch all results for quizzes created by this teacher
    List<StudentQuizResult> findByQuiz_CreatedBy_Id(Long teacherId);
}
