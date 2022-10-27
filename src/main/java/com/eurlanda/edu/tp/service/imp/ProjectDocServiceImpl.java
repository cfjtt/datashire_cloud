package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.FileService;
import com.eurlanda.edu.tp.service.ProjectDocService;
import com.eurlanda.edu.tp.webdomain.request.ReqAddProjectDoc;
import com.eurlanda.edu.tp.webdomain.response.ResProjectDocList;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.mapreduce.v2.app.webapp.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
@Service
public class ProjectDocServiceImpl implements ProjectDocService {
    @Autowired
    private TProjectDocFolderMapper tProjectDocFolderMapper;
    @Autowired
    private TProjectDocMapper tProjectDocMapper;
    @Autowired
    private TProjectDocAnnexMapper tProjectDocAnnexMapper;
    @Autowired
    private FileServiceImp fileServiceImp;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TProjectUserMapper tProjectUserMapper;
    @Value("${projectDocFileUrl}")
    private String projectDocFileUrl;
    @Value("${projectFileUrl}")
    private String projectFileUrl;


    /**
     * 创建文档
     * @param projectId
     * @param folderId
     * @param docName
     * @param userId
     */
    @Override
    public void createProjectDoc(int projectId, Integer folderId, String docName, int userId) throws ApplicationErrorException{
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            //查询文件夹Id
            TProjectDocFolder folder=tProjectDocFolderMapper.selectByPrimaryKey(folderId);
            if(folder!=null){
                //判断文档名是否存在
               Map<String,Object> map=new HashMap<>();
                map.put("folderId",folder.getId());
                map.put("docName",docName);
                TProjectDoc tProjectDoc=tProjectDocMapper.findByFolderIdAndDocName(map);
                if(tProjectDoc!=null){
                    throw new ApplicationErrorException(ErrorCode.DocIsExist);
                }else{
                    TProjectDoc doc=new TProjectDoc();
                    doc.setCreateTime(new Date());
                    doc.setUpdateTime(new Date());
                    doc.setFolderId(folder.getId());
                    doc.setCreator(userId);
                    doc.setEditor(userId);
                    doc.setDocName(docName);
                    tProjectDocMapper.insertSelective(doc);
                }
            }else{
                throw new ApplicationErrorException(ErrorCode.FolerIsNotExist);
            }
        }else{
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }
    }

    /**
     * 点击编辑文档按钮，拿到文档内容信息，以及文档最后的修改时间
     * @param docId
     * @return
     */
    @Override
    public Map<String,Object> editorProjectDoc(int docId)throws ApplicationErrorException {
        Map<String,Object> resultMap=new HashMap<>();
        TProjectDoc tProjectDoc=tProjectDocMapper.selectByPrimaryKey(docId);
        if(tProjectDoc!=null){
            //查询该文档下所有的附件
            List<TProjectDocAnnex> docAnnexList= tProjectDocAnnexMapper.findByDocId(docId);
            List<TProjectDocAnnex> annexList=new ArrayList<>();
            //查询文件是否真的存在
            if(docAnnexList!=null && docAnnexList.size()>0){
                for(TProjectDocAnnex annex:docAnnexList){
                    File file=new File(projectDocFileUrl+docId+"/"+annex.getFileName());
                    if(file.exists()){
                        annexList.add(annex);
                    }else{
                        tProjectDocAnnexMapper.deleteByPrimaryKey(annex.getId());
                    }
                }
            }
            resultMap.put("projectDoc",tProjectDoc);
            resultMap.put("docAnnexList",annexList);
            return resultMap;
        }else{
            throw new ApplicationErrorException(ErrorCode.DocIsNotExist);
        }
    }

    /**项目文档 编辑文档 保存
     *
     * @param reqAddProjectDoc
     * @throws ApplicationErrorException
     */
    @Override
    public void saveEditorProjectDoc(ReqAddProjectDoc reqAddProjectDoc)throws ApplicationErrorException{
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",reqAddProjectDoc.getProjectId());
        userMap.put("userId",reqAddProjectDoc.getEditor());
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            TProjectDoc projectDoc=tProjectDocMapper.selectByPrimaryKey(reqAddProjectDoc.getId());
            if(projectDoc!=null){
                if(!projectDoc.getDocName().equals(reqAddProjectDoc.getDocName())){
                    //查询文档名称 是否重复
                    Map<String,Object> map=new HashMap<>();
                    map.put("folderId",projectDoc.getFolderId());
                    map.put("docName",reqAddProjectDoc.getDocName());
                    TProjectDoc doc=tProjectDocMapper.findByFolderIdAndDocName(map);
                    if(doc!=null){
                        throw new ApplicationErrorException(ErrorCode.DocIsExist);
                    }
                }
                //判断该项目的最后保存时间是否和页面上的时间一致
                if(reqAddProjectDoc.getUpdateTime().getTime()==projectDoc.getUpdateTime().getTime()){
                    projectDoc.setContent(reqAddProjectDoc.getContent());
                    projectDoc.setEditor(reqAddProjectDoc.getEditor());
                    projectDoc.setUpdateTime(new Date());
                    projectDoc.setDocName(reqAddProjectDoc.getDocName());
                    tProjectDocMapper.updateByPrimaryKeySelective(projectDoc);
                }else{
                    throw new ApplicationErrorException(ErrorCode.DocSaveError);
                }
            }else{
                throw new ApplicationErrorException(ErrorCode.DocIsNotExist);
            }
        }else{
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }
    }


    /**
     * 项目文档 编辑文档 上传附件
     * 上传附件 文档的修改时间及编辑人不变
     */
    @Override
    public void uploadProjectDoc(int projectId,int docId,int userId,MultipartFile file) throws IOException,ApplicationErrorException {
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
       if(user!=null){
           //根据docId,查询文件夹名称
           TProjectDoc projectDoc=tProjectDocMapper.selectByPrimaryKey(docId);
           TProjectDocFolder projectDocFolder=tProjectDocFolderMapper.selectByPrimaryKey(projectDoc.getFolderId());
           if(projectDocFolder!=null){
               /*String folderPath="/"+docId;*/
               String folderPath=projectDocFileUrl+docId;
               File folderPathFile=new File(folderPath);
               if(!folderPathFile.exists()) {
                   boolean flag = folderPathFile.mkdirs();
                   if (flag) {
                       folderPathFile.setExecutable(true);
                       folderPathFile.setWritable(true);
                       folderPathFile.setReadable(true);
                   }
               }
                       String filePath=folderPath+"/"+file.getOriginalFilename();
                       File projectFile=new File(filePath);
                       if(!projectFile.exists()){
                           String fileName=fileServiceImp.saveFile(file,folderPath,2);
                           if(fileName!=null && !fileName.equals("")){
                               //数据库插入数据
                               TProjectDocAnnex docAnnex=new TProjectDocAnnex();
                               docAnnex.setCreateTime(new Date());
                               docAnnex.setDocId(docId);
                               docAnnex.setFileName(file.getOriginalFilename());
                               tProjectDocAnnexMapper.insert(docAnnex);
                           }else{
                               throw new ApplicationErrorException(ErrorCode.UPLOADFILE);
                           }
                       }else{
                           throw new ApplicationErrorException(ErrorCode.FileIsExist);
                       }
           }
       }else{
           throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
       }

    }

    /**
     * 点击编辑按钮事件
     * 根据文档编号，查询文档下面所有附件
     * @param docId
     * @return
     */
    @Override
    public Map<String,Object> findDocInfo(int docId) {
        TProjectDoc projectDoc=tProjectDocMapper.selectByPrimaryKey(docId);
        List<TProjectDocAnnex> docAnnexList= tProjectDocAnnexMapper.findByDocId(docId);
        Map<String,Object> map=new HashMap<>();
        map.put("projectDoc",projectDoc);
        map.put("docAnnexList",docAnnexList);
        return map;
    }

    /**
     * 删除文档
     * @param docId
     * @throws IOException
     * @throws ApplicationErrorException
     */
    @Override
    public void deleteProjectDoc(int projectId,int docId,int userId) throws IOException, ApplicationErrorException {
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            TProjectDoc projectDoc=tProjectDocMapper.selectByPrimaryKey(docId);
            if(projectDoc!=null){
                //判断该文档是否包含附件
                List<TProjectDocAnnex> docAnnexList= tProjectDocAnnexMapper.findByDocId(docId);
                if(docAnnexList!=null && docAnnexList.size()>0){
                    //删除文档文件夹
                    File file=new File(projectDocFileUrl+docId);
                    if(file.exists() && file.isDirectory()){
                        FileUtils.deleteDirectory(file);
                        //删除附件实体类
                        int a=tProjectDocAnnexMapper.deleteBydocId(docId);
                        if(a>=0){
                            //删除文档信息
                            tProjectDocMapper.deleteByPrimaryKey(docId);
                        }
                    }else{
                        //删除文档信息
                        tProjectDocMapper.deleteByPrimaryKey(docId);
                    }
                }else{
                    //删除文档信息
                    tProjectDocMapper.deleteByPrimaryKey(docId);
                }
            }else{
                throw new ApplicationErrorException(ErrorCode.DocIsNotExist);
            }
        }else{
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }

    }

    /**
     * 切换文件夹名称
     * 查询该文件夹下面的所有文档
     * folderId 0:查询所有文档  >0:根据文件ID 查询其文档
     * @param folderId
     */
    @Override
    public  Map<String,Object> findByFolderId(Integer projectId,Integer folderId,Integer page, Integer offset, String keywordStr) {
        Map<String,Object> resultMap=new HashMap<>();
        ResProjectDocList resProjectDocList=new ResProjectDocList();
        List<ResProjectDocList.DocInfo> docInfoList=new ArrayList<>();
        List<Map> mapList=new ArrayList<>();
        Integer start = null;
        if(offset == null ){
            offset = 0;
        }
        if(page != null && page>=1){
            start = (page-1)*offset;
        } else {
            start = 0;
        }
        Map<String,Object> findMap=new HashMap<>();
        keywordStr=keywordStr.replace("%","\\%").replace("_","\\_");
        findMap.put("start",start);
        findMap.put("offset",offset);
        findMap.put("projectId",projectId);
        findMap.put("folderId",folderId);
        findMap.put("keywordStr",keywordStr);
        int count=0;
        if(folderId!=null && folderId>0){
           mapList=tProjectDocMapper.findByfolderMap(findMap);
           count=tProjectDocMapper.findCountByMap(findMap);
        }else{
            //查询所有文档
            mapList=tProjectDocMapper.findByfolderMap(findMap);
            count=tProjectDocMapper.findCountByMap(findMap);
        }
        if(mapList!=null && mapList.size()>0){
            for(Map map:mapList){
                ResProjectDocList.DocInfo docInfo=new ResProjectDocList.DocInfo();
                docInfo.setContent((String) map.get("content"));
                docInfo.setCreateTime((Date) map.get("create_time"));
                docInfo.setUpdateTime((Date) map.get("update_time"));
                docInfo.setId(Integer.parseInt(map.get("id").toString()));
                docInfo.setFolderId(Integer.parseInt(map.get("folder_id").toString()));
                docInfo.setDocName((String) map.get("doc_name"));
                docInfo.setCreator(Integer.parseInt(map.get("creator").toString()));
                docInfo.setEditor(Integer.parseInt(map.get("editor").toString()));
                docInfo.setEditorName((String)map.get("editorName"));
                docInfo.setCreatorName((String)map.get("creatorName"));
                docInfoList.add(docInfo);
            }
        }
        resProjectDocList.setDocInfoList(docInfoList);
        resultMap.put("count",count);
        resultMap.put("docList",resProjectDocList.getDocInfoList());
      return resultMap;
    }

    /**
     * 上传图片
     * @param projectId
     * @param docId
     * @param userId
     * @param file
     * @throws IOException
     * @throws ApplicationErrorException
     */
    @Override
    public String uploadDocImage(int projectId, int docId, int userId, MultipartFile file) throws IOException, ApplicationErrorException {
        String imageUrl="";
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            //根据docId,
            TProjectDoc projectDoc=tProjectDocMapper.selectByPrimaryKey(docId);
            if(projectDoc!=null){
                String folderPath=projectDocFileUrl+docId+"/image/";
                File folderPathFile=new File(folderPath);
                if(!folderPathFile.exists()) {
                    boolean flag = folderPathFile.mkdirs();
                    if (flag) {
                        folderPathFile.setExecutable(true);
                        folderPathFile.setWritable(true);
                        folderPathFile.setReadable(true);
                    }
                }
                //上传图片
                String uploadFilePath = file.getOriginalFilename();
                if (!uploadFilePath.endsWith(".jpg") &&
                        !uploadFilePath.endsWith(".jpeg") &&
                        !uploadFilePath.endsWith(".bmp") &&
                        !uploadFilePath.endsWith(".png")) {
                    throw new ApplicationErrorException(ErrorCode.FileIsNotImage);
                }
                fileServiceImp.saveFileByNameWithId(file, folderPath, docId);
                imageUrl="/doc/previewDocImage?docId="+docId;
            }
        }else{
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }
        return imageUrl;
    }

    /**
     * 项目上传图片
     * @param projectId
     * @param userId
     * @param file
     * @return
     * @throws IOException
     * @throws ApplicationErrorException
     */
    public String uploadProjectImage(int projectId, int userId, MultipartFile file) throws IOException, ApplicationErrorException {
        String imageUrl="";
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            //根据docId,
            String folderPath=projectFileUrl+"/"+projectId+"/image";
            File folderPathFile=new File(folderPath);
            if(!folderPathFile.exists()) {
                boolean flag = folderPathFile.mkdirs();
                if (flag) {
                    folderPathFile.setExecutable(true);
                    folderPathFile.setWritable(true);
                    folderPathFile.setReadable(true);
                }
            }
            //上传图片
            String uploadFilePath = file.getOriginalFilename();
            if (!uploadFilePath.endsWith(".jpg") &&
                    !uploadFilePath.endsWith(".jpeg") &&
                    !uploadFilePath.endsWith(".bmp") &&
                    !uploadFilePath.endsWith(".png")) {
                throw new ApplicationErrorException(ErrorCode.FileIsNotImage);
            }
            fileServiceImp.saveFileByNameWithId(file, folderPath, projectId);
            imageUrl="/doc/previewProjectImage?projectId="+projectId;
        }else{
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }
        return imageUrl;
    }

    @Override
    public void checkDocIsExists(int docId)throws  ApplicationErrorException{
        TProjectDoc doc=tProjectDocMapper.selectByPrimaryKey(docId);
        if(doc!=null){
            int folderId=doc.getFolderId();
            TProjectDocFolder folder=tProjectDocFolderMapper.selectByPrimaryKey(folderId);
            if(folder==null){
                throw new ApplicationErrorException(ErrorCode.FolerIsNotExist);
            }
        }else{
            throw new ApplicationErrorException(ErrorCode.DocIsNotExist);
        }
    }
}
