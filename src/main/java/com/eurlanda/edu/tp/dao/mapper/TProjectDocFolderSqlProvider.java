package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.TProjectDocFolder;
import org.apache.ibatis.jdbc.SQL;

public class TProjectDocFolderSqlProvider {
    public String insertSelective(TProjectDocFolder folder) {
        SQL sql = new SQL();
        sql.INSERT_INTO("t_project_doc_folder");
        if (folder.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        if (folder.getName() != null) {
            sql.VALUES("`name`", "#{name,jdbcType=VARCHAR}");
        }

        if (folder.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }

        if (folder.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        if (folder.getProjectId() != null) {
            sql.VALUES("project_id", "#{projectId,jdbcType=INTEGER}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(TProjectDocFolder folder) {
        SQL sql = new SQL();
        sql.UPDATE("t_project_doc_folder");

        if (folder.getName() != null) {
            sql.SET("`name` = #{name,jdbcType=VARCHAR}");
        }

        if (folder.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }

        if (folder.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }

       if(folder.getProjectId()!=null){
            sql.SET("project_id = #{projectId,jdbcType=INTEGER}");
       }
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        return sql.toString();
    }
}
