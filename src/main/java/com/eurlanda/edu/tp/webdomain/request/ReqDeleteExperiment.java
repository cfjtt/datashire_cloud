package com.eurlanda.edu.tp.webdomain.request;

public class ReqDeleteExperiment {
    private int id;
    private int courseId;

    public ReqDeleteExperiment() {
    }

    public ReqDeleteExperiment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
