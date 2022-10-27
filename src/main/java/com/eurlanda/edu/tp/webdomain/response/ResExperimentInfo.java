package com.eurlanda.edu.tp.webdomain.response;

import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.dao.entity.Experiment;
import com.eurlanda.edu.tp.enums.CloudwareTypeEnum;

public class ResExperimentInfo {
    private int id;

    private String courseName;

    private String moduleName;

    private String experimentName;

    private String experimentDes;

    private int cloudwareTypeId;

    private String cloudwareType;

    private String dueDate;

    private String publishDate;

    private String experimentUrl;

    private String experimentContent;

    private Integer courseId;

    private Integer orderId;
    private Integer isAnswer;//是否有答案
    private String answerTableName;//数猎管理答案表

    private String answerUrl;//编程实验答案地址
    private String oldAnswerName;//管理员上传文件的名称
    private String stuIsAnswer;//当前学生是否提交答案

    public String getStuIsAnswer() {
        return stuIsAnswer;
    }

    public void setStuIsAnswer(String stuIsAnswer) {
        this.stuIsAnswer = stuIsAnswer;
    }

    public String getAnswerUrl() {
        return answerUrl;
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    public String getOldAnswerName() {
        return oldAnswerName;
    }

    public void setOldAnswerName(String oldAnswerName) {
        this.oldAnswerName = oldAnswerName;
    }

    public Integer getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(Integer isAnswer) {
        this.isAnswer = isAnswer;
    }

    public String getAnswerTableName() {
        return answerTableName;
    }

    public void setAnswerTableName(String answerTableName) {
        this.answerTableName = answerTableName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public ResExperimentInfo() { }

    public ResExperimentInfo(Experiment experiment) {
        this.setId(experiment.getId());
        this.setExperimentName(experiment.getName());
        this.setExperimentDes(experiment.getDescription());
        this.setCloudwareTypeId(experiment.getCloudwareType());
        CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt(experiment.getCloudwareType());
        this.setCloudwareType(cloudwareType == null ? "" : cloudwareType.toString());
        this.setDueDate(Utility.formatDate(experiment.getDeadlineDate()));
        this.setPublishDate(Utility.formatDate(experiment.getPublishDate()));
        this.setExperimentContent(experiment.getExperimentContent());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentDes() {
        return experimentDes;
    }

    public void setExperimentDes(String experimentDes) {
        this.experimentDes = experimentDes;
    }

    public int getCloudwareTypeId() {
        return cloudwareTypeId;
    }

    public void setCloudwareTypeId(int cloudwareTypeId) {
        this.cloudwareTypeId = cloudwareTypeId;
    }

    public String getCloudwareType() {
        return cloudwareType;
    }

    public void setCloudwareType(String cloudwareType) {
        this.cloudwareType = cloudwareType;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getExperimentUrl() {
        return experimentUrl;
    }

    public void setExperimentUrl(String experimentUrl) {
        this.experimentUrl = experimentUrl;
    }

    public String getExperimentContent() {
        return experimentContent;
    }

    public void setExperimentContent(String experimentContent) {
        this.experimentContent = experimentContent;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
