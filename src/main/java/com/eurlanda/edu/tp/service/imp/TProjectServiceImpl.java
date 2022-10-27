package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.TProject;
import com.eurlanda.edu.tp.dao.mapper.TProjectMapper;
import com.eurlanda.edu.tp.service.TProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TProjectServiceImpl implements TProjectService{
    @Autowired
    private TProjectMapper mapper;
    @Override
    public int addProject(TProject project) {
        return mapper.insert(project);
    }

    @Override
    public int deleteByPrimaryKey(int id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TProject> selectProjectSelective(TProject project) {
        return mapper.selectSelective(project);
    }

    @Override
    public List<TProject> selectSelectiveLike(Integer page, Integer offset, String search, Integer isPublic, Integer isSearchPublic) {
        int start = page >=1 ? (page-1)*offset : 0;
        return mapper.selectSelectiveLike(start,offset,search,isPublic,isSearchPublic);
    }

    @Override
    public Integer selectSelecttiveLikeCount(String search,Integer isPublic,Integer isSearchPublic) {
        return mapper.selectSelecttiveLikeCount(search,isPublic,isSearchPublic);
    }

    @Override
    public Integer getCountBySelectSelective(TProject project) {
        return mapper.getCountBySelectSelective(project);
    }

    @Override
    public List<TProject> selectProjectByUserId(int userId,int page,int offset) {
        Map<String,Object> paramMap = new HashMap<>();
        int start = page >=1 ? (page-1)*offset : 0;
        paramMap.put("start",start);
        paramMap.put("offset",offset);
        paramMap.put("userId",userId);
        return mapper.selectProjectByUserId(paramMap);
    }

    @Override
    public Integer selectCountProjectByUserId(int userId) {
        return mapper.selectCountProjectByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> selectProjectManagerUserByProjectId(int projectId) {
        return mapper.selectProjectManagerUserByProjectId(projectId);
    }

    @Override
    public int updateByPrimaryKey(TProject project) {
        return mapper.updateByPrimaryKey(project);
    }

    @Override
    public TProject selectByPrimaryKey(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer getManagerUserCountByUserId(int userId) {
        return mapper.getManagerUserCountByUserId(userId);
    }
}
