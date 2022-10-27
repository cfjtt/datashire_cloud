package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.HomeworkResource;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface HomeworkResourceMapper {
    @Delete({
        "delete from homework_resource",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into homework_resource (id, homework_id, ",
        "resource_id)",
        "values (#{id,jdbcType=INTEGER}, #{homeworkId,jdbcType=INTEGER}, ",
        "#{resourceId,jdbcType=INTEGER})"
    })
    int insert(HomeworkResource record);

    @InsertProvider(type=HomeworkResourceSqlProvider.class, method="insertSelective")
    int insertSelective(HomeworkResource record);

    @Select({
        "select",
        "id, homework_id, resource_id",
        "from homework_resource",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="homework_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    HomeworkResource selectByPrimaryKey(Integer id);

    @UpdateProvider(type=HomeworkResourceSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(HomeworkResource record);

    @Update({
        "update homework_resource",
        "set homework_id = #{homeworkId,jdbcType=INTEGER},",
          "resource_id = #{resourceId,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(HomeworkResource record);

    @Select({
            "select",
            "id, homework_id, resource_id",
            "from homework_resource",
            "where homework_id = #{homeworkId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="homework_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="resource_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    HomeworkResource selectByHomeworkId(Integer homeworkId);
}