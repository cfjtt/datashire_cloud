package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.TProjectUser;

import java.util.List;
import java.util.Map;

public interface TProjectUserService {
    int addProjectUser(TProjectUser projectUser);
    List<Map<String,Object>> getAllOtherUser(int projectId,String search);
    List<Map<String,Object>> getUserInProject(int projectId);
    List<Map<String,Object>> getUserManager(int projectId);
    void insertBatch(List<Map<String,Object>> projectUsers);
    void deleteBatch(Map<String,Object> maps);
    void updateProjectUser(Map<String,Object> map);
    List<Map<String,Object>> getAllUserInProject(int projectId);
    TProjectUser selectByUserIdAndProjectId(Map<String,Object> map);
    void deleteByProjectId(int projectId);
    void deleteByUserId(int userId);
    List<TProjectUser> selectSelective(TProjectUser projectUser);
}
