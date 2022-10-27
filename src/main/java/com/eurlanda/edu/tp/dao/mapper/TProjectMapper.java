package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.TProject;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TProjectMapper {
    @Delete({
            "delete from t_project",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);


    @Insert({
            "insert into t_project (name,is_public,bref_intro,status,results,create_time,update_time,creator)",
            "values (#{name,jdbcType=VARCHAR}," +
                    "#{isPublic,jdbcType=INTEGER},",
            "#{brefIntro,jdbcType=VARCHAR},#{status,jdbcType=INTEGER},#{results,jdbcType=VARCHAR},now(),#{updateTime,jdbcType=DATE},#{creator,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TProject project);

    @Select({
            "select",
            "id, name,is_public, bref_intro,status,results,create_time,update_time,creator",
            "from t_project",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="is_public", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="bref_intro", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="status", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="results", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="creator", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    TProject selectByPrimaryKey(Integer id);

    @Select({
            "<script>",
            "select id, name,is_public, bref_intro,status,results,create_time,update_time,creator",
            "from t_project where 1=1 ",
            "<if test='name != null'> and name =BINARY #{name}</if>",
            "<if test='isPublic != null'> and is_public = #{isPublic}</if>",
            "<if test='status != null'> and status = #{status}</if>",
            "<if test='creator != null'> and creator = #{creator}</if>",
            "order by id desc",
            "<if test='start != null and offset != null and offset>0 '> limit #{start},#{offset} lock in share mode </if>",
            "</script>"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="is_public", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="bref_intro", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="status", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="results", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="creator", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<TProject> selectSelective(TProject record);

    @Select({
            "<script>",
            "select  distinct id, name,is_public, bref_intro,status,results,create_time,update_time,creator from (",
            "select * from (",
            "select re.*, case re.status when 1 then '进行中' when  0 then '已完成' end as state,case re.is_public when 1 then '是' when  0 then '否' end as searchPublic,st.sno as no,st.name as uname from ( ",
            "select t.*,tpu.user_id ",
            "from t_project t left join t_project_user tpu on t.id = tpu.project_id and tpu.is_manager = 1 where 1=1 <if test='isPublic != null'> and t.is_public = #{isPublic} </if> ",
            " ) re  left join student st on re.user_id = st.user_id ",
            " union ",
            " select re.*, case re.status when 1 then '进行中' when  0 then '已完成' end as state,case re.is_public when 1 then '是' when  0 then '否' end as searchPublic,st.tno as no,st.name as uname from (",
            " select t.*,tpu.user_id ",
            " from t_project t left join t_project_user tpu on t.id = tpu.project_id and tpu.is_manager = 1 where 1=1 <if test='isPublic != null'> and t.is_public = #{isPublic} </if> ",
            " ) re  left join teacher st on re.user_id = st.user_id",
            ") result where 1=1 ",
            "<if test='search != null and search.length>0'>",
            " and ( name like CONCAT(CONCAT('%',#{search},'%')) or id like CONCAT(CONCAT('%',#{search},'%')) or no like CONCAT(CONCAT('%',#{search},'%')) or uname like CONCAT(CONCAT('%',#{search},'%')) or DATE_FORMAT(create_time,'%Y-%m-%d') like CONCAT(CONCAT('%',#{search},'%')) or state like CONCAT(CONCAT('%',#{search},'%'))",
            "<if test='isSearchPublic != null'>",
            " or searchPublic like CONCAT(CONCAT('%',#{search},'%')) ",
            "</if>",
            ") ",
            "</if>",
            " ) r2",
            " order by id desc ",
            "<if test='start != null and offset != null and offset>0 '> limit #{start},#{offset}</if>",
            "</script>"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="is_public", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="bref_intro", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="status", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="results", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="creator", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<TProject> selectSelectiveLike(@Param("start") Integer start,@Param("offset") Integer offset,@Param("search") String search,@Param("isPublic") Integer isPublic,@Param("isSearchPublic") Integer isSearchPublic);

    @Select({
            "<script>",
            "select count(1) from (",
            "select distinct id, name,is_public, bref_intro,status,results,create_time,update_time,creator from (",
            "select * from (",
            "select re.*, case re.status when 1 then '进行中' when  0 then '已完成' end as state,case re.is_public when 1 then '是' when  0 then '否' end as searchPublic,st.sno as no,st.name as uname from ( ",
            "select t.*,tpu.user_id ",
            "from t_project t left join t_project_user tpu on t.id = tpu.project_id and tpu.is_manager = 1 where 1=1 <if test='isPublic != null'> and t.is_public = #{isPublic} </if> ",
            " ) re  left join student st on re.user_id = st.user_id ",
            " union ",
            " select re.*, case re.status when 1 then '进行中' when  0 then '已完成' end as state,case re.is_public when 1 then '是' when  0 then '否' end as searchPublic,st.tno as no,st.name as uname from (",
            " select t.*,tpu.user_id ",
            " from t_project t left join t_project_user tpu on t.id = tpu.project_id and tpu.is_manager = 1 where 1=1 <if test='isPublic != null '> and t.is_public = #{isPublic} </if>",
            " ) re  left join teacher st on re.user_id = st.user_id",
            ") result where 1=1 ",
            "<if test='search != null and search.length>0'>",
            " and ( name like CONCAT(CONCAT('%',#{search},'%')) or id like CONCAT(CONCAT('%',#{search},'%')) or no like CONCAT(CONCAT('%',#{search},'%')) or uname like CONCAT(CONCAT('%',#{search},'%')) or DATE_FORMAT(create_time,'%Y-%m-%d') like CONCAT(CONCAT('%',#{search},'%')) or state like CONCAT(CONCAT('%',#{search},'%'))",
            "<if test='isSearchPublic != null'>",
            " or searchPublic like CONCAT(CONCAT('%',#{search},'%')) ",
            "</if>",
            ") ",
            "</if>",
            ") r2 ) r3",
            "</script>"
    })
    Integer selectSelecttiveLikeCount(@Param("search") String search,@Param("isPublic") Integer isPublic,@Param("isSearchPublic") Integer isSearchPublic);

    @Select({
            "<script>",
            "select count(1)",
            "from t_project where 1=1 ",
            "<if test='name != null'> and name = #{name}</if>",
            "<if test='isPublic != null'> and is_public = #{isPublic}</if>",
            "<if test='status != null'> and status = #{status}</if>",
            "<if test='creator != null'> and creator = #{creator}</if>",
            "</script>"
    })
    Integer getCountBySelectSelective(TProject record);

    @UpdateProvider(type=TProjectSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TProject record);

    @Update({
            "update t_project",
            "set name = #{name,jdbcType=VARCHAR},is_public = #{isPublic,jdbcType=INTEGER},",
            "bref_intro = #{brefIntro,jdbcType=VARCHAR},",
            "status = #{status,jdbcType=INTEGER},",
            "results = #{results,jdbcType=VARCHAR},",
            "update_time = now(),",
            "creator=#{creator,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TProject record);

    @Select({
            "<script>",
            "select tp.* ",
            "from t_project tp,t_project_user tpu where  tp.id = tpu.project_id and tpu.user_id = #{userId} ",
            "order by tp.id desc ",
            "<if test='start != null and offset != null and offset>0 '> limit #{start},#{offset}  </if>",
            "</script>"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="is_public", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="bref_intro", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="status", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="results", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="creator", javaType=Integer.class, jdbcType=JdbcType.INTEGER)
    })
    List<TProject> selectProjectByUserId(Map<String,Object> map);

    @Select({
            "<script>",
            "select count(1) ",
            "from t_project tp,t_project_user tpu where  tp.id = tpu.project_id and tpu.user_id=#{userId}",
            "</script>"
    })
    Integer selectCountProjectByUserId(int userId);

    @Select({
            "<script>",
            "select u.* ",
            "from t_project_user tpu,user u where tpu.user_id = u.id and tpu.is_manager=1 and tpu.project_id=#{projectId}",
            "</script>"
    })
    List<Map<String,Object>> selectProjectManagerUserByProjectId(int projectId);

    @Select({
            "<script>",
            "select count(1) ",
            "from t_project tp,t_project_user tpu where  tp.id = tpu.project_id and tpu.user_id=#{userId} and tpu.is_manager = 1",
            "</script>"
    })
    Integer getManagerUserCountByUserId(int userId);

}
