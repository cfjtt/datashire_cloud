package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class TProjectUser {
    private Integer id;
    private Integer projectId;
    private Integer userId;
    private Integer isManager;
    private Date createTime;

    public TProjectUser() {
    }

    public TProjectUser(Integer id, Integer projectId, Integer userId, Integer isManager, Date createTime) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.isManager = isManager;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsManager() {
        return isManager;
    }

    public void setIsManager(Integer isManager) {
        this.isManager = isManager;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
