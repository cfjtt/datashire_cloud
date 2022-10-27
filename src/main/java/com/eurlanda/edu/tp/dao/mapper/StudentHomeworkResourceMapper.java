package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.StudentHomeworkResource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface StudentHomeworkResourceMapper {
    @Delete({
        "delete from student_homework_resource",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into student_homework_resource (id, student_homework_id, ",
        "resource_id, type)",
        "values (#{id,jdbcType=INTEGER}, #{studentHomeworkId,jdbcType=INTEGER}, ",
        "#{resourceId,jdbcType=INTEGER}, #{type,jdbcType=INTEGER})"
    })
    int insert(StudentHomeworkResource record);

    @InsertProvider(type=StudentHomeworkResourceSqlProvider.class, method="insertSelective")
    int insertSelective(StudentHomeworkResource record);

    @Select({
        "select",
        "id, student_homework_id, resource_id, type",
        "from student_homework_resource",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="student_homework_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="type", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    StudentHomeworkResource selectByPrimaryKey(Integer id);

    @UpdateProvider(type=StudentHomeworkResourceSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(StudentHomeworkResource record);

    @Update({
        "update student_homework_resource",
        "set student_homework_id = #{studentHomeworkId,jdbcType=INTEGER},",
          "resource_id = #{resourceId,jdbcType=INTEGER},",
          "type = #{type,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(StudentHomeworkResource record);

    @Select({
            "select",
            "id, student_homework_id, resource_id, type",
            "from student_homework_resource",
            "where student_homework_id = #{studentHomeworkId,jdbcType=INTEGER} and type = #{type, jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="student_homework_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="type", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    StudentHomeworkResource selectByStudentHomeworkIdAndType(
            @Param("studentHomeworkId") Integer studentHomeworkId, @Param("type") Integer type);
}