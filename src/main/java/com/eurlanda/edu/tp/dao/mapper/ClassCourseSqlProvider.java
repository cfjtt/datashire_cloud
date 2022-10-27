package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.ClassCourse;
import com.eurlanda.edu.tp.dao.entity.Clazz;
import org.apache.ibatis.jdbc.SQL;

public class ClassCourseSqlProvider {


    public String insertSelective(ClassCourse record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("class_course");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        if (record.getCourseId() != null) {
            sql.VALUES("class_id", "#{classId,jdbcType=INTEGER}");
        }
        if (record.getCourseId() != null) {
            sql.VALUES("course_id", "#{courseId,jdbcType=INTEGER}");
        }

        if (record.getTeacherId() != null) {
            sql.VALUES("teacher_id", "#{teacherId,jdbcType=INTEGER}");
        }

        if (record.getDate() != null) {
            sql.VALUES("date", "#{date,jdbcType=DATE}");
        }
        if (record.getDate() != null) {
            sql.VALUES("school_year", "#{schoolYear,jdbcType=DATE}");
        }
        if (record.getDate() != null) {
            sql.VALUES("semester", "#{semester,jdbcType=DATE}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ClassCourse record) {
        SQL sql = new SQL();
        sql.UPDATE("class_course");
        if (record.getCourseId() != null) {
            sql.SET("class_id = #{classId,jdbcType=INTEGER}");
        }
        if (record.getCourseId() != null) {
            sql.SET("course_id = #{courseId,jdbcType=INTEGER}");
        }

        if (record.getDate() != null) {
            sql.SET("date = #{date,jdbcType=DATE}");
        }

        if (record.getTeacherId() != null) {
            sql.SET("teacher_id = #{teacherId,jdbcType=INTEGER}");
        }
        if (record.getTeacherId() != null) {
            sql.SET("school_year = #{schoolYear,jdbcType=INTEGER}");
        }

        if (record.getTeacherId() != null) {
            sql.SET("semester = #{semester,jdbcType=INTEGER}");
        }

        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }
}
