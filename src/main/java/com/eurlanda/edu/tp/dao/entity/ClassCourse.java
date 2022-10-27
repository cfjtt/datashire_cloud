package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class ClassCourse {
    private Integer id;

    private Integer classId;

    private Integer termId;

    private Integer courseId;

    private Date date;

    private Integer teacherId;
    private String schoolYear;
    private Integer semester;

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public ClassCourse() {
    }

    public ClassCourse(Integer id, Integer classId, Integer termId, Integer courseId, Date date, Integer teacherId) {
        this.id = id;
        this.classId = classId;
        this.termId = termId;
        this.courseId = courseId;
        this.date = date;
        this.teacherId = teacherId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public ClassCourse(Integer id, Integer classId, Integer termId, Integer courseId, Date date, Integer teacherId, String schoolYear, int semester) {
        this.id = id;
        this.classId = classId;
        this.termId = termId;
        this.courseId = courseId;
        this.date = date;
        this.teacherId = teacherId;
        this.schoolYear = schoolYear;
        this.semester = semester;
    }
    public ClassCourse(Integer id, Integer classId, String schoolYear,Integer semester, Integer courseId, Date date, Integer teacherId) {
        this.id = id;
        this.classId = classId;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.courseId = courseId;
        this.date = date;
        this.teacherId = teacherId;
    }
}
