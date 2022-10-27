package com.eurlanda.edu.tp.dao.entity;

public class TProjectConfig {
    private Integer id;
    private Integer role;
    private Integer packageId;
    private Integer projectNum;

    public TProjectConfig() {
    }

    public TProjectConfig(Integer id, Integer role, Integer packageId, Integer projectNum) {
        this.id = id;
        this.role = role;
        this.packageId = packageId;
        this.projectNum = projectNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(Integer projectNum) {
        this.projectNum = projectNum;
    }
}
