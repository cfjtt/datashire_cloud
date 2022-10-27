package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.TProjectDocAnnex;
import com.eurlanda.edu.tp.error.ApplicationErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ProjectDocAnnexService {

    //查询文档下所有附件
    List<TProjectDocAnnex> findByDocid(int docId);

    //删除附件
    String deleteDocAnnex(int projectId,int docId,int userId,String docAnnexName)throws ApplicationErrorException;

    //下载附件
    void downloadDocAnnex(int docId,String docAnnexName,HttpServletResponse response)throws IOException,ApplicationErrorException;

    //预览附件
    String previewDocAnnex(int docId,String docAnnexName,HttpServletResponse response)throws IOException,ApplicationErrorException;

}
