package com.eurlanda.edu.tp.webdomain.request;

/**
 * Created by Administrator on 2017/10/19.
 */
public class ReqUpdateHomework {
    private int homeworkId;
    private String homeworkName;
    private String homeworkDes;
    private String homeworkCreateDate;
    private String homeworkDueDate;
    private int cloudwareType;

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
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

    public String getHomeworkCreateDate() {
        return homeworkCreateDate;
    }

    public void setHomeworkCreateDate(String homeworkCreateDate) {
        this.homeworkCreateDate = homeworkCreateDate;
    }

    public String getHomeworkDueDate() {
        return homeworkDueDate;
    }

    public void setHomeworkDueDate(String homeworkDueDate) {
        this.homeworkDueDate = homeworkDueDate;
    }

    public int getCloudwareType() {
        return cloudwareType;
    }

    public void setCloudwareType(int cloudwareType) {
        this.cloudwareType = cloudwareType;
    }
}
