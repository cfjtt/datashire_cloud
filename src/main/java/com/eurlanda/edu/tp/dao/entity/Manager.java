package com.eurlanda.edu.tp.dao.entity;

public class Manager {
    private Integer id;

    private Integer userId;

    private String mno;

    private String name;

    private Integer gender;

    private String email;

    private String phone;

    public Manager(Integer id, Integer userId, String mno, String name, Integer gender, String email, String phone) {
        this.id = id;
        this.userId = userId;
        this.mno = mno;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public Manager() {
        super();
    }

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

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno == null ? null : mno.trim();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
}