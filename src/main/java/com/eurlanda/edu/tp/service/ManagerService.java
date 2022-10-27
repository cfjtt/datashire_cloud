package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.webdomain.request.ReqResetPassword;

public interface ManagerService {

    int resetPassword(ReqResetPassword reqResetPassword) throws ApplicationErrorException;

    User createUser(String username, RoleEnum role) throws ApplicationErrorException;
}
