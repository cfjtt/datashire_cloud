package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.UserStatus;
import com.eurlanda.edu.tp.dao.mapper.UserStatusMapper;
import com.eurlanda.edu.tp.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserStatusImpl implements UserStatusService{

    @Autowired
    private UserStatusMapper userStatusMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userStatusMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserStatus record) {
        return userStatusMapper.insert(record);
    }

    @Override
    public UserStatus selectByPrimaryKey(Integer id) {
        return userStatusMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserStatus record) {
        return userStatusMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserStatus record) {
        return userStatusMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<UserStatus> selectUserStatusByUserId(Integer userId) {
        return userStatusMapper.selectUserStatusByUserId(userId);
    }
}
