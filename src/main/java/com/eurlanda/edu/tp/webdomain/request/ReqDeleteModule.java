package com.eurlanda.edu.tp.webdomain.request;

public class ReqDeleteModule {
    private int moduleId;
    private int courseId;
    public ReqDeleteModule(int moduleId) {
        this.moduleId = moduleId;
    }

    public ReqDeleteModule() {
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
