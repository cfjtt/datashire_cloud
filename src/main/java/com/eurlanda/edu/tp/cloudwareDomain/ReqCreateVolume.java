package com.eurlanda.edu.tp.cloudwareDomain;

public class ReqCreateVolume {
    private int user_id;
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }
}
