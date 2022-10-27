package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class TProject {
    private Integer id;
    private String name;
    private Integer isPublic; // 是否公开 0：不公开 1：公开
    private String brefIntro;
    private Integer status;
    private String results;
    private Date createTime;
    private Date updateTime;
    private Integer creator;
    private Integer start;
    private Integer offset;
    private Student managerStudent;
    private Teacher managerTeacher;
    private Object managerUser;
    public TProject() {
    }

    public TProject(Integer id, String name, Integer isPublic, String brefIntro, Integer status, String results, Date createTime, Date updateTime, Integer creator) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.brefIntro = brefIntro;
        this.status = status;
        this.results = results;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.creator = creator;
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

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public String getBrefIntro() {
        return brefIntro;
    }

    public void setBrefIntro(String brefIntro) {
        this.brefIntro = brefIntro;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
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

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Student getManagerStudent() {
        return managerStudent;
    }

    public void setManagerStudent(Student managerStudent) {
        this.managerStudent = managerStudent;
    }

    public Teacher getManagerTeacher() {
        return managerTeacher;
    }

    public void setManagerTeacher(Teacher managerTeacher) {
        this.managerTeacher = managerTeacher;
    }

    public Object getManagerUser() {
        return managerUser;
    }

    public void setManagerUser(Object managerUser) {
        this.managerUser = managerUser;
    }
}
