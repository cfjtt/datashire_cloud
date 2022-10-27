package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class Experiment {
    private Integer id;

    private Integer moduleId;

    private String name;

    private String description;

    private Integer cloudwareType;

    private Date publishDate;

    private Date deadlineDate;

    private String experimentContent;

    private Integer orderId;
    private Integer isAnswer;//是否有答案
    private String answerTableName; //数猎实验关联的db表
    private String answerUrl;//编程实验答案地址
    private String oldAnswerName;//管理员上传文件的名称

    public String getOldAnswerName() {
        return oldAnswerName;
    }

    public void setOldAnswerName(String oldAnswerName) {
        this.oldAnswerName = oldAnswerName;
    }

    public String getAnswerUrl() {
        return answerUrl;
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    public String getAnswerTableName() {
        return answerTableName;
    }

    public void setAnswerTableName(String answerTableName) {
        this.answerTableName = answerTableName;
    }

    public Integer getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(Integer isAnswer) {
        this.isAnswer = isAnswer;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Experiment(Integer id, Integer moduleId, String name, String description, Integer cloudwareType, Date publishDate, Date deadlineDate, String experimentContent, Integer orderId) {
        this.id = id;
        this.moduleId = moduleId;
        this.name = name;
        this.description = description;
        this.cloudwareType = cloudwareType;
        this.publishDate = publishDate;
        this.deadlineDate = deadlineDate;
        this.experimentContent = experimentContent;
        this.orderId = orderId;
    }
    public Experiment(Integer id, Integer moduleId, String name, String description, Integer cloudwareType, Date publishDate, Date deadlineDate, String experimentContent, Integer orderId,Integer isAnswer,String answerTableName,String answerUrl,String oldAnswerName) {
        this.id = id;
        this.moduleId = moduleId;
        this.name = name;
        this.description = description;
        this.cloudwareType = cloudwareType;
        this.publishDate = publishDate;
        this.deadlineDate = deadlineDate;
        this.experimentContent = experimentContent;
        this.orderId = orderId;
        this.isAnswer=isAnswer;
        this.answerTableName=answerTableName;
        this.answerUrl=answerUrl;
        this.oldAnswerName=oldAnswerName;
    }

    public Experiment(Integer id, Integer moduleId, String name, String description, Integer cloudwareType, Date publishDate, Date deadlineDate, Integer orderId) {
        this(id, moduleId, name, description, cloudwareType, publishDate, deadlineDate, "",orderId);
    }

    public Experiment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getCloudwareType() {
        return cloudwareType;
    }

    public void setCloudwareType(Integer cloudwareType) {
        this.cloudwareType = cloudwareType;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getExperimentContent() {
        return experimentContent;
    }

    public void setExperimentContent(String experimentContent) {
        this.experimentContent = experimentContent == null ? null : experimentContent.trim();
    }
}