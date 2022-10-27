package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.HomeworkResource;
import org.apache.ibatis.jdbc.SQL;

public class HomeworkResourceSqlProvider {

    public String insertSelective(HomeworkResource record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("homework_resource");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getHomeworkId() != null) {
            sql.VALUES("homework_id", "#{homeworkId,jdbcType=INTEGER}");
        }
        
        if (record.getResourceId() != null) {
            sql.VALUES("resource_id", "#{resourceId,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(HomeworkResource record) {
        SQL sql = new SQL();
        sql.UPDATE("homework_resource");
        
        if (record.getHomeworkId() != null) {
            sql.SET("homework_id = #{homeworkId,jdbcType=INTEGER}");
        }
        
        if (record.getResourceId() != null) {
            sql.SET("resource_id = #{resourceId,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}