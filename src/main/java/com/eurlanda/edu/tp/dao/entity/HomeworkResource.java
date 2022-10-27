package com.eurlanda.edu.tp.dao.entity;

public class HomeworkResource {
    private Integer id;

    private Integer homeworkId;

    private Integer resourceId;

    public HomeworkResource(Integer id, Integer homeworkId, Integer resourceId) {
        this.id = id;
        this.homeworkId = homeworkId;
        this.resourceId = resourceId;
    }

    public HomeworkResource() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Integer homeworkId) {
        this.homeworkId = homeworkId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }
}