package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Teacher;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

public interface TeacherMapper {
    @Delete({
        "delete from teacher",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Delete({
            "delete from teacher",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    int deleteByUserId(Integer userId);

    @Insert({
        "insert into teacher (id, user_id, ",
        "tno, name, gender, ",
        "title, email, phone)",
        "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, ",
        "#{tno,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{gender,jdbcType=INTEGER}, ",
        "#{title,jdbcType=INTEGER}, #{email,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR})"
    })
    int insert(Teacher record);

    @InsertProvider(type=TeacherSqlProvider.class, method="insertSelective")
    int insertSelective(Teacher record);

    @Select({
        "select",
        "id, user_id, tno, name, gender, title, email, phone",
        "from teacher",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="tno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="title", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="email", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Teacher selectByPrimaryKey(Integer id);

    @UpdateProvider(type=TeacherSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Teacher record);

    @Update({
        "update teacher",
        "set user_id = #{userId,jdbcType=INTEGER},",
          "tno = #{tno,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "gender = #{gender,jdbcType=INTEGER},",
          "title = #{title,jdbcType=INTEGER},",
          "email = #{email,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Teacher record);

    @Update({
            "update teacher",
            "set tno = #{tno,jdbcType=VARCHAR},",
            "name = #{name,jdbcType=VARCHAR},",
            "gender = #{gender,jdbcType=INTEGER},",
            "title = #{title,jdbcType=INTEGER},",
            "email = #{email,jdbcType=VARCHAR},",
            "phone = #{phone,jdbcType=VARCHAR}",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    int updateByUserId(Teacher record);

    @Select({
            "select",
            "id, user_id, tno, name, gender, title, email, phone",
            "from teacher",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="tno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="title", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="email", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Teacher selectByUserId(Integer userId);

    @Select({
            "select",
            "id, user_id, tno, name, gender, title, email, phone",
            "from teacher"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="tno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="title", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="email", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Teacher> getAllTeachers();

    @Select({
            "select",
            "id, user_id, tno, name, gender, title, email, phone",
            "from teacher limit #{start},#{limit}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="tno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="title", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="email", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Teacher> getAllTeachersBylimit(Map<String,Object> map);

    @Select({
            "select",
            "t.id, t.user_id, t.tno, t.name, t.gender, t.title, t.email, t.phone",
            "from teacher t inner join class c on c.teacher_id=t.user_id",
            "where c.id = #{classId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="tno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="title", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="email", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Teacher selectByClassId(int classId);

    @Select({
            "select count(*) from teacher"
    })
    int getCount();

    /**
     * 查找出当前老师是否是数猎云相关的课程的老师
     * @param map
     * @return
     */

    /*课程与老师解除关联*/
    @Select({
            "select " +
                    "distinct t.*,c.name as course_name " +
                    "from class_course cc," +
                    "teacher t," +
                    "module m," +
                    "course c," +
                    "experiment e " +
                    "where t.user_id = cc.teacher_id " +
                    "and c.id = cc.course_id " +
                    "and m.course_id = c.id " +
                    "and m.id = e.module_id " +
                    "and e.cloudware_type = #{cloudware_type} " +
                    "and t.user_id=#{teacher_id}"
    })
    List<Map<String,Object>> selectTeacherIsShulieyunByUserId(Map<String,Object> map);



    /*课程与老师解除关联*/
    @Select({
            "select distinct t.*,c.name as course_name " +
                    "from teacher t," +
                    "class_course cc," +
                    "module m," +
                    "course c," +
                    "experiment e " +
                    "where " +
                    "t.user_id = cc.teacher_id " +
                    "AND c.id = cc.course_id " +
                    "and m.course_id = c.id " +
                    "and m.id = e.module_id " +
                    "and e.cloudware_type = #{cloudware_type} " +
                    "and t.id=#{teacher_id}"
    })
    List<Map<String,Object>> selectTeacherIsShulieyunByTeacherId(Map<String,Object> map);

}