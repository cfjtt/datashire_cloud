package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.TProjectUser;
import com.eurlanda.edu.tp.dao.mapper.TProjectUserMapper;
import com.eurlanda.edu.tp.service.TProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TProjectUserServiceimpl implements TProjectUserService{
    @Autowired
    private TProjectUserMapper mapper;

    @Override
    public int addProjectUser(TProjectUser projectUser) {
        return mapper.insert(projectUser);
    }

    @Override
    public List<Map<String, Object>> getAllOtherUser(int projectId,String search) {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("search",search);
        return mapper.getAllOtherUser(map);
    }

    @Override
    public List<Map<String, Object>> getUserInProject(int projectId) {
        return mapper.getUserInProject(projectId);
    }

    @Override
    public List<Map<String, Object>> getAllUserInProject(int projectId) {
        return mapper.getAllUserInProject(projectId);
    }

    @Override
    public List<Map<String, Object>> getUserManager(int projectId) {
        return mapper.getUserManager(projectId);
    }

    /**
     *
     * @param projectUsers  key:projectId,isManager,userId
     */
    @Override
    public void insertBatch(List<Map<String,Object>> projectUsers) {
        mapper.insertBatch(projectUsers);
    }

    /**
     *
     * @param maps key:projectId
     *                  userList user_id
     */
    @Override
    public void deleteBatch(Map<String, Object> maps) {
        mapper.deleteBatch(maps);
    }

    /**
     *
     * @param map  key:user_id,project_id,is_manager
     */
    @Override
    public void updateProjectUser(Map<String, Object> map) {
        mapper.updateProjectUser(map);
    }

    /**
     * @param map  key userId,projectId
     * @return
     */
    @Override
    public TProjectUser selectByUserIdAndProjectId(Map<String, Object> map) {
        return mapper.selectByUserIdAndProjectId(map);
    }

    @Override
    public void deleteByProjectId(int projectId) {
         mapper.deleteByProjectId(projectId);
    }

    @Override
    public void deleteByUserId(int userId) {
        mapper.deleteByUserId(userId);
    }

    @Override
    public List<TProjectUser> selectSelective(TProjectUser projectUser) {
        return mapper.selectSelective(projectUser);
    }
}
