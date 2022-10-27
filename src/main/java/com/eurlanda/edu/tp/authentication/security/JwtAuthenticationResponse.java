package com.eurlanda.edu.tp.authentication.security;

/**
 * Created by Justin on 2017/6/3.
 */

public class JwtAuthenticationResponse {

    private int userId;
    private int role;
    private String username;
    private String token;

    public JwtAuthenticationResponse(int userId, int role, String username, String token) {
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
