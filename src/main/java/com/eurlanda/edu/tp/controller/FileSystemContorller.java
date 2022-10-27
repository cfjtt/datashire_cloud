package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.FileService;
import com.eurlanda.edu.tp.webdomain.response.ResUploadImage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class FileSystemContorller {

    @Autowired
    private FileService fileService;
    @Value("${file.baseDir}")
    private String baseDir;

    @Value("${file.imageDir}")
    private String imageDir;

    @ApiOperation(value = "上传Image")
    @PostMapping(value = "/admin/course/experiment/piclib")
    public ResponseMessage<ResUploadImage> uploadImage(@RequestParam("file") MultipartFile file,String type,Integer courseId) throws
            ApplicationErrorException, IOException {
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("file",file);
        paraMap.put("type",type);
        paraMap.put("courseId",courseId);
        return new ResponseMessage.Success<>(fileService.uploadImage(paraMap));
    }

    @ApiOperation(value = "上传Markdown")
    @PostMapping(value = "/admin/course/experiment/markdown")
    public ResponseMessage<String> uploadMarkdown(@RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        return new ResponseMessage.Success<>(fileService.uploadMarkdown(file));
    }

    @ApiOperation(value = "上传Markdown,读取markdown，返回信息")
    @PostMapping(value = "/admin/course/experiment/markdownWithResolve")
    public ResponseMessage<String> uploadMarkdownWithResolve(@RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        return new ResponseMessage.Success<>(fileService.uploadMarkdownWithResolve(file));
    }

    @ApiOperation(value = "上传Report")
    @PostMapping(value = "/admin/course/experiment/report")
    public ResponseMessage<String> uploadReport(@RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        return new ResponseMessage.Success<>(fileService.uploadReport(file));
    }

    @ApiOperation(value = "下载Image")
    @GetMapping(value = "/image/{fileName:.+}")
    public void downloadImage(@PathVariable("fileName") String fileName, HttpServletResponse response) throws ApplicationErrorException, IOException {
        fileService.downloadImage(fileName, response);
    }

    @ApiOperation(value = "预览image")
    @GetMapping(value = "/previewImage")
    public void previewImage(@RequestParam("path") String path, HttpServletResponse response) throws ApplicationErrorException, IOException {
        if(path.startsWith(baseDir + imageDir)){
            fileService.previewImage(path, response);
        }else {
            fileService.previewImage(baseDir + imageDir + "/" + path,response);
        }
    }

    @ApiOperation(value = "下载Markdown")
    @GetMapping(value = "/markdown/{fileName:.+}")
    public void downloadMarkdown(@PathVariable("fileName") String fileName, HttpServletResponse response) throws ApplicationErrorException {
        fileService.downloadMarkdown(fileName, response);
    }

    @ApiOperation(value = "下载Report")
    @GetMapping(value = "/report/{path}/{fileName:.+}")
    public void downloadReport(@PathVariable("path") String path, @PathVariable("fileName") String fileName, HttpServletResponse response) throws ApplicationErrorException {
        fileService.downloadReport(path, fileName, response);
    }

    @ApiOperation(value="获取共享文件夹中信息")
    @PostMapping(value="/share/getShareFileList")
    public ResponseMessage<List<Map<String, Object>>>  getShareFileList(@RequestParam("userId") String userId, @RequestParam("path")String path,@RequestParam("type") int type)throws ApplicationErrorException,IOException{
        return new ResponseMessage.Success<>(fileService.getShareList(userId,path,type));
    }
    @ApiOperation(value="上传到共享文件夹")
    @PostMapping(value="/share/uploadShareFile")
    public ResponseMessage<String>  uploadShareFile(@RequestParam("userId") String userId,@RequestParam("path")String path,@RequestParam("file") MultipartFile file,@RequestParam("isCheckExists") String isCheckExists,@RequestParam("type") int type)throws ApplicationErrorException,IOException{
        return  new ResponseMessage.Success<>(fileService.shareUploadFile(userId,path,file,isCheckExists,type));
    }

    @ApiOperation(value="删除共享文件夹中文件")
    @PostMapping(value="/share/deleteShareFile")
    public ResponseMessage<String>  deleteShareFile(@RequestParam("userId") String userId,@RequestParam("fileName") String  fileName,@RequestParam("path")String path,@RequestParam("type") int type)throws ApplicationErrorException,IOException{
        return  new ResponseMessage.Success<>(fileService.deleteShareFile(userId,fileName,path,type));
    }
    @ApiOperation(value="下载共享文件夹中文件")
    @GetMapping(value="/share/downloadShareFile")
    public void   downloadShareFile(@RequestParam("userId") String userId,@RequestParam("fileName") String  fileName,@RequestParam("path")String path,HttpServletResponse response,@RequestParam("type") int type)throws ApplicationErrorException,IOException{
        fileService.downloadShareFile(userId,fileName,path,response,type);
    }
    @ApiOperation(value="创建共享文件夹")
    @PostMapping(value="/share/createShareDirectory")
    public ResponseMessage<String>   createShareDirectory(@RequestParam("userId") String userId,@RequestParam("directoryName") String  directoryName,@RequestParam("path")String path,@RequestParam("type") int type)throws ApplicationErrorException,IOException{
       return new ResponseMessage.Success<>(fileService.createShareDirectory(userId,directoryName,path,type));
    }

    @ApiOperation(value="重新命名共享文件夹")
    @PostMapping(value="/share/renameShareFile")
    public ResponseMessage<String>   renameShareFile(@RequestParam("userId") String userId,@RequestParam("oldFileName") String  oldFileName,@RequestParam("newFileName") String newFileName,@RequestParam("path") String path,@RequestParam("type") int type)throws ApplicationErrorException,IOException{
        return new ResponseMessage.Success<>(fileService.renameShareFile(userId,oldFileName,newFileName,path,type));
    }

    @ApiOperation(value="移动共享文件夹")
    @PostMapping(value="/share/moveShareFile")
    public ResponseMessage<String>   moveShareFile(@RequestParam("userId") String userId,@RequestParam("targetPath") String  targetPath,@RequestParam("fileNameList") List<String> fileNameList,@RequestParam("isCheckExists") String isCheckExists,@RequestParam("type") int type)throws ApplicationErrorException,IOException{
        return new ResponseMessage.Success<>(fileService.moveShareFile(userId,targetPath,fileNameList,isCheckExists,type));
    }

    @ApiOperation(value="查询共享文件夹是否存在")
    @PostMapping(value="/share/checkIsExistsShare")
    public ResponseMessage<String>   checkIsExistsShare(@RequestParam("userId") String userId,@RequestParam("path") String path,@RequestParam("type") int type)throws ApplicationErrorException,IOException{
        return new ResponseMessage.Success<>(fileService.findFileIsExists(userId,path,type));
    }

    @ApiOperation(value="获取实验答案信息")
    @PostMapping(value="/admin/experiment/getAnswerFile")
    public ResponseMessage<List<Map<String, Object>>> getExperimentAnswerFile(@RequestParam("courseId") Integer courseId,@RequestParam("experimentId") Integer experimentId,@RequestParam("type") int type,@RequestParam("courseName") String courseName)throws ApplicationErrorException,IOException{
        return new ResponseMessage.Success<>(fileService.getExperimentAnswerFile(courseId,experimentId,type,courseName));
    }
    @ApiOperation(value="提交实验答案信息")
    @PostMapping(value="/admin/experiment/submitAnswer")
    public ResponseMessage submitExperimentAnswerFile(@RequestParam("courseId") Integer courseId,@RequestParam("experimentId") Integer experimentId,@RequestParam("type") int type,@RequestParam("tableName") String tableName,@RequestParam("courseName") String courseName,MultipartFile file)throws ApplicationErrorException,IOException{
        try{
            String a=fileService.submitExperimentAnswerFile(courseId,experimentId,type,tableName,courseName,file);
        }catch (ApplicationErrorException e){
            if(e.getErrorCode() == ErrorCode.DBTABLENOTEXISTS.getCode()){
                return new ResponseMessage.Fail(ErrorCode.DBTABLENOTEXISTS.getCode(),e.getMessage());
            }else if(e.getErrorCode() == ErrorCode.UPLOADFILE.getCode()){
                return new ResponseMessage.Fail(ErrorCode.UPLOADFILE.getCode(),e.getMessage());
            }{
                throw e;
            }
        }
        return new ResponseMessage.Success<>();
    }
    @PostMapping(value="/admin/experiment/deleteAnswer")
    public ResponseMessage<String> deleteExperimentAnswer(@RequestParam("courseId") Integer courseId,@RequestParam("experimentId") Integer experimentId,@RequestParam("type") int type,@RequestParam("tableName") String tableName)throws ApplicationErrorException,IOException{
        return new ResponseMessage.Success<>(fileService.deleteExperimentAnswer(courseId,experimentId,type,tableName));
    }

}
