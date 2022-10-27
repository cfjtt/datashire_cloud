package com.eurlanda.edu.tp.authentication.authUser;

import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/2.
 * Write implements here for project.
 */
@Service
public class AuthUserInfoServiceImp implements AuthUserInfoService {

    @Autowired
    private UserService userService;

    @Override
    public AuthUserInfo getUserInfo(String username) {
        User user = userService.getUserInfo(username);
        if (user == null) {
            return null;
        }
        AuthUserInfo authUserInfo = new AuthUserInfo();
        authUserInfo.setUsername(user.getUsername());
        authUserInfo.setPassword(user.getPassword());
        authUserInfo.setPassword2(user.getPassword2());
        return authUserInfo;
    }

    @Override
    public AuthUserInfo addUserInfo(AuthUserInfo authUserInfoToAdd) {
        return null;
    }
}
