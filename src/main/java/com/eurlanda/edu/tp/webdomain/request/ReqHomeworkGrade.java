package com.eurlanda.edu.tp.webdomain.request;

public class ReqHomeworkGrade {
    private int studentHomeworkId;
    private int grade;
    private String comment;

    public int getStudentHomeworkId() {
        return studentHomeworkId;
    }

    public void setStudentHomeworkId(int studentHomeworkId) {
        this.studentHomeworkId = studentHomeworkId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
