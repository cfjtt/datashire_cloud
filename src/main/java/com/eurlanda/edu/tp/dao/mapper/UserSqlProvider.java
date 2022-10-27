package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.User;
import org.apache.ibatis.jdbc.SQL;

public class UserSqlProvider {

    public String insertSelective(User record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("user");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getRole() != null) {
            sql.VALUES("role", "#{role,jdbcType=INTEGER}");
        }
        
        if (record.getUsername() != null) {
            sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            sql.VALUES("password", "#{password,jdbcType=VARCHAR}");
        }
        if(record.getPassword2() != null){
            sql.VALUES("password2","#{password2,jdbcType=VARCHAR}");
        }
        if(record.getVolumeId() != null){
            sql.VALUES("volume_id","#{volumeId,jdbcType=VARCHAR}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(User record) {
        SQL sql = new SQL();
        sql.UPDATE("user");
        
        if (record.getRole() != null) {
            sql.SET("role = #{role,jdbcType=INTEGER}");
        }
        
        if (record.getUsername() != null) {
            sql.SET("username = #{username,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            sql.SET("password = #{password,jdbcType=VARCHAR}");
        }
        if(record.getPassword2() != null){
            sql.SET("password2 = #{password2,jdbcType=VARCHAR}");
        }
        if(record.getVolumeId() != null){
            sql.SET("volume_id = #{volumeId,jdbcType=VARCHAR}");
        }
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}