package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.TProjectDoc;
import org.apache.ibatis.jdbc.SQL;

public class TProjectDocSqlProvider {

    public String insertSelective(TProjectDoc record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("t_project_doc");
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        if(record.getFolderId()!=null){
            sql.VALUES("folder_id", "#{folderId,jdbcType=INTEGER}");
        }
        if(record.getContent()!=null){
            sql.VALUES("content", "#{content,jdbcType=VARCHAR}");
        }
        if (record.getCreator() != null) {
            sql.VALUES("creator", "#{creator,jdbcType=INTEGER}");
        }
        if (record.getEditor() != null) {
            sql.VALUES("editor", "#{editor,jdbcType=INTEGER}");
        }
        if (record.getDocName() != null) {
            sql.VALUES("`doc_name`", "#{docName,jdbcType=VARCHAR}");
        }

        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(TProjectDoc record) {
        SQL sql = new SQL();
        sql.UPDATE("t_project_doc");
        if (record.getFolderId() != null) {
            sql.SET("folder_id = #{folderId,jdbcType=INTEGER}");
        }
        if (record.getContent() != null) {
            sql.SET("content = #{content,jdbcType=VARCHAR}");
        }

        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        if (record.getCreator() != null) {
            sql.SET("creator = #{creator,jdbcType=INTEGER}");
        }
        if (record.getEditor() != null) {
            sql.SET("editor = #{editor,jdbcType=INTEGER}");
        }

        if (record.getDocName() != null) {
            sql.SET("doc_name = #{docName,jdbcType=VARCHAR}");
        }
        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }
}
