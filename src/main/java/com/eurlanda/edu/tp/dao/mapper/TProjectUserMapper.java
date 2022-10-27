package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.TProjectUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TProjectUserMapper {
    @Delete({
            "delete from t_project_user",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Delete({
            "<script>",
            "delete from t_project_user",
            " where project_id = #{maps.projectId,jdbcType=INTEGER}",
            " and user_id in ",
            "<foreach collection='maps.userList' open='(' close=')' separator=',' item='user'>#{user.user_id}</foreach>",
            "</script>"
    })
    void deleteBatch(@Param("maps") Map<String,Object> maps);

    @Insert({
            "insert into t_project_user (project_id,user_id,is_manager,create_time)",
            "values (#{projectId,jdbcType=INTEGER}," +
                    "#{userId,jdbcType=INTEGER},#{isManager,jdbcType=INTEGER},",
            "now())"
    })
    int insert(TProjectUser projectUser);

    @Insert({
            "<script>",
            "insert into t_project_user (project_id,user_id,is_manager,create_time)",
            " values ",
            "<foreach collection='projectUsers' item='projectUser' separator=',' >",
            "(#{projectUser.projectId,jdbcType=INTEGER}," +
                    "#{projectUser.userId,jdbcType=INTEGER},#{projectUser.isManager,jdbcType=INTEGER},",
            "now())",
            "</foreach>",
            "</script>"
    })
    void insertBatch(@Param("projectUsers") List<Map<String,Object>> projectUsers);

    @Select({
            "select",
            "id, project_id,user_id,is_manager,create_time",
            "from t_project_user",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="project_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="is_manager", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE)
    })
    TProjectUser selectByPrimaryKey(Integer id);


    @Update({
            "update t_project_user",
            "set project_id = #{projectId,jdbcType=INTEGER},user_id = #{userId,jdbcType=INTEGER},",
            "is_manager = #{isManager,jdbcType=INTEGER},",
            "create_time = #{createTime,jdbcType=DATE},",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TProjectUser record);

    @Select({
            "<script>",
            "select * from (",
            "select t.tno as no,t.name as name,t.user_id as user_id  from teacher t,(select role,id from user where id not in (select user_id  from t_project_user  where project_id=#{projectId}) ) otherUser where t.user_id = otherUser.id and otherUser.role=2",
            " union ",
            "select s.sno as no,s.name as name,s.user_id as user_id from student s,(select role,id from user where id not in (select user_id  from t_project_user  where project_id=#{projectId}) ) otherUser where s.user_id = otherUser.id and otherUser.role=1",
            ") result where 1=1 ",
            "<if test='search != null and search.trim().length>0'>",
            " and no like concat(concat('%',#{search}),'%') or name like  concat(concat('%',#{search}),'%')",
            "</if>",
            "limit 0,100",
            "</script>"
    })
    List<Map<String,Object>> getAllOtherUser(Map<String,Object> map);

    @Select({
            "select t.tno as no,t.name as name,t.user_id as user_id  from teacher t,(select role,id from user where id  in (select user_id  from t_project_user  where project_id=#{projectId} and is_manager !=1 ) ) otherUser where t.user_id = otherUser.id and otherUser.role=2",
            " union ",
            "select s.sno as no,s.name as name,s.user_id as user_id from student s,(select role,id from user where id  in (select user_id  from t_project_user  where project_id=#{projectId} and is_manager !=1 ) ) otherUser where s.user_id = otherUser.id and otherUser.role=1"
    })
    List<Map<String,Object>> getUserInProject(int projectId);

    @Select({
            "select t.tno as no,t.name as name,t.user_id as user_id  from teacher t,(select role,id from user where id  in (select user_id  from t_project_user  where project_id=#{projectId}  ) ) otherUser where t.user_id = otherUser.id and otherUser.role=2",
            " union ",
            "select s.sno as no,s.name as name,s.user_id as user_id from student s,(select role,id from user where id  in (select user_id  from t_project_user  where project_id=#{projectId}  ) ) otherUser where s.user_id = otherUser.id and otherUser.role=1"
    })
    List<Map<String,Object>> getAllUserInProject(int projectId);

    @Select({
            "select t.tno as no,t.name,t.user_id as user_id  from teacher t,(select role,id from user where id  in (select user_id  from t_project_user  where project_id=#{projectId} and is_manager =1 ) ) otherUser where t.user_id = otherUser.id and otherUser.role=2",
            " union ",
            "select s.sno as no,s.name as name,s.user_id as user_id from student s,(select role,id from user where id  in (select user_id  from t_project_user  where project_id=#{projectId} and is_manager =1 ) ) otherUser where s.user_id = otherUser.id and otherUser.role=1"
    })
    List<Map<String,Object>> getUserManager(int projectId);

    @Update({
            "update t_project_user set is_manager=#{is_manager} where project_id=#{project_id} and user_id = #{user_id}"
    })
    int updateProjectUser(Map<String,Object> map);



    @Select({
            "select",
            "id, project_id,user_id,is_manager,create_time",
            "from t_project_user",
            "where project_id = #{projectId} and user_id=#{userId} limit 0,1"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="project_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="is_manager", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE)
    })
    TProjectUser selectByUserIdAndProjectId(Map<String,Object> map);

    @Delete({
            "delete from t_project_user",
            "where project_id = #{projectId,jdbcType=INTEGER}"
    })
    void deleteByProjectId(int projectId);

    @Delete({
            "delete from t_project_user",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    void deleteByUserId(int userId);

    @Select({
            "<script>",
            "select",
            "id, project_id,user_id,is_manager,create_time",
            "from t_project_user",
            "where 1=1 ",
            "<if test='projectId != null'> and project_id = #{projectId} </if>",
            "<if test='userId != null'> and user_id = #{userId} </if>",
            "<if test='isManager != null'> and is_manager = #{isManager} </if>",
            "</script>"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="project_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="user_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="is_manager", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE)
    })
    List<TProjectUser> selectSelective(TProjectUser projectUser);
}
