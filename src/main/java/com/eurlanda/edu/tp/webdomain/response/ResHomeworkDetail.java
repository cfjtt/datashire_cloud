package com.eurlanda.edu.tp.webdomain.response;

public class ResHomeworkDetail {
    private String courseName;
    private String moduleName;
    private String homeworkName;
    private String homeworkDes;
    private int classId;
    private String cloudwareType;
    private String dueDate;
    private String publishDate;

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

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public String getHomeworkDes() {
        return homeworkDes;
    }

    public void setHomeworkDes(String homeworkDes) {
        this.homeworkDes = homeworkDes;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
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
}
