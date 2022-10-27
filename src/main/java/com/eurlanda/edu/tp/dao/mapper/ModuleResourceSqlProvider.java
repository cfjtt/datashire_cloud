package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.ModuleResource;
import org.apache.ibatis.jdbc.SQL;

public class ModuleResourceSqlProvider {

    public String insertSelective(ModuleResource record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("module_resource");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getModuleId() != null) {
            sql.VALUES("module_id", "#{moduleId,jdbcType=INTEGER}");
        }
        
        if (record.getResourceId() != null) {
            sql.VALUES("resource_id", "#{resourceId,jdbcType=INTEGER}");
        }
        
        if (record.getType() != null) {
            sql.VALUES("type", "#{type,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ModuleResource record) {
        SQL sql = new SQL();
        sql.UPDATE("module_resource");
        
        if (record.getModuleId() != null) {
            sql.SET("module_id = #{moduleId,jdbcType=INTEGER}");
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