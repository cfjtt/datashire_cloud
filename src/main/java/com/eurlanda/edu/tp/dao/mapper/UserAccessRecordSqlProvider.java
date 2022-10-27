package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.UserAccessRecord;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by test on 2018/4/20.
 */
public class UserAccessRecordSqlProvider {

    public String insertSelective(UserAccessRecord record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("user_access_record");

        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }

        if (record.getAccessDate() != null) {
            sql.VALUES("access_date", "#{accessDate,jdbcType=DATE}");
        }

        if (record.getDuration() != null) {
            sql.VALUES("duration", "#{duration,jdbcType=INTEGER}");
        }

        if (record.getLastTime() != null) {
            sql.VALUES("last_time", "#{lastTime,jdbcType=INTEGER}");
        }
        if (record.getStatus() != null) {
            sql.VALUES("status", "#{status,jdbcType=INTEGER}");
        }


        return sql.toString();
    }

    public String updateByPrimaryKeySelective(UserAccessRecord record) {
        SQL sql = new SQL();
        sql.UPDATE("user_access_record");

        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }

        if (record.getAccessDate() != null) {
            sql.VALUES("access_date", "#{accessDate,jdbcType=DATE}");
        }

        if (record.getDuration() != null) {
            sql.VALUES("duration", "#{duration,jdbcType=INTEGER}");
        }

        if (record.getLastTime() != null) {
            sql.VALUES("last_time", "#{lastTime,jdbcType=INTEGER}");
        }
        if (record.getStatus() != null) {
            sql.VALUES("status", "#{status,jdbcType=INTEGER}");
        }

        sql.WHERE("user_id = #{id,jdbcType=INTEGER}","access_date = #{accessDate,jdbcType=DATE}");

        return sql.toString();
    }

}
