package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Module;
import org.apache.ibatis.jdbc.SQL;

public class ModuleSqlProvider {

    public String insertSelective(Module record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("module");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getCourseId() != null) {
            sql.VALUES("course_id", "#{courseId,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }

        if (record.getOrderId() != null) {
            sql.VALUES("order_id", "#{orderId,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Module record) {
        SQL sql = new SQL();
        sql.UPDATE("module");
        
        if (record.getCourseId() != null) {
            sql.SET("course_id = #{courseId,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }

        if (record.getOrderId() != null) {
            sql.SET("order_id = #{orderId,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }

}