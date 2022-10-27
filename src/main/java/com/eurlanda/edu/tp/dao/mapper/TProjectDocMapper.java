package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.TProjectDoc;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TProjectDocMapper {
    @Delete({
            "delete from t_project_doc",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Delete({
            "delete from t_project_doc",
            "where folder_id = #{folderId,jdbcType=INTEGER}"
    })
    int deleteByFolderId(Integer folderId);


    @Insert({
            "insert into t_project_doc (folder_id,content,create_time,update_time,creator,editor,doc_name)",
            "values (#{folderId,jdbcType=INTEGER}," +
                    "#{content,jdbcType=VARCHAR},",
            "#{createTime,jdbcType=DATE},#{updateTime,jdbcType=DATE},#{creator,jdbcType=INTEGER},#{editor,jdbcType=INTEGER},#{docName,jdbcType=VARCHAR})"
    })
    int insert(TProjectDoc projectDoc);

    @Select({
            "select",
            "id, folder_id,content, create_time,update_time,creator,editor,doc_name",
            "from t_project_doc",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="folder_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="content", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.TIMESTAMP),
            @Arg(column="creator", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="editor", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="doc_name", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    TProjectDoc selectByPrimaryKey(Integer id);

    @UpdateProvider(type=TProjectDocSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TProjectDoc record);

    @Update({
            "update t_project_doc",
            "set folder_id = #{folderId,jdbcType=INTEGER},content = #{content,jdbcType=VARCHAR},",
            "create_time = #{createTime,jdbcType=DATE},",
            "update_time = #{updateTime,jdbcType=DATE},",
            "creator = #{creator,jdbcType=INTEGER},",
            "editor = #{editor,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TProjectDoc record);


    @Select({
            "<script>",
           "select d.id,d.doc_name,d.create_time,d.update_time,d.folder_id,d.content,d.creator,d.editor," +
                   "a.`name` as editorName,b.`name` as creatorName from t_project_doc d" +
                   "  inner join t_project_doc_folder f " +
                   " on f.id=d.folder_id" +
                   "   left join " +
                   "(select s.`name`,s.user_id from student s where s.user_id in (\n" +
                   "select editor from t_project_doc ) UNION \n" +
                   "select t.`name`,t.user_id from teacher t where t.user_id in (\n" +
                   "select editor from t_project_doc )) a" +
                   " on d.editor=a.user_id " +
                   "  left join " +
                   "(select s.`name`,s.user_id from student s where s.user_id in (\n" +
                   "select creator from t_project_doc ) UNION \n" +
                   "select t.`name`,t.user_id from teacher t where t.user_id in (\n" +
                   "select creator from t_project_doc )) b" +
                   " on d.creator=b.user_id " +
                   " where f.project_id=#{projectId} ",
            "<if test='folderId != null and folderId >0 '> and d.folder_id=#{folderId}</if>",
            "<if test='keywordStr != null and keywordStr.length()>0'>" +
                    " and (d.id like concat(concat('%',#{keywordStr},'%'))" +
                    " or d.doc_name like concat(concat('%',#{keywordStr},'%'))" +
                    " or DATE_FORMAT(d.create_time,'%Y-%m-%d') like binary concat(concat('%',#{keywordStr},'%'))" +
                    " or DATE_FORMAT(d.update_time,'%Y-%m-%d') like binary concat(concat('%',#{keywordStr},'%'))" +
                    " or a.`name` like concat(concat('%',#{keywordStr},'%'))" +
                    " or b.`name` like concat(concat('%',#{keywordStr},'%'))" +
                    ")" +
                    "</if>",
            " order by d.id desc",
            "<if test='start != null and offset != null and offset>0 '> limit #{start},#{offset}</if>",

            "</script>"
    })
    List<Map> findByfolderMap(Map<String,Object> map);



    @Select({
            "select",
            "id, folder_id,content, create_time,update_time,creator,editor,doc_name,u.role as creatorRole,s.role as editorRole",
            "from t_project_doc t",
            "inner join user u on t.creator=u.id",
            "inner join user s on t.editor=s.id",
            "where folder_id = #{folderId,jdbcType=INTEGER}"
    })
    List<Map> findByfolderId(Integer folderId);
    @Select({
            "select",
            "id, folder_id,content, create_time,update_time,creator,editor,doc_name",
            "from t_project_doc",
            "where folder_id = #{folderId,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="folder_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="content", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="creator", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="editor", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="doc_name", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<TProjectDoc> findDocfolderId(Integer folderId);


    @Select({
            "<script>",
            "select IFNULL(count(1),0) from t_project_doc d " +
                    "  inner join t_project_doc_folder f " +
                    " on f.id=d.folder_id " +
                    "   left join " +
                    "(select s.`name`,s.user_id from student s where s.user_id in (\n" +
                    "select editor from t_project_doc ) UNION \n" +
                    "select t.`name`,t.user_id from teacher t where t.user_id in (\n" +
                    "select editor from t_project_doc )) a" +
                    " on d.creator=a.user_id " +
                    "  left join " +
                    "(select s.`name`,s.user_id from student s where s.user_id in (\n" +
                    "select creator from t_project_doc ) UNION \n" +
                    "select t.`name`,t.user_id from teacher t where t.user_id in (\n" +
                    "select creator from t_project_doc )) b" +
                    " on d.editor=b.user_id " +
                    " where f.project_id=#{projectId} ",
            "<if test='folderId != null and folderId>0 '> and d.folder_id=#{folderId}</if>",
            "<if test='keywordStr != null and keywordStr.length()>0'>" +
                    " and (d.id like concat(concat('%',#{keywordStr},'%'))" +
                    " or d.doc_name like concat(concat('%',#{keywordStr},'%'))" +
                    " or DATE_FORMAT(d.create_time,'%Y-%m-%d') like binary concat(concat('%',#{keywordStr},'%'))" +
                    " or DATE_FORMAT(d.update_time,'%Y-%m-%d') like binary concat(concat('%',#{keywordStr},'%'))" +
                    " or a.`name` like concat(concat('%',#{keywordStr},'%'))" +
                    " or b.`name` like concat(concat('%',#{keywordStr},'%'))" +
                    ")" +
                    "</if>",
            "</script>"
    })
    int findCountByMap(Map<String,Object> map);


    @Select({
            "select count(1) from t_project_doc where folder_id=#{folderId}"
    })
    int findCountByFolderId(int folderId);

    @InsertProvider(type=TProjectDocSqlProvider.class,method = "insertSelective")
    int insertSelective(TProjectDoc doc);



    @Select({
            "select",
            "id, folder_id,content, create_time,update_time,creator,editor,doc_name,u.role as creatorRole,s.role as editorRole",
            "from t_project_doc t",
            "inner join user u on t.creator=u.id",
            "inner join user s on t.editor=s.id"
    })
    List<Map> findAllDoc();

    @Select({
            "select",
            "id, folder_id,content, create_time,update_time,creator,editor,doc_name",
            "from t_project_doc",
            "where folder_id = #{folderId} and doc_name=BINARY #{docName}"
    })
    @ConstructorArgs({
            @Arg(column="id", javaType=Integer.class, jdbcType= JdbcType.INTEGER, id=true),
            @Arg(column="folder_id", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="content", javaType=String.class, jdbcType=JdbcType.VARCHAR),
            @Arg(column="create_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="update_time", javaType=Date.class, jdbcType=JdbcType.DATE),
            @Arg(column="creator", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="editor", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
            @Arg(column="doc_name", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    TProjectDoc findByFolderIdAndDocName(Map<String,Object> map);
}
