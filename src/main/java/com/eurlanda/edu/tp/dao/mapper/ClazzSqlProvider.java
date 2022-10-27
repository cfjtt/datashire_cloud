package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Clazz;
import org.apache.ibatis.jdbc.SQL;

public class ClazzSqlProvider {

    public String insertSelective(Clazz record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("class");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        /*if (record.getTermId() != null) {
            sql.VALUES("term_id", "#{termId,jdbcType=INTEGER}");
        }
        if (record.getTeacherId() != null) {
            sql.VALUES("teacher_id", "#{teacherId,jdbcType=INTEGER}");
        }*/
        
        if (record.getDate() != null) {
            sql.VALUES("date", "#{date,jdbcType=DATE}");
        }
        
        if (record.getDuration() != null) {
            sql.VALUES("duration", "#{duration,jdbcType=VARCHAR}");
        }
        
        if (record.getStudentNum() != null) {
            sql.VALUES("student_num", "#{studentNum,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        

        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Clazz record) {
        SQL sql = new SQL();
        sql.UPDATE("class");
        
       /* if (record.getTermId() != null) {
            sql.SET("term_id = #{termId,jdbcType=INTEGER}");
        }*/

        
        if (record.getDate() != null) {
            sql.SET("date = #{date,jdbcType=DATE}");
        }
        
        if (record.getDuration() != null) {
            sql.SET("duration = #{duration,jdbcType=VARCHAR}");
        }
        
        if (record.getStudentNum() != null) {
            sql.SET("student_num = #{studentNum,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        /*if (record.getTeacherId() != null) {
            sql.SET("teacher_id = #{teacherId,jdbcType=INTEGER}");
        }*/
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}