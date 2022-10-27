package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.StudentHomework;
import org.apache.ibatis.jdbc.SQL;

public class StudentHomeworkSqlProvider {

    public String insertSelective(StudentHomework record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("student_homework");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getStudentId() != null) {
            sql.VALUES("student_id", "#{studentId,jdbcType=INTEGER}");
        }
        
        if (record.getHomeworkId() != null) {
            sql.VALUES("homework_id", "#{homeworkId,jdbcType=INTEGER}");
        }
        
        if (record.getCloudwareId() != null) {
            sql.VALUES("cloudware_id", "#{cloudwareId,jdbcType=INTEGER}");
        }
        
        if (record.getComment() != null) {
            sql.VALUES("comment", "#{comment,jdbcType=VARCHAR}");
        }
        
        if (record.getScore() != null) {
            sql.VALUES("score", "#{score,jdbcType=INTEGER}");
        }
        
        if (record.getSubmissionDate() != null) {
            sql.VALUES("submission_date", "#{submissionDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getLastEditDate() != null) {
            sql.VALUES("lastEdit_date", "#{lastEditDate,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(StudentHomework record) {
        SQL sql = new SQL();
        sql.UPDATE("student_homework");
        
        if (record.getStudentId() != null) {
            sql.SET("student_id = #{studentId,jdbcType=INTEGER}");
        }
        
        if (record.getHomeworkId() != null) {
            sql.SET("homework_id = #{homeworkId,jdbcType=INTEGER}");
        }
        
        if (record.getCloudwareId() != null) {
            sql.SET("cloudware_id = #{cloudwareId,jdbcType=INTEGER}");
        }
        
        if (record.getComment() != null) {
            sql.SET("comment = #{comment,jdbcType=VARCHAR}");
        }
        
        if (record.getScore() != null) {
            sql.SET("score = #{score,jdbcType=INTEGER}");
        }
        
        if (record.getSubmissionDate() != null) {
            sql.SET("submission_date = #{submissionDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getLastEditDate() != null) {
            sql.SET("lastEdit_date = #{lastEditDate,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}