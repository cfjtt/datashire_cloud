package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Manager;
import com.eurlanda.edu.tp.dao.entity.TProjectDocFolder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TProjectDocFolderMapper {
    @Delete({
            "delete from t_project_doc_folder",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);


    @Insert({
            "insert into t_project_doc_folder (name,create_time,update_time,project_id)",
            "values (#{name,jdbcType=VARCHAR}," +
                    "#{createTime,jdbcType=DATE},",
            "#{updateTime,jdbcType=DATE},#{projectId,jdbcType=INTEGER})"
    })
    int insert(TProjectDocFolder projectDocFolder);

    @InsertProvider(type=TProjectDocFolderSqlProvider.class, method="insertSelective")
    int insertSelective(TProjectDocFolder record);

    @UpdateProvider(type=TProjectDocFolderSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TProjectDocFolder record);

    @Select({
            "select",
            "id, name,create_time,update_time,project_id",
            "from t_project_doc_folder",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="project_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    TProjectDocFolder selectByPrimaryKey(Integer id);


    @Update({
            "update t_project_doc_folder",
            "set name = #{name,jdbcType=VARCHAR},create_time = #{createTime,jdbcType=DATE},",
            "update_time = #{updateTime,jdbcType=DATE},",
            "project_id = #{projectId,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TProjectDocFolder record);



    @Select({
            "select",
            "id, name,create_time,update_time,project_id",
            "from t_project_doc_folder",
            "where project_id=#{projectId} and name=BINARY #{name}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="project_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    TProjectDocFolder selectByProjectIdAndName(Map<String,Object> map);

    @Delete({
            "delete from t_project_doc_folder",
            "where project_id = #{projectId} and name=#{name}"
    })
    int deleteByProjectIdAndName(Map<String,Object> map);


    @Delete({
            "delete from t_project_doc_folder",
            "where project_id = #{projectId}"
    })
    int deleteByProjectId(Integer projectId);

    @Select({
            "select",
            "id, name,create_time,update_time,project_id",
            "from t_project_doc_folder",
            "where project_id=#{projectId}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="project_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<TProjectDocFolder> findByProjectId(int projectId);


    @Select({
            "select",
            "id, name,create_time,update_time,project_id",
            "from t_project_doc_folder where project_id=#{projectId}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="project_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<TProjectDocFolder> findAllFolder(Integer projectId);
}
