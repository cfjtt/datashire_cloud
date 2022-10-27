package com.eurlanda.edu.tp.authentication.auth;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.authentication.authUser.AuthUserInfo;

/**
 * Created by Justin on 2017/6/3.
 */

public interface AuthService {

    /**
     * register
     * @param authUserInfoToAdd
     * @return
     */
    AuthUserInfo register(AuthUserInfo authUserInfoToAdd);

    /**
     * login
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

    /**
     * refresh
     * @param oldToken
     * @return
     */
    String refresh(String oldToken);

    void updatePassword(int userId, String oldPassword, String newPassword, String confirmPassword) throws
            ApplicationErrorException;


    void licenseValidate();

}
