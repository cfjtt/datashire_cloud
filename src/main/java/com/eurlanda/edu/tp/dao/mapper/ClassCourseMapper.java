package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.ClassCourse;
import com.eurlanda.edu.tp.dao.entity.Course;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClassCourseMapper {

    @Delete({
            "delete from class_course",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);


    @Insert({
            "insert into class_course (id,class_id,course_id,teacher_id,date,school_year,semester)",
            "values (#{id,jdbcType=INTEGER}, #{classId,jdbcType=INTEGER}," +
                    "#{courseId,jdbcType=INTEGER},",
                     "#{teacherId,jdbcType=INTEGER},#{date,jdbcType=DATE}),#{schoolYear,jdbcType=VARCHAR},#{semester,jdbcType=INTEGER}",
    })
    int insert(ClassCourse classCourse);

    @InsertProvider(type=ClassCourseSqlProvider.class,method="insertSelective")
    int insertSelective(ClassCourse classCourse);

    @Select({
            "select",
            "id, class_id,course_id, date,teacher_id,school_year,semester",
            "from class_course",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="class_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="school_year", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="semester", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="date", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="teacher_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    ClassCourse selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ClassCourseSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ClassCourse record);

    @Update({
            "update class_course",
            "set semester = #{semester,jdbcType=INTEGER},school_year = #{schoolYear,jdbcType=INTEGER},",
            "course_id = #{courseId,jdbcType=INTEGER},",
            "class_id = #{classId,jdbcType=INTEGER},",
            "date = #{date,jdbcType=DATE},",
            "teacher_id = #{teacherId,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ClassCourse record);


    @Select({
            "select id, class_id,course_id, date,teacher_id,school_year,semester" +
                    " from class_course ",
            "where class_id =#{classId,jdbcType=INTEGER} and course_id =#{courseId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="class_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="school_year", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="semester", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="date", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="teacher_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
   ClassCourse findClassCourseByClassIdAndCourseId(Map<String,Object> map);

    @Delete({
            "delete from class_course where class_id =#{classId,jdbcType=INTEGER}"
    })
    void deleteByClassId(Integer classId);

    @Select({
            "select",
            "id, class_id,course_id, date,teacher_id,school_year,semester",
            "from class_course",
            "where class_id = #{classId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="class_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="school_year", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="semester", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="date", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="teacher_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<ClassCourse> findClassCourseByClassId(Integer classId);


    @Select({
            "select c.id as courseId,c.`name` as courseName,cc.school_year as schoolYear,cc.semester as semester," +
                    "h.name as teacherName,cc.teacher_id as teacherId from class_course cc\n" +
                    "inner join course c\n" +
                    "on cc.course_id=c.id\n" +
                    "inner join teacher h\n" +
                    "on h.user_id=cc.teacher_id\n" +
                    "where cc.class_id=#{classId}"
    })
    List<Map> findClassDetailByClassId(Integer classId);

    @Delete({
            "delete from class_course where class_id =#{classId,jdbcType=INTEGER} " +
                    "and course_id=#{courseId,jdbcType=INTEGER} and teacher_id=#{teacherId} " +
                    "and school_year=#{schoolYear} and semester=#{semester}"
    })
    void deleteByClassIdAndCourseId(Map map);


    @Select({
            "select count(1) from class_course cc\n" +
                    "inner join student_class sc \n" +
                    "on cc.class_id=sc.class_id\n" +
                    "where cc.class_id!=#{classId} and cc.course_id=#{courseId}\n" +
                    "and sc.student_id=#{studentId}"
    })
    int findCourseByNotClassAndStuId(Map<String,Object> map);
    @Select({
            "select count(1) from class_course \n" +
                    "where class_id!=#{classId} and course_id=#{courseId} and teacher_id=#{teacherId}"
    })
    int findCourseByNotClassAndTeacherId(Map<String,Object> map);

    @Select({
            "select c.id as classId,c.`name` as className,c.student_num as studentNum from class_course cc \n" +
                    "INNER JOIN class c on cc.class_id=c.id\n" +
                    "where teacher_id=#{teacherId} \n" +
                    "GROUP BY cc.class_id"
    })
    List<Map> getClassAndCourseByTeacher(int teacherId);

    @Select({
            "select c.id,c.name,c.description,c.volume_id from class_course cc " +
                    "inner join course c\n" +
                    "on cc.course_id=c.id " +
                    "where cc.class_id=#{classId} and cc.teacher_id=#{teacherId}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Course> getCourseByTeacherAndClassId(Map<String,Object> map);

}
