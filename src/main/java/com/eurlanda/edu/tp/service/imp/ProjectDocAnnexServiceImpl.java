package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.TProjectDocAnnex;
import com.eurlanda.edu.tp.dao.entity.TProjectUser;
import com.eurlanda.edu.tp.dao.mapper.TProjectDocAnnexMapper;
import com.eurlanda.edu.tp.dao.mapper.TProjectUserMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.ProjectDocAnnexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ProjectDocAnnexServiceImpl implements ProjectDocAnnexService {

    @Autowired
    private TProjectDocAnnexMapper tProjectDocAnnexMapper;
    @Autowired
    private FileServiceImp fileServiceImp;
    @Autowired
    private TProjectUserMapper tProjectUserMapper;
    @Value("${projectDocFileUrl}")
    private String projectDocFileUrl;


    @Override
    public List<TProjectDocAnnex> findByDocid(int docId) {
        List<TProjectDocAnnex> projectDocAnnexeList=tProjectDocAnnexMapper.findByDocId(docId);
        return projectDocAnnexeList;
    }

    @Override
    public String deleteDocAnnex(int projectId,int docId,int userId,String docAnnexName)throws ApplicationErrorException {
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("projectId",projectId);
        userMap.put("userId",userId);
        TProjectUser user=tProjectUserMapper.selectByUserIdAndProjectId(userMap);
        if(user!=null){
            String filePath=projectDocFileUrl+docId+"/"+docAnnexName;
            File file=new File(filePath);
            if(file.exists()){
                boolean flag=file.delete();
                if(flag){
                    //删除数据库中信息
                    Map<String,Object> map=new HashMap<>();
                    map.put("docId",docId);
                    map.put("fileName",docAnnexName);
                    tProjectDocAnnexMapper.deleteBydocIdAndfileName(map);
                }
            }else{
                throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
            }
            return "1";
        }else{
            throw new ApplicationErrorException(ErrorCode.WITHOUTAUTHORITY);//不是该项目组成员
        }
    }

    /**
     * 下载
     * @param docId
     * @param docAnnexName
     * @param response
     * @throws ApplicationErrorException
     */
    @Override
    public void downloadDocAnnex(int docId, String docAnnexName,HttpServletResponse response) throws IOException,ApplicationErrorException {
        try{
            String filePath=projectDocFileUrl+docId;
            String fileName = URLDecoder.decode(docAnnexName, "UTF-8");
            fileServiceImp.getFile(fileName,response,filePath);
        }catch (Exception e){

        }
    }

    @Override
    public String previewDocAnnex(int docId,String docAnnexName,HttpServletResponse response)throws IOException,ApplicationErrorException {
        StringBuilder fileContent=new StringBuilder();
        String filePath=projectDocFileUrl+docId+"/"+docAnnexName;
        File file=new File(filePath);
        if(file.exists()){
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                int i = bis.read(buff);
                while (i != -1) {
                    fileContent.append(buff);
                    i = bis.read(buff);
                }
                os.flush();
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }

        }else{
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
        return fileContent.toString();
    }
}
