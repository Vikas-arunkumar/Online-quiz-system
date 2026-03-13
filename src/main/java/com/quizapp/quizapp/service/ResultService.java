package com.quizapp.quizapp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.quizapp.quizapp.dto.AnalyticsDtos.LowPerformer;
import com.quizapp.quizapp.dto.AnalyticsDtos.RecentScore;
import com.quizapp.quizapp.dto.AnalyticsDtos.StudentDashboard;
import com.quizapp.quizapp.dto.AnalyticsDtos.StudentOverview;
import com.quizapp.quizapp.dto.AnalyticsDtos.TeacherAnalytics;
import com.quizapp.quizapp.model.Question;
import com.quizapp.quizapp.model.Quiz;
import com.quizapp.quizapp.model.StudentQuizResult;
import com.quizapp.quizapp.model.User;
import com.quizapp.quizapp.repo.QuestionRepository;
import com.quizapp.quizapp.repo.QuizRepository;
import com.quizapp.quizapp.repo.ResultRepository;
import com.quizapp.quizapp.repo.StudentQuizResultRepository;
import com.quizapp.quizapp.repo.UserRepository;

@Service
public class ResultService {

    private final ResultRepository resultRepo;
    private final UserRepository userRepo;
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final StudentQuizResultRepository studentQuizResultRepo;

    @Value("${app.low-score-threshold:50}")
    private int lowScoreThreshold;

    public ResultService(
            ResultRepository rr,
            UserRepository ur,
            QuizRepository qr,
            QuestionRepository qnr,
            StudentQuizResultRepository sqr
    ) {
        this.resultRepo = rr;
        this.userRepo = ur;
        this.quizRepo = qr;
        this.questionRepo = qnr;
        this.studentQuizResultRepo = sqr;
    }


    // ✅ Evaluate and save student result
    public StudentQuizResult evaluateAttempt(Long quizId, Long studentId, Map<Long, String> answers) {

        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<Question> questions = questionRepo.findByQuizId(quizId);

        int correct = 0;
        List<Map<String,Object>> details = new ArrayList<>();

        for (Question q : questions) {
            String sel = answers.getOrDefault(q.getId(), "");
            boolean ok = q.getCorrectOption().equalsIgnoreCase(sel);
            if (ok) correct++;

            Map<String,Object> d = new LinkedHashMap<>();
            d.put("questionId", q.getId());
            d.put("selected", sel);
            d.put("correct", q.getCorrectOption());
            details.add(d);
        }

        int total = questions.size();
        int percent = total == 0 ? 0 : (correct * 100) / total;

        StudentQuizResult result = new StudentQuizResult(
                student, quiz, total, correct, percent, LocalDateTime.now(), details.toString()
        );

        return resultRepo.save(result);
    }


    // ✅ Student Dashboard Analytics
    public StudentDashboard studentDashboard(Long studentId) {

        User student = userRepo.findById(studentId).orElseThrow();
        List<StudentQuizResult> results = resultRepo.findByStudentId(student.getId());

        StudentDashboard dash = new StudentDashboard();
        dash.studentId = student.getId();
        dash.totalAttempts = results.size();

        dash.attemptsByTopic = new HashMap<>();
        dash.avgScoreByTopic = new HashMap<>();

        Map<String, List<StudentQuizResult>> byTopic =
                results.stream().collect(Collectors.groupingBy(r -> r.getQuiz().getTopic()));

        for (Map.Entry<String, List<StudentQuizResult>> e : byTopic.entrySet()) {
            dash.attemptsByTopic.put(e.getKey(), e.getValue().size());
            dash.avgScoreByTopic.put(
                    e.getKey(),
                    e.getValue().stream().mapToInt(StudentQuizResult::getScorePercent).average().orElse(0)
            );
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dash.recentScores = results.stream()
                .sorted(Comparator.comparing(StudentQuizResult::getSubmittedAt).reversed())
                .limit(10)
                .map(r -> {
                    RecentScore s = new RecentScore();
                    s.resultId = r.getId();
                    s.quizTitle = r.getQuiz().getTitle();
                    s.topic = r.getQuiz().getTopic();
                    s.scorePercent = r.getScorePercent();
                    s.submittedAt = r.getSubmittedAt().format(fmt);
                    return s;
                }).toList();

        return dash;
    }


    // ✅ Teacher Analytics Dashboard
    public TeacherAnalytics teacherAnalytics(Long teacherId){

        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<StudentQuizResult> all = studentQuizResultRepo.findByQuiz_CreatedBy_Id(teacherId);

        Map<User, List<StudentQuizResult>> byStudent =
                all.stream().collect(Collectors.groupingBy(StudentQuizResult::getStudent));

        TeacherAnalytics ta = new TeacherAnalytics();
        ta.teacherId = teacherId;
        ta.students = new ArrayList<>();
        ta.lowPerformers = new ArrayList<>();

        for (Map.Entry<User, List<StudentQuizResult>> e : byStudent.entrySet()) {
            User s = e.getKey();
            List<StudentQuizResult> rs = e.getValue();

            double avg = rs.stream().mapToInt(StudentQuizResult::getScorePercent).average().orElse(0);
            int below = (int) rs.stream().filter(r -> r.getScorePercent() < lowScoreThreshold).count();

            StudentOverview so = new StudentOverview();
            so.studentId = s.getId();
            so.name = s.getName();
            so.attempts = rs.size();
            so.averageScore = avg;
            ta.students.add(so);

            if (below >= 2 || avg < lowScoreThreshold) {
                LowPerformer lp = new LowPerformer();
                lp.studentId = s.getId();
                lp.name = s.getName();
                lp.timesBelowThreshold = below;
                lp.avgScore = avg;
                ta.lowPerformers.add(lp);
            }
        }

        return ta;
    }


    // ✅ Group quiz results by topic for the teacher
    public Map<String, List<StudentQuizResult>> getResultsGroupedByTopic(Long teacherId) {
        List<StudentQuizResult> results = studentQuizResultRepo.findByQuiz_CreatedBy_Id(teacherId);

        Map<String, List<StudentQuizResult>> map = new HashMap<>();

        for (StudentQuizResult r : results) {
            String topic = r.getQuiz().getTopic();
            map.computeIfAbsent(topic, k -> new ArrayList<>()).add(r);
        }

        return map;
    }
}
