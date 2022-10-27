package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.dao.entity.TProjectDoc;
import com.eurlanda.edu.tp.dao.entity.TProjectDocAnnex;
import com.eurlanda.edu.tp.dao.entity.TProjectUser;
import com.eurlanda.edu.tp.dao.mapper.TProjectDocAnnexMapper;
import com.eurlanda.edu.tp.dao.mapper.TProjectUserMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.FileService;
import com.eurlanda.edu.tp.service.ProjectDocAnnexService;
import com.eurlanda.edu.tp.service.ProjectDocService;
import com.eurlanda.edu.tp.webdomain.request.ReqAddProjectDoc;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("doc")
public class TProjectDocController {
    @Autowired
    private ProjectDocService projectDocService;
    @Autowired
    private ProjectDocAnnexService projectDocAnnexService;
    @Autowired
    private TProjectDocAnnexMapper tProjectDocAnnexMapper;
    @Autowired
    private TProjectUserMapper tProjectUserMapper;
    @Value("${projectDocFileUrl}")
    private String projectDocFileUrl;
    @Value("${projectFileUrl}")
    private String projectFileUrl;
    @Autowired
    private FileService fileService;
    /**
     * 创建文档
     */
    @ApiOperation(value = "创建文档")
    @PostMapping("addProjectDoc")
    public ResponseMessage addProjectDoc(Integer projectId, Integer folderId,String docName,Integer userId)throws Exception{
        projectDocService.createProjectDoc(projectId,folderId,docName,userId);
        return new ResponseMessage.Success();
    }

    /**
     *点击 编辑文档按钮 拿取数据
     */
    @ApiOperation(value = "点击编辑文档按钮")
    @PostMapping("updateProjectDoc")
    public ResponseMessage<Map<String,Object>> editorProjectDoc(Integer docId)throws Exception{
        Map<String,Object> docMap=projectDocService.editorProjectDoc(docId);
        return new ResponseMessage.Success(docMap);
    }

    /**
     * 保存编辑文档
     */
    @ApiOperation(value = "保存编辑文档")
    @PostMapping("saveEditorProjectDoc")
    public ResponseMessage saveEditorProjectDoc(@RequestBody ReqAddProjectDoc reqAddProjectDoc)throws Exception{
        projectDocService.saveEditorProjectDoc(reqAddProjectDoc);
        return new ResponseMessage.Success();
    }


    /**
     * 删除文档
     */
    @ApiOperation(value = "删除文档")
    @PostMapping("deleteProjectDoc")
    public ResponseMessage deleteProjectDoc(Integer projectId,Integer docId,Integer userId)throws Exception{
        projectDocService.deleteProjectDoc(projectId,docId,userId);
        return new ResponseMessage.Success();
    }

    /**
     * 上传附件
     *
     */
    @ApiOperation(value = "上传附件")
    @PostMapping("uploadDocFile")
    public ResponseMessage uploadDocFile(Integer projectId, Integer docId, Integer userId, MultipartFile file)throws Exception{
        projectDocService.uploadProjectDoc(projectId,docId,userId,file);
        return new ResponseMessage.Success();
    }
    /**
     * 查询文档
     */
    @ApiOperation(value = "查询文档")
    @PostMapping("findDoc")
    public ResponseMessage<Map<String,Object>> findByFolderId(Integer projectId,Integer folderId, Integer page, Integer offset, String keywordStr)throws ApplicationErrorException,IOException{
        Map<String,Object> map=projectDocService.findByFolderId(projectId,folderId,page,offset,keywordStr);
        return new ResponseMessage.Success(map);
    }

    /**
     * 删除附件
     */
    @ApiOperation(value = "删除附件")
    @PostMapping("deleteDocFile")
    public ResponseMessage deleteDocFile(Integer projectId, Integer docId, Integer userId, String docAnnexName)throws ApplicationErrorException,IOException{
        projectDocAnnexService.deleteDocAnnex(projectId,docId,userId,docAnnexName);
        return new ResponseMessage.Success();
    }

