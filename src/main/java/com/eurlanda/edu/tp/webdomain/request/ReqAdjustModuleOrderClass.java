package com.eurlanda.edu.tp.webdomain.request;

/**
 * Created by test on 2018/5/29.
 */
public class ReqAdjustModuleOrderClass {

    private Integer orderOne;
    private Integer moduleIdOne;
    private Integer moduleIdTwo;
    private Integer orderTwo;
    private Integer courseId;

    public Integer getModuleIdOne() {
        return moduleIdOne;
    }

    public void setModuleIdOne(Integer moduleIdOne) {
        this.moduleIdOne = moduleIdOne;
    }

    public Integer getModuleIdTwo() {
        return moduleIdTwo;
    }

    public void setModuleIdTwo(Integer moduleIdTwo) {
        this.moduleIdTwo = moduleIdTwo;
    }

    public Integer getOrderOne() {
        return orderOne;
    }

    public void setOrderOne(Integer orderOne) {
        this.orderOne = orderOne;
    }

    public Integer getOrderTwo() {
        return orderTwo;
    }

    public void setOrderTwo(Integer orderTwo) {
        this.orderTwo = orderTwo;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
