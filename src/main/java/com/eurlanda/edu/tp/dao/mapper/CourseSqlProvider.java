package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Course;
import org.apache.ibatis.jdbc.SQL;

public class CourseSqlProvider {

    public String insertSelective(Course record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("course");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
       /* if (record.getTeacherId() != null) {
            sql.VALUES("teacher_id", "#{teacherId,jdbcType=INTEGER}");
        }*/
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getDescription() != null) {
            sql.VALUES("description", "#{description,jdbcType=VARCHAR}");
        }
        if (record.getVolumeId() != null) {
            sql.VALUES("volume_id", "#{volumeId,jdbcType=VARCHAR}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Course record) {
        SQL sql = new SQL();
        sql.UPDATE("course");
        
       /* if (record.getTeacherId() != null) {
            sql.SET("teacher_id = #{teacherId,jdbcType=INTEGER}");
        }*/
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getDescription() != null) {
            sql.SET("description = #{description,jdbcType=VARCHAR}");
        }

        if (record.getVolumeId() != null) {
            sql.SET("volume_id = #{volumeId,jdbcType=INTEGER}");
        }

        if (record.getFolderName() != null) {
            sql.SET("folder_name = #{folderName,jdbcType=VARCHAR}");
        }

        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}