    /**
     * 下载附加
     */
    @ApiOperation(value = "下载附加")
    @GetMapping("downloadDocFile")
    public ResponseMessage downloadDocFile(int docId,String docAnnexName,HttpServletResponse response)throws ApplicationErrorException,IOException{
        projectDocAnnexService.downloadDocAnnex(docId,docAnnexName,response);
        return new ResponseMessage.Success();
    }

    /**
     * 预览附加
     */
    @ApiOperation(value = "预览附加")
    @GetMapping("previewDocAnnex")
    public ResponseMessage<String> previewDocAnnex(int docId,String docAnnexName,HttpServletResponse response)throws ApplicationErrorException,IOException{
        String info=projectDocAnnexService.previewDocAnnex(docId,docAnnexName,response);
        return new ResponseMessage.Success(info);
    }
    /**
     * 查询附加 根据文档编号
     */
    @ApiOperation(value = "查询附加")
    @PostMapping("findDocAnnexBydocId")
    public ResponseMessage<List<TProjectDocAnnex>> findDocAnnexBydocId(int docId)throws ApplicationErrorException{
        List<TProjectDocAnnex> projectDocAnnexList=projectDocAnnexService.findByDocid(docId);
        List<TProjectDocAnnex> annexList=new ArrayList<>();
        //查询文件是否真的存在
        if(projectDocAnnexList!=null && projectDocAnnexList.size()>0){
            for(TProjectDocAnnex annex:projectDocAnnexList){
                File file=new File(projectDocFileUrl+docId+"/"+annex.getFileName());
                if(file.exists()){
                    annexList.add(annex);
                }else{
                    tProjectDocAnnexMapper.deleteByPrimaryKey(annex.getId());
                }
            }
        }
        return new ResponseMessage.Success(annexList);
    }



    /**
     * 是否有项目权限
     */
    @ApiOperation(value = "是否有项目权限")
    @PostMapping("isProjectUser")
    public ResponseMessage<Integer> isProjectUser(Integer projectId,Integer userId)throws ApplicationErrorException,IOException{
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            return new ResponseMessage.Success(1);
        }else{
            return new ResponseMessage.Success(0);
        }

    }



    /**
     * 上传图片
     *
     */
    @ApiOperation(value = "上传图片")
    @PostMapping("uploadDocImage")
    public ResponseMessage<String> uploadDocImage(Integer projectId, Integer docId, Integer userId, MultipartFile file)throws Exception{
        String imageUrl=projectDocService.uploadDocImage(projectId,docId,userId,file);
        return new ResponseMessage.Success(imageUrl);
    }

    /**
     * 上传图片
     *
     */
    @ApiOperation(value = "上传图片")
    @PostMapping("uploadProjectImage")
    public ResponseMessage<String> uploadProjectImage(Integer projectId, Integer userId, MultipartFile file)throws Exception{
        String imageUrl=projectDocService.uploadProjectImage(projectId,userId,file);
        return new ResponseMessage.Success(imageUrl);
    }

    @ApiOperation(value = "预览图片")
    @GetMapping("previewProjectImage")
    public void previewImage(Integer projectId,HttpServletResponse response){
        String path = projectFileUrl+"/"+projectId+"/image/"+projectId;
        fileService.previewImage(path,response);
    }

    @ApiOperation(value = "预览图片")
    @GetMapping("previewDocImage")
    public void previewDOCImage(Integer docId,HttpServletResponse response){
        String path = projectDocFileUrl+docId+"/image/"+docId;
        fileService.previewImage(path,response);
    }


    @ApiOperation(value = "查询文档是否存在")
    @PostMapping("checkDocIsExists")
    public ResponseMessage checkDocIsExists(Integer docId)throws ApplicationErrorException{
        projectDocService.checkDocIsExists(docId);
        return  new ResponseMessage.Success();
    }




}
