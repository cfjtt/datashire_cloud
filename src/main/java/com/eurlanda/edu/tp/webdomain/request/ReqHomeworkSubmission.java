package com.eurlanda.edu.tp.webdomain.request;

public class ReqHomeworkSubmission {
    private int homeworkId;
    private int studentId;
    private String homework_url;

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

    public String getHomework_url() {
        return homework_url;
    }

    public void setHomework_url(String homework_url) {
        this.homework_url = homework_url;
    }
}
