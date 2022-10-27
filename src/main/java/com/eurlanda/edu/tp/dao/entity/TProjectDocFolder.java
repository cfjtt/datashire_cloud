package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class TProjectDocFolder {
    private Integer id;
    private String name;
    private Date createTime;
    private Date updateTime;
    private Integer projectId;

    public TProjectDocFolder(Integer id, String name, Date createTime, Date updateTime, Integer projectId) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.projectId = projectId;
    }

    public TProjectDocFolder() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
