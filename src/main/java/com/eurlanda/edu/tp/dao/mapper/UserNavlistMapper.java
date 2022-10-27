package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.UserNavlist;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface UserNavlistMapper {
    @Delete({
        "delete from user_navlist",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into user_navlist (id, role, ",
        "navlist_id)",
        "values (#{id,jdbcType=INTEGER}, #{role,jdbcType=INTEGER}, ",
        "#{navlistId,jdbcType=INTEGER})"
    })
    int insert(UserNavlist record);

    @InsertProvider(type=UserNavlistSqlProvider.class, method="insertSelective")
    int insertSelective(UserNavlist record);

    @Select({
        "select",
        "id, role, navlist_id",
        "from user_navlist",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="role", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="navlist_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    UserNavlist selectByPrimaryKey(Integer id);

    @UpdateProvider(type=UserNavlistSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserNavlist record);

    @Update({
        "update user_navlist",
        "set role = #{role,jdbcType=INTEGER},",
          "navlist_id = #{navlistId,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(UserNavlist record);
}