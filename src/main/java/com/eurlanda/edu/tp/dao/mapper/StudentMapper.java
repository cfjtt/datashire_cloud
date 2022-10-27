package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Student;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

public interface StudentMapper {
    @Delete({
        "delete from student",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Delete({
            "delete from student where user_id = #{userId}"
    })
    int deleteStudentByUserId(Integer userId);

    @Insert({
        "insert into student (id, user_id, ",
        "sno, name, gender, ",
        "birthday,grade,phone)",
        "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, ",
        "#{sno,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{gender,jdbcType=INTEGER}, ",
        "#{birthday,jdbcType=VARCHAR},#{grade,jdbcType=VARCHAR},#{phone,jdbcType=VARCHAR})"
    })
    int insert(Student record);

    @InsertProvider(type=StudentSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "record.id")
    int insertSelective(Student record);

    @Select({
        "select",
        "id, user_id, sno, name, gender, birthday,grade,phone",
        "from student",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="sno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="birthday", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="grade", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)

    })
    Student selectByPrimaryKey(Integer id);

    @Select({
            "<script>",
            "select id, user_id, sno, name, gender, birthday,grade,phone",
            "from student where user_id in",
            "<foreach collection='idList' item='id' open='(' close=')' separator=',' > #{id}</foreach>",
            "</script>"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="sno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="birthday", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="grade", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)

    })
   List<Student> selectByUserIdList(Map<String,Object> map);

    @Select({
            "select s.user_id userId, s.sno, s.name, s.gender,u.password2 as password,s.grade as grade,s.phone as phone ",
            "from student s ",
            "inner join user u on s.user_id = u.id "
    })
    List<Map> getAllStudentInfo();


    @UpdateProvider(type=StudentSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Student record);

    @Update({
        "update student",
        "set user_id = #{userId,jdbcType=INTEGER},",
          "sno = #{sno,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "gender = #{gender,jdbcType=INTEGER},",
          "birthday = #{birthday,jdbcType=VARCHAR},",
            "grade = #{grade,jdbcType=VARCHAR},",
            "phone = #{phone,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Student record);

    @Update({
            "update student",
            "set sno = #{sno,jdbcType=VARCHAR},",
            "name = #{name,jdbcType=VARCHAR},",
            "gender = #{gender,jdbcType=INTEGER},",
            "birthday = #{birthday,jdbcType=VARCHAR},",
            "grade = #{grade,jdbcType=VARCHAR},",
            "phone = #{phone,jdbcType=VARCHAR}",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    int updateByUserId(Student record);

    @Select({
            "select",
            "s.id, s.user_id, s.sno, s.name, s.gender, s.birthday,s.grade,s.phone",
            "from student s",
                "inner join student_class sc on sc.student_id = s.user_id",
            "where sc.class_id = #{classId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="sno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="birthday", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="grade", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Student> selectByClassId(int classId);

    @Select({
            "select",
            "id, user_id, sno, name, gender, birthday,grade,phone",
            "from student",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="sno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="birthday", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="grade", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Student selectByUserId(Integer userId);

    @Select({
            "select",
            "id, user_id, sno, name, gender, birthday,grade,phone",
            "from student",
            "where sno = #{sno,jdbcType=VARCHAR}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="sno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="birthday", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="grade", javaType=String.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Student selectByStudentNo(String sno);

    @Select({
            "select count(*) from student"
    })
    int getCount();


    @Select({
            "select count(*) from class c inner join  student_class sc on c.id=sc.class_id inner join class_course cc on c.id = cc.class_id inner join course r on r.id=cc.course_id \n" +
                    "where c.id!=#{classId} and r.id=#{courseId} and sc.student_id=#{userId}"
    })
    int selectExistsCourseByClassIdAndCourseIdAndStuId(Map<String,Object> map);


    @Select({
            "select count(1) from student_class sc \n" +
                    "inner join class c\n" +
                    "on sc.class_id=c.id\n" +
                    "inner join class_course cc \n"+
                    "on c.id = cc.class_id \n"+
                    "inner join course r\n" +
                    "on r.id = cc.course_id\n" +
                    "where c.id!=#{classId} \n" +
                    "and sc.student_id=#{studentId}\n" +
                    "and r.id=#{courseId}\n"
    })
    int findSlyByNotClassAndStuId(Map<String,Object> map);

}