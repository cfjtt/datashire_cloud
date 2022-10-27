package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.TProject;

import java.util.List;
import java.util.Map;

public interface TProjectService {
    /**
     * 添加project
     * @param project
     * @return
     */
    int addProject(TProject project);

    List<TProject> selectProjectSelective(TProject project);

    /**
     * 全局模糊查询
     * @param page
     * @param offset
     * @param search
     * @return
     */
    List<TProject> selectSelectiveLike(Integer page,Integer offset,String search,Integer isPublic,Integer isSearchPublic);
    Integer selectSelecttiveLikeCount(String search,Integer isPublic,Integer isSearchPublic);

    Integer getCountBySelectSelective(TProject project);

    List<TProject> selectProjectByUserId(int userId,int page,int offset);

    Integer selectCountProjectByUserId(int userId);

    List<Map<String,Object>> selectProjectManagerUserByProjectId(int projectId);

    int updateByPrimaryKey(TProject project);

    TProject selectByPrimaryKey(Integer id);

    int deleteByPrimaryKey(int id);

    Integer getManagerUserCountByUserId(int userId);


}
