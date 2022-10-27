package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Resource;
import org.apache.ibatis.jdbc.SQL;

public class ResourceSqlProvider {

    public String insertSelective(Resource record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("resource");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getUrl() != null) {
            sql.VALUES("url", "#{url,jdbcType=VARCHAR}");
        }
        
        if (record.getWidth() != null) {
            sql.VALUES("width", "#{width,jdbcType=INTEGER}");
        }
        
        if (record.getHeight() != null) {
            sql.VALUES("height", "#{height,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Resource record) {
        SQL sql = new SQL();
        sql.UPDATE("resource");
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getUrl() != null) {
            sql.SET("url = #{url,jdbcType=VARCHAR}");
        }
        
        if (record.getWidth() != null) {
            sql.SET("width = #{width,jdbcType=INTEGER}");
        }
        
        if (record.getHeight() != null) {
            sql.SET("height = #{height,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}