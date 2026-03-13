package com.quizapp.quizapp.controller;

import com.quizapp.quizapp.dto.QuizDtos.*;
import com.quizapp.quizapp.model.Question;
import com.quizapp.quizapp.model.Quiz;
import com.quizapp.quizapp.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService service;

    public QuizController(QuizService s) { this.service = s; }

    @PostMapping
    public ResponseEntity<Quiz> create(@RequestBody @Valid CreateQuizRequest r) {
        return ResponseEntity.ok(service.createQuiz(r.title, r.topic, r.teacherId));
    }

    @PostMapping("/questions")
    public ResponseEntity<Question> addQ(@RequestBody @Valid CreateQuestionRequest r) {
        return ResponseEntity.ok(service.addQuestion(r.quizId, r.text, r.optionA, r.optionB, r.optionC, r.optionD, r.correctOption));
    }

    @GetMapping
    public List<Quiz> all() { return service.all(); }

    @GetMapping("/{id}/questions")
    public List<QuestionView> questions(@PathVariable Long id) {
        return service.questions(id).stream()
                .map(q -> new QuestionView(q.getId(), q.getText(), q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()))
                .toList();
    }
}
