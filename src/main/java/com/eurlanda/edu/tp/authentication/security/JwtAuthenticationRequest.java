package com.eurlanda.edu.tp.authentication.security;

/**
 * Created by Justin on 2017/6/3.
 */

public class JwtAuthenticationRequest {

    private String username;
    private String password;

    private Boolean isOffline;

    public Boolean getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(Boolean offline) {
        isOffline = offline;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
