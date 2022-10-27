package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.StudentHomeworkResource;
import org.apache.ibatis.jdbc.SQL;

public class StudentHomeworkResourceSqlProvider {

    public String insertSelective(StudentHomeworkResource record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("student_homework_resource");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getStudentHomeworkId() != null) {
            sql.VALUES("student_homework_id", "#{studentHomeworkId,jdbcType=INTEGER}");
        }
        
        if (record.getResourceId() != null) {
            sql.VALUES("resource_id", "#{resourceId,jdbcType=INTEGER}");
        }
        
        if (record.getType() != null) {
            sql.VALUES("type", "#{type,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(StudentHomeworkResource record) {
        SQL sql = new SQL();
        sql.UPDATE("student_homework_resource");
        
        if (record.getStudentHomeworkId() != null) {
            sql.SET("student_homework_id = #{studentHomeworkId,jdbcType=INTEGER}");
        }
        
        if (record.getResourceId() != null) {
            sql.SET("resource_id = #{resourceId,jdbcType=INTEGER}");
        }
        
        if (record.getType() != null) {
            sql.SET("type = #{type,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}