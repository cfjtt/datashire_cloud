package com.eurlanda.edu.tp.webdomain.request;

/**
 * Created by test on 2018/6/7.
 */
public class ReqRecordUserOnline {

    private Integer userId;
    private String token;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
