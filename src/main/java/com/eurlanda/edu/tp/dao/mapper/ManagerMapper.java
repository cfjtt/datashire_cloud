package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Manager;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface ManagerMapper {
    @Delete({
        "delete from manager",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into manager (id, user_id, ",
        "mno, name, gender, ",
        "email, phone)",
        "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, ",
        "#{mno,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{gender,jdbcType=INTEGER}, ",
        "#{email,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR})"
    })
    int insert(Manager record);

    @InsertProvider(type=ManagerSqlProvider.class, method="insertSelective")
    int insertSelective(Manager record);

    @Select({
        "select",
        "id, user_id, mno, name, gender, email, phone",
        "from manager",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="mno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="email", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Manager selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ManagerSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Manager record);

    @Update({
        "update manager",
        "set user_id = #{userId,jdbcType=INTEGER},",
          "mno = #{mno,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "gender = #{gender,jdbcType=INTEGER},",
          "email = #{email,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Manager record);
}