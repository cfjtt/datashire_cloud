package com.eurlanda.edu.tp.webdomain.request;

/**
 * Created by test on 2018/6/9.
 */
public class ReqSubmitHomeworkOfShuLie {

    //实验id
    private Integer experimentId;

    //学生id
    private Integer userId;

    private Integer courseId;
    //学生提交作业的表名
    private String tableName;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
