package com.quizapp.quizapp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quizapp.quizapp.model.Question;
import com.quizapp.quizapp.model.Quiz;
import com.quizapp.quizapp.model.Role;
import com.quizapp.quizapp.model.User;
import com.quizapp.quizapp.repo.QuestionRepository;
import com.quizapp.quizapp.repo.QuizRepository;
import com.quizapp.quizapp.repo.UserRepository;

@Service
public class QuizService {

    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;

    public QuizService(QuizRepository q, QuestionRepository qr, UserRepository ur) {
        this.quizRepo = q; this.questionRepo = qr; this.userRepo = ur;
    }

    @Transactional
    public Quiz createQuiz(String title, String topic, Long teacherId) {
        User teacher = userRepo.findById(teacherId).orElseThrow();
        if (teacher.getRole() != Role.TEACHER) throw new RuntimeException("Not a teacher");

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setTopic(topic);
        quiz.setCreatedBy(teacher);

        return quizRepo.save(quiz);
    }

    @Transactional
    public Question addQuestion(Long quizId, String t, String a, String b, String c, String d, String correct) {
        Quiz qz = quizRepo.findById(quizId).orElseThrow();
        Question q = new Question();
        q.setQuiz(qz);
        q.setText(t);
        q.setOptionA(a);
        q.setOptionB(b);
        q.setOptionC(c);
        q.setOptionD(d);
        q.setCorrectOption(correct);
        return questionRepo.save(q);
    }

    public List<Quiz> all() { return quizRepo.findAll(); }

    public List<Question> questions(Long quizId) {
        return questionRepo.findByQuizId(quizId);
    }
}
