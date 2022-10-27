package com.eurlanda.edu.tp.dao.entity;

public class Module {
    private Integer id;

    private Integer courseId;

    private String name;

    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Module(Integer id, Integer courseId, String name,Integer orderId) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.orderId = orderId;
    }

    public Module(Integer courseId, String name,Integer orderId) {

        this(0, courseId, name,orderId);
    }

    public Module() {
        super();
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}