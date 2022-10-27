package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Module;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

public interface ModuleMapper {
    @Delete({
        "delete from module",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into module (id, course_id, ",
        "name,order_id)",
        "values (#{id,jdbcType=INTEGER}, #{courseId,jdbcType=INTEGER}, ",
        "#{name,jdbcType=VARCHAR},#{orderId,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert(Module record);

    @InsertProvider(type=ModuleSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insertSelective(Module record);

    @Select({
        "select",
        "id, course_id, name, order_id",
            "from module",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="order_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    Module selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ModuleSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Module record);

    @Update({
        "update module",
        "set course_id = #{courseId,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Module record);

    @Select({
            "select *",
            "from module",
            "where course_id = #{courseId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
            @Arg(column="course_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="order_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<Module> selectByCourseID(Integer courseId);

    @Select({
            "select m.id moduleId, m.name moduleName, m.order_id moOrderId ,e.id experimentId, e.name experimentName, ",
            "e.description experimentDes, e.cloudware_type cloudwareType, e.deadline_date dueDate, ",
            "e.publish_date publishDate,e.experiment_content experimentContent, e.order_id exOrderId,e.is_answer isAnswer,e.answer_table_name answerTableName, ",
            "e.answer_url answerUrl ,e.old_answer_name oldAnswerName",
            "from module m left join experiment e on e.module_id = m.id",
            "where course_id=#{courseId, jdbcType=INTEGER}",
            "order by moduleId"
    })
    List<Map> selectModuleExperimentInfoByCourseId(int courseId);

    @Select({
            "select * from module where course_id= #{courseId} and name = #{name}"
    })
    List<Module> selectModuleSelective(Module record);

    @Select({
            "select * from module where course_id= #{courseId} and name = #{name} and id != #{id}"
    })
    List<Module> selectModuleIsExist(Module record);

    @Select({
            "SELECT count(1) FROM module m",
            "JOIN course c" ,
            "ON m.course_id = c.id",
            "WHERE m.id = #{moduleId}"
    })
    Integer selectCourseCountByModuleId(@Param("moduleId") Integer moduleId);

    @Select({
        "SELECT MAX(order_id) FROM module " +
        "WHERE course_id = #{courseId}"
    })
    Integer selectMaxOrderIdByCourseId(@Param("courseId") Integer courseId);


    @Select({
            "SELECT id,order_id orderId FROM module WHERE order_id > #{orderId} AND course_id = #{courseId}"
    })
    List<Module> selectInfluenceOrderIdByCourseId(Module module);

}