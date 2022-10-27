package com.eurlanda.edu.tp.dao.entity;

public class ImageConfig {
    private Integer id;
    private Integer cloudwareType;
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCloudwareType() {
        return cloudwareType;
    }

    public void setCloudwareType(Integer cloudwareType) {
        this.cloudwareType = cloudwareType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
