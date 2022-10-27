package com.eurlanda.edu.tp.webdomain.request;

public class ReqDeleteClass {
    private int classId;

    public ReqDeleteClass() {
    }

    public ReqDeleteClass(int classId) {
        this.classId = classId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
