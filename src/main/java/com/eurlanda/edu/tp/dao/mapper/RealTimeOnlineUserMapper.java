package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.RealTimeOnlineUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by test on 2018/4/24.
 */
public interface RealTimeOnlineUserMapper {


    @Select({
            "SELECT online_count onlineCount, online_time_stamp onlineTimeStamp",
            "FROM real_time_online_user",
            "WHERE online_time_stamp > #{endTime}",
            "ORDER BY online_time_stamp DESC"
    })
    List<RealTimeOnlineUser> selectOnlineUserCountByTimeRange(Map<String, Object> map);

    @Insert({
            "INSERT INTO real_time_online_user (`online_count`, `online_time_stamp`)",
            "VALUES (#{onlineCount}, #{onlineTimeStamp})"
    })
    Integer insert(RealTimeOnlineUser realTimeOnlineUser);



    @Delete({
            "DELETE FROM real_time_online_user",
            "WHERE online_time_stamp < #{endTime}"
    })
    Integer deleteExpireData(Map<String,Object> paraMap);


}
