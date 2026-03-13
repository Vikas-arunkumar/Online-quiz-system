package com.quizapp.quizapp.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuizDtos {

    public static class CreateQuizRequest {
        @NotBlank public String title;
        @NotBlank public String topic;
        @NotNull public Long teacherId;
    }

    public static class CreateQuestionRequest {
        @NotNull public Long quizId;
        @NotBlank public String text;
        @NotBlank public String optionA;
        @NotBlank public String optionB;
        @NotBlank public String optionC;
        @NotBlank public String optionD;
        @NotBlank public String correctOption;
    }

    public static class QuestionView {
        public Long id; public String text;
        public String optionA, optionB, optionC, optionD;

        public QuestionView(Long id, String text, String a, String b, String c, String d) {
            this.id = id; this.text = text;
            this.optionA = a; this.optionB = b;
            this.optionC = c; this.optionD = d;
        }
    }

    public static class AttemptAnswer {
    private Long questionId;
    private String chosenAnswer; // not selectedOption

    public AttemptAnswer() {}

    public AttemptAnswer(Long questionId, String chosenAnswer) {
        this.questionId = questionId;
        this.chosenAnswer = chosenAnswer;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getChosenAnswer() {
        return chosenAnswer;
    }
}

    public static class AttemptRequest {
        public Long quizId;
        public Long studentId;
        public List<AttemptAnswer> answers;
    }

    public static class AttemptResponse {
        public Long resultId;
        public int scorePercent, correct, total;
        public String message;

        public AttemptResponse(Long id, int p, int c, int t, String m) {
            this.resultId = id; this.scorePercent = p;
            this.correct = c; this.total = t; this.message = m;
        }
    }
}
