package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.TProject;
import org.apache.ibatis.jdbc.SQL;

public class TProjectSqlProvider {
    public String updateByPrimaryKeySelective(TProject record) {
        SQL sql = new SQL();
        sql.UPDATE("t_project");
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        if (record.getIsPublic() != null) {
            sql.SET("is_public = #{isPublic,jdbcType=INTEGER}");
        }

        if (record.getBrefIntro() != null) {
            sql.SET("bref_intro = #{brefIntro,jdbcType=VARCHAR}");
        }

        if (record.getStatus() != null) {
            sql.SET("status = #{status,jdbcType=VARCHAR}");
        }
        if (record.getResults() != null) {
            sql.SET("results = #{results,jdbcType=VARCHAR}");
        }

        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=DATE}");
        }
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=DATE}");
        }
        if (record.getCreator() != null) {
            sql.SET("creator = #{creator,jdbcType=INTEGER}");
        }

        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }
}
