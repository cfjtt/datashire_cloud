package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.ProjectDocFolderService;
import com.eurlanda.edu.tp.webdomain.response.ResProjectDocList;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
@Service
public class ProjectDocFolderServiceImpl implements ProjectDocFolderService {

    @Autowired
    private TProjectDocFolderMapper tProjectDocFolderMapper;
    @Autowired
    private TProjectDocMapper tProjectDocMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TProjectUserMapper tProjectUserMapper;
    @Autowired
    private TProjectDocAnnexMapper tProjectDocAnnexMapper;
    @Value("${projectDocFileUrl}")
    private String projectDocFileUrl;



    @Override
    public String createProjectFolder(int projectId, String folderName,int userId) throws ApplicationErrorException {
       //查询用户是否是该项目组成员
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            //查询文件夹是否存在
            Map<String,Object> map=new HashMap<>();
            map.put("projectId",projectId);
            map.put("name",folderName);
            TProjectDocFolder tProjectDocFolder=tProjectDocFolderMapper.selectByProjectIdAndName(map);
            if(tProjectDocFolder!=null){
                throw new ApplicationErrorException(ErrorCode.FolerIsExist);//文件夹已经存在
            }else{
                TProjectDocFolder docFolder=new TProjectDocFolder();
                docFolder.setName(folderName);
                docFolder.setCreateTime(new Date());
                docFolder.setUpdateTime(new Date());
                docFolder.setProjectId(projectId);
                tProjectDocFolderMapper.insert(docFolder);
            }
            return "1";
        }else{
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }


    }

    /**
     * 编辑文件夹
     * @param projectId
     * @param oldName
     * @param newName
     * @return
     * @throws ApplicationErrorException
     */
    @Override
    public String updateProjectFolder(int projectId, String oldName, String newName,int userId) throws ApplicationErrorException {
        String stats="1";
        //查询原文件是否存在
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            Map<String,Object> map=new HashMap<>();
            map.put("projectId",projectId);
            map.put("name",oldName);
            TProjectDocFolder oldFolder=tProjectDocFolderMapper.selectByProjectIdAndName(map);
            if(oldFolder!=null){
                if(!oldName.equals(newName)){
                    //查询新文件夹名称是否存在
                    map=new HashMap<>();
                    map.put("projectId",projectId);
                    map.put("name",newName);
                    TProjectDocFolder newFolder=tProjectDocFolderMapper.selectByProjectIdAndName(map);
                    if(newFolder!=null){
                        throw new ApplicationErrorException(ErrorCode.FolerIsExist);
                    }else{
                        oldFolder.setName(newName);
                    }
                }
                oldFolder.setUpdateTime(new Date());
                tProjectDocFolderMapper.updateByPrimaryKeySelective(oldFolder);
            }else{
                throw new ApplicationErrorException(ErrorCode.FolerIsNotExist);
            }
            return stats;
        }else {
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }
    }

    @Override
    public String deleteProjectFoler(int projectId, Integer folderId,int userId) throws ApplicationErrorException {
        String stats="1";
        //查询文件夹是否存在
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            TProjectDocFolder folder=tProjectDocFolderMapper.selectByPrimaryKey(folderId);
            if(folder!=null){
                //判断文件夹是否有文档
                int docCount=tProjectDocMapper.findCountByFolderId(folder.getId());
                if(docCount>0){
                    throw new ApplicationErrorException(ErrorCode.FolerExistDoc);
                }
                //删除实体类
                tProjectDocFolderMapper.deleteByPrimaryKey(folderId);
            }else{
                throw new ApplicationErrorException(ErrorCode.FolerIsNotExist);
            }

            return stats;
        }else{
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }
    }

    @Override
    public Map<String,Object> findProjectFileList(int projectId) {
        ResProjectDocList resProjectDocList=new ResProjectDocList();
        //查询所有文件夹
        List<TProjectDocFolder> projectDocFolders=tProjectDocFolderMapper.findByProjectId(projectId);
        if(projectDocFolders!=null && projectDocFolders.size()>0){
            List<ResProjectDocList.DocInfo> docInfoList=new ArrayList<>();
            for(TProjectDocFolder projectDocFolder:projectDocFolders){
                //根据folderId查询所有文档
                List<Map> tProjectDocMap=tProjectDocMapper.findByfolderId(projectDocFolder.getId());
                if(tProjectDocMap!=null && tProjectDocMap.size()>0){
                    for(Map tProjectDoc:tProjectDocMap){
                        ResProjectDocList.DocInfo docInfo=new ResProjectDocList.DocInfo();
                        docInfo.setContent((String) tProjectDoc.get("content"));
                        docInfo.setCreateTime((Date) tProjectDoc.get("create_time"));
                        docInfo.setUpdateTime((Date) tProjectDoc.get("update_time"));
                        docInfo.setId(Integer.parseInt((String) tProjectDoc.get("id")));
                        docInfo.setFolderId(Integer.parseInt((String) tProjectDoc.get("folder_id")));
                        docInfo.setDocName((String) tProjectDoc.get("doc_name"));
                        docInfo.setCreator(Integer.parseInt((String) tProjectDoc.get("creator")));
                        docInfo.setEditor(Integer.parseInt((String) tProjectDoc.get("editor")));
                        //根据角色查询 姓名
                        int creatorRole=Integer.parseInt((String) tProjectDoc.get("creatorRole"));
                        //学生
                        if(creatorRole>0 && creatorRole==1){
                            Student student=studentMapper.selectByUserId(docInfo.getCreator());
                            if(student!=null){
                                docInfo.setCreatorName(student.getName());
                            }

                        }else if(creatorRole>0 && creatorRole==2){//老师
                            Teacher teacher=teacherMapper.selectByUserId(docInfo.getCreator());
                            if(teacher!=null){
                                docInfo.setCreatorName(teacher.getName());
                            }
                        }
                        //
                        int editorRole=Integer.parseInt((String) tProjectDoc.get("editorRole"));
                            //学生
                        if(editorRole>0 && editorRole==1){
                            Student student=studentMapper.selectByUserId(docInfo.getEditor());
                            if(student!=null){
                                docInfo.setEditorName(student.getName());
                            }

                        }else if(editorRole>0 && editorRole==2){//老师
                            Teacher teacher=teacherMapper.selectByUserId(docInfo.getEditor());
                            if(teacher!=null){
                                docInfo.setEditorName(teacher.getName());
                            }
                        }
                        docInfoList.add(docInfo);
                    }
                }
                resProjectDocList.setDocInfoList(docInfoList);
            }
        }
        Map<String,Object> map=new HashMap<>();
        map.put("folderList",projectDocFolders);
        map.put("docList",resProjectDocList);
        return map;
    }


    /**
     * 查询所有文件夹
     * @return
     */
    @Override
    public List<TProjectDocFolder> findFolderByProject(Integer projectId) {
        List<TProjectDocFolder> docFolders=tProjectDocFolderMapper.findAllFolder(projectId);
        return docFolders;
    }

    @Override
    public void deleteAllByProjectId(Integer projectId)throws IOException,ApplicationErrorException {
        List<TProjectDocFolder> folderList=tProjectDocFolderMapper.findAllFolder(projectId);
        if(folderList!=null && folderList.size()>0){
            for(TProjectDocFolder folder:folderList){
                List<TProjectDoc> docList=tProjectDocMapper.findDocfolderId(folder.getId());
                if(docList!=null && docList.size()>0){
                    for(TProjectDoc doc:docList){
                        List<TProjectDocAnnex> annexList=tProjectDocAnnexMapper.findByDocId(doc.getId());
                        if(annexList!=null && annexList.size()>0){
                            //删除整个文件夹
                            File file=new File(projectDocFileUrl+doc.getId());
                            if(file.exists() && file.isDirectory()){
                                FileUtils.deleteDirectory(file);
                                //删除附件
                                tProjectDocAnnexMapper.deleteBydocId(doc.getId());
                            }
                        }
                    }
                    //删除文档
                    tProjectDocMapper.deleteByFolderId(folder.getId());
                }
            }
            //删除项目
            tProjectDocFolderMapper.deleteByProjectId(projectId);
        }
    }
}
