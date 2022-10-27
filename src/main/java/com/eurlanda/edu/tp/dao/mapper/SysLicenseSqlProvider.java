package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.SysLicense;
import com.eurlanda.edu.tp.dao.entity.UserAccessRecord;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by test on 2018/6/6.
 */
public class SysLicenseSqlProvider {


    public String insertSelective(SysLicense license) {
        SQL sql = new SQL();
        sql.INSERT_INTO("sys_license");

        if (license.getKey() != null) {
            sql.VALUES("`key`", "#{key}");
        }

        if (license.getLicense() != null) {
            sql.VALUES("license", "#{license}");
        }

        if (license.getStatus() != null) {
            sql.VALUES("status", "#{status}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(SysLicense license) {
        SQL sql = new SQL();
        sql.UPDATE("sys_license");

        if (license.getKey() != null) {
            sql.SET("`key` = #{key}");
        }

        if (license.getLicense() != null) {
            sql.SET("license", "#{license}");
        }

        if (license.getStatus() != null) {
            sql.SET("status", "#{status}");
        }

        return sql.toString();
    }
}
