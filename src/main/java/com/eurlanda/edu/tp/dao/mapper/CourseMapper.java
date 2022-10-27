package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.webdomain.response.ResHotCourseList;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

public interface CourseMapper {
    @Delete({
        "delete from course",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into course (id, ",
        "name, description, folder_name,volume_id, author,phone)",
        "values (#{id,jdbcType=INTEGER}, ",
        "#{name,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR},#{folderName,jdbcType=VARCHAR},#{volumeId,jdbcType=VARCHAR},#{author,jdbcType=VARCHAR},#{phone,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert(Course record);

    @InsertProvider(type=CourseSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insertSelective(Course record);

    @UpdateProvider(type=CourseSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Course record);

    @Update({
        "update course",
        "set author = #{author,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "volume_id = #{volumeId,jdbcType=VARCHAR},",
            "phone = #{phone,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Course record);

    @Select({
        "select",
        "id, name, description,volume_id,author,phone",
        "from course",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Course selectByPrimaryKey(Integer id);



    @Select({
            "select",
            "id,folder_name folderName,name,description,volume_id volumeId,author,phone",
            "from course",
            "where id = #{id,jdbcType=INTEGER}"
    })
    Course selectByPrimaryKey2(Integer id);

    @Select({
            "select c.id courseId, c.name courseName, c.description courseDes,c.author,c.phone",
            "from course c",
            "order by c.id"

    })
    List<Map> getAllCourses();


    /*课程已于老师解除关联*/
   /* @Select({
            "select exists (select 1 from course",
            "where teacher_id=#{teacherId, jdbcType=INTEGER})"
    })
    boolean isTeacherUsedByCourse(int teacherId);*/

    @Select({
            "select c.id, ifnull(course_actual_student.actual_student_num, 0) actual_student_num, ",
                    "ifnull(course_total_student.total_student_num, 0) total_student_num, c.name course_name, c.description, r.url, c.author teacher_name, c.phone ",
                    "from course c",
                          "inner join course_resource cr on cr.course_id = c.id and type = 1",
                          "inner join resource r on r.id = cr.resource_id",
                          /*"inner join teacher t on t.user_id = c.teacher_id",*/
                          "left join",
                            "(select cc.course_id course_id , count(*) as actual_student_num",
                             "from class c inner join class_course cc on c.id = cc.class_id inner join student_class sc on sc.class_id  = c.id",
                             "group by cc.course_id) as course_actual_student on course_actual_student.course_id=c.id",
                          "left join",
                            "(select cc.course_id as course_id , sum(c.student_num) as total_student_num",
                             "from class c,class_course cc where c.id = cc.class_id",
                             "group by cc.course_id",
                            ") as course_total_student on course_actual_student.course_id = course_total_student.course_id",
                    "order by actual_student_num desc"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="actual_student_num", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="total_student_num", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="course_name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="url", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="teacher_name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="phone", javaType=String.class, jdbcType=JdbcType.VARCHAR),
    })
    List<ResHotCourseList.ResHotCourseDetail> getHotCourses();

    @Select({
            "select count(*) from course"
    })
    int getCount();

    @Select({
            "select c.* " +
                    "from course c," +
                    "class cs," +
                    "class_course cc " +
                    "where c.id = cc.course_id " +
                    "and cs.id = #{classId} " +
                    "and cc.class_id = cs.id"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Course> getCourseByClassId(int classId);

    @Select({
            "select c.* from course c," +
                    "class cs," +
                    "class_course cc " +
                    "where c.id = cc.course_id " +
                    "and cs.id = #{classId} " +
                    "and cc.class_id = cs.id"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Course> getCourseListByClassId(int classId);

    @Select({
            "select exists (select 1 from course c,experiment e,module m where c.id = m.course_id and e.module_id = m.id and e.cloudware_type = 7 and c.id = #{courseId} )"
    })
    boolean selectCourseExistShulieyun(int courseId);

    @Select({
            "select exists (select 1 from course c,experiment e,module m where c.id = m.course_id and e.module_id = m.id and e.cloudware_type !=7 and c.id = #{courseId} )"
    })
    boolean selectCourseExistOtherExperiment(int courseId);
    @Select({
            "select EXISTS(select 1 from experiment e " +
                    "inner join module m " +
                    "on e.module_id=m.id " +
                    "inner join course c " +
                    "on c.id=m.course_id " +
                    "inner join class_course cc "+
                    "on cc.course_id = c.id "+
                    "inner join class s " +
                    "on s.id = cc.class_id " +
                    "inner join student_class sc " +
                    "on sc.class_id=s.id " +
                    "where e.cloudware_type!=7 " +
                    "and sc.student_id=#{userId}) " +
                    "UNION ALL " +
                    "select EXISTS(select 1 from experiment e " +
                    "inner join module m " +
                    "on e.module_id=m.id " +
                    "inner join course c " +
                    "on c.id=m.course_id " +
                    "inner join class_course cc "+
                    "on c.id = cc.course_id "+
                    "inner join class s " +
                    "on s.id = cc.class_id " +
                    "inner join teacher t " +
                    "on cc.teacher_id=t.user_id " +
                    "where e.cloudware_type!=7 " +
                    "and t.user_id=#{user})"
    })
    List<Integer> selectCourseExistOtherExperimentByUserId(int userId);

    @Select({
            "<script>",
            "SELECT COUNT(1) FROM course WHERE `name` = #{name}",
            "<if test = 'id != null'> and id != #{id}</if>",
            "</script>"
    })
    Integer selectCountByCourseName(Course course);

    @Select({
            "SELECT * FROM course WHERE `name` = #{name}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Course> selectByCourseName(Course course);

    @Select({
            "SELECT * FROM course WHERE `name` = #{name}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
   Course selectByName(String name);


    /*@Select({
        "SELECT c.id from course c",
            "JOIN teacher t",
            "ON c.teacher_id = t.user_id",
            "WHERE t.user_id = #{teacherId}"
    })
    List<Course> selectCourseByTeacherId(Integer teacherId);*/

    @Select({
            "select * from course"
    })
    List<Course> selectAllCourses();



    @Select({
            "select count(*) as eCount,e.id as eId from course c inner join module m on c.id=m.course_id inner join experiment e on e.module_id=m.id where c.id=#{courseId} and e.cloudware_type=7"
    })
    Map<String,Object> findExperimentCountByCouseId(int courseId);

    @Select({
            "select DISTINCT(u.username) from class_course cc\n" +
                    "inner join course c\n" +
                    "on cc.course_id=c.id\n" +
                    "inner join student_class sc \n" +
                    "on cc.class_id=sc.class_id\n" +
                    "inner join `user` u\n" +
                    "on u.id=sc.student_id\n" +
                    "where c.id=#{courseId}\n"
    })
    List<String> findStuByCourseId(int courseId);
    /*课程已于老师解除关联，新建的课程teacher_id为null*/
    @Select({
            /*"select DISTINCT(tno) from teacher \n" +
                    "where user_id in (select DISTINCT(teacher_id) from course where id=#{courseId})\n" +
                    "UNION\n" +*/
                    "select DISTINCT(tno) from teacher where user_id in (\n" +
                    "select cc.teacher_id from class_course cc\n" +
                    "inner join course c\n" +
                    "on cc.course_id=c.id\n" +
                    "where c.id=#{courseId}\n" +
                    ")"
    })
    List<String> findTeaByCourseId(int courseId);


    @Select({
            "SELECT\n" +
                    "\tcount(DISTINCT(m.id)) AS eCount,\n" +
                    "\tm.id AS eId\n" +
                    "FROM\n" +
                    "\tcourse c\n" +
                    "INNER JOIN module m ON c.id = m.course_id\n" +
                    "INNER JOIN experiment e ON e.module_id = m.id\n" +
                    "WHERE\n" +
                    "\tc.id = #{courseId}\n" +
                    "AND e.cloudware_type = 7;"
    })
    Map<String,Object> findModelCountByCouseId(int courseId);

    /*课程已于老师解除关联，新建的课程teacher_id为null*/
    @Select({
            "SELECT\n" +
            "\tc.id,c.`name`,c.description\n" +
            "FROM\n" +
            "\tcourse c\n" +
            "INNER JOIN module m ON c.id = m.course_id\n" +
            "INNER JOIN experiment e ON e.module_id = m.id\n" +
            "WHERE\n" +
            "\tc.id = #{courseId}\n" +
            "AND e.cloudware_type = 7;"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<Course> findShulieyunCourseById(int courseId);

    @Select({
            "SELECT course.`name`,sum(class.student_num) value  FROM course join class_course on course.id = class_course.course_id ",
            "JOIN class  ",
            "on class.id = class_course.class_id",
            "GROUP BY course.`name`",
            "ORDER BY value DESC",
            "LIMIT 0,10"
    })

    List<Map> selectCourseBySelectedCount();
    @Select({
            "select DISTINCT(c.id),c.name,c.description,c.volume_id from student_class sc\n" +
                    "inner join class_course cc\n" +
                    "on sc.class_id=cc.class_id\n" +
                    "inner join course c\n" +
                    "on cc.course_id=c.id\n" +
                    "where sc.class_id=#{classId} and sc.student_id=#{studentId}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR),
    })
    List<Course> findCourseByStuId(Map<String,Object> map);

    @Select({
            "SELECT id FROM course",
            "WHERE course.`name` = #{courseName}"
    })
    Course selectCourseByCourseName(String courseName);


    @Select({
         "SELECT * FROM course " +
         "LIMIT #{pageIndex},#{pageSize}"
    })
    List<Course> selectAllCoursePaged(Map<String,Object> map);



}