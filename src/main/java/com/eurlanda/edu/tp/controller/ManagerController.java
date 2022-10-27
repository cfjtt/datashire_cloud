package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.Util.RecordUtils;
import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.UserAccessRecordMapper;
import com.eurlanda.edu.tp.enums.CloudwareTypeEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.webdomain.request.*;
import com.eurlanda.edu.tp.webdomain.response.*;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;


@CrossOrigin
@RestController
@RequestMapping("admin")
public class ManagerController {


    static Logger log = Logger.getLogger(ManagerController.class.getName());

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ClassService classService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private UserService userService;
    @Autowired
    private  StudentExperimentService studentExperimentService;
    @Autowired
    private EduApi eduApi;
    @Autowired
    private UserStatusService statusService;
    @Autowired
    private RancherService rancherService;
    @Autowired
    private ClassCourseService classCourseService;

    @Autowired
    private SysLicenseService sysLicenseService;
    @Autowired
    private JobScoreService jobScoreService;

    @Autowired
    private UserAccessRecordMapper userAccessRecordMapper;

    @Autowired
    private org.springframework.core.env.Environment environment;

    @Autowired
    private TProjectUserService projectUserService;

    @Value("${default.password}")
    private String defaultPassword;



    @ApiOperation(value = "获取所有老师信息", notes = "")
    @GetMapping(value = "teacher/all")
    public ResponseMessage<ResTeacherList> getAllTeachers() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(teacherService.getAllTeacherList());
    }

    @ApiOperation(value = "获取所有老师信息分页", notes = "")
    @PostMapping(value = "teacher/byLimit")
    public ResponseMessage<ResTeacherList> getTeacherByLimit(@RequestBody Map<String,Object> map) throws ApplicationErrorException {
        int currentPage = Integer.parseInt(map.get("currentPage")+"");
        int limit = Integer.parseInt(map.get("limit")+"");
        return new ResponseMessage.Success<>(teacherService.getAllTeachersBylimit(currentPage,limit));
    }

    @ApiOperation(value = "创建老师信息", notes = "")
    @PostMapping(value = "teacher/creation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage createTeacher(@RequestBody ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException {
        teacherService.createTeacher(reqUpdateTeacher);
        return new ResponseMessage.Success();
    }


    @ApiOperation(value = "更新老师信息", notes = "")
    @PostMapping(value = "teacher/updation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage updateTeacher(@RequestBody ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException {
        //查找当前老师是否存在数猎云相关的课程(编号，姓名，性别等，不包括修改密码)
        //先查询出老师编号是否存在，不存在，则说明需要此次更新的是编号，那么就意味着需要调用添加addUser接口
        Teacher aleadyTeacher = teacherService.getTeacherByUserId(reqUpdateTeacher.getId());
        User user = userService.getUserInfo(reqUpdateTeacher.getId());
        String aleadyNo = aleadyTeacher.getTno();
        boolean flag = teacherService.checkTnoIsExist(reqUpdateTeacher);
        teacherService.updateTeacher(reqUpdateTeacher);

       /* if(!flag) {
            //教师编号不存在
            List<Map<String,Object>> delUserList = new ArrayList<>();
            List<Map<String,Object>> addUserList = new ArrayList<>();
            int teacher_id = reqUpdateTeacher.getId();
            int cloudware_type = CloudwareTypeEnum.SHU_LIE_YUN.getCode();
            List<Map<String,Object>> teacherList = teacherService.selectTeacherIsShulieyunByUserId(cloudware_type, teacher_id);
            if (teacherList != null && teacherList.size() > 0) {
                //先删除原来的
                Map<String,Object> delMap = new HashMap<>();
                List<String> delList = new ArrayList<>();
                delList.add(aleadyNo);
                delMap.put("userList",delList);
                delUserList.add(delMap);

                for(Map<String,Object> teacherMap : teacherList){
                    //在插入新的
                    Map<String,Object> addMap = new HashMap<>();
                    addMap.put("coursename",teacherMap.get("course_name"));
                    List<Map<String,Object>> addList = new ArrayList<>();
                    Map<String,Object> addChildMap = new HashMap<>();
                    addChildMap.put("phone",reqUpdateTeacher.getTeacherNo());
                    addChildMap.put("password",user.getPassword2());
                    addList.add(addChildMap);
                    addMap.put("userList",addList);
                    addUserList.add(addMap);
                }

                //调用接口，增加,删除
                for(Map<String,Object> needdelMap : delUserList){
                    eduApi.delUser(needdelMap);
                }
                for(Map<String,Object> addMap : addUserList){
                    eduApi.addUser(addMap);
                }
            }
        }*/
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "删除老师", notes = "")
    @PostMapping(value = "teacher/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteTeacher(@RequestBody ReqDeleteTeacher reqDeleteTeacher) throws ApplicationErrorException {
        //需要找出该老师下面的，所有已经关联了的课程，然后调用数猎云接口，删除course_teacher表里面相关的数据
        // 查找该老师是否已经绑定课程或者班级
        boolean flag = classService.isTeacherUsedByClass(reqDeleteTeacher.getTeacherId());
        if(flag){
            return new ResponseMessage.Fail(ErrorCode.TeacherIsUsedByClass.getCode(),ErrorCode.TeacherIsUsedByClass.getErrorStringFormat());
        }
        TProjectUser projectUser = new TProjectUser();
        projectUser.setUserId(reqDeleteTeacher.getTeacherId());
        projectUser.setIsManager(1);
        List<TProjectUser> projectUsers = projectUserService.selectSelective(projectUser);
        if(projectUsers.size()>0){
            return new ResponseMessage.Fail(ErrorCode.USERISPROJECTMANAGER.getCode(),ErrorCode.USERISPROJECTMANAGER.getErrorStringFormat());
        }
        //课程与老师已经解除关联不再做校验
        /*flag = courseService.isTeacherUsedByCourse(reqDeleteTeacher.getTeacherId());
        if(flag){
            return new ResponseMessage.Fail(ErrorCode.TeacherIsUsedByCourse.getCode(),ErrorCode.TeacherIsUsedByCourse.getErrorStringFormat());
        }*/
        //判断student_exper是否有老师数据
        List<ReqStudentExperiment> studentExperiments=studentExperimentService.getStudentAllExperiments(reqDeleteTeacher.getTeacherId());
        if(studentExperiments!=null && studentExperiments.size()>0){
            for(ReqStudentExperiment reqStudentExperiment:studentExperiments){
                studentExperimentService.deleteStudentExperiment(reqStudentExperiment.getExperimentId(),
                        reqStudentExperiment.getStudentId());
            }
        }
        List<Map<String,Object>> teacherList = teacherService.selectTeacherIsShulieyunByUserId(CloudwareTypeEnum.SHU_LIE_YUN.getCode(),reqDeleteTeacher.getTeacherId());
        if(teacherList!=null && teacherList.size()>0){
            Teacher teacher = teacherService.getTeacherByUserId(reqDeleteTeacher.getTeacherId());
            if(teacher!=null) {
                //说明需要同步
                Map<String, Object> delMap = new HashMap<>();
                List<String> userList = new ArrayList<>();
                userList.add(teacher.getTno());
                delMap.put("userList", userList);
                delMap.put("courseName","");
                eduApi.delUser(delMap);
            }
        }
        teacherService.deleteTeacherByTeacherUserId(reqDeleteTeacher);

        //删除项目成员信息
        projectUserService.deleteByUserId(reqDeleteTeacher.getTeacherId());
        return new ResponseMessage.Success();
    }

   /* @ApiOperation(value = "获取所有学期信息", notes = "")
    @GetMapping(value = "semester/all")
    public ResponseMessage<ResSemesterList> getAllSemesters() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(termService.getAllTerms());
    }

    @ApiOperation(value = "新增学期", notes = "")
    @PostMapping(value = "semester/creation")
    public ResponseMessage createSemester(@RequestBody Term term) throws ApplicationErrorException {
        try{
            termService.create(term);
        }catch (ApplicationErrorException e){
            if(e.getErrorCode() == ErrorCode.TermAlreadyExists.getCode()){
                return new ResponseMessage.Fail(ErrorCode.TermAlreadyExists.getCode(),e.getMessage());
            }else if(e.getErrorCode() == ErrorCode.InvalidSemester.getCode()){
                return new ResponseMessage.Fail(ErrorCode.InvalidSemester.getCode(),e.getMessage());
            }
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "更新学期", notes = "")
    @PostMapping(value = "semester/updation")
    public ResponseMessage updateSemester(@RequestBody Term term) throws ApplicationErrorException {
        termService.update(term);
        return new ResponseMessage.Success();
    }
    @ApiOperation(value = "根据编号查询学期", notes = "")
    @PostMapping(value = "semester/findTearmById")
    public ResponseMessage<Term> findTearmById(@RequestParam int tearmId) throws ApplicationErrorException {
        return new ResponseMessage.Success(termService.getTermById(tearmId));
    }

    @ApiOperation(value = "删除学期", notes = "")
    @PostMapping(value = "semester/deletion")
    public ResponseMessage deleteSemester(@RequestBody ReqDeleteSemester reqDeleteSemester) throws ApplicationErrorException {
        termService.delete(reqDeleteSemester);
        return new ResponseMessage.Success();
    }*/

    @ApiOperation(value = "删除作业", notes = "")
    @PostMapping(value = "class/homework/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteHomework(@RequestBody ReqDeleteHomework reqDeleteHomework) throws ApplicationErrorException {
        homeworkService.deleteHomework(reqDeleteHomework);
        return new ResponseMessage.Success();
    }


    @ApiOperation(value = "更新作业", notes = "")
    @PostMapping(value = "class/homework/updation")
    public ResponseMessage updateHomework(@RequestBody ReqUpdateHomework reqUpdateHomework) throws ApplicationErrorException {
        homeworkService.updateHomework(reqUpdateHomework);
        //判断课程是否是数猎云课程，不是，需要将数猎云已经初始化的信息删除
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "添加作业", notes = "")
    @PostMapping(value = "class/homework/creation")
    public ResponseMessage createHomework(@RequestBody ReqCreateHomework reqCreateHomework) throws ApplicationErrorException {
        homeworkService.createHomework(reqCreateHomework);
        //判断课程，初始化一系列基本信息
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "获取班级作业", notes = "")
    @GetMapping(value = "/class/{classId}/homework")
    public ResponseMessage<ResClassHomework> getClassHomework(@PathVariable("classId") int classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(homeworkService.getClassHomework(classId));
    }

    @ApiOperation(value = "获取课程信息", notes = "")
    @GetMapping(value = "course/all")
    public ResponseMessage<ResCourseList> getAllCourses() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getAllCourses());
    }

    @ApiOperation(value = "获取所有课程", notes = "")
    @GetMapping(value = "course/allWithOutCondition")
    public ResponseMessage<List<Course>> selectAllCourses(){
        return new ResponseMessage.Success<>(courseService.selectAllCourses());
    }

    @ApiOperation(value = "创建课程信息", notes = "")
    @PostMapping(value = "course/creation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage createCourse(@RequestBody ReqAddCourse reqAddCourse) throws ApplicationErrorException {
        //验证是否已经存在相同的课程
        courseService.createCourse(reqAddCourse);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "更新课程信息", notes = "")
    @PostMapping(value = "course/updation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage updateCourse(@RequestBody Course course) throws ApplicationErrorException {
        try{
            courseService.updateCourse(course);
        }catch (ApplicationErrorException e){
            if(e.getMessage().equals(ErrorCode.CourseNotExists.getErrorStringFormat())){
                return new ResponseMessage.Fail(ErrorCode.CourseNotExists.getCode(),ErrorCode.CourseNotExists.getErrorStringFormat());
            }else {
                throw e;
            }
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "删除课程信息", notes = "")
    @PostMapping(value = "course/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteCourse(@RequestBody ReqDeleteCourse reqDeleteCourse) throws ApplicationErrorException {
        try{
            //查询该课程是否和数猎云有关系
            boolean isShulieyun=courseService.selectCourseExistShulieyun(reqDeleteCourse.getId());
            Course course = courseService.getCourseById(reqDeleteCourse.getId());
            String volumeId = null;
            if(course != null){
                volumeId = course.getVolumeId();
            }
            List<String> userList=new ArrayList<>();
            if(isShulieyun){
               userList=findShulieyunUserByCourse(reqDeleteCourse.getId());
            }
            courseService.deleteCourse(reqDeleteCourse);
            if(volumeId != null){
                rancherService.deleteVolume(volumeId);
            }
            //解除与数列云的关系
            if(userList!=null && userList.size()>0){
                Map<String,Object> map=new HashMap();
                map.put("userList",userList);
                map.put("courseName",course.getName());
                eduApi.delUser(map);
            }

            if(isShulieyun){
                //删除数猎云相关数据
                String result = eduApi.deleteSpaceRelevantData(course.getName(),1);
                if(result.equals("error")){
                    log.error("删除课程调用数猎云接口错误！");
                    throw new ApplicationErrorException(ErrorCode.GeneralError);
                }
            }



        }catch (ApplicationErrorException e){
            if(e.getErrorCode() == ErrorCode.CourseNotExists.getCode()){
                return new ResponseMessage.Fail(ErrorCode.CourseNotExists.getCode(),e.getMessage());
            }else if(e.getErrorCode() == ErrorCode.CourseIsUsedByClass.getCode()){
                return new ResponseMessage.Fail(ErrorCode.CourseIsUsedByClass.getCode(),e.getMessage());
            }else {
                throw e;
            }
        }

        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "删除班级学生", notes = "删除班级里的一个学生")
    @PostMapping(value = "/class/student/deletion")
    public ResponseMessage deleteClassStudent(@RequestBody
    ReqDeleteClassStudent reqDeleteClassStudent) throws ApplicationErrorException {
        Student student = studentService.selectByUserId(reqDeleteClassStudent.getStudentId());
        //判断student_exper是否有老师数据
        List<ReqStudentExperiment> studentExperiments=studentExperimentService.getStudentAllExperiments(reqDeleteClassStudent.getStudentId());
        if(studentExperiments!=null && studentExperiments.size()>0){
            for(ReqStudentExperiment reqStudentExperiment:studentExperiments){
                studentExperimentService.deleteStudentExperiment(reqStudentExperiment.getExperimentId(),
                        reqStudentExperiment.getStudentId());
            }
        }
        classService.deleteClassStudent(reqDeleteClassStudent,0);
        //判断该学生在其他班级里是否有该课程
        Map map=new HashMap();
        map.put("classId",reqDeleteClassStudent.getClassId());
        map.put("courseId",reqDeleteClassStudent.getCourseId());
        map.put("userId",reqDeleteClassStudent.getStudentId());
       int exists=studentService.selectExistsCourseByClassIdAndCourseIdAndStuId(map);
        if(exists==0){
            //同步接口
            Course course=courseService.getCourseById(reqDeleteClassStudent.getCourseId());
            Map<String,Object> objectMap = new HashMap<>();
            List<String> userList = new ArrayList<>();
            userList.add(student.getSno());
            objectMap.put("userList",userList);
            objectMap.put("courseName",course.getName());
            eduApi.delUser(objectMap);
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "重置密码", notes = "")
    @PostMapping(value = "/password/resetion")
    public ResponseMessage resetPassword(@RequestBody ReqResetPassword reqResetPassword) throws ApplicationErrorException {
        try{
            managerService.resetPassword(reqResetPassword);

            Date curDate = RecordUtils.getYearMonthDay(new Date());
            UserAccessRecord paraRecord = new UserAccessRecord();
            paraRecord.setUserId(reqResetPassword.getUserId());
            paraRecord.setAccessDate(curDate);
            UserAccessRecord record = userAccessRecordMapper.selectUserRecordByUserIdAndDate(paraRecord);
            if(record.getStatus() == 1){
                User user = userService.selectByPrimaryKey2(reqResetPassword.getUserId());
                //在线情况下才记录重置密码的状态
                User paraUser = new User();
                paraUser.setTokenCreateTime(-1l);//重置密码之后设为-1
                paraUser.setIsRemind(user.getIsRemind());
                paraUser.setId(reqResetPassword.getUserId());
                paraUser.setToken(user.getToken());
                userService.updateByPrimaryKey2(paraUser);//更新token生成时间。
            }
        }catch (ApplicationErrorException e){
           throw e;
        }
        User user = userService.getUserInfo(reqResetPassword.getUserId());
        if(user!=null) {
            String phone = user.getUsername();
            String password = user.getPassword2();
            Map<String,Object> paramMap = new HashMap<>();
            List<Map<String,Object>> userList = new ArrayList<>();
            Map<String,Object> userMap = new HashMap<>();
            userMap.put("phone",phone);
            userMap.put("password",password);
            userList.add(userMap);
            paramMap.put("userList",userList);
            eduApi.updatePassword(paramMap);
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "更新学生信息", notes = "")
    @PostMapping(value = "/class/student/updation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage updateStudentInfo(@RequestBody ReqUpdateStudent reqUpdateStudent) throws ApplicationErrorException {
        studentService.updateStudentInfo(reqUpdateStudent);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "添加班级学生", notes = "")
    @PostMapping(value = "/class/student/creation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage addClassStudent(@RequestBody ReqAddClassStudent reqAddClassStudent) throws ApplicationErrorException {
        try{
            classService.addClassStudent(reqAddClassStudent);
        }catch (ApplicationErrorException e){
            if(e.getErrorCode() == ErrorCode.CourseNotExists.getCode()){
                return new ResponseMessage.Fail(e.getErrorCode(),e.getMessage());
            }else {
                throw e;
            }
        }
        //判断该课程是否是数猎云相关课程，是同步数猎云用户接口
        boolean flag = classService.selectIsShulieyunCourseByClassId(reqAddClassStudent.getClassId(),CloudwareTypeEnum.SHU_LIE_YUN.getCode());
        if(flag){
            //需要同步数猎云
            String phone = reqAddClassStudent.getStudentNo();
            String password = null;
            String courseName = null;
            Student student = studentService.getStudentByNo(reqAddClassStudent.getStudentNo());
            if(student!=null){
                User user = userService.getUserInfo(student.getUserId());
                if(user!=null){
                    password = user.getPassword2();
                    //查询出课程名字
                    List<Course> courseList = courseService.getCourseByClassId(reqAddClassStudent.getClassId());
                    if(courseList!=null && courseList.size()>0){
                        for(Course course:courseList){
                            courseName = course.getName();
                            Map<String,Object> paramMap = new HashMap<>();
                            List<Map<String,Object>> userList = new ArrayList<>();
                            Map<String,Object> userMap = new HashMap<>();
                            userMap.put("phone",phone);
                            userMap.put("password",password);
                            userList.add(userMap);
                            paramMap.put("coursename",courseName);
                            paramMap.put("userList",userList);
                            eduApi.addUser(paramMap);
                        }

                    }
                }
            }
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "获取班级所有学生信息", notes = "")
    @GetMapping(value = "class/{classId}/students/all")
    public ResponseMessage<ResClassStudents> getAllClassStudentInfo(@PathVariable("classId") int classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(classService.getAllClassStudentInfo(classId));
    }

    @ApiOperation(value = "获取所有学生信息", notes = "")
    @GetMapping(value = "/students/all")
    public ResponseMessage<ResClassStudents> getAllStudentInfo() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(studentService.getAllStudentInfo());
    }

    @ApiOperation(value = "添加学生", notes = "")
    @PostMapping(value = "/student/creation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage addStudent(@RequestBody ReqAddStudent reqAddStudent) throws ApplicationErrorException {
            try{
                studentService.addStudentInfo(reqAddStudent);
            }catch (ApplicationErrorException e){
                if(e.getErrorCode() == ErrorCode.StudentAlreadyExists.getCode()){
                    return new ResponseMessage.Fail(ErrorCode.StudentAlreadyExists.getCode(),e.getMessage());
                }else{
                    throw e;
                }
            }
        return new ResponseMessage.Success();
    }
    @ApiOperation(value = "修改学生", notes = "")
    @PostMapping(value = "/student/update")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage updateStudent(@RequestBody ReqUpdateStudent reqUpdateStudent) throws ApplicationErrorException {
        try{
            studentService.updateStu(reqUpdateStudent);
        }catch (ApplicationErrorException e){
            if(e.getErrorCode() == ErrorCode.StudentAlreadyExists.getCode()){
                return new ResponseMessage.Fail(ErrorCode.StudentAlreadyExists.getCode(),e.getMessage());
            }else if(e.getErrorCode() == ErrorCode.StudentNotExists.getCode()){
                return new ResponseMessage.Fail(ErrorCode.StudentNotExists.getCode(),e.getMessage());
            }else{
                throw e;
            }
        }
        return new ResponseMessage.Success();
    }
    @ApiOperation(value = "批量导入学生数据", notes = "")
    @PostMapping(value = "/student/batchCreation")
    public ResponseMessage<ResBatchCreateStudent> batchStudentCreation(@RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        return new ResponseMessage.Success<ResBatchCreateStudent>(studentService.batchCreateStudent( file));
    }
    @ApiOperation(value = "删除学生数据", notes = "")
    @PostMapping(value = "/student/delete")
    public ResponseMessage deleteStudent(@RequestParam("studentNo") String studentNo) throws ApplicationErrorException, IOException {
        Student student = studentService.getStudentByNo(studentNo);
        if(student!=null){
            //查询这个学生多少个班级里
            List<StudentClass> studentClassList=studentService.getAllStudentClassByUserId(student.getUserId());
            TProjectUser projectUser = new TProjectUser();
            projectUser.setUserId(student.getUserId());
            projectUser.setIsManager(1);
            List<TProjectUser> projectUsers = projectUserService.selectSelective(projectUser);
            if(studentClassList!=null && studentClassList.size()>0){
                throw new  ApplicationErrorException(ErrorCode.StudentIsUsedByClass);
               /*
               for(StudentClass studentClass:studentClassList){
                    //查询这个学生所有的课程
                    Map<String,Object> map=new HashMap<>();
                    map.put("classId",studentClass.getClassId());
                    map.put("studentId",studentClass.getStudentId());
                   List<Course> courses=courseService.findCourseByStuId(map);
                   if(courses!=null && courses.size()>0){
                       //用于和数列云做关联集合
                       List<String> userList=new ArrayList<>();
                       for(Course course:courses){
                           ReqDeleteClassStudent deleteClassStudent=new ReqDeleteClassStudent();
                           deleteClassStudent.setClassId(studentClass.getClassId());
                           deleteClassStudent.setCourseId(course.getId());
                           deleteClassStudent.setStudentId(student.getUserId());
                           //删除student_class,student,user 表对应记录
                           classService.deleteClassStudent(deleteClassStudent,1);
                           //查询这个课程 是否和数猎云有关系
                           boolean isShuliuyun=courseService.selectCourseExistShulieyun(course.getId());
                           if(isShuliuyun){
                               userList.add(student.getSno());
                               Map<String,Object> objectMap = new HashMap<>();
                               objectMap.put("userList",userList);
                               objectMap.put("courseName",course.getName());
                               eduApi.delUser(objectMap);
                           }
                       }
                   }
                }*/
            } else if(projectUsers.size()>0){
                throw  new  ApplicationErrorException(ErrorCode.USERISPROJECTMANAGER);
            } else {
                //判断student_exper是否有数据
                List<ReqStudentExperiment> studentExperiments=studentExperimentService.getStudentAllExperiments(student.getUserId());
                if(studentExperiments!=null && studentExperiments.size()>0){
                    for(ReqStudentExperiment reqStudentExperiment:studentExperiments){
                        studentExperimentService.deleteStudentExperiment(reqStudentExperiment.getExperimentId(),
                                reqStudentExperiment.getStudentId());
                    }
                }
                studentService.deleteStuByStuNotClassAndCourse(student);
            }
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "删除选课", notes = "")
    @PostMapping(value = "class/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteClass(@RequestBody ReqDeleteClass reqDeleteClass) throws ApplicationErrorException {
        classService.deleteClass(reqDeleteClass);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "更新选课信息", notes = "")
    @PostMapping(value = "class/updation")
    public ResponseMessage updateClassInfo(@RequestBody ReqUpdateClass reqUpdateClass) throws ApplicationErrorException {
            String className=reqUpdateClass.getClassName();
            if(className!=null && !className.equals("")){
                //查询是否已经存在该班级
                Clazz clazz=classService.findClassByNameAndCId(reqUpdateClass);
                if(clazz!=null){
                    throw new ApplicationErrorException(ErrorCode.ClassAlreadyExisted);
                }
                classService.updateClassInfo(reqUpdateClass);
            }
        return new ResponseMessage.Success();
    }



    /*@ApiOperation(value = "更新选课信息", notes = "")
    @PostMapping(value = "class/updation")
    public ResponseMessage updateClassInfo(@RequestBody ReqUpdateClass reqUpdateClass) throws ApplicationErrorException {
        //验证修改后班级名是否与已有班级名重复
        int courseId = reqUpdateClass.getCourseId();
        //现在的班级对应的课程信息
        Course nowCourse = courseService.getCourseById(courseId);
        // 关联的班级信息 原来班级信息
        Clazz clazz = classService.getClassById(reqUpdateClass.getClassId());
        //查询原来班级对应的课程
        Course oldCourse=courseService.getCourseById(clazz.getCourseId());
        User oldUser = userService.getUserInfo(clazz.getTeacherId());
        User newUser = userService.getUserInfo(reqUpdateClass.getTeacherId());
        String oldCourseName = null;
        if(oldCourse!=null){
            oldCourseName = oldCourse.getName();
        }
        boolean isSynchronized = courseService.selectCourseExistShulieyun(oldCourse.getId());
        //查询这班级的学生
        ResClassStudents classStudents = classService.getAllClassStudentInfo(reqUpdateClass.getClassId());
        classService.updateClassInfo(reqUpdateClass);
        List<Map<String, Object>> addUserList = new ArrayList<>();
        if(isSynchronized) {
            List<Integer> userIdList = new ArrayList<>();
            int isUpdateTeacher=0;
            if(clazz.getTeacherId().intValue()!=reqUpdateClass.getTeacherId()){
                //判断原来的老师在其他班级中是否还有该课程
                Map<String,Object> map=new HashMap<>();
                map.put("classId",reqUpdateClass.getClassId());
                map.put("teacherId",clazz.getTeacherId());
                map.put("courseId",oldCourse.getId());
                int teacherCount=classService.findSlyByTeaIdAndClassIdAndCourseId(map);
                if(teacherCount==0){
                    //查询课程表 这个课程对应的老师是否是该老师 是的话也不能解除该老师和该课程的关系
                    Course teaCourse=courseService.getCourseById(oldCourse.getId());
                    if(teaCourse!=null && teaCourse.getTeacherId().intValue()!=clazz.getTeacherId().intValue()){
                        //解除原来老师和数猎场的关系
                        userIdList.add(clazz.getTeacherId());
                        isUpdateTeacher=1;
                    }
                }
                //调用接口，将新增加的老师与数列云做关联
                if(newUser!=null) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("phone", newUser.getUsername());
                    userMap.put("password",newUser.getPassword2());
                    addUserList.add(userMap);
                }
            }
            //判断课程是否修改
            if(nowCourse.getId().intValue()!=oldCourse.getId().intValue()){
                //根据班级和课程编号查询关联表
                Map<String,Object> selectMap=new HashMap<>();
                selectMap.put("classId",clazz.getId());
                selectMap.put("courseId",oldCourse.getId());
                ClassCourse classCourse=classCourseService.findClassCourseByClassIdAndCourseId(selectMap);
                if(classCourse!=null){
                    //修改班级和课程关联表
                    classCourse.setCourseId(nowCourse.getId());
                    classCourseService.updateClassCourseByClassIdAndCourseId(classCourse);
                }
                if(classStudents != null){
                    if(classStudents.getStudentList()!=null && classStudents.getStudentList().size()>0){
                        for(ResClassStudents.ResClassStudent resClassStudent:classStudents.getStudentList()){
                            //判断学生在其他班级中是否有该课程
                            Map<String,Object> map=new HashMap<>();
                            map.put("classId",reqUpdateClass.getClassId());
                            map.put("studentId",resClassStudent.getId());
                            map.put("courseId",oldCourse.getId());
                            //判断该班级学生在其他班级中是否有相同课程
                            int count=studentService.findSlyByNotClassAndStuId(map);
                            if(count>0){
                                continue;
                            }
                            userIdList.add(resClassStudent.getId());
                        }
                    }
                }
                //判断老师是否已经与原来的课程脱离了关系  没有的话 查询关联老师
                if(isUpdateTeacher==0){
                    //查询原来老师在别的班级里是否有该课程
                    Map<String,Object> map=new HashMap<>();
                    map.put("classId",reqUpdateClass.getClassId());
                    map.put("teacherId",clazz.getTeacherId());
                    map.put("courseId",oldCourse.getId());
                    int teacherCount=classService.findSlyByTeaIdAndClassIdAndCourseId(map);
                    if(teacherCount==0){
                        //查询课程表 这个课程对应的老师是否是该老师 是的话也不能解除该老师和该课程的关系
                        Course teaCourse=courseService.getCourseById(oldCourse.getId());
                        if(teaCourse!=null && teaCourse.getTeacherId().intValue()!=clazz.getTeacherId().intValue()){
                            //解除原来老师和数猎场的关系
                            userIdList.add(clazz.getTeacherId());
                        }
                    }
                }
                //判断修改后的课程是否和数列云有关系
                boolean isShulieyun = courseService.selectCourseExistShulieyun(nowCourse.getId());
                if(isShulieyun){
                      //同步学生
                    if(classStudents != null){
                        for(ResClassStudents.ResClassStudent resClassStudent : classStudents.getStudentList()) {
                            //根据学生编号到用户表中查询信息
                            User stuUser=userService.getUserInfo(resClassStudent.getStudentNo());
                            if(stuUser!=null){
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("phone", resClassStudent.getStudentNo());
                                userMap.put("password", stuUser.getPassword2());
                                addUserList.add(userMap);
                            }

                        }
                    }
                    //判断是否修改了老师 没有修改将原来的老师添加与现在课程关联
                    if(isUpdateTeacher==0){
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("phone", oldUser.getUsername());
                        userMap.put("password", oldUser.getPassword2());
                        addUserList.add(userMap);
                    }
                }
            }
            //解除相关用户与数列云的关系
            if(userIdList!=null && userIdList.size()>0){
                //查询用户名
                Map<String,Object> map=new HashMap<>();
                map.put("idList",userIdList);
                List<String> userList=userService.findUserNameListByUserIdList(map);
                if(userList!=null && userList.size()>0){
                    if(oldCourse!=null){
                        map=new HashMap<>();
                        map.put("userList",userList);
                        map.put("courseName",oldCourse.getName());
                        eduApi.delUser(map);
                    }
                }
            }

        }else{
            *//**
             *  原来课程就没有数列云  判断课程是否修改  课程修改判断是否是数列云
             *  是数列云 将老师和学生同步
             *//*
            if(nowCourse.getId().intValue()!=oldCourse.getId().intValue()){
                //根据班级和课程编号查询关联表
                Map<String,Object> selectMap=new HashMap<>();
                selectMap.put("classId",clazz.getId());
                selectMap.put("courseId",oldCourse.getId());
                ClassCourse classCourse=classCourseService.findClassCourseByClassIdAndCourseId(selectMap);
                if(classCourse!=null){
                    //修改班级和课程关联表
                    classCourse.setCourseId(nowCourse.getId());
                    classCourseService.updateClassCourseByClassIdAndCourseId(classCourse);
                }
                boolean isShulieyun = courseService.selectCourseExistShulieyun(nowCourse.getId());
                if(isShulieyun){
                    //同步学生
                    if(classStudents != null){
                        for(ResClassStudents.ResClassStudent resClassStudent : classStudents.getStudentList()) {
                            //根据学生编号到用户表中查询信息
                            User stuUser=userService.getUserInfo(resClassStudent.getStudentNo());
                            if(stuUser!=null){
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("phone", resClassStudent.getStudentNo());
                                userMap.put("password", stuUser.getPassword2());
                                addUserList.add(userMap);
                            }
                        }
                    }
                    //判断是否修改了老师 没有修改将原来的老师添加与现在课程关联
                    if(clazz.getTeacherId().intValue()!=reqUpdateClass.getTeacherId()){
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("phone", newUser.getUsername());
                        userMap.put("password", newUser.getPassword2());
                        addUserList.add(userMap);
                    }else{
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("phone", oldUser.getUsername());
                        userMap.put("password", oldUser.getPassword2());
                        addUserList.add(userMap);
                    }
                }

            }
        }
        //同步老师和学生
        if(addUserList.size()>0) {
            Map<String, Object> addUserMap = new HashMap<>();
            //如果课程修改  那么将老师和学生与新的课程关联
            if(nowCourse.getId().intValue()!=oldCourse.getId().intValue()){
                addUserMap.put("coursename", nowCourse.getName());
            }else{
                addUserMap.put("coursename", oldCourseName);
            }
            addUserMap.put("userList", addUserList);
            eduApi.addUser(addUserMap);
        }
        return new ResponseMessage.Success();
    }*/

    @ApiOperation(value = "新增班级", notes = "")
    @PostMapping(value = "class/creation")
    public ResponseMessage createClass(@RequestBody ReqAddClass reqAddClass) throws ApplicationErrorException {
        classService.createClass(reqAddClass);
        //查询出该课程下面是否存在数猎云的实验，存在则同步数猎云
        boolean isSync = courseService.selectCourseExistShulieyun(reqAddClass.getCourseId());
        if(isSync){
            User user = userService.getUserInfo(reqAddClass.getTeacherId());
            if(user!=null) {
                Map<String, Object> addUserMap = new HashMap<>();
                List<Map<String, Object>> addUserList = new ArrayList<>();
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("phone", user.getUsername());
                userMap.put("password", user.getPassword2());
                addUserList.add(userMap);
                //获取课程名字
                Course course = courseService.getCourseById(reqAddClass.getCourseId());
                if(course!=null) {
                    addUserMap.put("coursename", course.getName());
                    addUserMap.put("userList", addUserList);
                }
                eduApi.addUser(addUserMap);
            }
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "获取所有班级信息", notes = "")
    @GetMapping(value = "class/all")
    public ResponseMessage<ResClassInfos> getAllClassInfo() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(classService.getAllClassInfo());
    }

    @ApiOperation(value = "获取课程实验信息", notes = "")
    @GetMapping(value = "course/{courseId}/experiments")
    public ResponseMessage<ResCourseModuleExperiments> getCourseExperiments(@PathVariable int courseId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getCourseModuleExperiments(courseId));
    }

    @ApiOperation(value = "查询章节", notes = "")
    @PostMapping(value = "course/module/getModule")
    public ResponseMessage<Module> getModule(@RequestBody Module module) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(moduleService.getModuleInfo(module));
    }


    @ApiOperation(value = "新增章节", notes = "")
    @PostMapping(value = "course/module/creation")
    public ResponseMessage<ResModuleId> createModule(@RequestBody Module module) throws ApplicationErrorException {
        ResModuleId resModuleId = moduleService.createModule(module);
        return new ResponseMessage.Success<>(resModuleId);
    }

    @ApiOperation(value = "编辑章节", notes = "")
    @PostMapping(value = "course/module/update")
    public ResponseMessage updateModule(@RequestBody Module module) throws ApplicationErrorException {
        moduleService.updateModule(module);
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value = "删除课时", notes = "")
    @PostMapping(value = "course/module/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteModule(@RequestBody ReqDeleteModule reqDeleteModule) throws ApplicationErrorException {
        Map<String,Object> map=new HashMap<>();
        List<String> userList=new ArrayList<>();
        Course course=null;
        try{
            //查询删除的模型是否是该课程的最后一个模型
            map=courseService.findModelCountByCouseId(reqDeleteModule.getCourseId());
            if(map!=null && map.size()>0){
                Integer ecount=Integer.parseInt(map.get("eCount").toString());
                if(ecount==1){
                    //判断最后一个实验编号是否和删除实验的编号相等
                    Integer eId=Integer.parseInt(map.get("eId").toString());
                    if(eId.intValue()==reqDeleteModule.getModuleId()){
                        //调用数列云 删除关系方法
                        userList=findShulieyunUserByCourse(reqDeleteModule.getCourseId());
                    }
                }
            }
           course =courseService.getCourseById(reqDeleteModule.getCourseId());
            moduleService.deleteModule(reqDeleteModule);
        }catch (ApplicationErrorException e){
            if(e.getMessage().equals(ErrorCode.ModuleNotExists.getErrorStringFormat())){
                return new ResponseMessage.Fail(ErrorCode.ModuleNotExists.getCode(),ErrorCode.ModuleNotExists.getErrorStringFormat());
            }else {
                throw e;
            }
        }
        if(userList!=null && userList.size()>0){
            map.put("userList",userList);
            map.put("courseName",course.getName());
            eduApi.delUser(map);
        }

        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "查找该课程名字是否存在", notes = "")
    @PostMapping(value = "course/module/moduleIsExist")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage moduleIsExist(@RequestBody Module module) throws ApplicationErrorException {
        return new ResponseMessage.Success(moduleService.moduleIsExist(module));
    }


    @ApiOperation(value = "删除实验", notes = "")
    @PostMapping(value = "/course/experiment/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteExperiment(@RequestBody ReqDeleteExperiment reqDeleteExperiment) throws ApplicationErrorException {
        Map<String,Object> map=new HashMap<>();
        List<String> userList=new ArrayList<>();
        Course course=null;
        try{
            //判断该实验是否是该课程的最后一个实验
            map=courseService.findExperimentCountByCouseId(reqDeleteExperiment.getCourseId());
            if(map!=null && map.size()>0){
                Integer ecount=Integer.parseInt(map.get("eCount").toString());
                if(ecount==1){
                    //判断最后一个实验编号是否和删除实验的编号相等
                    Integer eId=Integer.parseInt(map.get("eId").toString());
                    if(eId.intValue()==reqDeleteExperiment.getId()){
                        //调用数列云 删除关系方法
                       userList= findShulieyunUserByCourse(reqDeleteExperiment.getCourseId());
                    }
                }
            }
            course=courseService.getCourseById(reqDeleteExperiment.getCourseId());
            experimentService.deleteExperiment(reqDeleteExperiment);
            //判断该课程是否是数猎云课程(判断标准，判断是否存在数猎云类型的实验),不是数猎云课程但是数猎云中存在相关信息，那么删除相关信息

        }catch (ApplicationErrorException e){
            if(e.getMessage().equals(ErrorCode.ModuleNotExists.getErrorStringFormat())){
                return new ResponseMessage.Fail(ErrorCode.ModuleNotExists.getCode(),ErrorCode.ModuleNotExists.getErrorStringFormat());
            }
        }
        //解除与数列云的关系
        if(userList!=null && userList.size()>0){
            map=new HashMap();
            map.put("userList",userList);
            map.put("courseName",course.getName());
            eduApi.delUser(map);
        }


        return new ResponseMessage.Success();
    }


    /**
     * 根据课程查询相关用户
     * @param
     * @return
     * @throws ApplicationErrorException
     */

    public List<String> findShulieyunUserByCourse(int courseId){
        List<String> userList=new ArrayList<>();
        //查询该课程对应的学生
        //老师与课程关联解除 // TODO: 2018/5/16  
        List<String> studentList=courseService.findStuByCourseId(courseId);
        if(studentList!=null && studentList.size()>0){
            userList.addAll(studentList);
        }
        //查询该课程对应的老师 和班级里使用该课程对应的授课老师
        List<String> teacherList=courseService.findTeaByCourseId(courseId);
        if(teacherList!=null && teacherList.size()>0){
            userList.addAll(teacherList);
        }
        /*if(userList!=null && userList.size()>0){
            //根据课程编号查询课程名字
            Course course=null;
            try{
                 course=courseService.getCourseById(courseId);
            }catch (Exception e){
                e.printStackTrace();
            }
            Map<String,Object> map=new HashMap();
            map.put("userList",userList);
            map.put("courseName",course.getName());
            eduApi.delUser(map);
        }*/
        return userList;
    }





    @ApiOperation(value = "增加实验", notes = "")
    @PostMapping(value = "/course/module/experiment/creation")
    public ResponseMessage createExperiment(@RequestBody ReqExperiment reqExperiment) throws ApplicationErrorException {
        try{
            experimentService.createExperiment(reqExperiment);
        }catch (ApplicationErrorException e){
            if(e.getMessage().equals(ErrorCode.ModuleNotExists.getErrorStringFormat())){
                return new ResponseMessage.Fail(ErrorCode.ModuleNotExists.getCode(),ErrorCode.ModuleNotExists.getErrorStringFormat());
            }else {
                throw e;
            }
        }
        if(reqExperiment.getCloudwareType()==CloudwareTypeEnum.SHU_LIE_YUN.getCode()){
            //查询出该课程的名字
            Course course = experimentService.getCourseByModuleId(reqExperiment.getModuleId());
            if(course!=null){
                String courseName = course.getName();
                Map<String,Object> initCourseMap = new HashMap<>();
                initCourseMap.put("coursename",courseName);
                eduApi.initCourse(initCourseMap);
                List<Map<String,Object>> userList = new ArrayList<>();
                //查找出该课程下面的所有的人，包括老师和学生
                // 查找出老师相关信息
                /*User teacher = userService.getUserInfo(course.getTeacherId());
                if(teacher!=null) {
                    Teacher teacher1 = teacherService.getTeacherByUserId(course.getTeacherId());
                    Map<String, Object> teacherMap = new HashMap<>();
                    teacherMap.put("phone", teacher1.getTno());
                    teacherMap.put("password",teacher.getPassword2());
                    userList.add(teacherMap);
                }*/
                //查找出班级老师
                List<Clazz> clazzes = classService.selectByCourseId(course.getId());
                if(clazzes!=null && clazzes.size()>0){
                    for(Clazz clazz : clazzes){
                        Integer teacherId = clazz.getTeacherId();
                        User user = userService.getUserInfo(teacherId);
                        if(user!=null){
                            Map<String, Object> teacherMap = new HashMap<>();
                            teacherMap.put("phone", user.getUsername());
                            teacherMap.put("password",user.getPassword2());
                            userList.add(teacherMap);
                        }
                    }
                }
                //查询出所有的学生
                List<Map<String,Object>> studentList = studentService.getAllStudentByCourseId(course.getId());
                if(studentList!=null && studentList.size()>0){
                    userList.addAll(studentList);
                }
                Map<String,Object> addUserMap = new HashMap<>();
                addUserMap.put("coursename",course.getName());
                addUserMap.put("userList",userList);
                eduApi.addUser(addUserMap);
            }
        }
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "查询该实验名字是否存在", notes = "")
    @PostMapping(value = "/course/module/experiment/experimentExist")
    public ResponseMessage experimentIsExist(@RequestBody Experiment experiment) throws ApplicationErrorException {
        return new ResponseMessage.Success(experimentService.experimentIsExist(experiment));
    }

    @ApiOperation(value = "更新实验", notes = "")
    @PostMapping(value = "/course/module/experiment/updation")
    public ResponseMessage updateExperiment(@RequestBody ReqExperiment reqExperiment) throws ApplicationErrorException {
        experimentService.updateExperiment(reqExperiment);

        //判断该课程是否是数猎云课程(判断标准，判断是否存在数猎云类型的实验),不是数猎云课程但是数猎云中存在相关信息，那么删除相关信息
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "获取实验信息", notes = "")
    @PostMapping(value = "/course/module/experiment/getExperiment")
    public ResponseMessage<ResExperimentInfo> getExperimentInfo(@RequestParam int id) throws ApplicationErrorException {
        ResExperimentInfo info = experimentService.getExperiment(id);
        return new ResponseMessage.Success<>(info);
    }
    @ApiOperation(value = "获取课时图片资源", notes = "")
    @GetMapping(value = "/course/{moduleId}/lib")
    public ResponseMessage<ResModuleImages> getModuleImageUrls(@PathVariable("moduleId") int moduleId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(moduleService.getModuleImageUrls(moduleId));
    }

    @ApiOperation(value = "增加课时图片资源", notes = "")
    @PostMapping(value = "/module/lib/add")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage<Integer> addModuleResource(@RequestBody ReqAddModuleResource reqAddModuleResource) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(moduleService.addModuleResource(reqAddModuleResource));
    }

    @ApiOperation(value = "删除课时图片资源", notes = "")
    @PostMapping(value = "/module/lib/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteModuleResource(@RequestBody ReqDeleteModuleResource reqDeleteModuleResource) throws ApplicationErrorException {
        moduleService.deleteModuleResource(reqDeleteModuleResource);
        return new ResponseMessage.Success();
    }

//    @ApiOperation(value = "上传markdown文档", notes = "")
//    @PostMapping(value = "/course/experiment/markdown")
//    public ResponseMessage<String> uploadMarkdown(@RequestParam("file") MultipartFile file) throws ApplicationErrorException {
//        return new ResponseMessage.Success<>(fileService.uploadFile(file, ResourceTypeEnum.MARKDOWN));
//    }
//
//    @ApiOperation(value = "上传图片资源", notes = "")
//    @PostMapping(value = "/course/experiment/piclib")
//    public ResponseMessage<String> uploadImage(@RequestParam("file") MultipartFile file) throws ApplicationErrorException {
//        return new ResponseMessage.Success<>(fileService.uploadFile(file, ResourceTypeEnum.IMAGE));
//    }

    @ApiOperation(value = "批量导入学生数据", notes = "")
    @PostMapping(value = "/class/student/batchCreation")
    public ResponseMessage<ResBatchAddStudent> batchStudentCreation(@RequestParam("classId") int classId, @RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        //studentService.batchStudentCreation(classId, file);
        return new ResponseMessage.Success<ResBatchAddStudent>();
    }

    @ApiOperation(value = "批量导入教师数据", notes = "")
    @PostMapping(value = "/teacher/batchCreation")
    public ResponseMessage<ResBatchAddTeacher> batchTeacherCreation(@RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        return new ResponseMessage.Success<>(teacherService.batchTeacherCreation(file));
    }

    @ApiOperation(value="获取数猎场数据库信息")
    @PostMapping(value="/getDataShireDBList")
    public ResponseMessage<Map<String,Object>>  getDatashireDBList(@RequestParam("phone") String phone,
                                                                   @RequestParam("isAdmin") boolean isAdmin,
                                                                   @RequestParam("courseName")String courseName,
                                                                   @RequestParam("type") int type,
                                                                   @RequestParam(required = false) String paraSpaceName) {
        return new ResponseMessage.Success<>(eduApi.getDataShireDBList(phone,isAdmin,courseName,type,paraSpaceName));
    }

    @ApiOperation(value="获取数猎场文件信息")
    @PostMapping(value="/getDataShireFileList")
    public ResponseMessage<Map<String,Object>>  getDatashireFileList(@RequestParam("phone") String phone,
                                                                     @RequestParam("path")String path,
                                                                     @RequestParam(required = false)String paraSpaceName,
                                                                     @RequestParam("isAdmin") boolean isAdmin,
                                                                     @RequestParam("courseName") String courseName,
                                                                     @RequestParam("type") int type) {
        return new ResponseMessage.Success<>(eduApi.getDataShireFileList(phone,path,isAdmin,courseName,type,paraSpaceName));
    }

    @ApiOperation(value="获取项目的数猎场Hdfs文件信息")
    @PostMapping(value="/getProjectDataShireFileList")
    public ResponseMessage<Map<String,Object>>  getProjectDatashireFileList(@RequestParam("projectId") Integer projectId,@RequestParam("path")String path) {
        return new ResponseMessage.Success<>(eduApi.getProjectFileList(projectId,path));
    }

    @ApiOperation(value="勾选/取消可视化")
    @PostMapping(value="/operateTable")
    public ResponseMessage<String>  operateTable(@RequestParam String spaceName,@RequestParam String tableName,@RequestParam String operate,@RequestParam String phone) {
        return new ResponseMessage.Success<>(eduApi.operateTable(spaceName,tableName,Boolean.parseBoolean(operate),phone));
    }

    @ApiOperation(value="删除表")
    @PostMapping(value="/deleteTable")
    public ResponseMessage<String> deleteTable(@RequestParam String spaceName,@RequestParam String tableName,@RequestParam String phone,@RequestParam String courseName,@RequestParam boolean isAdmin,@RequestParam int type){
        return new ResponseMessage.Success<>(eduApi.deleteDbTable(spaceName,tableName,phone,courseName,isAdmin,type));
    }

    // deleteCloudHDFSFile
    @ApiOperation(value="删除文件")
    @PostMapping(value="/deleteCloudHDFSFile")
    public ResponseMessage<String> deleteFile(@RequestParam String spaceName,@RequestParam String fileName,@RequestParam String phone,@RequestParam String path,@RequestParam boolean isAdmin,@RequestParam String courseName,@RequestParam int type){
        return new ResponseMessage.Success<>(eduApi.deleteFile(spaceName,fileName,path,phone,isAdmin,courseName,type));
    }

    @ApiOperation(value="获取服务器时间")
    @PostMapping(value="/getServerTime")
    public ResponseMessage<Date> getServerTime(){
        return new ResponseMessage.Success<>(new Date());
    }


    @ApiOperation(value="获取课程相关信息")
    @PostMapping(value="/getCourseById")
    public ResponseMessage<Course> getCourseById(@RequestParam Integer courseId) throws ApplicationErrorException {
        Course course = new Course();
        course.setId(courseId);
        course = courseService.getCourseById(courseId);
        if(course != null){
            //查询出该课程是否存在非数猎云课程
            boolean flag = courseService.selectCourseExistOtherExperiment(courseId);
            course.setProgramExist(flag);
            //查找该课程是否存在数猎云课程
            boolean flag1 = courseService.selectCourseExistShulieyun(courseId);
            course.setShulieyunExist(flag1);
        }
        return new ResponseMessage.Success<>(course);
    }

    @ApiOperation(value="获取课程相关信息")
    @PostMapping(value="/getCourseByCourseName")
    public ResponseMessage<Course> getCourseByCourseName(@RequestParam String courseName) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.selectByCourseName(courseName));
    }
    @ApiOperation(value="获取非本班级的学生名单")
    @PostMapping(value="/getStudentByNotClass")
    public ResponseMessage<ResClassStudents> getStudentByNotClass(@RequestParam Integer classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(classService.getStudentByNotThisClass(classId));
    }


    @ApiOperation(value="删除班级的学生")
    @PostMapping(value="/deleteStudentByClass")
    public ResponseMessage<ResClassStudents> deleteStudentByClass(@RequestParam Integer classId,@RequestParam Integer userId) throws ApplicationErrorException {
        Student student = studentService.selectByUserId(userId);
        //判断student_exper是否有数据
       /* List<ReqStudentExperiment> studentExperiments=studentExperimentService.getStudentAllExperiments(userId);
        if(studentExperiments!=null && studentExperiments.size()>0){
            for(ReqStudentExperiment reqStudentExperiment:studentExperiments){
                studentExperimentService.deleteStudentExperiment(reqStudentExperiment.getExperimentId(),
                        reqStudentExperiment.getStudentId());
            }
        }*/
        //删除班级和学生表关联
        ReqDeleteClassStudent reqDeleteClassStudent=new ReqDeleteClassStudent();
        reqDeleteClassStudent.setStudentId(userId);
        reqDeleteClassStudent.setClassId(classId);
        classService.deleteClassStudent(reqDeleteClassStudent,0);
        //删除学生成绩的关联表
        jobScoreService.deleteBystuIdAndCidAndCouId(classId,userId);
        if(student!=null){
            List<Integer> courseIdList=new ArrayList();
            //查询这个班级所关联的所有课程
            List<ClassCourse> classCourseList=classCourseService.findClassCourseByClassId(classId);
            if(classCourseList!=null && classCourseList.size()>0){
                for(ClassCourse classCourse:classCourseList){
                    courseIdList.add(classCourse.getCourseId());
                }
            }
            if(courseIdList!=null && courseIdList.size()>0){
                for(Integer courseId:courseIdList){
                    //判断课程是和数列云有关系
                    boolean isShuliuyun=courseService.selectCourseExistShulieyun(courseId);
                    if(isShuliuyun){
                        //判断这个学生在其他班级里是否有这个课程
                        Map map=new HashMap();
                        map.put("classId",reqDeleteClassStudent.getClassId());
                        map.put("courseId",courseId);
                        map.put("userId",reqDeleteClassStudent.getStudentId());
                        int exists=studentService.selectExistsCourseByClassIdAndCourseIdAndStuId(map);
                        if(exists==0){
                            //同步接口
                            Course course=courseService.getCourseById(courseId);
                            Map<String,Object> objectMap = new HashMap<>();
                            List<String> userList = new ArrayList<>();
                            userList.add(student.getSno());
                            objectMap.put("userList",userList);
                            objectMap.put("courseName",course.getName());
                            eduApi.delUser(objectMap);
                        }
                    }


                }
            }

        }
        return new ResponseMessage.Success();
    }


    @ApiOperation(value="获取班级的所有课程")
    @PostMapping(value="/getCourseByClassId")
    public ResponseMessage<ResTeacherClassList> getCourseByClassId(@RequestParam Integer classId) throws ApplicationErrorException {
        ResTeacherClassList teacherClassList=new ResTeacherClassList();
        //查询这个班级所关联的所有课程
        List<ResClassDetail> classDetailList=classCourseService.findClassDetailByClassId(classId);
       teacherClassList.setTeacherClassList(classDetailList);
        return new ResponseMessage.Success<>(teacherClassList);
    }
    @ApiOperation(value="批量添加班级学生")
    @PostMapping(value="/class/addStudentList")
    public ResponseMessage addStudentList(@RequestBody ReqAddClassStudent[] addClassStuList) throws ApplicationErrorException {
            if(addClassStuList!=null && addClassStuList.length>0){
                for(ReqAddClassStudent reqAddClassStudent:addClassStuList){
                    addClassStudent(reqAddClassStudent);
                }
            }
        return new ResponseMessage.Success<>();
    }

    @ApiOperation(value="批量删除班级学生")
    @PostMapping(value="/class/deleteStudentList")
    public ResponseMessage deleteStudentList(@RequestBody ReqDeleteClassStudent[] deleteClassStudents) throws ApplicationErrorException {
        if(deleteClassStudents!=null && deleteClassStudents.length>0){
            for(ReqDeleteClassStudent reqAddClassStudent:deleteClassStudents){
                deleteStudentByClass(reqAddClassStudent.getClassId(),reqAddClassStudent.getStudentId());
            }
        }
        return new ResponseMessage.Success<>();
    }
    @ApiOperation(value="批量添加班级课程")
    @PostMapping(value="/class/addClassCourseList")
    public ResponseMessage addClassCourseList(@RequestBody ReqAddClassCourse[] reqAddClassCourses) throws ApplicationErrorException {
        if(reqAddClassCourses!=null && reqAddClassCourses.length>0){
            for(ReqAddClassCourse reqAddClassCourse:reqAddClassCourses){
                classService.addClassCourse(reqAddClassCourse);
            }
        }
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value="批量删除班级课程")
    @PostMapping(value="/class/delClassCourseList")
    public ResponseMessage deleteClassCourseList(@RequestBody ReqDeleteClassCourse[] reqDeleteClassCourses) throws ApplicationErrorException {
        if(reqDeleteClassCourses!=null && reqDeleteClassCourses.length>0){
            for(ReqDeleteClassCourse reqDeleteClassCourse:reqDeleteClassCourses){
                classService.deleteClassCourse(reqDeleteClassCourse);
            }
        }
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value="调整moduleOrder")
    @PostMapping(value="/module/adjustModuleOrder")
    public ResponseMessage<Map> adjustModuleOrder(@RequestBody ReqAdjustModuleOrderClass reqEntity) throws ApplicationErrorException {
        Map<String,Object> result = moduleService.adjustModuleOrder(reqEntity);
        return new ResponseMessage.Success<>(result);
    }

    @ApiOperation(value="调整experimentOrder")
    @PostMapping(value="/experiment/adjustExperimentOrder")
    public ResponseMessage<Map> adjustExperimentOrder(@RequestBody ReqAdjustExperimentOrderClass reqEntity) throws ApplicationErrorException {
        Map<String,Object> resultMap = experimentService.adjustExperimentOrder(reqEntity);
        return new ResponseMessage.Success<>(resultMap);
    }


    @ApiOperation(value = "获取系统key信息", notes = "只生成一次")
    @GetMapping(value = "/getKey")
    public ResponseMessage<Map> getSysKey() throws ApplicationErrorException {
        String key = sysLicenseService.getSysKey();
        Map<String,Object> result = new HashMap<>();
        result.put("key",key);
        return new ResponseMessage.Success<>(result);
    }


    @ApiOperation(value = "是否导入了license", notes = "如果没有导入license则返回错误码，否则返回限制信息")
    @GetMapping(value = "/isImportLicense")
    public ResponseMessage<Map> isImportLicense() throws ApplicationErrorException {
        Map<String,Object> result = sysLicenseService.decodeLicense();
        return new ResponseMessage.Success<>(result);
    }


    @ApiOperation(value = "导入或更新license")
    @PostMapping(value = "/importOrUpdateLicense")
    public ResponseMessage<Map> importOrUpdateLicense(@RequestBody ReqImportOrUpdateLicense license) throws
            ApplicationErrorException{
        Map<String,Object> map = sysLicenseService.importOrUpdateLicense(license);
        return new ResponseMessage.Success<>(map);
    }
    @ApiOperation(value = "判断课程是否有答案")
    @PostMapping(value = "/course/isAnswer")
    public ResponseMessage<Boolean> getCourseIsAnswer(@RequestParam Integer experimentId) throws
            ApplicationErrorException{
        Experiment experiment=experimentService.findExpById(experimentId);
        if(experiment!=null){
            if(experiment.getIsAnswer()==1){
                return new ResponseMessage.Success<>(true);
            }else{
                return new ResponseMessage.Success<>(false);
            }
        }
        return null;
    }

    @ApiOperation(value = "获取连接信息")
    @PostMapping(value = "/getPropertiesValue")
    public ResponseMessage<String> getPropertiesValue(@RequestParam String parameter) throws ApplicationErrorException{
        String value=environment.getProperty(parameter);
        return new ResponseMessage.Success<>(value);
    }



}


