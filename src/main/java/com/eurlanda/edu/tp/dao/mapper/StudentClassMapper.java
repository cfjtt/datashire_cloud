package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.StudentClass;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

public interface StudentClassMapper {
    @Delete({
        "delete from student_class",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into student_class (id, student_id, ",
        "class_id)",
        "values (#{id,jdbcType=INTEGER}, #{studentId,jdbcType=INTEGER}, ",
        "#{classId,jdbcType=INTEGER})"
    })
    int insert(StudentClass record);

    @InsertProvider(type=StudentClassSqlProvider.class, method="insertSelective")
    int insertSelective(StudentClass record);

    @Select({
        "select",
        "id, student_id, class_id",
        "from student_class",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="student_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="class_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    StudentClass selectByPrimaryKey(Integer id);

    @UpdateProvider(type=StudentClassSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(StudentClass record);

    @Update({
        "update student_class",
        "set student_id = #{studentId,jdbcType=INTEGER},",
          "class_id = #{classId,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(StudentClass record);

    @Select({
            "select *",
            "from student_class",
            "where student_id = #{studentId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="student_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="class_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<StudentClass> selectByStudentUserId(Integer studentId);

    @Delete("DELETE FROM student_class WHERE student_id=#{studentId} AND class_id=#{classId}")
    int deleteByClassIdAndStudentId(@Param("classId") Integer classId, @Param("studentId") Integer studentId);

    @Select({
            "select *",
            "from student_class",
            "where class_id = #{classId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="student_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="class_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<StudentClass> selectByClassID(Integer classId);

    @Delete({"delete from student_class where class_id=#{classId}"})
    int deleteByClassId(@Param("classId") Integer classId);

    @Select({
            "select exists (select 1 from student_class",
            "where class_id=#{classId, jdbcType=INTEGER})"
    })
    boolean isClassUsedByStudentClass(int classId);

    @Select({
            "select count(*)",
            "from student_class",
            "where class_id in (select c.id from class c,class_course cc ",
                               "where c.id = cc.class_id and cc.course_id=#{courseId, jdbcType=INTEGER})"
    })
    int getStudentNumByCourseId(int courseId);

    @Select({
            "select c.id classId, c.name className,r.url, c.duration, c.student_num studentNum, ",
            "cs.description, cs.id courseId, cs.name courseName, tc.email teacherContact, tc.name teacherName, c.date classDate",
            "from student_class sc",
            "left join class_course cc on sc.class_id = cc.class_id",
            "inner join class c on sc.class_id = c.id",
            "inner join course cs on cc.course_id = cs.id",
            "left join course_resource cr on cr.course_id = cs.id and cr.type = 1",
            "left join resource r on cr.resource_id = r.id",
            "inner join teacher tc on cc.teacher_id = tc.user_id",
            "where sc.student_id=#{studentId, jdbcType=INTEGER}"
    })
    List<Map> getStudentClassInfoByUserId(int studentId);

    @Select({
            "select hw.id homeworkId, hw.name homeworkName, hw.description homeworkDes, hw.cloudware_type cloudwareType,",
            "hw.publish_date publishDate, hw.deadline_date deadlineDate, c.id classId, c.name className,",
            "t.name teacherName, shw.id studentHomeworkId, shw.submission_date submissionDate",
            "from student_class sc",
            "inner join class c on sc.class_id = c.id",
            "inner join class_course cc on c.id = cc.class_id",
            "inner join teacher t on cc.teacher_id = t.user_id",
            "inner join homework hw on hw.class_id = c.id",
            "left join student_homework shw on shw.homework_id = hw.id",
            "where sc.student_id=#{studentId, jdbcType=INTEGER}",
            "order by publishDate;"
    })
    List<Map> getStudentHomeworkByStudentId(int studentId);

    @Select({
            "select sc.student_id studentId, s.sno, s.name studentName,",
            "sh.id studentHomeworkId, sh.submission_date submissionDate, sh.lastEdit_date lastEditDate, sh.score",
            "from student_class sc",
            "inner join student s on s.user_id=sc.student_id",
            "left join student_homework sh on sh.student_id=s.user_id and homework_id=#{homeworkId,jdbcType=INTEGER}",
            "where sc.class_id=#{classId,jdbcType=INTEGER}",
    })
    List<Map> getStudentHomeworkByClassAndHomeworkId(@Param("classId") Integer classId, @Param("homeworkId") Integer homeworkId);

    @Select({
            "select s.user_id userId, s.sno, s.name, s.gender,u.password2 as password,s.grade,s.phone",
            "from student_class sc",
            "inner join student s on sc.student_id = s.user_id",
            "inner join user u on s.user_id = u.id ",
            "where sc.class_id = #{classId,jdbcType=INTEGER}",
    })
    List<Map> getAllStudentByClassId(int classId);

    @Select({
            "select s.sno as phone ,u.password2 as password from class c,class_course cc,student_class sc,student s,user u where c.id = cc.class_id and cc.course_id=#{courseId} and c.id = sc.class_id and sc.student_id = s.user_id and s.user_id=u.id"
    })
    List<Map<String,Object>> getAllStudentByCourseId(int courseId);

    @Select({
            "<script>",
                "SELECT COUNT(1) FROM student_class WHERE class_id in ",
                "<foreach item='item' index='index' collection='array'",
                    "open='(' separator=',' close=')'>",
                    "#{item}",
                "</foreach>",
            "</script>"
    })
    Integer selectNumsOfClass(String[] list);



    @Select({
            "select s.user_id",
            "from student_class sc",
            "inner join student s on sc.student_id = s.user_id",
            "inner join user u on s.user_id = u.id ",
            "where sc.class_id = #{classId,jdbcType=INTEGER}",
    })
    List<Integer> getAllStudentIdsByClassId(int classId);
}