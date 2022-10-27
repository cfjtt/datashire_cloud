package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.CourseResource;
import org.apache.ibatis.jdbc.SQL;

public class CourseResourceSqlProvider {

    public String insertSelective(CourseResource record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("course_resource");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getCourseId() != null) {
            sql.VALUES("course_id", "#{courseId,jdbcType=INTEGER}");
        }
        
        if (record.getResourceId() != null) {
            sql.VALUES("resource_id", "#{resourceId,jdbcType=INTEGER}");
        }
        
        if (record.getType() != null) {
            sql.VALUES("type", "#{type,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(CourseResource record) {
        SQL sql = new SQL();
        sql.UPDATE("course_resource");
        
        if (record.getCourseId() != null) {
            sql.SET("course_id = #{courseId,jdbcType=INTEGER}");
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