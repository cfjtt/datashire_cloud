package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Manager;
import com.eurlanda.edu.tp.dao.entity.TProjectDocAnnex;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TProjectDocAnnexMapper {
    @Delete({
            "delete from t_project_doc_annex",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);


    @Insert({
            "insert into t_project_doc_annex (doc_id,file_name,create_time)",
            "values (#{docId,jdbcType=INTEGER}," +
                    "#{fileName,jdbcType=VARCHAR},",
            "#{createTime,jdbcType=DATE})"
    })
    int insert(TProjectDocAnnex projectDocAnnex);

    @Select({
            "select",
            "id, doc_id,file_name,create_time",
            "from t_project_doc_annex",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="doc_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="file_name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE)
    })
    TProjectDocAnnex selectByPrimaryKey(Integer id);


    @Update({
            "update t_project_doc_annex",
            "set doc_id = #{docId,jdbcType=INTEGER},file_name = #{fileName,jdbcType=VARCHAR},",
            "create_time = #{createTime,jdbcType=DATE},",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TProjectDocAnnex record);



    @Select({
            "select",
            "id, doc_id,file_name,create_time",
            "from t_project_doc_annex",
            "where doc_id = #{docId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="doc_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="file_name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE)
    })
    List<TProjectDocAnnex> findByDocId(Integer docId);


    @Delete({
            "delete from t_project_doc_annex where doc_id=#{docId} and file_name=#{fileName}"
    })
    int deleteBydocIdAndfileName(Map<String,Object> map);

    @Delete({"delete from t_project_doc_annex where doc_id=#{docId}"})
    int deleteBydocId(Integer docId);

}
