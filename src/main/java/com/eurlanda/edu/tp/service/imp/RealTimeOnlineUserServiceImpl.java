package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.RealTimeOnlineUser;
import com.eurlanda.edu.tp.dao.mapper.RealTimeOnlineUserMapper;
import com.eurlanda.edu.tp.dao.mapper.UserAccessRecordMapper;
import com.eurlanda.edu.tp.service.RealTimeOnlineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 2018/4/24.
 */


@Service
public class RealTimeOnlineUserServiceImpl implements RealTimeOnlineUserService{

    @Autowired
    private UserAccessRecordMapper userAccessRecordMapper;

    @Autowired
    private RealTimeOnlineUserMapper realTimeOnlineUserMapper;


    /**
     * 实时查询当前在线人数并做记录
     */
    @Scheduled(cron="*/30 * * * * ? ")
    @Override
    public void recordRealTimeOnlineData() {
        Integer count = userAccessRecordMapper.selectCurrentOnlineUserCount();
        Long currentTimeMillis = System.currentTimeMillis();
        RealTimeOnlineUser realTimeOnlineUser = new RealTimeOnlineUser();
        realTimeOnlineUser.setOnlineCount(count);
        realTimeOnlineUser.setOnlineTimeStamp(currentTimeMillis);
        realTimeOnlineUserMapper.insert(realTimeOnlineUser);
    }

    /**
     * 删除过期数据
     */
    @Scheduled(cron="0 0/10 * * * ? ")
    @Override
    public void deleteExpireData() {
        Long currentTimeMillis = System.currentTimeMillis() - (40 * 60 * 1000);
        Map<String,Object> map = new HashMap<>();
        map.put("endTime",currentTimeMillis);
        realTimeOnlineUserMapper.deleteExpireData(map);
    }
}
