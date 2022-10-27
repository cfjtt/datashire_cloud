package com.eurlanda.edu.tp.dao.entity;

public class UserNavlist {
    private Integer id;

    private Integer role;

    private Integer navlistId;

    public UserNavlist(Integer id, Integer role, Integer navlistId) {
        this.id = id;
        this.role = role;
        this.navlistId = navlistId;
    }

    public UserNavlist() {
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

    public Integer getNavlistId() {
        return navlistId;
    }

    public void setNavlistId(Integer navlistId) {
        this.navlistId = navlistId;
    }
}