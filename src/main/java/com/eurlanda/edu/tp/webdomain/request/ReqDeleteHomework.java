package com.eurlanda.edu.tp.webdomain.request;

public class ReqDeleteHomework {
    private int homeworkId;

    public ReqDeleteHomework(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    public ReqDeleteHomework(){}

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }
}
