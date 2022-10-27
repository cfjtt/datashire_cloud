package com.eurlanda.edu.tp.dao.entity;

/**
 * Created by test on 2018/6/6.
 */
public class SysLicense {

    private String key;//为deep生成key,只生成一次

    private String license;//由key和用户要求，过期时间，在线人数生成的license

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
