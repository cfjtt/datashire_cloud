package com.eurlanda.edu.tp.service;


import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.response.ResUploadImage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService {

    ResUploadImage uploadImage(Map<String,Object> paraMap) throws ApplicationErrorException, IOException;

    String uploadMarkdown(MultipartFile file) throws ApplicationErrorException, IOException;

    String uploadReport(MultipartFile file) throws ApplicationErrorException, IOException;

    void downloadImage(String fileName, HttpServletResponse response) throws ApplicationErrorException, IOException;

    void downloadMarkdown(String fileName, HttpServletResponse response) throws ApplicationErrorException;

    void downloadReport(String path, String fileName, HttpServletResponse response) throws ApplicationErrorException;
    String uploadMarkdownWithResolve(MultipartFile file) throws ApplicationErrorException, IOException;

    //图片预览
    void previewImage(String path,HttpServletResponse response);

    //共享文件夹中上传文件
    String shareUploadFile(String userId,String path,MultipartFile file,String isCheckExists,int type)throws ApplicationErrorException, IOException;
    //删除共享文件夹中文件
    String deleteShareFile(String userId,String fileName,String path,int type)throws ApplicationErrorException,IOException;
    //下载共享文件夹中文件
    void downloadShareFile(String userId,String fileName,String path,HttpServletResponse response,int type)throws ApplicationErrorException,IOException;
    //共享文件夹重命名
    String renameShareFile(String userId,String oldfileName,String newFileName,String path,int type)throws ApplicationErrorException,IOException;
    //获取共享文件中所有信息
    List<Map<String, Object>> getShareList(String userId, String path,int type)throws ApplicationErrorException,IOException;
    //创建文件夹
    String createShareDirectory(String userId,String directoryPath,String path,int type);
    //移动共享文件夹
    String moveShareFile(String userId,String targetPath,List<String> fileNameList,String isCheckExists,int type)throws ApplicationErrorException,IOException;
     String findFileIsExists(String userId,String path,int type);

    //获取共享文件中所有信息
    List<Map<String, Object>> getExperimentAnswerFile(int courseId,int experimentId,int type,String courseName)throws ApplicationErrorException,IOException;

    String submitExperimentAnswerFile(int courseId,int experimentId,int type,String tableName,String courseName,MultipartFile file)throws ApplicationErrorException,IOException;

    String deleteExperimentAnswer(int courseId,int experimentId,int type,String tableName)throws ApplicationErrorException,IOException;
    //获取共享文件中所有信息
    List<Map<String, Object>> getJobShareList(Integer userId, String path,int type)throws ApplicationErrorException,IOException;
}
