package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Clazz;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClazzMapper {
    @Delete({
        "delete from class",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into class (id,date, duration,student_num, name)",
        "values (#{id,jdbcType=INTEGER},",
        "#{date,jdbcType=DATE}, #{duration,jdbcType=VARCHAR}, ",
        "#{studentNum,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR} ",
    })
    int insert(Clazz record);

    @InsertProvider(type=ClazzSqlProvider.class, method="insertSelective")
    int insertSelective(Clazz record);

   @Select({
           "select ",
           "id,name,student_num,duration,date",
           "from class ",
           "where id = #{id,jdbcType=INTEGER}"
   })
   @ConstructorArgs({
           @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
           @Arg(column="student_num", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
           @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
           @Arg(column="duration", javaType=String.class, jdbcType=JdbcType.VARCHAR),
           @Arg(column="date", javaType=Date.class, jdbcType=JdbcType.DATE)
   })
    Clazz selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ClazzSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Clazz record);

    @Update({
        "update class",
        "set ",
          "date = #{date,jdbcType=DATE},",
          "duration = #{duration,jdbcType=VARCHAR},",
          "student_num = #{studentNum,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Clazz record);

    @Select({
            "select c.id classId, c.name className,r.url, c.duration,",
            "c.student_num studentNum, cs.description, cs.id courseId, cs.name courseName,",
            "t.email, t.name teacherName, c.date classDate,cc.school_year as schoolYear,cc.semester as semester",
            "from class c",
            "inner join class_course cc on c.id = cc.class_id",
            "inner join course cs on cc.course_id = cs.id",
            "left join course_resource cr on cr.course_id = cs.id and cr.type = 1",
            "left join resource r on cr.resource_id = r.id",
            "inner join teacher t on cc.teacher_id = t.user_id",
            "where t.user_id = #{teacherUserId,jdbcType=INTEGER}",
            "order by c.id",
    })
    List<Map> selectAllClassInfoByTeacherUserId(int teacherUserId);

    @Select({
            "select c.id classId,, c.name className,r.url, c.duration,",
            "ifnull(student_count.student_num,0) studentNum, cs.description, cs.id courseId, cs.name courseName,",
            "t.user_id teacherId, t.email, t.name teacherName, c.date classDate",
            "from class c",
            "left join (",
            "select class_id, count(*) student_num",
            "from student_class",
            "group by class_id",
            "        ) student_count on student_count.class_id = c.id",
            "inner join course cs on c.course_id = cs.id",
            "inner join class_course cc on cc.class_id=c.id",
            "left join course_resource cr on cr.course_id = cs.id and cr.type = 1",
            "left join resource r on cr.resource_id = r.id",
            "inner join teacher t on cc.teacher_id = t.user_id",
            "order by c.id"
    })
    List<Map> selectAllClassInfo();



    @Select({"select id as classId,name as className,student_num as studentNum from class"})
    List<Map> selectAllClass();


    @Select({
            "select",
            "c.id, cc.course_id, c.date, c.duration, c.student_num, c.name",
            "from class c inner join class_course cc on c.id == cc.class_id"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="date", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="duration", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="student_num", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Clazz> selectAll();

    @Select({
            "select exists (select 1 from class c inner join class_course cc on c.id = cc.class_id",
            "where cc.term_id=#{termId, jdbcType=INTEGER})"
    })
    boolean isTermUsedByClass(int termId);

    @Select({
            "select exists (select 1 from class c inner join class_course cc on c.id = cc.class_id",
            "where cc.course_id=#{courseId, jdbcType=INTEGER})"
    })
    boolean isCourseUsedByClass(int courseId);

    @Select({
            "select exists (select 1 from student_class",
            "where student_id=#{studentId, jdbcType=INTEGER} and class_id=#{classId, jdbcType=INTEGER})"
    })
    boolean isStudentInClass(@Param("studentId") Integer studentId, @Param("classId") Integer classId);

    @Select({
            "select",
            "c.id, cc.course_id, c.date, c.duration, c.student_num, c.name,cc.teacher_id",
            "from class c inner join class_course cc on c.id = cc.class_id",
            "where cc.course_id = #{courseId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="date", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="duration", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="student_num", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="teacher_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<Clazz> selectByCourseId(@Param("courseId") int courseId);

    @Select({
        "select count(*)",
        "from class c inner join class_course cc on c.id = cc.class_id",
        "where cc.course_id = #{courseId,jdbcType=INTEGER}"
    })
    int getClassNumByCourseId(@Param("courseId") int courseId);

    @Select({
            "select exists (select 1 from class c inner join class_course cc on c.id = cc.class_id",
            "where cc.teacher_id=#{teacherId, jdbcType=INTEGER})"
    })
    boolean isTeacherUsedByClass(int teacherId);

    @Select({
            "select exists (select * from class c,class_course cc,experiment e,module m where c.id = #{class_id} and c.id = cc.class_id and cc.course_id = m.course_id and m.id = e.module_id and e.cloudware_type = #{cloudware_type})"
    })
    boolean selectIsShulieyunCourseByClassId(Map<String,Object> map);

    @Select({
            "<script>",
            "select count(1) from class c,class_course cc WHERE c.`name` = #{name} and cc.course_id = #{courseId} and cc.class_id = c.id",
            "<if test = 'id != null'> and c.id != #{id}</if>",
            "</script>"
    })
    Integer selectCountByClassNameAndCourse(Clazz clazz);
    @Select({
            "<script>",
            "select count(1) from class WHERE `name` = #{name}",
            "<if test = 'id != null'> and id != #{id}</if>",
            "</script>"
    })
    Integer selectCountByClassName(Clazz clazz);

    @Select({
            "select count(1) from class c\n" +
                    "inner join class_course cc \n"+
                    "on c.id = cc.class_id \n"+
                    "inner join course r\n" +
                    "on cc.course_id=r.id\n" +
                    "inner join module m\n" +
                    "on m.course_id=r.id\n" +
                    "inner join experiment e\n" +
                    "on e.module_id=m.id\n" +
                    "where e.cloudware_type=7\n" +
                    "and c.id=#{classId}"
    })
    int findSlyByClassId(int classId);


    @Select({
            "select count(*) from class s,class_course cc\n" +
                    "where cc.teacher_id=#{teacherId}\n" +
                    "and cc.course_id=#{courseId}\n" +
                    "and s.id!=#{classId}\n"+
                    "and s.id = cc.class_id"
    })
    int findSlyByTeaIdAndClassIdAndCourseId(Map<String,Object> map);


    @Select({
            "select DISTINCT(u.id) as userId,s.sno as studentNo,s.`name` as studentName," +
                    "s.gender as gender,s.grade as grade,s.phone as phone,u.password2 as `password`\n" +
                    " from student s\n" +
                    "inner join `user` u\n" +
                    "on s.user_id=u.id\n" +
                    "where user_id not in (select student_id from student_class where class_id=#{classId})"
    })
    List<Map> findStuByNotThisClass(int classId);

    /*课程与老师解除关联SQL修改*/
    @Select({
            "select c.id as courseId,c.`name` as courseName,c.`author` as teacherName from class s\n" +
                    "inner join class_course cc on s.id = cc.class_id \n"+
                    "inner join course c\n" +
                    "on cc.course_id=c.id\n" +
                    /*"inner join teacher t\n" +
                    "on t.user_id=c.teacher_id\n" +*/
                    "where s.id=#{classId}"
    })
    List<Map> getClassDetailByClassId(int classId);

    @Select({
            "select * from class where name=#{className}"
    })
    Clazz getClassByClassName(String className);

    @Select({
            "select * from class where name=#{className} and id!=#{classId}"
    })
    Clazz findClassByNameAndCId(Map<String,Object> map);

}