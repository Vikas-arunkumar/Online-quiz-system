package com.quizapp.quizapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quizapp.quizapp.model.Question;
import com.quizapp.quizapp.model.Quiz;
import com.quizapp.quizapp.model.Role;
import com.quizapp.quizapp.model.StudentQuizResult;
import com.quizapp.quizapp.model.User;
import com.quizapp.quizapp.repo.QuestionRepository;
import com.quizapp.quizapp.repo.QuizRepository;
import com.quizapp.quizapp.repo.ResultRepository;
import com.quizapp.quizapp.service.ResultService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private final QuizRepository quizRepository;
    private final ResultRepository resultRepository;
    private final QuestionRepository questionRepository;
    private final ResultService resultService;

    public DashboardController(QuizRepository quizRepository,
                               ResultRepository resultRepository,
                               QuestionRepository questionRepository,
                               ResultService resultService) {
        this.quizRepository = quizRepository;
        this.resultRepository = resultRepository;
        this.questionRepository = questionRepository;
        this.resultService = resultService;
    }

    // Student Dashboard
    @GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "student-dashboard";
    }

    // Student – View all quizzes
    @GetMapping("/student/quizzes")
    public String studentQuizzes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Quiz> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);
        return "student-quizzes";
    }

    // Student – View Results History
    @GetMapping("/student/results")
    public String studentResults(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<StudentQuizResult> results = resultRepository.findByStudentId(user.getId());
        model.addAttribute("results", results);
        return "student-results";
    }

    // Open quiz to answer (fetch questions via repository)
    @GetMapping("/student/quiz/{quizId}")
    public String showQuiz(@PathVariable Long quizId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != Role.STUDENT) return "redirect:/login";

        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) return "redirect:/student/quizzes";

        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        return "student-quiz";
    }

    // Submit quiz answers (convert form Map -> List<AttemptAnswer>)
   @PostMapping("/student/quiz/{quizId}/submit")
public String submitQuiz(
        @PathVariable Long quizId,
        @RequestParam Map<String, String> formData,
        HttpSession session) {

    User student = (User) session.getAttribute("user");
    if (student == null) return "redirect:/login";

    Map<Long, String> answers = new HashMap<>();
    formData.forEach((key, value) -> {
        if (key.startsWith("q_")) {
            Long qId = Long.parseLong(key.substring(2));
            answers.put(qId, value);
        }
    });

    resultService.evaluateAttempt(quizId, student.getId(), answers);
    return "redirect:/student/results";
}

@GetMapping("/teacher/dashboard")
public String teacherDashboard(HttpSession session, Model model) {
    User teacher = (User) session.getAttribute("user");
    if (teacher == null || teacher.getRole() != Role.TEACHER) {
        return "redirect:/login";
    }

    Map<String, List<StudentQuizResult>> resultsGrouped =
            resultService.getResultsGroupedByTopic(teacher.getId());

    model.addAttribute("resultsGrouped", resultsGrouped);

    return "teacher-dashboard";

}
    @GetMapping("/teacher/results")
public String teacherResults(HttpSession session, Model model) {

    User teacher = (User) session.getAttribute("user");
    if (teacher == null || teacher.getRole() != Role.TEACHER) {
        return "redirect:/login";
    }

    Map<String, List<StudentQuizResult>> resultsGrouped =
            resultService.getResultsGroupedByTopic(teacher.getId());

    model.addAttribute("resultsGrouped", resultsGrouped);
    return "teacher-results";
}

}
