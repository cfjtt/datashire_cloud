package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.Cloudware;
import org.apache.ibatis.jdbc.SQL;

public class CloudwareSqlProvider {

    public String insertSelective(Cloudware record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("cloudware");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getWebSocket() != null) {
            sql.VALUES("web_socket", "#{webSocket,jdbcType=VARCHAR}");
        }
        
        if (record.getServiceId() != null) {
            sql.VALUES("service_id", "#{serviceId,jdbcType=VARCHAR}");
        }
        
        if (record.getInstanceId() != null) {
            sql.VALUES("instance_id", "#{instanceId,jdbcType=VARCHAR}");
        }
        
        if (record.getServiceName() != null) {
            sql.VALUES("service_name", "#{serviceName,jdbcType=VARCHAR}");
        }
        
        if (record.getPulsarId() != null) {
            sql.VALUES("pulsar_id", "#{pulsarId,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Cloudware record) {
        SQL sql = new SQL();
        sql.UPDATE("cloudware");
        
        if (record.getWebSocket() != null) {
            sql.SET("web_socket = #{webSocket,jdbcType=VARCHAR}");
        }
        
        if (record.getServiceId() != null) {
            sql.SET("service_id = #{serviceId,jdbcType=VARCHAR}");
        }
        
        if (record.getInstanceId() != null) {
            sql.SET("instance_id = #{instanceId,jdbcType=VARCHAR}");
        }
        
        if (record.getServiceName() != null) {
            sql.SET("service_name = #{serviceName,jdbcType=VARCHAR}");
        }
        
        if (record.getPulsarId() != null) {
            sql.SET("pulsar_id = #{pulsarId,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}