package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Teacher;
import org.apache.ibatis.jdbc.SQL;

public class TeacherSqlProvider {

    public String insertSelective(Teacher record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("teacher");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }
        
        if (record.getTno() != null) {
            sql.VALUES("tno", "#{tno,jdbcType=VARCHAR}");
        }
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getGender() != null) {
            sql.VALUES("gender", "#{gender,jdbcType=INTEGER}");
        }
        
        if (record.getTitle() != null) {
            sql.VALUES("title", "#{title,jdbcType=INTEGER}");
        }
        
        if (record.getEmail() != null) {
            sql.VALUES("email", "#{email,jdbcType=VARCHAR}");
        }
        
        if (record.getPhone() != null) {
            sql.VALUES("phone", "#{phone,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Teacher record) {
        SQL sql = new SQL();
        sql.UPDATE("teacher");
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }
        
        if (record.getTno() != null) {
            sql.SET("tno = #{tno,jdbcType=VARCHAR}");
        }
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getGender() != null) {
            sql.SET("gender = #{gender,jdbcType=INTEGER}");
        }
        
        if (record.getTitle() != null) {
            sql.SET("title = #{title,jdbcType=INTEGER}");
        }
        
        if (record.getEmail() != null) {
            sql.SET("email = #{email,jdbcType=VARCHAR}");
        }
        
        if (record.getPhone() != null) {
            sql.SET("phone = #{phone,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}