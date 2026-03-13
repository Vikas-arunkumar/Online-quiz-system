package com.quizapp.quizapp.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsDtos {

    public static class StudentDashboard {
        public Long studentId;
        public int totalAttempts;
        public Map<String,Integer> attemptsByTopic;
        public Map<String,Double> avgScoreByTopic;
        public List<RecentScore> recentScores;
    }

    public static class RecentScore {
        public Long resultId;
        public String quizTitle, topic;
        public int scorePercent;
        public String submittedAt;
    }

    public static class TeacherAnalytics {
        public Long teacherId;
        public List<StudentOverview> students;
        public List<LowPerformer> lowPerformers;
    }

    public static class StudentOverview {
        public Long studentId;
        public String name;
        public int attempts;
        public double averageScore;
    }

    public static class LowPerformer {
        public Long studentId;
        public String name;
        public int timesBelowThreshold;
        public double avgScore;
    }
}

