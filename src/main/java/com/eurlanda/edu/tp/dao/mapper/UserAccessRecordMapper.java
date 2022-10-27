package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.UserAccessRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * Created by test on 2018/4/20.
 */
public interface UserAccessRecordMapper {

    @Insert({
            "INSERT INTO  `user_access_record` (`user_id`, `access_date`, `duration`, `last_time`, `status`,`login_time`) " ,
            "VALUES (#{userId}, #{accessDate}, #{duration}, #{lastTime},#{status},#{loginTime})"
    })
    int insertUserAccessRecord(UserAccessRecord record);

    @Select({
            "SELECT `user_id` userId , `access_date` accessDate, `duration`, `last_time` lastTime, `status` FROM user_access_record",
            "WHERE user_id = #{userId}",
            "AND access_date = #{accessDate}"
    })
    UserAccessRecord selectUserRecordByUserIdAndDate(UserAccessRecord record);

    @Update({
          "<script>",
          "UPDATE `user_access_record`",
          "<trim prefix='set' suffixOverrides=','>",
              "<if test = 'duration != null'>duration = #{duration},</if> ",
              "<if test = 'lastTime != null'>last_time = #{lastTime},</if>",
              "<if test = 'status != null'>status = #{status},</if> ",
              "<if test = 'loginTime != null'>login_time = #{loginTime},</if> ",
          "</trim>",
          "WHERE `user_id`= #{userId} AND `access_date` = #{accessDate}",
          "</script>",
    })
    int updateUserRecordByUserIdAndDate(UserAccessRecord record);


    @Delete({
            "DELETE FROM user_access_record",
            "WHERE access_date < SUBDATE(CURDATE(),INTERVAL 6 MONTH)"
    })
    int deleteExpireRecord();


    @Select({
        "SELECT `user_id` userId , `access_date` accessDate, `duration`, `last_time` lastTime, `status` from user_access_record",
        "WHERE `status` = 1",
        "AND last_time < #{start}",
        "LIMIT #{pageIndex},#{pageSize}"
    })
    List<UserAccessRecord> selectNotUpdatedUserRecordPaged(Map<String,Object> map);

    @Select({
            "SELECT `user_id` userId , `access_date` accessDate, `duration`, `last_time` lastTime, `status` from user_access_record",
            "WHERE last_time > #{endTime}",
            "AND access_date = #{accessDate}"
    })
    List<UserAccessRecord> selectRealTimeOnlineUserByTimeRange(Map<String,Object> para);


    @Select({
            "SELECT",
                "t.duration,",
                "COUNT(1) count",
            "FROM",
                "(",
                    "SELECT",
                        "floor(SUM(duration) / 60) duration",
                    "FROM",
                        "user_access_record",
                    "WHERE",
                        "access_date > SUBDATE(CURDATE(), INTERVAL #{difference} WEEK)",
                    "GROUP BY",
                        "user_id",
                ") t",
            "GROUP BY",
                "t.duration"
    })
    List<Map> selectAccumulativeOnlineUserByTimeRange(Map<String,Object> paraMap);


    @Select({
            "SELECT\n" +
                    "\tCOUNT(1)\n" +
                    "FROM\n" +
                    "\tuser_access_record\n" +
                    "WHERE\n" +
                    "\tSTATUS = 1\n" +
                    "AND user_id != (SELECT id FROM `user` WHERE role = 3)"
    })
    Integer selectCurrentOnlineUserCount();

    @Select({
            "SELECT access_date accessDate,group_concat( DISTINCT user_id separator ',') idsStr,sum(duration) duration FROM user_access_record",
            "WHERE access_date > SUBDATE(CURDATE(),INTERVAL #{difference} DAY)",
            "AND access_date != CURDATE()",
            "GROUP BY access_date"
    })
    List<Map> selectDayOnlineAvgDataByAccessDate(Map<String,Object> paraMap);

    @Select({
            "SELECT YEARWEEK(access_date,3) weekNum,group_concat( DISTINCT user_id separator ',') idsStr,sum(duration) duration FROM user_access_record",
            "WHERE access_date > SUBDATE(CURDATE(),INTERVAL #{difference} WEEK)",
            "GROUP BY weekNum"
    })
    List<Map> selectWeekOnlineAvgDataByAccessDate(Map<String,Object> paraMap);

    @Select({
            "SELECT DATE_FORMAT(access_date,'%Y-%m') monthNum,group_concat( DISTINCT user_id separator ',') idsStr,sum(duration) duration FROM user_access_record",
            "WHERE access_date > SUBDATE(CURDATE(),INTERVAL #{difference} MONTH)",
            "GROUP BY monthNum"
    })
    List<Map> selectMonthOnlineAvgDataByAccessDate(Map<String,Object> paraMap);

    @Select({
            "SELECT record.login_time,`user`.id user_id FROM user_access_record record",
            "JOIN `user` ",
            "ON `user`.id = record.user_id",
            "AND record.login_time is NOT NULL",
           /* "WHERE access_date = CURDATE()",*/
            /*"and login_time > SUBDATE(#{date},INTERVAL #{difference} MINUTE)",*/
            "ORDER BY record.login_time desc",
            "LIMIT #{pageIndex},#{pageSize}",
    })
    List<Map> selectRecentlyLoginUser(Map<String,Object> paraMap);
}
