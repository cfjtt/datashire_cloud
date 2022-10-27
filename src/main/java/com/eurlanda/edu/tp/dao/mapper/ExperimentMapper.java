package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.Experiment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExperimentMapper {
    @Delete({
        "delete from experiment",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into experiment (id, module_id, ",
        "name, description, ",
        "cloudware_type, publish_date, ",
        "deadline_date, experiment_content,order_id)",
        "values (#{id,jdbcType=INTEGER}, #{moduleId,jdbcType=INTEGER}, ",
        "#{name,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{cloudwareType,jdbcType=INTEGER}, #{publishDate,jdbcType=TIMESTAMP}, ",
        "#{deadlineDate,jdbcType=TIMESTAMP}, #{experimentContent,jdbcType=LONGVARCHAR},#{orderId,jdbcType=INTEGER})"
    })
    int insert(Experiment record);

    @InsertProvider(type=ExperimentSqlProvider.class, method="insertSelective")
    int insertSelective(Experiment record);

    @Select({
        "select",
        "id, module_id, name, description, cloudware_type, publish_date, deadline_date, ",
        "experiment_content,order_id,is_answer,answer_table_name,answer_url,old_answer_name",
        "from experiment",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="module_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="cloudware_type", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="publish_date", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
        @Arg(column="deadline_date", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
        @Arg(column="experiment_content", javaType=String.class, jdbcType=JdbcType.LONGVARCHAR),
        @Arg(column="order_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="is_answer", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="answer_table_name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="answer_url", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="old_answer_name", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Experiment selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ExperimentSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Experiment record);

    @Update({
        "update experiment",
        "set module_id = #{moduleId,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "cloudware_type = #{cloudwareType,jdbcType=INTEGER},",
          "publish_date = #{publishDate,jdbcType=TIMESTAMP},",
          "deadline_date = #{deadlineDate,jdbcType=TIMESTAMP},",
          "experiment_content = #{experimentContent,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKeyWithBLOBs(Experiment record);

    @Update({
        "update experiment",
        "set module_id = #{moduleId,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "cloudware_type = #{cloudwareType,jdbcType=INTEGER},",
          "publish_date = #{publishDate,jdbcType=TIMESTAMP},",
          "deadline_date = #{deadlineDate,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Experiment record);

    @Select({
            "select",
            "id, module_id, name, description, cloudware_type, publish_date, deadline_date, order_id",
            "from experiment",
            "where module_id = #{moduleId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="module_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="cloudware_type", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="publish_date", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
            @Arg(column="deadline_date", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
            @Arg(column="order_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<Experiment> selectByModuleId(Integer moduleId);

    @Select({
            "select",
            "e.id, e.module_id, e.name, e.description, e.cloudware_type, e.publish_date, e.deadline_date",
            "from experiment e inner join module m on e.module_id = m.id",
            "where m.course_id = #{courseId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="module_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="cloudware_type", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="publish_date", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
            @Arg(column="deadline_date", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP)
    })
    List<Experiment> selectByCourseId(Integer courseId);

    @Select({
            "select exists (select 1 from experiment",
            "where module_id=#{moduleId, jdbcType=INTEGER})"
    })
    boolean isModuleUsedByExperiment(int moduleId);

    @Select({
            "select exists (select 1 from student_experiment",
            "where experiment_id=#{experimentId, jdbcType=INTEGER})"
    })
    boolean isExperimentUsedByStudentExperiment(int experimentId);

    @Select({
            "select count(*) from experiment"
    })
    int getCount();

    @Select({
            "select DISTINCT c.* " +
                    "from course c," +
                    "experiment e," +
                    "module m " +
                    "where c.id = m.course_id " +
                    "and e.module_id = m.id " +
                    "and e.module_id = #{moduleId}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="description", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="volume_id", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    Course getCourseByModuleId(int moduleId);

    @Select({
            "SELECT COUNT(1) FROM module WHERE id in ( SELECT module_id FROM experiment WHERE id = #{id})"
    })
    Integer selectModuleIsExistByExperiment(Integer id);

    @Select({
            "select * from experiment where module_id = #{moduleId} and name = #{name}"
    })
    List<Experiment> selectExperiment(Experiment experiment);


    @Select({
            "<script>",
            "SELECT COUNT(1) FROM experiment",
            "WHERE module_id = #{moduleId}",
            "AND `name` = #{name}",
            "<if test = 'id != null'> and id != #{id}</if>",
            "</script>"
    })
    Integer selectCountByModuleIdAndName(Experiment experiment);


    @Select({
        "SELECT\n" +
                "\tt.type,\n" +
                "\tCOUNT(t.sno) count\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT DISTINCT\n" +
                "\t\t\tex.cloudware_type type,\n" +
                "\t\t\tc.`name`,\n" +
                "\t\t\tclass.id,\n" +
                "\t\t\tstudent_class.student_id sno\n" +
                "\t\tFROM\n" +
                "\t\t\texperiment ex\n" +
                "\t\tJOIN module m ON ex.module_id = m.id\n" +
                "\t\tJOIN course c ON m.course_id = c.id\n" +
                "\t\tJOIN class_course cc ON c.id = cc.course_id\n" +
                "\t\tJOIN class ON class.id = cc.class_id\n" +
                "\t\tJOIN student_class ON student_class.class_id = class.id\n" +
                "\t\tORDER BY type\n" +
                "\t) t\n" +
                "GROUP BY\n" +
                "\ttype\n"
    })
    List<Map> selectCloudwareTypeCount();


    @Select({
            "SELECT MAX(order_id) FROM experiment " +
                    "WHERE module_id = #{moduleId}"
    })
    Integer selectMaxOrderIdByModuleId(@Param("moduleId") Integer moduleId);



    @Select({
            "SELECT id,order_id orderId FROM experiment WHERE order_id > #{orderId} AND module_id = #{moduleId}"
    })
    List<Experiment> selectInfluenceOrderIdByModuleId(Experiment module);

    @Select({"<script>"+
            "select DISTINCT m.`name` as moduleName,e.id as experimentId,e.`name` as experimentName from course c\n" +
                    "inner join  module m\n" +
                    "on c.id=m.course_id\n" +
                    "inner join experiment e\n" +
                    " on m.id=e.module_id\n" +
                    "inner join job_score j\n" +
                    "on j.experiment_id=e.id\n" +
                    " where c.id=#{courseId} and j.class_id=#{classId} and j.create_time is not null " +
                    "and j.student_id in " +
            "<foreach collection='idList' item='id' open='(' close=')' separator=',' > #{id}</foreach> " +
            "order by m.order_id,e.order_id " +
            "</script>"
    })
    List<Map> selectModuleAndExplerimentByStu(Map<String,Object> map);

    @Select({"select DISTINCT m.`name` as moduleName,e.id as experimentId,e.`name` as experimentName from course c\n" +
            "inner join  module m\n" +
            "on c.id=m.course_id\n" +
            "inner join experiment e\n" +
            " on m.id=e.module_id\n" +
            "inner join job_score j\n" +
            "on j.experiment_id=e.id\n" +
            " where c.id=#{courseId} and j.class_id=#{classId} and j.create_time is not null " +
            "order by m.order_id,e.order_id "
    })
    List<Map> selectModuleAndExpleriment(Map<String,Object> map);

}