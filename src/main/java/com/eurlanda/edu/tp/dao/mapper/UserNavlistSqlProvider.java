package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.UserNavlist;
import org.apache.ibatis.jdbc.SQL;

public class UserNavlistSqlProvider {

    public String insertSelective(UserNavlist record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("user_navlist");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getRole() != null) {
            sql.VALUES("role", "#{role,jdbcType=INTEGER}");
        }
        
        if (record.getNavlistId() != null) {
            sql.VALUES("navlist_id", "#{navlistId,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(UserNavlist record) {
        SQL sql = new SQL();
        sql.UPDATE("user_navlist");
        
        if (record.getRole() != null) {
            sql.SET("role = #{role,jdbcType=INTEGER}");
        }
        
        if (record.getNavlistId() != null) {
            sql.SET("navlist_id = #{navlistId,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}