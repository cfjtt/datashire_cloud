package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("project")
public class TProjectController {
    @Autowired
    private TProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private EduApi eduApi;
    @Autowired
    private TProjectConfigService configService;
    @Autowired
    private TProjectUserService projectUserService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ProjectDocFolderService docFolderService;
    protected final Log logger = LogFactory.getLog(this.getClass());

    @ApiOperation(value = "创建项目")
    @PostMapping("addProject")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage addProject(@RequestBody TProject project) throws Exception {
        if(project!=null) {
            //创建数猎场
            if(project.getName() == null || project.getName().trim().length()==0){
                logger.error("科研项目名字不能为空");
                return new ResponseMessage.Fail(ErrorCode.RESEARCHNAMENULL.getCode(),ErrorCode.RESEARCHNAMENULL.getErrorStringFormat());
            }
            User user = null;
            if(project.getCreator() != null){
                user = userService.getUserInfo(project.getCreator());
            }
            //查询出该用户的权限
            if(user == null){
                logger.error("该用户不存在");
                return new ResponseMessage.Fail(ErrorCode.UserNotExist.getCode(),ErrorCode.UserNotExist.getErrorStringFormat());
            }
            //判断是老师还是学生
            if(user.getRole()== RoleEnum.MANAGER.getCode()){
                //管理员没有权限创建项目
                logger.error("管理员没有权限创建科研项目");
                return new ResponseMessage.Fail(ErrorCode.WITHOUTAUTHORITY.getCode(),ErrorCode.WITHOUTAUTHORITY.getErrorStringFormat());
            }
            project.setName(URLDecoder.decode(project.getName(),"UTF-8"));
            project.setBrefIntro(URLDecoder.decode(project.getBrefIntro(),"UTF-8"));
            //判断该名称是否存在
            TProject nameProject = new TProject();
            nameProject.setName(project.getName());
            List<TProject> projects = projectService.selectProjectSelective(nameProject);
            if(projects.size()>0){
                logger.error("该项目已存在");
                return new ResponseMessage.Fail(ErrorCode.RESEARCHNAMEISEXISTS.getCode(),ErrorCode.RESEARCHNAMEISEXISTS.getErrorStringFormat());
            }
            //判断是否超过允许的项目数量
            TProjectConfig projectConfig = new TProjectConfig();
            projectConfig.setRole(user.getRole());
            List<TProjectConfig> projectConfigs = configService.selectSelective(projectConfig);
            if(projectConfigs.size()==0){
                logger.error("该用户对应的套餐不存在");
                return new ResponseMessage.Fail(ErrorCode.RESEARCHPACKAGENOTEXISTS.getCode(),ErrorCode.RESEARCHPACKAGENOTEXISTS.getErrorStringFormat());
            }
            projectConfig = projectConfigs.get(0);
            int existNum = projectConfig.getProjectNum();
            //查询出该用户作为项目组长 已经存在的项目
            int existProject = projectService.getManagerUserCountByUserId(user.getId());
            if(existProject >= existNum){
                logger.error("超过允许创建的项目数");
                return new ResponseMessage.Fail(ErrorCode.OVERLIMITRESEARCHPROJECTNUM.getCode(),ErrorCode.OVERLIMITRESEARCHPROJECTNUM.getErrorStringFormat());
            }
            //开始创建数猎场,数据库等信息
            String result = eduApi.addResearchDatashireSpace(URLEncoder.encode(project.getName(),"UTF-8"),user.getUsername(),user.getPassword2(),user.getRole(),projectConfig.getPackageId());
            if("0".equals(result)){
               logger.error("创建数猎场失败");
               return new ResponseMessage.Fail(ErrorCode.GeneralError.getCode(),ErrorCode.GeneralError.getErrorStringFormat());
            }
            //创建项目

            projectService.addProject(project);
            //添加用户
            TProjectUser projectUser = new TProjectUser();
            projectUser.setCreateTime(new Date());
            projectUser.setIsManager(1);
            projectUser.setProjectId(project.getId());
            projectUser.setUserId(user.getId());
            projectUserService.addProjectUser(projectUser);

        } else {
            return new ResponseMessage.Fail(ErrorCode.InvalidParam.getCode(),ErrorCode.InvalidParam.getErrorStringFormat());
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "我的项目")
    @PostMapping("getMyProject")
    public ResponseMessage<Map<String,Object>> getMyProject(Integer page, Integer offset,Integer creator){
        List<TProject> projects = new ArrayList<>();
        int count = 0;
        if(creator != null){
            projects = projectService.selectProjectByUserId(creator,page,offset);
            for(TProject project : projects){
                //查询出组长
                selectManagerUser(project);
            }
            count = projectService.selectCountProjectByUserId(creator);
        }
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("count",count);
        returnMap.put("projectList",projects);
        return new ResponseMessage.Success<>(returnMap);
    }

    @ApiOperation(value = "我的项目")
    @PostMapping("getProjectById")
    public ResponseMessage<TProject> getProjectById(Integer projectId){
        TProject project = null;
        if(projectId != null){
            project = projectService.selectByPrimaryKey(projectId);
            if(project == null){
                return new ResponseMessage.Fail(ErrorCode.PROJECTISNOTEXISTS.getCode(),ErrorCode.PROJECTISNOTEXISTS.getErrorStringFormat());
            }
            //查询出组长
            selectManagerUser(project);
        } else {
            return new ResponseMessage.Fail(ErrorCode.PROJECTISNOTEXISTS.getCode(),ErrorCode.PROJECTISNOTEXISTS.getErrorStringFormat());
        }
        return new ResponseMessage.Success<>(project);
    }

    public void selectManagerUser(TProject project){
        List<Map<String,Object>> managerUserMapList = projectService.selectProjectManagerUserByProjectId(project.getId());
        if(managerUserMapList.size()>0){
        Map<String,Object> managerUserMap = managerUserMapList.get(0);
        if(managerUserMap!=null) {
            int role = Integer.parseInt(managerUserMap.get("role") + "");
            if (role == RoleEnum.STUDENT.getCode()) {
                int userId = Integer.parseInt(managerUserMap.get("id") + "");
                Student student = studentService.selectByUserId(userId);
                if (student != null) {
                    student.setRole(role);
                }
                //project.setManagerStudent(student);
                project.setManagerUser(student);
            } else if (role == RoleEnum.TEACHER.getCode()) {
                int userId = Integer.parseInt(managerUserMap.get("id") + "");
                try {
                    Teacher teacher = teacherService.getTeacherByUserId(userId);
                    teacher.setRole(role);
                    //project.setManagerTeacher(teacher);
                    project.setManagerUser(teacher);
                } catch (ApplicationErrorException e) {
                    e.printStackTrace();
                }

            }
        }
        }
    }
    @ApiOperation(value = "所有项目")
    @PostMapping("getAllProject")
    public ResponseMessage<Map<String,Object>> getAllProject(Integer page, Integer offset){
        TProject project = new TProject();
        //所有项目是查询是否所有公开的项目
        project.setIsPublic(1);
        Integer start = null;
        if(offset == null ){
            offset = 0;
        }
        if(page != null && page>=1){
            start = (page-1)*offset;
        } else {
            start = 0;
        }
        project.setStart(start);
        project.setOffset(offset);
        List<TProject> projects = projectService.selectProjectSelective(project);
        for(TProject tProject : projects){
            //查询出组长
            selectManagerUser(tProject);
        }
        //获取所有的项目个数
        Integer count = projectService.getCountBySelectSelective(project);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("count",count);
        returnMap.put("projectList",projects);
        return new ResponseMessage.Success<>(returnMap);
    }

    @ApiOperation(value = "项目管理/搜索所有项目")
    @PostMapping("searchProject")
    public ResponseMessage<Map<String,Object>> searchProject(Integer page,Integer offset,String search,Integer isPublic,Integer isSearchPublic){
        search=search.replace("\\","\\\\");
        search=search.replace("%","\\%").replace("_","\\_");
        List<TProject> projects = projectService.selectSelectiveLike(page,offset,search,isPublic,isSearchPublic);
        for(TProject project : projects){
            //查询出组长
            selectManagerUser(project);
        }
        //获取项目的个数
        Integer count = projectService.selectSelecttiveLikeCount(search,isPublic,isSearchPublic);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("count",count);
        returnMap.put("projectList",projects);
        return new ResponseMessage.Success<>(returnMap);
    }

    @ApiOperation(value = "校验名字是否存在")
    @GetMapping("checkNameIsExist")
    public ResponseMessage<String> checkNameIsExist(String name){
        TProject project = new TProject();
        try {
            name = URLDecoder.decode(name,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        project.setName(name);
        List<TProject> projects = projectService.selectProjectSelective(project);
        if(projects.size()>0){
            return new ResponseMessage.Fail(ErrorCode.RESEARCHNAMEISEXISTS.getCode(),ErrorCode.RESEARCHNAMEISEXISTS.getErrorStringFormat());
        }
        return new ResponseMessage.Success<>();
    }

    @ApiOperation(value = "项目管理/项目信息")
    @PostMapping("updateProjectMsg")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage<String> updateProjectMsg(@RequestBody TProject project) throws Exception {
        //查找出该项目原来的名字
        if(project == null || project.getId() == null || project.getId()==0){
            return new ResponseMessage.Fail(ErrorCode.PROJECTISNOTEXISTS.getCode(),ErrorCode.PROJECTISNOTEXISTS.getErrorStringFormat());
        }
        TProject existProject = projectService.selectByPrimaryKey(project.getId());
        if(existProject == null){
            //该项目不存在
            return new ResponseMessage.Fail(ErrorCode.PROJECTISNOTEXISTS.getCode(),ErrorCode.PROJECTISNOTEXISTS.getErrorStringFormat());
        }
        projectService.updateByPrimaryKey(project);
        if(!existProject.getName().equals(project.getName())){
            //需要同步数猎云，修改数猎场的名字
            String result = eduApi.updateProjectName(existProject.getName(),project.getName());
            if(!"1".equals(result)){
                throw new Exception("修改项目名字，同步到数猎云失败");
            }
        }
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value = "项目管理/项目成员管理")
    @PostMapping("getUserProject")
    public ResponseMessage<Map<String,Object>> getProjectUser(Integer projectId){
        //查询出管理员和项目成员之外的其他人
        List<Map<String,Object>> allOtherUserList = projectUserService.getAllOtherUser(projectId,null);
        //查询出项目成员
        List<Map<String,Object>> userInProjectList = projectUserService.getUserInProject(projectId);
        //查询出项目组长
        List<Map<String,Object>> userManagerList = projectUserService.getUserManager(projectId);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("userNotInProject",allOtherUserList);
        returnMap.put("userInProject",userInProjectList);
        returnMap.put("userIsManager",userManagerList);

        return new ResponseMessage.Success<>(returnMap);
    }

    @ApiOperation(value = "项目管理/项目成员管理")
    @PostMapping("getUserProjectSearch")
    public ResponseMessage<Map<String,Object>> getProjectUserSearch(Integer projectId,String search){
        search=search.replace("\\","\\\\");
        search=search.replace("%","\\%").replace("_","\\_");
        List<Map<String,Object>> allOtherUserList = projectUserService.getAllOtherUser(projectId,search);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("userNotInProject",allOtherUserList);
        return new ResponseMessage.Success<>(returnMap);
    }

    @ApiOperation(value = "项目管理/成员搜索")
    @PostMapping("searchUserProject")
    public ResponseMessage<Map<String,Object>> searchProjectUser(int projectId,String search){
        //查询出管理员和项目成员之外的其他人
        List<Map<String,Object>> allOtherUserList = projectUserService.getAllOtherUser(projectId,search);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("userNotInProject",allOtherUserList);
        return new ResponseMessage.Success<>(returnMap);
    }

    @ApiOperation(value = "项目管理/成员保存")
    @PostMapping("saveUserProject")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage<String> saveProjectUser(Integer projectId, Integer[] userInProjects,Integer userManagerId) throws Exception {
        //查询出project信息
        TProject project = projectService.selectByPrimaryKey(projectId);
        if(project == null){
            logger.error("该项目不存在");
            return new ResponseMessage.Fail(ErrorCode.PROJECTISNOTEXISTS.getCode(),ErrorCode.PROJECTISNOTEXISTS.getErrorStringFormat());
        }
        //查询出项目目前已有的人
        List<Map<String,Object>> alreadyuserInProjectList = projectUserService.getAllUserInProject(projectId);
        //修改后项目中的人
        List<Integer> userInProjectList = new ArrayList<>(Arrays.asList(userInProjects));
        //该项目目前的管理员
        List<Map<String,Object>> userManagerMapList = projectUserService.getUserManager(projectId);
        int alreadyUserManagerId = 0;
        if(userManagerMapList.size()>0){
            Map<String,Object> userMap = userManagerMapList.get(0);
            if(userMap.containsKey("user_id") && userMap.get("user_id") != null){
                alreadyUserManagerId = Integer.parseInt(userManagerMapList.get(0).get("user_id")+"");
            }
        }
        Iterator<Map<String,Object>> alreadyUserIterator = alreadyuserInProjectList.iterator();
        while(alreadyUserIterator.hasNext()){
            Map<String,Object> userMap = alreadyUserIterator.next();
            Iterator<Integer> userInProjectIterator = userInProjectList.iterator();
            while(userInProjectIterator.hasNext()){
                Integer userId = userInProjectIterator.next();
                if(Integer.parseInt(userMap.get("user_id")+"") == userId){
                    userInProjectIterator.remove();
                    alreadyUserIterator.remove();
                }
            }

        }

        List<Map<String, Object>> saveProjectUserList = new ArrayList<>();
        if(userInProjectList.size()>0) {
            saveProjectUserList = userService.findUserMsgAndIdByUserIdList(userInProjectList);
            //需要添加批量保存到数据库
            //userInProjectList
            for (Map<String, Object> map : saveProjectUserList) {
                map.put("projectId", projectId);
                map.put("isManager", 0);
            }
            projectUserService.insertBatch(saveProjectUserList);
        }
        //需要删除的
        //alreadyuserInProjectList
        if(alreadyuserInProjectList.size()>0) {
            Map<String, Object> needDelMap = new HashMap<>();
            needDelMap.put("projectId", projectId);
            needDelMap.put("userList", alreadyuserInProjectList);
            projectUserService.deleteBatch(needDelMap);
        }

        //修改管理员
        if(alreadyUserManagerId != userManagerId){
            //解除授权
            Map<String,Object> revokeManager = new HashMap<>();
            revokeManager.put("user_id",alreadyUserManagerId);
            revokeManager.put("project_id",projectId);
            revokeManager.put("is_manager",0);
            projectUserService.updateProjectUser(revokeManager);

            //添加授权
            //判断该用户是否projectUser中，不存在，创建一个，存在更新角色
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("projectId",projectId);
            paramMap.put("userId",userManagerId);
            TProjectUser user = projectUserService.selectByUserIdAndProjectId(paramMap);
            if(user == null){
                //不存在，添加
                TProjectUser managerUser = new TProjectUser();
                managerUser.setIsManager(1);
                managerUser.setUserId(userManagerId);
                managerUser.setProjectId(projectId);
                projectUserService.addProjectUser(managerUser);
            } else {
                Map<String, Object> grantManager = new HashMap<>();
                grantManager.put("user_id", userManagerId);
                grantManager.put("project_id", projectId);
                grantManager.put("is_manager", 1);
                projectUserService.updateProjectUser(grantManager);
            }

        }

        //同步数猎云接口,修改space_user
        String projectName = project.getName();
        //查询出组长的no
        User user = userService.getUserInfo(userManagerId);
        Map<String,Object> userManager = new HashMap<>();
        if(user != null){
            userManager.put("no",user.getUsername());
            userManager.put("password2",user.getPassword2());
        }
        String result = eduApi.updateProjecSpaceUser(projectName,saveProjectUserList,alreadyuserInProjectList,userManager);
        if(!"1".equals(result)){
            throw new Exception("科研项目，同步数猎云失败");
        }
        return new ResponseMessage.Success<>();
    }



    @ApiOperation(value = "项目管理/删除项目")
    @PostMapping("delProject")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage<String> delProject(Integer projectId) throws IOException, ApplicationErrorException {
        //查询项目相关信息
        TProject project = projectService.selectByPrimaryKey(projectId);
        if(project == null){
            //该项目不存在
            return new ResponseMessage.Fail(ErrorCode.PROJECTISNOTEXISTS.getCode(),ErrorCode.PROJECTISNOTEXISTS.getErrorStringFormat());
        }
        //删除项目
        projectService.deleteByPrimaryKey(projectId);
        //删除项目里面的人
        projectUserService.deleteByProjectId(projectId);
        //删除文档
        //删除附件
        //删除文件夹
        //deleteAllByProjectId
        docFolderService.deleteAllByProjectId(projectId);
        //删除数猎场相关
        if(project != null) {
            String spaceName = project.getName();
            eduApi.deleteSpaceRelevantData(spaceName,2);
        }
        return new ResponseMessage.Success<>();
    }


}
