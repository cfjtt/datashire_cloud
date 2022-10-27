package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.CourseResource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface CourseResourceMapper {
    @Delete({
        "delete from course_resource",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into course_resource (id, course_id, ",
        "resource_id, type)",
        "values (#{id,jdbcType=INTEGER}, #{courseId,jdbcType=INTEGER}, ",
        "#{resourceId,jdbcType=INTEGER}, #{type,jdbcType=INTEGER})"
    })
    int insert(CourseResource record);

    @InsertProvider(type=CourseResourceSqlProvider.class, method="insertSelective")
    int insertSelective(CourseResource record);

    @Select({
        "select",
        "id, course_id, resource_id, type",
        "from course_resource",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="type", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    CourseResource selectByPrimaryKey(Integer id);

    @UpdateProvider(type=CourseResourceSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(CourseResource record);

    @Update({
        "update course_resource",
        "set course_id = #{courseId,jdbcType=INTEGER},",
          "resource_id = #{resourceId,jdbcType=INTEGER},",
          "type = #{type,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(CourseResource record);

    @Select({
            "select",
            "id, course_id, resource_id, type",
            "from course_resource",
            "where course_id = #{courseId,jdbcType=INTEGER} and type = #{type, jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="type", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    CourseResource selectByCourseIdAndType(@Param("courseId") Integer courseId, @Param("type") Integer type);
}