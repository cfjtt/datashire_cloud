package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.TProjectConfig;

import java.util.List;

public interface TProjectConfigService {
    List<TProjectConfig> selectSelective(TProjectConfig projectConfig);
}
