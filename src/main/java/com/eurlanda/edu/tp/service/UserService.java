package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User getUserInfo(String username);

    User getUserInfo(int userId);

    void deleteUserById(int id);

    void updateUser(User user);

    User selectByPrimaryKey2(Integer id);
    void updateByPrimaryKey2(User user);

    List<String> findUserNameListByUserIdList(Map<String,Object> map);
    User selectByUserName2(String userName);
    List<Map<String,Object>> findUserMsgAndIdByUserIdList(List<Integer> idList);
}
