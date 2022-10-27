package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.TProject;
import com.eurlanda.edu.tp.dao.entity.TProjectDocFolder;
import com.eurlanda.edu.tp.error.ApplicationErrorException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProjectDocFolderService {

    public String createProjectFolder(int projectId,String folderName,int userId)throws ApplicationErrorException;
    public String  updateProjectFolder(int projectId,String oldName,String newName,int userId) throws ApplicationErrorException;
    public String deleteProjectFoler(int projectId,Integer folderId,int userId)throws ApplicationErrorException;
    public Map<String,Object> findProjectFileList(int projectId);

    List<TProjectDocFolder> findFolderByProject(Integer projectId);

    void deleteAllByProjectId(Integer id)throws IOException,ApplicationErrorException;

}
