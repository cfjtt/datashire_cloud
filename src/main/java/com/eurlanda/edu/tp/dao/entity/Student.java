package com.eurlanda.edu.tp.dao.entity;

public class Student {
    private Integer id;

    private Integer userId;

    private String sno;

    private String name;

    private Integer gender;

    private String birthday;
    private String grade;
    private String phone;
    private Integer role;
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Student(Integer id, Integer userId, String sno, String name, Integer gender, String birthday, String grade,String phone) {
        this.id = id;
        this.userId = userId;
        this.sno = sno;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.grade=grade;
        this.phone=phone;
    }

    public Student(String sno, String name, Integer gender, String birthday,String grade,String phone) {
        this(0, 0, sno, name, gender, birthday,grade,phone);
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Student(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno == null ? null : sno.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}