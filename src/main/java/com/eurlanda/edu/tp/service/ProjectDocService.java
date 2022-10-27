package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqAddProjectDoc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ProjectDocService {
    public void createProjectDoc(int projectId,Integer folderId,String docName,int userId)throws ApplicationErrorException;
    public void saveEditorProjectDoc(ReqAddProjectDoc reqAddProjectDoc)throws ApplicationErrorException;
    public Map<String,Object> editorProjectDoc(int docId)throws ApplicationErrorException;
    public void uploadProjectDoc(int projectId,int docId,int userId,MultipartFile file) throws IOException,ApplicationErrorException;

    public Map<String,Object> findDocInfo(int docId);

    void deleteProjectDoc(int projectId,int docId,int userId)throws IOException,ApplicationErrorException;

     Map<String,Object> findByFolderId(Integer projectId,Integer folderId,Integer page,Integer offset,String keyWordStr);

    String uploadDocImage(int projectId,int docId,int userId,MultipartFile file) throws IOException,ApplicationErrorException;

    String uploadProjectImage(int projectId, int userId, MultipartFile file) throws IOException, ApplicationErrorException;

    void checkDocIsExists(int doc)throws ApplicationErrorException;
}
