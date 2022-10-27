package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.UserStatus;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserStatusMapper {
    @Delete({
            "delete from user_status",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into user_status (user_id, ",
            "status)",
            "values (#{userId,jdbcType=INTEGER}, ",
            "#{status,jdbcType=INTEGER})"
    })
    int insert(UserStatus record);

    @Select({
            "select",
            "id, user_id, status",
            "from user_status",
            "where id = #{id,jdbcType=INTEGER}"
    })
    UserStatus selectByPrimaryKey(Integer id);

    @UpdateProvider(type=UserStatusSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserStatus record);

    @Update({
            "update user_status",
            "set user_id = #{userId,jdbcType=INTEGER},",
            "status = #{status,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(UserStatus record);

    @Select({
            "select",
            "id, user_id, status",
            "from user_status",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    List<UserStatus> selectUserStatusByUserId(Integer userId);
}
