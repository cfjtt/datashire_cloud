package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResHomeworkSubmissionList {
    private String moduleName;
    private String courseName;
    private String className;

    private List<ResHomeworkList> homeworkList;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ResHomeworkList> getHomeworkList() {
        return homeworkList;
    }

    public void setHomeworkList(List<ResHomeworkList> homeworkList) {
        this.homeworkList = homeworkList;
    }

    public static class ResHomeworkList {
        private int homeworkId;
        private String homeworkName;
        private String cloudwareType;
        private int completedCount;
        private int nonCompletedCount;

        private List<ResHomeworkSubmissionDetail> homeworkSubmissionList;

        public int getHomeworkId() {
            return homeworkId;
        }

        public void setHomeworkId(int homeworkId) {
            this.homeworkId = homeworkId;
        }

        public String getHomeworkName() {
            return homeworkName;
        }

        public void setHomeworkName(String homeworkName) {
            this.homeworkName = homeworkName;
        }

        public String getCloudwareType() {
            return cloudwareType;
        }

        public void setCloudwareType(String cloudwareType) {
            this.cloudwareType = cloudwareType;
        }

        public int getCompletedCount() {
            return completedCount;
        }

        public void setCompletedCount(int completedCount) {
            this.completedCount = completedCount;
        }

        public int getNonCompletedCount() {
            return nonCompletedCount;
        }

        public void setNonCompletedCount(int nonCompletedCount) {
            this.nonCompletedCount = nonCompletedCount;
        }

        public List<ResHomeworkSubmissionDetail> getHomeworkSubmissionList() {
            return homeworkSubmissionList;
        }

        public void setHomeworkSubmissionList(List<ResHomeworkSubmissionDetail> homeworkSubmissionList) {
            this.homeworkSubmissionList = homeworkSubmissionList;
        }
    }

    public static class ResHomeworkSubmissionDetail{
        private int studentHomeworkId;
        private int homeworkId;
        private int studentId;
        private String studentNo;
        private String studentName;
        private boolean completed;
        private String dueDate;
        private String submissionDate;
        private String lastEditDate;
        private int score;

        public int getStudentHomeworkId() {
            return studentHomeworkId;
        }

        public void setStudentHomeworkId(int studentHomeworkId) {
            this.studentHomeworkId = studentHomeworkId;
        }

        public int getHomeworkId() {
            return homeworkId;
        }

        public void setHomeworkId(int homeworkId) {
            this.homeworkId = homeworkId;
        }

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public String getSubmissionDate() {
            return submissionDate;
        }

        public void setSubmissionDate(String submissionDate) {
            this.submissionDate = submissionDate;
        }

        public String getLastEditDate() {
            return lastEditDate;
        }

        public void setLastEditDate(String lastEditDate) {
            this.lastEditDate = lastEditDate;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
