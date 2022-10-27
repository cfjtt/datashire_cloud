package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.SysLicense;
import org.apache.ibatis.annotations.*;

/**
 * Created by test on 2018/6/6.
 */
public interface SysLicenseMapper {


    @Select({
            "select * from sys_license"
    })
    SysLicense selectLicenseData();


    @Insert({
            "INSERT INTO `sys_license` (`key`, `license`,`status`) VALUES (#{key},#{license},#{status});"
    })
    void insertLicenseData(SysLicense sysLicense);

    @Insert({
            "<script>",
            "UPDATE `sys_license` ",
            "<trim prefix='set' suffixOverrides=','>  " +
                "<if test='key!=null'>key=#{key},</if> ",
                "<if test='license!=null'>license=#{license},</if> ",
                "<if test='status!=null'>status=#{status},</if> ",
            "</trim>",
            "</script>"
    })
    int updateLicenseData(SysLicense sysLicense);



    @InsertProvider(type=SysLicenseSqlProvider.class, method="insertSelective")
    int insertSelective(SysLicense sysLicense);


    @UpdateProvider(type=SysLicenseSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(SysLicense sysLicense);



}
