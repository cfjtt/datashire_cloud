package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.dao.mapper.UserMapper;
import com.eurlanda.edu.tp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserInfo(String username) {
        return userMapper.selectByUserName(username);
    }

    @Override
    public User getUserInfo(int userId){
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void deleteUserById(int id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public List<String> findUserNameListByUserIdList(Map<String,Object> map) {
        return userMapper.findUserNameListByUserIdList(map);
    }

    @Override
    public User selectByPrimaryKey2(Integer id) {
        return userMapper.selectByPrimaryKey2(id);
    }

    @Override
    public void updateByPrimaryKey2(User user) {
        userMapper.updateByPrimaryKey2(user);
    }

    @Override
    public User selectByUserName2(String userName) {
        return userMapper.selectByUserName2(userName);
    }

    @Override
    public List<Map<String, Object>> findUserMsgAndIdByUserIdList(List<Integer> idList) {
        return userMapper.findUserMsgAndIdByUserIdList(idList);
    }
}
