package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    @Delete({
        "delete from user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into user (id, role, ",
        "username, password,password2,volume_id)",
        "values (#{id,jdbcType=INTEGER}, #{role,jdbcType=INTEGER}, ",
        "#{username,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR},#{password2,jdbcType=VARCHAR},#{volumeId,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert(User record);

    @InsertProvider(type=UserSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insertSelective(User record);

    @Select({
        "select",
        "id, role, username, password,password2,volume_id",
        "from user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="role", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="username", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="password", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="password2", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    User selectByPrimaryKey(Integer id);



    @Select({
            "select",
            "id, role, username, password,password2,volume_id volumeId,token_create_time tokenCreateTime,is_remind isRemind,token token  ",
            "from user",
            "where id = #{id,jdbcType=INTEGER}"
    })
    User selectByPrimaryKey2(Integer id);

    @Select({
            "select",
            "id, role, username, password,password2,volume_id ",
            "from user",
            "where username = #{userName,jdbcType=VARCHAR}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="role", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="username", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="password", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="password2", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    User selectByUserName(String userName);


    @Select({
            "select",
            "id, role, username, password,password2,volume_id,token_create_time tokenCreateTime,is_remind isRemind,token token ",
            "from user",
            "where username = #{userName,jdbcType=VARCHAR}"
    })

    User selectByUserName2(String userName);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Update({
        "update user",
          "set token_create_time = #{tokenCreateTime},",
          "is_remind = #{isRemind},",
          "token = #{token}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey2(User record);

    @Update({
            "update user",
            "set role = #{role,jdbcType=INTEGER},",
            "username = #{username,jdbcType=VARCHAR},",
            "password = #{password,jdbcType=VARCHAR},",
            "password2 = #{password2,jdbcType=VARCHAR},",
            "volume_id = #{volumeId,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(User record);



    @Select({
            "<script>",
            "select username from user where id in ",
            "<foreach collection='idList' item='id' open='(' close=')' separator=',' > #{id} </foreach>",
            "</script>"
    })

    @ConstructorArgs({
            @Arg(column="username", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })

    List<String> findUserNameListByUserIdList(Map<String,Object> map);

    @Select({
            "<script>",
            "select username as no ,password2,id as userId from user where id in ",
            "<foreach collection='idList' item='id' open='(' close=')' separator=',' > #{id} </foreach>",
            "</script>"
    })
    List<Map<String,Object>> findUserMsgAndIdByUserIdList(@Param("idList") List<Integer> idList);
}