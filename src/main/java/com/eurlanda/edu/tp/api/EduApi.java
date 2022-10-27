package com.eurlanda.edu.tp.api;

import com.alibaba.fastjson.JSON;
import com.eurlanda.edu.tp.Util.HttpClientUtils;
import com.eurlanda.edu.tp.dao.entity.TProject;
import com.eurlanda.edu.tp.service.TProjectService;
import com.eurlanda.edu.tp.service.TProjectUserService;
import com.eurlanda.edu.tp.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用数猎云相关接口，唯一位置
 */
@Service
public class EduApi {


    static Logger log = Logger.getLogger(EduApi.class.getName());

    @Autowired
    private TProjectUserService tProjectUserService;

    @Value("${shulie.url}")
    private String shulieurl;

    @Autowired
    private UserService userService;

    @Autowired
    private TProjectService projectService;

    @Autowired
    private TProjectService tProjectService;
    /**
     * addUser
     *   key:coursename
     *   key:userList  [{phone,password}]
     * @return
     */
    public  String addUser(Map<String,Object> map){
        String result = null;
        try {
            String url = shulieurl+"/api/addUser";
            if(map.containsKey("userList")){
                map.put("userList", JSON.toJSONString(map.get("userList")));
            }
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除用户
     * @param map
     * key:userList  [phone]
     * @return
     */
    public  String delUser(Map<String,Object> map){
        String result = null;
        String url = shulieurl+"/api/delUser";
        try {
            if(map.containsKey("userList")){
                map.put("userList", JSON.toJSONString(map.get("userList")));
            }
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 修改密码
     * @param map
     * key:userList [phone,password]
     * @return
     */
    public String updatePassword(Map<String,Object> map){
        String result = null;
        String url = shulieurl+"/api/updateUser";
        try {
            if(map.containsKey("userList")){
                map.put("userList", JSON.toJSONString(map.get("userList")));
            }
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 课程初始化(只初始化课程，用户添加调用addUser)
     * @param map
     * @return
     */
    public String initCourse(Map<String,Object> map){
        String result = null;
        String url = shulieurl+"/api/initCourse";
        try {
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取数据库列表信息
     * @param phone
     * @return
     */
    public Map<String,Object> getDataShireDBList(String phone,boolean isAdmin,String courseName,int type,String paraSpaceName){
        String url = shulieurl+"/api/dataShireCloudDBManager";
        Map<String,Object> map = new HashMap<>();
        map.put("phone",phone);
        map.put("isAdmin",isAdmin);
        map.put("courseName",courseName);
        map.put("type",type);
        if(paraSpaceName != null){
            if(paraSpaceName.startsWith("项目[")){
                map.put("paraSpaceName",paraSpaceName);
            }else {
                map.put("paraSpaceName","项目[" + paraSpaceName + "]");
            }
        }
        String result = null;
        Map<String,Object> returnMap = new HashMap<>();
        try {
            result = HttpClientUtils.doPost(url,map);
            returnMap = (Map<String, Object>) JSON.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    /**
     * 查询表是否存在
     *type 1:课程对应的cloudDB   2：用户自己的CloudDB
     */
    public Map<String,Object> checkCloudTableIsExists(String phone,String courseName,String tableName,int type){
        String url = shulieurl+"/api/checkTableIsExist";
        Map<String,Object> map = new HashMap<>();
        if(type==2){
            map.put("phone",phone);
        }
        map.put("courseName",courseName);
        map.put("tableName",tableName);
        map.put("type",type);
        String result = null;
        Map<String,Object> returnMap = new HashMap<>();
        try {
            result = HttpClientUtils.doPost(url,map);
            returnMap = (Map<String, Object>) JSON.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    public Map<String,Object> getProjectFileList(Integer projectId,String path){
        String url = shulieurl+"/api/projectGetHdfsFile";
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> maps = tProjectUserService.getUserManager(projectId);
        String phone = null;
        if(maps != null && maps.size() > 0){
            phone = maps.get(0).get("no") + "";
        }
        TProject project = tProjectService.selectByPrimaryKey(projectId);
        String spaceName = null;
        if(project != null){
            spaceName = project.getName();
        }
        map.put("phone",phone);
        map.put("path",path);
        map.put("spaceName", spaceName);
        String result = null;
        Map<String,Object> returnMap = new HashMap<>();
        try {
            result = HttpClientUtils.doPost(url,map);
            returnMap = (Map<String, Object>) JSON.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    /**
     * 获取文件列表信息
     * @param phone
     * @return
     */
    public Map<String,Object>  getDataShireFileList(String phone,String path,boolean isAdmin,String courseName,int type){
        return getDataShireFileList(phone,path,isAdmin,courseName,type,null);
    }


    /**
     * 获取文件列表信息
     * @param phone
     * @return
     */
    public Map<String,Object>  getDataShireFileList(String phone,String path,boolean isAdmin,String courseName,int type,String paraSpaceName){
        String url = shulieurl+"/api/dataShireCloudFileManager";
        Map<String,Object> map = new HashMap<>();
        map.put("phone",phone);
        map.put("path",path);
        map.put("isAdmin",isAdmin);
        map.put("courseName",courseName);
        map.put("type",type);
        if(paraSpaceName != null){
            if(paraSpaceName.startsWith("项目[")){
                map.put("paraSpaceName",paraSpaceName);
            }else {
                map.put("paraSpaceName","项目[" + paraSpaceName + "]");
            }
        }
        String result = null;
        Map<String,Object> returnMap = new HashMap<>();
        try {
            result = HttpClientUtils.doPost(url,map);
            returnMap = (Map<String, Object>) JSON.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    /**
     * 删除表
     * @param spaceName
     * @param tableName
     * @param phone
     * @return
     */
    public String deleteDbTable(String spaceName,String tableName,String phone,String courseName,boolean isAdmin,int type){
        String url = shulieurl+"/api/deleteCloudDBTable";
        Map<String,Object> map = new HashMap<>();
        map.put("spaceName",spaceName);
        map.put("tableName",tableName);
        map.put("phone",phone);
        map.put("courseName",courseName);
        map.put("isAdmin",isAdmin);
        map.put("type",type);
        String result = null;
        try {
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除文件
     * @param spaceName
     * @param fileName
     * @param path
     * @param phone
     * @return
     */
    public String deleteFile(String spaceName,String fileName,String path,String phone,boolean isAdmin,String courseName,int type){
        String url=shulieurl+"/api/deleteCloudHDFSFile";
        Map<String,Object> map = new HashMap<>();
        String result = null;
        try {
        map.put("spaceName", URLEncoder.encode(spaceName,"UTF-8"));
        map.put("fileName",  URLEncoder.encode(fileName,"UTF-8"));
        map.put("path",path);
        map.put("phone",phone);
        map.put("isAdmin",isAdmin);
        map.put("courseName",URLEncoder.encode(courseName,"UTF-8"));
        map.put("type",type);
        result = HttpClientUtils.doPost(url,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 勾选/取消可视化
     * @param spaceName
     * @param tableName
     * @param operate
     * @param phone
     * @return
     */
    public String operateTable(String spaceName,String tableName,boolean operate,String phone){
        String url = shulieurl+"/api/operateBiTable";
        Map<String,Object> map = new HashMap<>();
        map.put("spaceName",spaceName);
        map.put("tableName",tableName);
        map.put("phone",phone);
        if(operate){
            map.put("operate","add");
        } else {
            map.put("operate","remove");
        }
        String result = null;
        try {
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新课程信息
     * @param oldName
     * @param newName
     * @return
     */
    public String updateCourse(String oldName,String newName){
        String url = shulieurl+"/api/updateCourse";
        Map<String,Object> map = new HashMap<>();
        map.put("oldName",oldName);
        map.put("newName",newName);
        String result = null;
        try {
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String getHdfsPathByCourse(String courseName) {
        String url = shulieurl+"/api/getHdfsPathAndProjectInfo";
        Map<String,Object> map = new HashMap<>();
        map.put("courseName",courseName);
        String result = null;
        try {
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String getCloudDbNameByCourse(String courseName) {
        String url = shulieurl+"/api/getTrainDb";
        Map<String,Object> map = new HashMap<>();
        map.put("courseName",courseName);
        String result = null;
        try {
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public String getRepositoryIdByUsername(String username) {
        String url = shulieurl+"/api/getRepositoryId";
        Map<String,Object> map = new HashMap<>();
        map.put("phone",username);
        String result = null;
        try {
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有行业
     * @param key
     * @param isTop
     * @return
     */
    public String getAllIndustry(String key,String isTop){
        String url = shulieurl+"/industry/admin/selectAll";
        Map<String,Object> map = new HashMap<>();
        map.put("key",key);
        map.put("isTop",isTop);
        String result = null;
        try{
            result = HttpClientUtils.doPost(url,map);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取行业总数，数据集总数，数据集总大小，下载次数
     * @return  industryCount,dmCount,dmSize,downCount
     *
     */
    public String getDataIndustryDownloadCount(){
        String url = shulieurl+"/dmdata/admin/getDataIndustryDownloadCount";
        String result = null;
        try {
            result = HttpClientUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询出行业信息
     * @param industryId
     * @param page
     * @param offset
     * @param name
     * @return
     */
    public String getDmData(Integer industryId,Integer page,Integer offset,String name){
        String url = shulieurl+"/dmdata/admin/getDmdataListByDEEP";
        Map<String,Object> paramMap = new HashMap<>();
        if(industryId != null) {
            paramMap.put("industryId", industryId);
        }
        paramMap.put("page",page);
        paramMap.put("offset",offset);
        if(name != null) {
            paramMap.put("name", name);
        }
        String result = null;
        try {
            result = HttpClientUtils.doPost(url,paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除为课程或项目建立的数猎场相关的数据
     * @param name 课程名或项目名
     * @param type 1.为课程创建的数猎场 2.为项目创建的数猎场
     * @return
     */
    public String deleteSpaceRelevantData(String name,int type){
        if(name == null || name.equals("")){
            log.error("deleteSpaceRelevantData所给参数name为空！");
            return "error";
        }
        if(type != 1 && type != 2){
            log.error("deleteSpaceRelevantData所给参数type为未知的操作类型");
            return "error";
        }
        String url = shulieurl + "/api/deleteSpaceRelevantData";
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("name",name);
        paraMap.put("type",type);
        String result = null;
        try {
            result = HttpClientUtils.doPost(url,paraMap);
        } catch (Exception e) {
            log.error("deleteSpaceRelevantData调用数猎云接口错误",e);
            return "error";
        }
        return result;
    }

    /**
     * 科研项目创建数猎场
     * @param name 命名负责:项目[项目名字](防止和普通的数猎场重名) hdfs路径 udata/u+repositoryId  db库 u+repositoryId+db
     * @param userId 用户id，根据用户id查找出老师或者学生的身份，管理员没法创建
     * @return
     *
     */
    public String addResearchDatashireSpace(String name,String phone,String password,Integer role,Integer packageId){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        paramMap.put("role",role);
        paramMap.put("phone",phone);
        paramMap.put("packageId",packageId);
        paramMap.put("password",password);
        String result = null;
        try{
            String url = shulieurl+"/api/addResearchDatashireSpace";
            result = HttpClientUtils.doPost(url,paramMap);
        } catch (Exception e){
            log.error("添加科研项目，调用数猎云接口失败");
            return "error";
        }
        return result;
    }

    public String updateProjecSpaceUser(String projectName, List<Map<String,Object>> saveProjectUserList,List<Map<String,Object>> delProjectUserList,Map userManager){
        projectName = "项目["+projectName+"]";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("projectName",projectName);
        paramMap.put("saveProjectUserListStr",JSON.toJSONString(saveProjectUserList));
        paramMap.put("delProjectUserListStr",JSON.toJSONString(delProjectUserList));
        paramMap.put("userManager",JSON.toJSONString(userManager));
        String result = null;
        try{
            String url = shulieurl+"/api/updateProjectSpaceUser";
            result = HttpClientUtils.doPost(url,paramMap);
        } catch (Exception e){
            log.error("科研项目，同步数猎云失败");
            return "error";
        }
        return result;

    }

    public String updateProjectName(String oldName,String newName){
        oldName = "项目["+oldName+"]";
        newName = "项目["+newName+"]";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("oldName",oldName);
        paramMap.put("newName",newName);
        String result = null;
        try{
            String url = shulieurl+"/api/updateProjectName";
            result = HttpClientUtils.doPost(url,paramMap);
        } catch (Exception e){
            log.error("科研项目，同步数猎云失败");
            return "error";
        }
        return result;
    }

}
