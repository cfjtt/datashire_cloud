package com.eurlanda.edu.tp.dao.entity;

public class CourseResource {
    private Integer id;

    private Integer courseId;

    private Integer resourceId;

    private Integer type;

    public CourseResource(Integer id, Integer courseId, Integer resourceId, Integer type) {
        this.id = id;
        this.courseId = courseId;
        this.resourceId = resourceId;
        this.type = type;
    }

    public CourseResource(Integer courseId, Integer resourceId, Integer type) {
        this(0, courseId, resourceId, type);
    }

    public CourseResource() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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