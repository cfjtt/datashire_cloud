package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.UserStatus;
import org.apache.ibatis.jdbc.SQL;

public class UserStatusSqlProvider {
    public String updateByPrimaryKeySelective(UserStatus record) {
        SQL sql = new SQL();
        sql.UPDATE("user_status");

        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }

        if (record.getStatus() != null) {
            sql.SET("status = #{status,jdbcType=INTEGER}");
        }

        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }
}
