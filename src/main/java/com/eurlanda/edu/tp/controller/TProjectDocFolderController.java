package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.dao.entity.TProjectDocFolder;
import com.eurlanda.edu.tp.dao.mapper.TProjectDocFolderMapper;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.ProjectDocFolderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("folder")
public class TProjectDocFolderController {
    @Autowired
    private ProjectDocFolderService projectDocFolderService;
    @Autowired
    private TProjectDocFolderMapper tProjectDocFolderMapper;
    /**
     * 创建文件夹
     */
    @ApiOperation(value = "创建文件夹")
    @PostMapping("addProjectFolder")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage addProjectFolder(Integer projectId,String folderName,Integer userId)throws Exception{
        projectDocFolderService.createProjectFolder(projectId,folderName,userId);
        return new ResponseMessage.Success();
    }

    /**
     * 编辑文件夹
     */
    @ApiOperation(value = "编辑文件夹")
    @PostMapping("updateProjectFolder")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage updateProjectFolder(Integer projectId,String oldName,String newName,Integer userId)throws Exception{
        projectDocFolderService.updateProjectFolder(projectId,oldName,newName,userId);
        return new ResponseMessage.Success();
    }

    /**
     * 删除文件夹
     */
    @ApiOperation(value = "删除文件夹")
    @PostMapping("deleteProjectFolder")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteProjectFolder(Integer projectId,Integer folderId,Integer userId)throws Exception{
        projectDocFolderService.deleteProjectFoler(projectId,folderId,userId);
        return new ResponseMessage.Success();
    }


    /**
     * 查询所有文件夹
     */
    @ApiOperation(value = "查询所有文件夹")
    @PostMapping("findFolderByProjectId")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage<List<TProjectDocFolder>> findFolderByProjectId(Integer projectId)throws Exception{
       List<TProjectDocFolder>  folderList=projectDocFolderService.findFolderByProject(projectId);
        return new ResponseMessage.Success(folderList);
    }



    /**
     * 查询所有文件夹
     */
    @ApiOperation(value = "根据编号 查询文件夹")
    @PostMapping("findFolderByd")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage<TProjectDocFolder> findFolderByd(Integer folderId)throws Exception{
        TProjectDocFolder  folder=tProjectDocFolderMapper.selectByPrimaryKey(folderId);
        return new ResponseMessage.Success(folder);
    }
}
