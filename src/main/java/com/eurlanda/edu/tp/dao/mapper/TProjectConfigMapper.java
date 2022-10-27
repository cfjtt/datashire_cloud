package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.TProjectConfig;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface TProjectConfigMapper {
    @Delete({
            "delete from t_project_config",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);


    @Insert({
            "insert into t_project_config (role,package_id,project_num)",
            "values (#{role,jdbcType=INTEGER}," +
                    "#{packageId,jdbcType=INTEGER},#{projectNum,jdbcType=INTEGER})"
    })
    int insert(TProjectConfig projectConfig);

    @Select({
            "select",
            "id, role,package_id,project_num",
            "from t_project_config",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="role", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="package_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="project_num", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    TProjectConfig selectByPrimaryKey(Integer id);


    @Update({
            "update t_project_config",
            "set role = #{role,jdbcType=INTEGER},package_id = #{packageId,jdbcType=INTEGER},",
            "project_num = #{projectNum,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TProjectConfig record);

    @Select({
            "<script>",
            "select id, role,package_id, project_num",
            "from t_project_config where 1=1 ",
            "<if test='role != null'> and role = #{role}</if>",
            "<if test='packageId != null'> and package_id = #{packageId}</if>",
            "<if test='projectNum != null'> and project_num = #{projectNum}</if>",
            "<if test='id != null'> and id = #{id}</if>",
            "</script>"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="role", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="package_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="project_num", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<TProjectConfig> selectSelective(TProjectConfig record);
}
