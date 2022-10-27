package com.eurlanda.edu.tp.dao.entity;

public class StudentClass {
    private Integer id;

    private Integer studentId;

    private Integer classId;

    public StudentClass(Integer id, Integer studentId, Integer classId) {
        this.id = id;
        this.studentId = studentId;
        this.classId = classId;
    }

    public StudentClass() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }
}