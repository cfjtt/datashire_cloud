package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.StudentExperiment;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperiment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

public interface StudentExperimentMapper {
    @Delete({
        "delete from student_experiment",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into student_experiment (id, student_id, ",
        "experiment_id, cloudware_id)",
        "values (#{id,jdbcType=INTEGER}, #{studentId,jdbcType=INTEGER}, ",
        "#{experimentId,jdbcType=INTEGER}, #{cloudwareId,jdbcType=INTEGER})"
    })
    int insert(StudentExperiment record);

    @InsertProvider(type=StudentExperimentSqlProvider.class, method="insertSelective")
    int insertSelective(StudentExperiment record);

    @Select({
        "select",
        "id, student_id, experiment_id, cloudware_id",
        "from student_experiment",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="student_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="experiment_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="cloudware_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    StudentExperiment selectByPrimaryKey(Integer id);

    @UpdateProvider(type=StudentExperimentSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(StudentExperiment record);

    @Update({
        "update student_experiment",
        "set student_id = #{studentId,jdbcType=INTEGER},",
          "experiment_id = #{experimentId,jdbcType=INTEGER},",
          "cloudware_id = #{cloudwareId,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(StudentExperiment record);


    @Select("SELECT cloudware_id FROM student_experiment WHERE student_id=#{studentId} AND experiment_id=#{experimentId}")
    int selectCloudwareIdByStudentIdAndExperimentId(@Param("studentId") Integer studentId, @Param("experimentId") Integer experimentId);

    @Select({"SELECT *",
            "FROM student_experiment",
            "WHERE student_id=#{studentId} AND experiment_id=#{experimentId}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="student_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="experiment_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="cloudware_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<StudentExperiment> selectByStudentIdAndExperimentId(@Param("studentId") Integer studentId, @Param("experimentId") Integer experimentId);

    @Select({"SELECT id",
            "FROM student_experiment",
            "WHERE experiment_id=#{experimentId}"
    })
    List<Integer> selectStudentExperimentByExperimentId(Integer experimentId);

    @Select({
            "select e.id experimentId, e.name experimentName, m.name moduleName",
            "from student_experiment se",
            "inner join experiment e on se.experiment_id = e.id",
            "inner join module m on e.module_id = m.id",
            "where student_id = #{studentId,jdbcType=INTEGER}",
            "limit 1",
    })
    List<Map> selectLastExperimentByUserId(int studentId);

    @Select({
            "select student_id as studentId,experiment_id as experimentId from student_experiment where student_id = #{studentId}"
    })
    List<ReqStudentExperiment> selectExperimentByStudentId(int studentId);
}