package com.eurlanda.edu.tp.dao.entity;

public class StudentHomeworkResource {
    private Integer id;

    private Integer studentHomeworkId;

    private Integer resourceId;

    private Integer type;

    public StudentHomeworkResource(Integer id, Integer studentHomeworkId, Integer resourceId, Integer type) {
        this.id = id;
        this.studentHomeworkId = studentHomeworkId;
        this.resourceId = resourceId;
        this.type = type;
    }

    public StudentHomeworkResource(Integer studentHomeworkId, Integer resourceId, Integer type){
        this(0, studentHomeworkId,resourceId,type);
    }

    public StudentHomeworkResource() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentHomeworkId() {
        return studentHomeworkId;
    }

    public void setStudentHomeworkId(Integer studentHomeworkId) {
        this.studentHomeworkId = studentHomeworkId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}