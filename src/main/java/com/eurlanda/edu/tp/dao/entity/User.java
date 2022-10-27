package com.eurlanda.edu.tp.dao.entity;

public class User {
    private Integer id;
    //1.学生 2.老师 3.管理员
    private Integer role;

    private String username;

    private String password;
    private String password2;

    private String volumeId;

    private Long tokenCreateTime;//token创建时间
    private Integer isRemind;//是否提醒过重复登录

    private String token;//登录保存的token

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTokenCreateTime() {
        return tokenCreateTime;
    }

    public void setTokenCreateTime(Long tokenCreateTime) {
        this.tokenCreateTime = tokenCreateTime;
    }

    public Integer getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(Integer isRemind) {
        this.isRemind = isRemind;
    }

    public User(Integer id, Integer role, String username, String password, String password2, String volumeId) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.password2 = password2;
        this.volumeId = volumeId;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }
}