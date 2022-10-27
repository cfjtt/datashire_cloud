package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.ClassCourse;
import com.eurlanda.edu.tp.dao.entity.JobScore;
import com.eurlanda.edu.tp.webdomain.response.ResJobScore;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JobScoreMapper {

    @Delete({
            "delete from job_score",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);


    @Insert({
            "insert into job_score (id,class_id,course_id,student_id,experiment_id,score,create_time,table_name,file_url)",
            "values (#{id,jdbcType=INTEGER}, #{classId,jdbcType=INTEGER}," +
                    "#{courseId,jdbcType=INTEGER},",
            "#{studentId,jdbcType=INTEGER}," +
                    "#{experimentId,jdbcType=INTEGER},#{score,jdbcType=INTEGER},#{createTime,jdbcType=TIMESTAMP}" +
                    "#{table_name,jdbcType=VARCHAR},#{file_url,jdbcType=VARCHAR}",
    })
    int insert(JobScore jobScore);

    @InsertProvider(type=JobScoreSqlProvider.class,method="insertSelective")
    int insertSelective(JobScore jobScore);

    @Select({
            "select",
            "id, class_id,course_id, student_id,experiment_id,score,create_time,table_name,file_url",
            "from job_score",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="class_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="student_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="experiment_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="score", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
            @Arg(column="table_name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="file_url", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    JobScore selectByPrimaryKey(Integer id);

    @UpdateProvider(type=JobScoreSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(JobScore jobScore);

    @Update({
            "update job_score",
            "set class_id = #{classId,jdbcType=INTEGER},course_id = #{courseId,jdbcType=INTEGER},",
            "student_id = #{studentId,jdbcType=INTEGER},",
            "experiment_id = #{experimentId,jdbcType=INTEGER},",
            "score = #{score,jdbcType=INTEGER},",
            "create_time = #{createTime,jdbcType=DATE}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(JobScore jobScore);

    @Select("<script>"+
            "select s.`user_id` as studentId,s.`name` as studentName,s.gender,s.sno,j.score,j.create_time from job_score j\n" +
            "inner join student s\n" +
            "on j.student_id=s.user_id\n" +
            "where j.class_id=#{classId} and j.course_id=#{courseId} and j.experiment_id=#{experimentId} " +
            "and j.student_id in " +
            "<foreach collection='idList' item='id' open='(' close=')' separator=',' > #{id}</foreach>" +
            "</script>")
    @ConstructorArgs({
            @Arg(column="studentId", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="studentName", javaType=String.class, jdbcType= JdbcType.VARCHAR),
            @Arg(column="gender", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="sno", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="score", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP)
    })
    List<ResJobScore> findStudentScore(Map<String,Object> map);

    @Delete("delete from job_score where class_id=#{classId}")
    int deleteScoreByClassId(Integer classId);


    @Select({
            "SELECT\n" +
                    "\tid,\n" +
                    "\tclass_id classId,\n" +
                    "\tcourse_id courseId,\n" +
                    "\tstudent_id studentId,\n" +
                    "\texperiment_id experimentId,\n" +
                    "\ttable_name tableName,\n" +
                    "\tfile_url fileUrl\n" +
                    "FROM\n" +
                    "\tjob_score\n" +
                    "WHERE\n" +
                    "\tscore IS NULL\n" +
                    "ORDER BY\n" +
                    "\tcreate_time"
    })
    List<JobScore> selectUnapprovedHomework();
    @Select("select id,score,create_time,table_name,file_url from job_score\n" +
            "where class_id=#{classId}\n" +
            "and course_id=#{courseId}\n" +
            "and experiment_id=#{experimentId}\n" +
            "and student_id=#{studentId}")
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="score", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
            @Arg(column="table_name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="file_url", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<JobScore> selectStuScoreByMap(Map<String,Object> map);



    @Select("select student_id from student_class " +
            "where class_id=#{classId} and student_id not in " +
            "(select student_id from job_score " +
            "where class_id=#{classId} and course_id=#{courseId} and experiment_id=#{experimentId});\n")
    List<Integer> findNotSubWorkStu(Map<String,Object> map);

    @Select("select student_id from student_class " +
            "where class_id=#{classId} and student_id in " +
            "(select student_id from job_score " +
            "where class_id=#{classId} and course_id=#{courseId} and experiment_id=#{experimentId});\n")
    List<Integer> findSubWorkStu(Map<String,Object> map);

    @Select("delete from job_score where class_id=#{classId} and student_id=#{studentId}")
    void deleteByCidAndCouidAndStuid(Map<String,Object> map);

    @Select("delete from job_score where class_id=#{classId} and course_id=#{courseId}")
    void deleteByCidAndCouid(Map<String,Object> map);



}
