package com.eurlanda.edu.tp.webdomain.request;

/**
 * Created by test on 2018/6/9.
 */
public class ReqSubmitHomeworkOfOther {


    private Integer experimentId;

    //学生提交答案的地址
    private String homeworkPath;

    private Integer userId;

    private Integer courseId;
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public String getHomeworkPath() {
        return homeworkPath;
    }

    public void setHomeworkPath(String homeworkPath) {
        this.homeworkPath = homeworkPath;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

}
