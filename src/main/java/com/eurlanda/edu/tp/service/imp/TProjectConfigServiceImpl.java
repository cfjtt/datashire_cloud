package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.TProjectConfig;
import com.eurlanda.edu.tp.dao.mapper.TProjectConfigMapper;
import com.eurlanda.edu.tp.service.TProjectConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TProjectConfigServiceImpl implements TProjectConfigService{
    @Autowired
    private TProjectConfigMapper mapper;

    @Override
    public List<TProjectConfig> selectSelective(TProjectConfig projectConfig) {
        return mapper.selectSelective(projectConfig);
    }
}
