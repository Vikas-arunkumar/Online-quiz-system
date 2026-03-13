package com.quizapp.quizapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quizapp.quizapp.model.Question;
import com.quizapp.quizapp.model.Quiz;
import com.quizapp.quizapp.model.Role;
import com.quizapp.quizapp.model.User;
import com.quizapp.quizapp.repo.QuestionRepository;
import com.quizapp.quizapp.repo.QuizRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher")
public class TeacherQuizController {

    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    public TeacherQuizController(QuizRepository quizRepo,QuestionRepository questionRepo) {
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
    }

    // ✅ Show Create Quiz Page
    @GetMapping("/create-quiz")
    public String createQuizPage(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != Role.TEACHER) {
            return "redirect:/login";
        }

        return "teacher-create-quiz";
    }

    // ✅ Handle Quiz Submit (Ignore questions for now — optional params)
    @PostMapping("/create-quiz")
public String createQuiz(
        @RequestParam String title,
        @RequestParam String topic,
        HttpSession session
) {
    User teacher = (User) session.getAttribute("user");
    if (teacher == null) return "redirect:/login";

    Quiz quiz = new Quiz();
    quiz.setTitle(title);
    quiz.setTopic(topic);
    quiz.setCreatedBy(teacher);

    Quiz savedQuiz = quizRepo.save(quiz);

    // ✅ Redirect to add-question page
    return "redirect:/teacher/quiz/" + savedQuiz.getId() + "/add-questions";
    }
    @GetMapping("/quiz/{id}/add-questions")
    public String showAddQuestions(@PathVariable Long id, HttpSession session, org.springframework.ui.Model model) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null) return "redirect:/login";

        model.addAttribute("quizId", id);   
        return "teacher-add-questions";
    }

    @PostMapping("/quiz/{id}/add-questions")
    public String saveQuestion(
        @PathVariable Long id,
        @RequestParam String questionText,
        @RequestParam String optionA,
        @RequestParam String optionB,
        @RequestParam String optionC,
        @RequestParam String optionD,
        @RequestParam String correctOption
    ) {
        Quiz quiz = quizRepo.findById(id).orElseThrow();
        Question q = new Question();
        q.setQuiz(quiz);
        q.setText(questionText);
        q.setOptionA(optionA);
        q.setOptionB(optionB);
        q.setOptionC(optionC);
        q.setOptionD(optionD);
        q.setCorrectOption(correctOption);
        questionRepo.save(q);

        return "redirect:/teacher/quiz/" + id + "/add-questions?added";
    }

}
