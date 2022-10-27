package com.eurlanda.edu.tp.authentication.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Justin on 2017/6/2.
 */

public class JwtUser implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Date lastPasswordResetDate;

    public JwtUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = null;
        this.lastPasswordResetDate = null;
    }

    public JwtUser(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Date lastPasswordResetDate) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 这个是自定义的，返回上次密码重置日期
    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
