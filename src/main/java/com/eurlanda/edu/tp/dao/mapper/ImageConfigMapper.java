package com.eurlanda.edu.tp.dao.mapper;

import com.eurlanda.edu.tp.dao.entity.ImageConfig;
import com.eurlanda.edu.tp.dao.entity.UserStatus;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ImageConfigMapper {
    @Delete({
            "delete from image_config",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into image_config (cloudware_type, ",
            "address)",
            "values (#{cloudwareType,jdbcType=INTEGER}, ",
            "#{address,jdbcType=INTEGER})"
    })
    int insert(ImageConfig record);

    @Select({
            "select",
            "id, cloudware_type, address",
            "from image_config",
            "where id = #{id,jdbcType=INTEGER}"
    })
    UserStatus selectByPrimaryKey(Integer id);

    @Update({
            "update image_config",
            "set cloudware_type = #{cloudwareType,jdbcType=INTEGER},",
            "address = #{address,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ImageConfig record);

    @Select({
            "select",
            "id, cloudware_type, address",
            "from image_config",
            "where cloudware_type = #{cloudware_type,jdbcType=INTEGER}"
    })
    List<ImageConfig> selectImageAddressByCloudware(Integer cloudware_type);
}
