package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Homework;
import org.apache.ibatis.jdbc.SQL;

public class HomeworkSqlProvider {

    public String insertSelective(Homework record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("homework");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getModuleId() != null) {
            sql.VALUES("module_id", "#{moduleId,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getDescription() != null) {
            sql.VALUES("description", "#{description,jdbcType=VARCHAR}");
        }
        
        if (record.getCloudwareType() != null) {
            sql.VALUES("cloudware_type", "#{cloudwareType,jdbcType=INTEGER}");
        }
        
        if (record.getPublishDate() != null) {
            sql.VALUES("publish_date", "#{publishDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getDeadlineDate() != null) {
            sql.VALUES("deadline_date", "#{deadlineDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getClassId() != null) {
            sql.VALUES("class_id", "#{classId,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Homework record) {
        SQL sql = new SQL();
        sql.UPDATE("homework");
        
        if (record.getModuleId() != null) {
            sql.SET("module_id = #{moduleId,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getDescription() != null) {
            sql.SET("description = #{description,jdbcType=VARCHAR}");
        }
        
        if (record.getCloudwareType() != null) {
            sql.SET("cloudware_type = #{cloudwareType,jdbcType=INTEGER}");
        }
        
        if (record.getPublishDate() != null) {
            sql.SET("publish_date = #{publishDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getDeadlineDate() != null) {
            sql.SET("deadline_date = #{deadlineDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getClassId() != null) {
            sql.SET("class_id = #{classId,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}