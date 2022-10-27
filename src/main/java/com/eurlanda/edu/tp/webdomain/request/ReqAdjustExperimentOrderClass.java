package com.eurlanda.edu.tp.webdomain.request;

/**
 * Created by test on 2018/5/29.
 */
public class ReqAdjustExperimentOrderClass {

    private Integer experimentOne;
    private Integer orderOne;
    private Integer experimentTwo;
    private Integer orderTwo;
    private Integer moduleId;
    private Integer courseId;


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

    public Integer getExperimentOne() {
        return experimentOne;
    }

    public void setExperimentOne(Integer experimentOne) {
        this.experimentOne = experimentOne;
    }

    public Integer getExperimentTwo() {
        return experimentTwo;
    }

    public void setExperimentTwo(Integer experimentTwo) {
        this.experimentTwo = experimentTwo;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
