package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class Clazz {
    private Integer id;

    private Integer termId;

    private Integer courseId;

    private Date date;

    private String duration;

    private Integer studentNum;

    private String name;

    private Integer teacherId;

    public Clazz(Integer id, Integer termId, Integer courseId, Date date, String duration, Integer studentNum, String name, Integer teacherId) {
        this.id = id;
        this.termId = termId;
        this.courseId = courseId;
        this.date = date;
        this.duration = duration;
        this.studentNum = studentNum;
        this.name = name;
        this.teacherId = teacherId;
    }
    public Clazz(Integer id,Integer courseId, Date date, String duration, Integer studentNum, String name, Integer teacherId) {
        this.id = id;
        this.courseId = courseId;
        this.date = date;
        this.duration = duration;
        this.studentNum = studentNum;
        this.name = name;
        this.teacherId = teacherId;
    }

    public Clazz(Integer id, Integer studentNum, String name, String duration,Date date) {
        this.id = id;
        this.studentNum = studentNum;
        this.name = name;
        this.duration = duration;
        this.date = date;
    }

    public Clazz() {
        this.studentNum = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
    }

    public Integer getStudentNum() {
        return studentNum == null ? 0 : studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
}