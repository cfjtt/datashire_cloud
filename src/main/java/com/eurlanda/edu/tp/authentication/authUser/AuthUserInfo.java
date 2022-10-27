package com.eurlanda.edu.tp.authentication.authUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Justin on 2017/6/2.
 */

public class AuthUserInfo {

    private String username;
    private String password;
    private Date lastPasswordResetDate;
    private String password2;
    private List<String> roles = new ArrayList<>();

    private String name;

    public AuthUserInfo() {
    }


    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
