package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.UserStatus;

import java.util.List;

public interface UserStatusService {
    int deleteByPrimaryKey(Integer id);
    int insert(UserStatus record);
    UserStatus selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(UserStatus record);
    int updateByPrimaryKey(UserStatus record);
    List<UserStatus> selectUserStatusByUserId(Integer userId);
}
