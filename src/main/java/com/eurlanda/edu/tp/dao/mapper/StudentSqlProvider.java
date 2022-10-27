package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Student;
import org.apache.ibatis.jdbc.SQL;

public class StudentSqlProvider {

    public String insertSelective(Student record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("student");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }
        
        if (record.getSno() != null) {
            sql.VALUES("sno", "#{sno,jdbcType=VARCHAR}");
        }
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getGender() != null) {
            sql.VALUES("gender", "#{gender,jdbcType=INTEGER}");
        }
        
        if (record.getBirthday() != null) {
            sql.VALUES("birthday", "#{birthday,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Student record) {
        SQL sql = new SQL();
        sql.UPDATE("student");
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }
        
        if (record.getSno() != null) {
            sql.SET("sno = #{sno,jdbcType=VARCHAR}");
        }
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getGender() != null) {
            sql.SET("gender = #{gender,jdbcType=INTEGER}");
        }
        
        if (record.getBirthday() != null) {
            sql.SET("birthday = #{birthday,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}