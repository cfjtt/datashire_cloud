package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.JobScore;
import org.apache.ibatis.jdbc.SQL;

public class JobScoreSqlProvider {
    public String insertSelective(JobScore record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("job_score");
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getClassId() != null) {
            sql.VALUES("class_id", "#{classId,jdbcType=INTEGER}");
        }

        if (record.getCourseId() != null) {
            sql.VALUES("course_id", "#{courseId,jdbcType=INTEGER}");
        }

        if (record.getStudentId() != null) {
            sql.VALUES("student_id", "#{studentId,jdbcType=INTEGER}");
        }

        if (record.getExperimentId() != null) {
            sql.VALUES("experiment_id", "#{experimentId,jdbcType=INTEGER}");
        }

        if (record.getScore() != null) {
            sql.VALUES("score", "#{score,jdbcType=INTEGER}");
        }
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        if (record.getTableName() != null) {
            sql.VALUES("table_name", "#{tableName,jdbcType=VARCHAR}");
        }
        if (record.getFileUrl() != null) {
            sql.VALUES("file_url", "#{fileUrl,jdbcType=VARCHAR}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(JobScore record) {
        SQL sql = new SQL();
        sql.UPDATE("job_score");
        if (record.getClassId() != null) {
            sql.SET("class_id = #{classId,jdbcType=INTEGER}");
        }

        if (record.getCourseId() != null) {
            sql.SET("course_id = #{courseId,jdbcType=INTEGER}");
        }

        if (record.getStudentId() != null) {
            sql.SET("student_id = #{studentId,jdbcType=INTEGER}");
        }

        if (record.getExperimentId() != null) {
            sql.SET("experiment_id = #{experimentId,jdbcType=INTEGER}");
        }

        if (record.getScore() != null) {
            sql.SET("score = #{score,jdbcType=INTEGER}");
        }

        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        if (record.getTableName() != null) {
            sql.SET("table_name = #{tableName,jdbcType=VARCHAR}");
        }

        if (record.getFileUrl() != null) {
            sql.SET("file_url = #{fileUrl,jdbcType=VARCHAR}");
        }
        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }
}
