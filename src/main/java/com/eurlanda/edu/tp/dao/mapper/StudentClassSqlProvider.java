package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.StudentClass;
import org.apache.ibatis.jdbc.SQL;

public class StudentClassSqlProvider {

    public String insertSelective(StudentClass record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("student_class");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getStudentId() != null) {
            sql.VALUES("student_id", "#{studentId,jdbcType=INTEGER}");
        }
        
        if (record.getClassId() != null) {
            sql.VALUES("class_id", "#{classId,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(StudentClass record) {
        SQL sql = new SQL();
        sql.UPDATE("student_class");
        
        if (record.getStudentId() != null) {
            sql.SET("student_id = #{studentId,jdbcType=INTEGER}");
        }
        
        if (record.getClassId() != null) {
            sql.SET("class_id = #{classId,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}