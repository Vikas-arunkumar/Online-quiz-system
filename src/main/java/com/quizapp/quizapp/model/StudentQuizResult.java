package com.quizapp.quizapp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class StudentQuizResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User student;

    @ManyToOne
    private Quiz quiz;

    private int totalQuestions;
    private int correctAnswers;
    private int scorePercent;
    private LocalDateTime submittedAt;

    @Column(length = 2000)
    private String details;

    public StudentQuizResult() {}

    public StudentQuizResult(User student, Quiz quiz, int total, int correct, int percent,
                             LocalDateTime submittedAt, String details) {
        this.student = student;
        this.quiz = quiz;
        this.totalQuestions = total;
        this.correctAnswers = correct;
        this.scorePercent = percent;
        this.submittedAt = submittedAt;
        this.details = details;
    }

    public Long getId() { return id; }
    public User getStudent() { return student; }
    public Quiz getQuiz() { return quiz; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getScorePercent() { return scorePercent; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public String getDetails() { return details; }
}

