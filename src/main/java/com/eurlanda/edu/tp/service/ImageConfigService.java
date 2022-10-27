package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.ImageConfig;
import com.eurlanda.edu.tp.dao.entity.UserStatus;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ImageConfigService {
    int deleteByPrimaryKey(Integer id);
    int insert(ImageConfig record);
    UserStatus selectByPrimaryKey(Integer id);
    int updateByPrimaryKey(ImageConfig record);
    List<ImageConfig> selectImageAddressByCloudware(Integer cloudware_type);
}
