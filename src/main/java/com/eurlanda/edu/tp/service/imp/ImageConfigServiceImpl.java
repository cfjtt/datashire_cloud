package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.ImageConfig;
import com.eurlanda.edu.tp.dao.entity.UserStatus;
import com.eurlanda.edu.tp.dao.mapper.ImageConfigMapper;
import com.eurlanda.edu.tp.dao.mapper.UserStatusMapper;
import com.eurlanda.edu.tp.service.ImageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageConfigServiceImpl implements ImageConfigService{

    @Autowired
    private ImageConfigMapper configMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return configMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ImageConfig record) {
        return configMapper.insert(record);
    }

    @Override
    public UserStatus selectByPrimaryKey(Integer id) {
        return configMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(ImageConfig record) {
        return configMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<ImageConfig> selectImageAddressByCloudware(Integer cloudware_type) {
        return configMapper.selectImageAddressByCloudware(cloudware_type);
    }
}
