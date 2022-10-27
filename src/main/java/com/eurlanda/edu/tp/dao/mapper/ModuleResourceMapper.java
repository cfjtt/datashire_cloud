package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.ModuleResource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

public interface ModuleResourceMapper {
    @Delete({
        "delete from module_resource",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into module_resource (id, module_id, ",
        "resource_id, type)",
        "values (#{id,jdbcType=INTEGER}, #{moduleId,jdbcType=INTEGER}, ",
        "#{resourceId,jdbcType=INTEGER}, #{type,jdbcType=INTEGER})"
    })
    int insert(ModuleResource record);

    @InsertProvider(type=ModuleResourceSqlProvider.class, method="insertSelective")
    int insertSelective(ModuleResource record);

    @Select({
        "select",
        "id, module_id, resource_id, type",
        "from module_resource",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="module_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="type", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    ModuleResource selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ModuleResourceSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ModuleResource record);

    @Update({
        "update module_resource",
        "set module_id = #{moduleId,jdbcType=INTEGER},",
          "resource_id = #{resourceId,jdbcType=INTEGER},",
          "type = #{type,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ModuleResource record);

    @Select({
            "select",
            "id, module_id, resource_id, type",
            "from module_resource",
            "where module_id = #{moduleId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="module_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="type", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<ModuleResource> selectByModuleId(Integer moduleId);

    @Select({
            "select",
            "id, module_id, resource_id, type",
            "from module_resource",
            "where module_id = #{moduleId,jdbcType=INTEGER} and resource_id = #{resourceId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="module_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="type", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    ModuleResource selectByModuleIdAndResourceId(@Param("moduleId") int moduleId, @Param("resourceId") int resourceId);

    @Select({
            "select",
            "r.id resourceId, r.name, r.url, r.width, r.height",
            "from module_resource mr",
            "inner join resource r on mr.resource_id = r.id",
            "where mr.module_id = #{moduleId,jdbcType=INTEGER} and type = #{type,jdbcType=INTEGER}"
    })
    List<Map> selectAllByModuleIdAndType(@Param("moduleId") int moduleId, @Param("type") int type);
}