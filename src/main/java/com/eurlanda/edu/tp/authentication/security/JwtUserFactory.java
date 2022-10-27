package com.eurlanda.edu.tp.authentication.security;

import com.eurlanda.edu.tp.authentication.authUser.AuthUserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Justin on 2017/6/2.
 */

public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(AuthUserInfo authUserInfo) {
        return new JwtUser(
                authUserInfo.getUsername(),
                authUserInfo.getPassword(),
                mapToGrantedAuthorities(authUserInfo.getRoles()),
                authUserInfo.getLastPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
