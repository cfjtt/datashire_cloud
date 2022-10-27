package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class JobScore {
    private Integer id;
    private Integer classId;
    private Integer courseId;
    private Integer studentId;
    private Integer experimentId;
    private Integer score;
    private Date createTime;
    private String tableName;//数猎实验答案表名
    private String fileUrl;//编程实验答案文件地址

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public JobScore(Integer id, Integer classId, Integer courseId, Integer studentId, Integer experimentId, Integer score, Date createTime) {
        this.id = id;
        this.classId = classId;
        this.courseId = courseId;
        this.studentId = studentId;
        this.experimentId = experimentId;
        this.score = score;
        this.createTime = createTime;
    }

    public JobScore(Integer id, Integer classId, Integer courseId, Integer studentId, Integer experimentId, Integer score, Date createTime, String tableName, String fileUrl) {
        this.id = id;
        this.classId = classId;
        this.courseId = courseId;
        this.studentId = studentId;
        this.experimentId = experimentId;
        this.score = score;
        this.createTime = createTime;
        this.tableName = tableName;
        this.fileUrl = fileUrl;
    }

    public JobScore(Integer id, Integer score, Date createTime, String tableName, String fileUrl) {
        this.id = id;
        this.score = score;
        this.createTime = createTime;
        this.tableName = tableName;
        this.fileUrl = fileUrl;
    }

    public JobScore() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
