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



    @ApiOperation(value = "????????????????????????", notes = "")
    @GetMapping(value = "teacher/all")
    public ResponseMessage<ResTeacherList> getAllTeachers() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(teacherService.getAllTeacherList());
    }

    @ApiOperation(value = "??????????????????????????????", notes = "")
    @PostMapping(value = "teacher/byLimit")
    public ResponseMessage<ResTeacherList> getTeacherByLimit(@RequestBody Map<String,Object> map) throws ApplicationErrorException {
        int currentPage = Integer.parseInt(map.get("currentPage")+"");
        int limit = Integer.parseInt(map.get("limit")+"");
        return new ResponseMessage.Success<>(teacherService.getAllTeachersBylimit(currentPage,limit));
    }

    @ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "teacher/creation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage createTeacher(@RequestBody ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException {
        teacherService.createTeacher(reqUpdateTeacher);
        return new ResponseMessage.Success();
    }


    @ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "teacher/updation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage updateTeacher(@RequestBody ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException {
        //??????????????????????????????????????????????????????(???????????????????????????????????????????????????)
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????addUser??????
        Teacher aleadyTeacher = teacherService.getTeacherByUserId(reqUpdateTeacher.getId());
        User user = userService.getUserInfo(reqUpdateTeacher.getId());
        String aleadyNo = aleadyTeacher.getTno();
        boolean flag = teacherService.checkTnoIsExist(reqUpdateTeacher);
        teacherService.updateTeacher(reqUpdateTeacher);

       /* if(!flag) {
            //?????????????????????
            List<Map<String,Object>> delUserList = new ArrayList<>();
            List<Map<String,Object>> addUserList = new ArrayList<>();
            int teacher_id = reqUpdateTeacher.getId();
            int cloudware_type = CloudwareTypeEnum.SHU_LIE_YUN.getCode();
            List<Map<String,Object>> teacherList = teacherService.selectTeacherIsShulieyunByUserId(cloudware_type, teacher_id);
            if (teacherList != null && teacherList.size() > 0) {
                //??????????????????
                Map<String,Object> delMap = new HashMap<>();
                List<String> delList = new ArrayList<>();
                delList.add(aleadyNo);
                delMap.put("userList",delList);
                delUserList.add(delMap);

                for(Map<String,Object> teacherMap : teacherList){
                    //???????????????
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

                //?????????????????????,??????
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

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "teacher/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteTeacher(@RequestBody ReqDeleteTeacher reqDeleteTeacher) throws ApplicationErrorException {
        //??????????????????????????????????????????????????????????????????????????????????????????????????????course_teacher????????????????????????
        // ???????????????????????????????????????????????????
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
        //????????????????????????????????????????????????
        /*flag = courseService.isTeacherUsedByCourse(reqDeleteTeacher.getTeacherId());
        if(flag){
            return new ResponseMessage.Fail(ErrorCode.TeacherIsUsedByCourse.getCode(),ErrorCode.TeacherIsUsedByCourse.getErrorStringFormat());
        }*/
        //??????student_exper?????????????????????
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
                //??????????????????
                Map<String, Object> delMap = new HashMap<>();
                List<String> userList = new ArrayList<>();
                userList.add(teacher.getTno());
                delMap.put("userList", userList);
                delMap.put("courseName","");
                eduApi.delUser(delMap);
            }
        }
        teacherService.deleteTeacherByTeacherUserId(reqDeleteTeacher);

        //????????????????????????
        projectUserService.deleteByUserId(reqDeleteTeacher.getTeacherId());
        return new ResponseMessage.Success();
    }

   /* @ApiOperation(value = "????????????????????????", notes = "")
    @GetMapping(value = "semester/all")
    public ResponseMessage<ResSemesterList> getAllSemesters() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(termService.getAllTerms());
    }

    @ApiOperation(value = "????????????", notes = "")
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

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "semester/updation")
    public ResponseMessage updateSemester(@RequestBody Term term) throws ApplicationErrorException {
        termService.update(term);
        return new ResponseMessage.Success();
    }
    @ApiOperation(value = "????????????????????????", notes = "")
    @PostMapping(value = "semester/findTearmById")
    public ResponseMessage<Term> findTearmById(@RequestParam int tearmId) throws ApplicationErrorException {
        return new ResponseMessage.Success(termService.getTermById(tearmId));
    }

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "semester/deletion")
    public ResponseMessage deleteSemester(@RequestBody ReqDeleteSemester reqDeleteSemester) throws ApplicationErrorException {
        termService.delete(reqDeleteSemester);
        return new ResponseMessage.Success();
    }*/

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "class/homework/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteHomework(@RequestBody ReqDeleteHomework reqDeleteHomework) throws ApplicationErrorException {
        homeworkService.deleteHomework(reqDeleteHomework);
        return new ResponseMessage.Success();
    }


    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "class/homework/updation")
    public ResponseMessage updateHomework(@RequestBody ReqUpdateHomework reqUpdateHomework) throws ApplicationErrorException {
        homeworkService.updateHomework(reqUpdateHomework);
        //????????????????????????????????????????????????????????????????????????????????????????????????
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "class/homework/creation")
    public ResponseMessage createHomework(@RequestBody ReqCreateHomework reqCreateHomework) throws ApplicationErrorException {
        homeworkService.createHomework(reqCreateHomework);
        //?????????????????????????????????????????????
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "??????????????????", notes = "")
    @GetMapping(value = "/class/{classId}/homework")
    public ResponseMessage<ResClassHomework> getClassHomework(@PathVariable("classId") int classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(homeworkService.getClassHomework(classId));
    }

    @ApiOperation(value = "??????????????????", notes = "")
    @GetMapping(value = "course/all")
    public ResponseMessage<ResCourseList> getAllCourses() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getAllCourses());
    }

    @ApiOperation(value = "??????????????????", notes = "")
    @GetMapping(value = "course/allWithOutCondition")
    public ResponseMessage<List<Course>> selectAllCourses(){
        return new ResponseMessage.Success<>(courseService.selectAllCourses());
    }

    @ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "course/creation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage createCourse(@RequestBody ReqAddCourse reqAddCourse) throws ApplicationErrorException {
        //???????????????????????????????????????
        courseService.createCourse(reqAddCourse);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "??????????????????", notes = "")
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

    @ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "course/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteCourse(@RequestBody ReqDeleteCourse reqDeleteCourse) throws ApplicationErrorException {
        try{
            //??????????????????????????????????????????
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
            //???????????????????????????
            if(userList!=null && userList.size()>0){
                Map<String,Object> map=new HashMap();
                map.put("userList",userList);
                map.put("courseName",course.getName());
                eduApi.delUser(map);
            }

            if(isShulieyun){
                //???????????????????????????
                String result = eduApi.deleteSpaceRelevantData(course.getName(),1);
                if(result.equals("error")){
                    log.error("??????????????????????????????????????????");
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

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @PostMapping(value = "/class/student/deletion")
    public ResponseMessage deleteClassStudent(@RequestBody
    ReqDeleteClassStudent reqDeleteClassStudent) throws ApplicationErrorException {
        Student student = studentService.selectByUserId(reqDeleteClassStudent.getStudentId());
        //??????student_exper?????????????????????
        List<ReqStudentExperiment> studentExperiments=studentExperimentService.getStudentAllExperiments(reqDeleteClassStudent.getStudentId());
        if(studentExperiments!=null && studentExperiments.size()>0){
            for(ReqStudentExperiment reqStudentExperiment:studentExperiments){
                studentExperimentService.deleteStudentExperiment(reqStudentExperiment.getExperimentId(),
                        reqStudentExperiment.getStudentId());
            }
        }
        classService.deleteClassStudent(reqDeleteClassStudent,0);
        //???????????????????????????????????????????????????
        Map map=new HashMap();
        map.put("classId",reqDeleteClassStudent.getClassId());
        map.put("courseId",reqDeleteClassStudent.getCourseId());
        map.put("userId",reqDeleteClassStudent.getStudentId());
       int exists=studentService.selectExistsCourseByClassIdAndCourseIdAndStuId(map);
        if(exists==0){
            //????????????
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

    @ApiOperation(value = "????????????", notes = "")
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
                //?????????????????????????????????????????????
                User paraUser = new User();
                paraUser.setTokenCreateTime(-1l);//????????????????????????-1
                paraUser.setIsRemind(user.getIsRemind());
                paraUser.setId(reqResetPassword.getUserId());
                paraUser.setToken(user.getToken());
                userService.updateByPrimaryKey2(paraUser);//??????token???????????????
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

    @ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "/class/student/updation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage updateStudentInfo(@RequestBody ReqUpdateStudent reqUpdateStudent) throws ApplicationErrorException {
        studentService.updateStudentInfo(reqUpdateStudent);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "??????????????????", notes = "")
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
        //??????????????????????????????????????????????????????????????????????????????
        boolean flag = classService.selectIsShulieyunCourseByClassId(reqAddClassStudent.getClassId(),CloudwareTypeEnum.SHU_LIE_YUN.getCode());
        if(flag){
            //?????????????????????
            String phone = reqAddClassStudent.getStudentNo();
            String password = null;
            String courseName = null;
            Student student = studentService.getStudentByNo(reqAddClassStudent.getStudentNo());
            if(student!=null){
                User user = userService.getUserInfo(student.getUserId());
                if(user!=null){
                    password = user.getPassword2();
                    //?????????????????????
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

    @ApiOperation(value = "??????????????????????????????", notes = "")
    @GetMapping(value = "class/{classId}/students/all")
    public ResponseMessage<ResClassStudents> getAllClassStudentInfo(@PathVariable("classId") int classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(classService.getAllClassStudentInfo(classId));
    }

    @ApiOperation(value = "????????????????????????", notes = "")
    @GetMapping(value = "/students/all")
    public ResponseMessage<ResClassStudents> getAllStudentInfo() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(studentService.getAllStudentInfo());
    }

    @ApiOperation(value = "????????????", notes = "")
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
    @ApiOperation(value = "????????????", notes = "")
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
    @ApiOperation(value = "????????????????????????", notes = "")
    @PostMapping(value = "/student/batchCreation")
    public ResponseMessage<ResBatchCreateStudent> batchStudentCreation(@RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        return new ResponseMessage.Success<ResBatchCreateStudent>(studentService.batchCreateStudent( file));
    }
    @ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "/student/delete")
    public ResponseMessage deleteStudent(@RequestParam("studentNo") String studentNo) throws ApplicationErrorException, IOException {
        Student student = studentService.getStudentByNo(studentNo);
        if(student!=null){
            //????????????????????????????????????
            List<StudentClass> studentClassList=studentService.getAllStudentClassByUserId(student.getUserId());
            TProjectUser projectUser = new TProjectUser();
            projectUser.setUserId(student.getUserId());
            projectUser.setIsManager(1);
            List<TProjectUser> projectUsers = projectUserService.selectSelective(projectUser);
            if(studentClassList!=null && studentClassList.size()>0){
                throw new  ApplicationErrorException(ErrorCode.StudentIsUsedByClass);
               /*
               for(StudentClass studentClass:studentClassList){
                    //?????????????????????????????????
                    Map<String,Object> map=new HashMap<>();
                    map.put("classId",studentClass.getClassId());
                    map.put("studentId",studentClass.getStudentId());
                   List<Course> courses=courseService.findCourseByStuId(map);
                   if(courses!=null && courses.size()>0){
                       //?????????????????????????????????
                       List<String> userList=new ArrayList<>();
                       for(Course course:courses){
                           ReqDeleteClassStudent deleteClassStudent=new ReqDeleteClassStudent();
                           deleteClassStudent.setClassId(studentClass.getClassId());
                           deleteClassStudent.setCourseId(course.getId());
                           deleteClassStudent.setStudentId(student.getUserId());
                           //??????student_class,student,user ???????????????
                           classService.deleteClassStudent(deleteClassStudent,1);
                           //?????????????????? ???????????????????????????
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
                //??????student_exper???????????????
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

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "class/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteClass(@RequestBody ReqDeleteClass reqDeleteClass) throws ApplicationErrorException {
        classService.deleteClass(reqDeleteClass);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "class/updation")
    public ResponseMessage updateClassInfo(@RequestBody ReqUpdateClass reqUpdateClass) throws ApplicationErrorException {
            String className=reqUpdateClass.getClassName();
            if(className!=null && !className.equals("")){
                //?????????????????????????????????
                Clazz clazz=classService.findClassByNameAndCId(reqUpdateClass);
                if(clazz!=null){
                    throw new ApplicationErrorException(ErrorCode.ClassAlreadyExisted);
                }
                classService.updateClassInfo(reqUpdateClass);
            }
        return new ResponseMessage.Success();
    }



    /*@ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "class/updation")
    public ResponseMessage updateClassInfo(@RequestBody ReqUpdateClass reqUpdateClass) throws ApplicationErrorException {
        //??????????????????????????????????????????????????????
        int courseId = reqUpdateClass.getCourseId();
        //????????????????????????????????????
        Course nowCourse = courseService.getCourseById(courseId);
        // ????????????????????? ??????????????????
        Clazz clazz = classService.getClassById(reqUpdateClass.getClassId());
        //?????????????????????????????????
        Course oldCourse=courseService.getCourseById(clazz.getCourseId());
        User oldUser = userService.getUserInfo(clazz.getTeacherId());
        User newUser = userService.getUserInfo(reqUpdateClass.getTeacherId());
        String oldCourseName = null;
        if(oldCourse!=null){
            oldCourseName = oldCourse.getName();
        }
        boolean isSynchronized = courseService.selectCourseExistShulieyun(oldCourse.getId());
        //????????????????????????
        ResClassStudents classStudents = classService.getAllClassStudentInfo(reqUpdateClass.getClassId());
        classService.updateClassInfo(reqUpdateClass);
        List<Map<String, Object>> addUserList = new ArrayList<>();
        if(isSynchronized) {
            List<Integer> userIdList = new ArrayList<>();
            int isUpdateTeacher=0;
            if(clazz.getTeacherId().intValue()!=reqUpdateClass.getTeacherId()){
                //????????????????????????????????????????????????????????????
                Map<String,Object> map=new HashMap<>();
                map.put("classId",reqUpdateClass.getClassId());
                map.put("teacherId",clazz.getTeacherId());
                map.put("courseId",oldCourse.getId());
                int teacherCount=classService.findSlyByTeaIdAndClassIdAndCourseId(map);
                if(teacherCount==0){
                    //??????????????? ????????????????????????????????????????????? ??????????????????????????????????????????????????????
                    Course teaCourse=courseService.getCourseById(oldCourse.getId());
                    if(teaCourse!=null && teaCourse.getTeacherId().intValue()!=clazz.getTeacherId().intValue()){
                        //???????????????????????????????????????
                        userIdList.add(clazz.getTeacherId());
                        isUpdateTeacher=1;
                    }
                }
                //?????????????????????????????????????????????????????????
                if(newUser!=null) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("phone", newUser.getUsername());
                    userMap.put("password",newUser.getPassword2());
                    addUserList.add(userMap);
                }
            }
            //????????????????????????
            if(nowCourse.getId().intValue()!=oldCourse.getId().intValue()){
                //??????????????????????????????????????????
                Map<String,Object> selectMap=new HashMap<>();
                selectMap.put("classId",clazz.getId());
                selectMap.put("courseId",oldCourse.getId());
                ClassCourse classCourse=classCourseService.findClassCourseByClassIdAndCourseId(selectMap);
                if(classCourse!=null){
                    //??????????????????????????????
                    classCourse.setCourseId(nowCourse.getId());
                    classCourseService.updateClassCourseByClassIdAndCourseId(classCourse);
                }
                if(classStudents != null){
                    if(classStudents.getStudentList()!=null && classStudents.getStudentList().size()>0){
                        for(ResClassStudents.ResClassStudent resClassStudent:classStudents.getStudentList()){
                            //????????????????????????????????????????????????
                            Map<String,Object> map=new HashMap<>();
                            map.put("classId",reqUpdateClass.getClassId());
                            map.put("studentId",resClassStudent.getId());
                            map.put("courseId",oldCourse.getId());
                            //????????????????????????????????????????????????????????????
                            int count=studentService.findSlyByNotClassAndStuId(map);
                            if(count>0){
                                continue;
                            }
                            userIdList.add(resClassStudent.getId());
                        }
                    }
                }
                //?????????????????????????????????????????????????????????  ???????????? ??????????????????
                if(isUpdateTeacher==0){
                    //??????????????????????????????????????????????????????
                    Map<String,Object> map=new HashMap<>();
                    map.put("classId",reqUpdateClass.getClassId());
                    map.put("teacherId",clazz.getTeacherId());
                    map.put("courseId",oldCourse.getId());
                    int teacherCount=classService.findSlyByTeaIdAndClassIdAndCourseId(map);
                    if(teacherCount==0){
                        //??????????????? ????????????????????????????????????????????? ??????????????????????????????????????????????????????
                        Course teaCourse=courseService.getCourseById(oldCourse.getId());
                        if(teaCourse!=null && teaCourse.getTeacherId().intValue()!=clazz.getTeacherId().intValue()){
                            //???????????????????????????????????????
                            userIdList.add(clazz.getTeacherId());
                        }
                    }
                }
                //???????????????????????????????????????????????????
                boolean isShulieyun = courseService.selectCourseExistShulieyun(nowCourse.getId());
                if(isShulieyun){
                      //????????????
                    if(classStudents != null){
                        for(ResClassStudents.ResClassStudent resClassStudent : classStudents.getStudentList()) {
                            //?????????????????????????????????????????????
                            User stuUser=userService.getUserInfo(resClassStudent.getStudentNo());
                            if(stuUser!=null){
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("phone", resClassStudent.getStudentNo());
                                userMap.put("password", stuUser.getPassword2());
                                addUserList.add(userMap);
                            }

                        }
                    }
                    //??????????????????????????? ?????????????????????????????????????????????????????????
                    if(isUpdateTeacher==0){
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("phone", oldUser.getUsername());
                        userMap.put("password", oldUser.getPassword2());
                        addUserList.add(userMap);
                    }
                }
            }
            //???????????????????????????????????????
            if(userIdList!=null && userIdList.size()>0){
                //???????????????
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
             *  ??????????????????????????????  ????????????????????????  ????????????????????????????????????
             *  ???????????? ????????????????????????
             *//*
            if(nowCourse.getId().intValue()!=oldCourse.getId().intValue()){
                //??????????????????????????????????????????
                Map<String,Object> selectMap=new HashMap<>();
                selectMap.put("classId",clazz.getId());
                selectMap.put("courseId",oldCourse.getId());
                ClassCourse classCourse=classCourseService.findClassCourseByClassIdAndCourseId(selectMap);
                if(classCourse!=null){
                    //??????????????????????????????
                    classCourse.setCourseId(nowCourse.getId());
                    classCourseService.updateClassCourseByClassIdAndCourseId(classCourse);
                }
                boolean isShulieyun = courseService.selectCourseExistShulieyun(nowCourse.getId());
                if(isShulieyun){
                    //????????????
                    if(classStudents != null){
                        for(ResClassStudents.ResClassStudent resClassStudent : classStudents.getStudentList()) {
                            //?????????????????????????????????????????????
                            User stuUser=userService.getUserInfo(resClassStudent.getStudentNo());
                            if(stuUser!=null){
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("phone", resClassStudent.getStudentNo());
                                userMap.put("password", stuUser.getPassword2());
                                addUserList.add(userMap);
                            }
                        }
                    }
                    //??????????????????????????? ?????????????????????????????????????????????????????????
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
        //?????????????????????
        if(addUserList.size()>0) {
            Map<String, Object> addUserMap = new HashMap<>();
            //??????????????????  ?????????????????????????????????????????????
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

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "class/creation")
    public ResponseMessage createClass(@RequestBody ReqAddClass reqAddClass) throws ApplicationErrorException {
        classService.createClass(reqAddClass);
        //?????????????????????????????????????????????????????????????????????????????????
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
                //??????????????????
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

    @ApiOperation(value = "????????????????????????", notes = "")
    @GetMapping(value = "class/all")
    public ResponseMessage<ResClassInfos> getAllClassInfo() throws ApplicationErrorException {
        return new ResponseMessage.Success<>(classService.getAllClassInfo());
    }

    @ApiOperation(value = "????????????????????????", notes = "")
    @GetMapping(value = "course/{courseId}/experiments")
    public ResponseMessage<ResCourseModuleExperiments> getCourseExperiments(@PathVariable int courseId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getCourseModuleExperiments(courseId));
    }

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "course/module/getModule")
    public ResponseMessage<Module> getModule(@RequestBody Module module) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(moduleService.getModuleInfo(module));
    }


    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "course/module/creation")
    public ResponseMessage<ResModuleId> createModule(@RequestBody Module module) throws ApplicationErrorException {
        ResModuleId resModuleId = moduleService.createModule(module);
        return new ResponseMessage.Success<>(resModuleId);
    }

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "course/module/update")
    public ResponseMessage updateModule(@RequestBody Module module) throws ApplicationErrorException {
        moduleService.updateModule(module);
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "course/module/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteModule(@RequestBody ReqDeleteModule reqDeleteModule) throws ApplicationErrorException {
        Map<String,Object> map=new HashMap<>();
        List<String> userList=new ArrayList<>();
        Course course=null;
        try{
            //????????????????????????????????????????????????????????????
            map=courseService.findModelCountByCouseId(reqDeleteModule.getCourseId());
            if(map!=null && map.size()>0){
                Integer ecount=Integer.parseInt(map.get("eCount").toString());
                if(ecount==1){
                    //??????????????????????????????????????????????????????????????????
                    Integer eId=Integer.parseInt(map.get("eId").toString());
                    if(eId.intValue()==reqDeleteModule.getModuleId()){
                        //??????????????? ??????????????????
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

    @ApiOperation(value = "?????????????????????????????????", notes = "")
    @PostMapping(value = "course/module/moduleIsExist")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage moduleIsExist(@RequestBody Module module) throws ApplicationErrorException {
        return new ResponseMessage.Success(moduleService.moduleIsExist(module));
    }


    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "/course/experiment/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteExperiment(@RequestBody ReqDeleteExperiment reqDeleteExperiment) throws ApplicationErrorException {
        Map<String,Object> map=new HashMap<>();
        List<String> userList=new ArrayList<>();
        Course course=null;
        try{
            //??????????????????????????????????????????????????????
            map=courseService.findExperimentCountByCouseId(reqDeleteExperiment.getCourseId());
            if(map!=null && map.size()>0){
                Integer ecount=Integer.parseInt(map.get("eCount").toString());
                if(ecount==1){
                    //??????????????????????????????????????????????????????????????????
                    Integer eId=Integer.parseInt(map.get("eId").toString());
                    if(eId.intValue()==reqDeleteExperiment.getId()){
                        //??????????????? ??????????????????
                       userList= findShulieyunUserByCourse(reqDeleteExperiment.getCourseId());
                    }
                }
            }
            course=courseService.getCourseById(reqDeleteExperiment.getCourseId());
            experimentService.deleteExperiment(reqDeleteExperiment);
            //???????????????????????????????????????(?????????????????????????????????????????????????????????),????????????????????????????????????????????????????????????????????????????????????

        }catch (ApplicationErrorException e){
            if(e.getMessage().equals(ErrorCode.ModuleNotExists.getErrorStringFormat())){
                return new ResponseMessage.Fail(ErrorCode.ModuleNotExists.getCode(),ErrorCode.ModuleNotExists.getErrorStringFormat());
            }
        }
        //???????????????????????????
        if(userList!=null && userList.size()>0){
            map=new HashMap();
            map.put("userList",userList);
            map.put("courseName",course.getName());
            eduApi.delUser(map);
        }


        return new ResponseMessage.Success();
    }


    /**
     * ??????????????????????????????
     * @param
     * @return
     * @throws ApplicationErrorException
     */

    public List<String> findShulieyunUserByCourse(int courseId){
        List<String> userList=new ArrayList<>();
        //??????????????????????????????
        //??????????????????????????? // TODO: 2018/5/16  
        List<String> studentList=courseService.findStuByCourseId(courseId);
        if(studentList!=null && studentList.size()>0){
            userList.addAll(studentList);
        }
        //?????????????????????????????? ????????????????????????????????????????????????
        List<String> teacherList=courseService.findTeaByCourseId(courseId);
        if(teacherList!=null && teacherList.size()>0){
            userList.addAll(teacherList);
        }
        /*if(userList!=null && userList.size()>0){
            //????????????????????????????????????
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





    @ApiOperation(value = "????????????", notes = "")
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
            //???????????????????????????
            Course course = experimentService.getCourseByModuleId(reqExperiment.getModuleId());
            if(course!=null){
                String courseName = course.getName();
                Map<String,Object> initCourseMap = new HashMap<>();
                initCourseMap.put("coursename",courseName);
                eduApi.initCourse(initCourseMap);
                List<Map<String,Object>> userList = new ArrayList<>();
                //???????????????????????????????????????????????????????????????
                // ???????????????????????????
                /*User teacher = userService.getUserInfo(course.getTeacherId());
                if(teacher!=null) {
                    Teacher teacher1 = teacherService.getTeacherByUserId(course.getTeacherId());
                    Map<String, Object> teacherMap = new HashMap<>();
                    teacherMap.put("phone", teacher1.getTno());
                    teacherMap.put("password",teacher.getPassword2());
                    userList.add(teacherMap);
                }*/
                //?????????????????????
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
                //????????????????????????
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

    @ApiOperation(value = "?????????????????????????????????", notes = "")
    @PostMapping(value = "/course/module/experiment/experimentExist")
    public ResponseMessage experimentIsExist(@RequestBody Experiment experiment) throws ApplicationErrorException {
        return new ResponseMessage.Success(experimentService.experimentIsExist(experiment));
    }

    @ApiOperation(value = "????????????", notes = "")
    @PostMapping(value = "/course/module/experiment/updation")
    public ResponseMessage updateExperiment(@RequestBody ReqExperiment reqExperiment) throws ApplicationErrorException {
        experimentService.updateExperiment(reqExperiment);

        //???????????????????????????????????????(?????????????????????????????????????????????????????????),????????????????????????????????????????????????????????????????????????????????????
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "??????????????????", notes = "")
    @PostMapping(value = "/course/module/experiment/getExperiment")
    public ResponseMessage<ResExperimentInfo> getExperimentInfo(@RequestParam int id) throws ApplicationErrorException {
        ResExperimentInfo info = experimentService.getExperiment(id);
        return new ResponseMessage.Success<>(info);
    }
    @ApiOperation(value = "????????????????????????", notes = "")
    @GetMapping(value = "/course/{moduleId}/lib")
    public ResponseMessage<ResModuleImages> getModuleImageUrls(@PathVariable("moduleId") int moduleId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(moduleService.getModuleImageUrls(moduleId));
    }

    @ApiOperation(value = "????????????????????????", notes = "")
    @PostMapping(value = "/module/lib/add")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage<Integer> addModuleResource(@RequestBody ReqAddModuleResource reqAddModuleResource) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(moduleService.addModuleResource(reqAddModuleResource));
    }

    @ApiOperation(value = "????????????????????????", notes = "")
    @PostMapping(value = "/module/lib/deletion")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteModuleResource(@RequestBody ReqDeleteModuleResource reqDeleteModuleResource) throws ApplicationErrorException {
        moduleService.deleteModuleResource(reqDeleteModuleResource);
        return new ResponseMessage.Success();
    }

//    @ApiOperation(value = "??????markdown??????", notes = "")
//    @PostMapping(value = "/course/experiment/markdown")
//    public ResponseMessage<String> uploadMarkdown(@RequestParam("file") MultipartFile file) throws ApplicationErrorException {
//        return new ResponseMessage.Success<>(fileService.uploadFile(file, ResourceTypeEnum.MARKDOWN));
//    }
//
//    @ApiOperation(value = "??????????????????", notes = "")
//    @PostMapping(value = "/course/experiment/piclib")
//    public ResponseMessage<String> uploadImage(@RequestParam("file") MultipartFile file) throws ApplicationErrorException {
//        return new ResponseMessage.Success<>(fileService.uploadFile(file, ResourceTypeEnum.IMAGE));
//    }

    @ApiOperation(value = "????????????????????????", notes = "")
    @PostMapping(value = "/class/student/batchCreation")
    public ResponseMessage<ResBatchAddStudent> batchStudentCreation(@RequestParam("classId") int classId, @RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        //studentService.batchStudentCreation(classId, file);
        return new ResponseMessage.Success<ResBatchAddStudent>();
    }

    @ApiOperation(value = "????????????????????????", notes = "")
    @PostMapping(value = "/teacher/batchCreation")
    public ResponseMessage<ResBatchAddTeacher> batchTeacherCreation(@RequestParam("file") MultipartFile file) throws ApplicationErrorException, IOException {
        return new ResponseMessage.Success<>(teacherService.batchTeacherCreation(file));
    }

    @ApiOperation(value="??????????????????????????????")
    @PostMapping(value="/getDataShireDBList")
    public ResponseMessage<Map<String,Object>>  getDatashireDBList(@RequestParam("phone") String phone,
                                                                   @RequestParam("isAdmin") boolean isAdmin,
                                                                   @RequestParam("courseName")String courseName,
                                                                   @RequestParam("type") int type,
                                                                   @RequestParam(required = false) String paraSpaceName) {
        return new ResponseMessage.Success<>(eduApi.getDataShireDBList(phone,isAdmin,courseName,type,paraSpaceName));
    }

    @ApiOperation(value="???????????????????????????")
    @PostMapping(value="/getDataShireFileList")
    public ResponseMessage<Map<String,Object>>  getDatashireFileList(@RequestParam("phone") String phone,
                                                                     @RequestParam("path")String path,
                                                                     @RequestParam(required = false)String paraSpaceName,
                                                                     @RequestParam("isAdmin") boolean isAdmin,
                                                                     @RequestParam("courseName") String courseName,
                                                                     @RequestParam("type") int type) {
        return new ResponseMessage.Success<>(eduApi.getDataShireFileList(phone,path,isAdmin,courseName,type,paraSpaceName));
    }

    @ApiOperation(value="????????????????????????Hdfs????????????")
    @PostMapping(value="/getProjectDataShireFileList")
    public ResponseMessage<Map<String,Object>>  getProjectDatashireFileList(@RequestParam("projectId") Integer projectId,@RequestParam("path")String path) {
        return new ResponseMessage.Success<>(eduApi.getProjectFileList(projectId,path));
    }

    @ApiOperation(value="??????/???????????????")
    @PostMapping(value="/operateTable")
    public ResponseMessage<String>  operateTable(@RequestParam String spaceName,@RequestParam String tableName,@RequestParam String operate,@RequestParam String phone) {
        return new ResponseMessage.Success<>(eduApi.operateTable(spaceName,tableName,Boolean.parseBoolean(operate),phone));
    }

    @ApiOperation(value="?????????")
    @PostMapping(value="/deleteTable")
    public ResponseMessage<String> deleteTable(@RequestParam String spaceName,@RequestParam String tableName,@RequestParam String phone,@RequestParam String courseName,@RequestParam boolean isAdmin,@RequestParam int type){
        return new ResponseMessage.Success<>(eduApi.deleteDbTable(spaceName,tableName,phone,courseName,isAdmin,type));
    }

    // deleteCloudHDFSFile
    @ApiOperation(value="????????????")
    @PostMapping(value="/deleteCloudHDFSFile")
    public ResponseMessage<String> deleteFile(@RequestParam String spaceName,@RequestParam String fileName,@RequestParam String phone,@RequestParam String path,@RequestParam boolean isAdmin,@RequestParam String courseName,@RequestParam int type){
        return new ResponseMessage.Success<>(eduApi.deleteFile(spaceName,fileName,path,phone,isAdmin,courseName,type));
    }

    @ApiOperation(value="?????????????????????")
    @PostMapping(value="/getServerTime")
    public ResponseMessage<Date> getServerTime(){
        return new ResponseMessage.Success<>(new Date());
    }


    @ApiOperation(value="????????????????????????")
    @PostMapping(value="/getCourseById")
    public ResponseMessage<Course> getCourseById(@RequestParam Integer courseId) throws ApplicationErrorException {
        Course course = new Course();
        course.setId(courseId);
        course = courseService.getCourseById(courseId);
        if(course != null){
            //????????????????????????????????????????????????
            boolean flag = courseService.selectCourseExistOtherExperiment(courseId);
            course.setProgramExist(flag);
            //??????????????????????????????????????????
            boolean flag1 = courseService.selectCourseExistShulieyun(courseId);
            course.setShulieyunExist(flag1);
        }
        return new ResponseMessage.Success<>(course);
    }

    @ApiOperation(value="????????????????????????")
    @PostMapping(value="/getCourseByCourseName")
    public ResponseMessage<Course> getCourseByCourseName(@RequestParam String courseName) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.selectByCourseName(courseName));
    }
    @ApiOperation(value="?????????????????????????????????")
    @PostMapping(value="/getStudentByNotClass")
    public ResponseMessage<ResClassStudents> getStudentByNotClass(@RequestParam Integer classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(classService.getStudentByNotThisClass(classId));
    }


    @ApiOperation(value="?????????????????????")
    @PostMapping(value="/deleteStudentByClass")
    public ResponseMessage<ResClassStudents> deleteStudentByClass(@RequestParam Integer classId,@RequestParam Integer userId) throws ApplicationErrorException {
        Student student = studentService.selectByUserId(userId);
        //??????student_exper???????????????
       /* List<ReqStudentExperiment> studentExperiments=studentExperimentService.getStudentAllExperiments(userId);
        if(studentExperiments!=null && studentExperiments.size()>0){
            for(ReqStudentExperiment reqStudentExperiment:studentExperiments){
                studentExperimentService.deleteStudentExperiment(reqStudentExperiment.getExperimentId(),
                        reqStudentExperiment.getStudentId());
            }
        }*/
        //??????????????????????????????
        ReqDeleteClassStudent reqDeleteClassStudent=new ReqDeleteClassStudent();
        reqDeleteClassStudent.setStudentId(userId);
        reqDeleteClassStudent.setClassId(classId);
        classService.deleteClassStudent(reqDeleteClassStudent,0);
        //??????????????????????????????
        jobScoreService.deleteBystuIdAndCidAndCouId(classId,userId);
        if(student!=null){
            List<Integer> courseIdList=new ArrayList();
            //??????????????????????????????????????????
            List<ClassCourse> classCourseList=classCourseService.findClassCourseByClassId(classId);
            if(classCourseList!=null && classCourseList.size()>0){
                for(ClassCourse classCourse:classCourseList){
                    courseIdList.add(classCourse.getCourseId());
                }
            }
            if(courseIdList!=null && courseIdList.size()>0){
                for(Integer courseId:courseIdList){
                    //????????????????????????????????????
                    boolean isShuliuyun=courseService.selectCourseExistShulieyun(courseId);
                    if(isShuliuyun){
                        //?????????????????????????????????????????????????????????
                        Map map=new HashMap();
                        map.put("classId",reqDeleteClassStudent.getClassId());
                        map.put("courseId",courseId);
                        map.put("userId",reqDeleteClassStudent.getStudentId());
                        int exists=studentService.selectExistsCourseByClassIdAndCourseIdAndStuId(map);
                        if(exists==0){
                            //????????????
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


    @ApiOperation(value="???????????????????????????")
    @PostMapping(value="/getCourseByClassId")
    public ResponseMessage<ResTeacherClassList> getCourseByClassId(@RequestParam Integer classId) throws ApplicationErrorException {
        ResTeacherClassList teacherClassList=new ResTeacherClassList();
        //??????????????????????????????????????????
        List<ResClassDetail> classDetailList=classCourseService.findClassDetailByClassId(classId);
       teacherClassList.setTeacherClassList(classDetailList);
        return new ResponseMessage.Success<>(teacherClassList);
    }
    @ApiOperation(value="????????????????????????")
    @PostMapping(value="/class/addStudentList")
    public ResponseMessage addStudentList(@RequestBody ReqAddClassStudent[] addClassStuList) throws ApplicationErrorException {
            if(addClassStuList!=null && addClassStuList.length>0){
                for(ReqAddClassStudent reqAddClassStudent:addClassStuList){
                    addClassStudent(reqAddClassStudent);
                }
            }
        return new ResponseMessage.Success<>();
    }

    @ApiOperation(value="????????????????????????")
    @PostMapping(value="/class/deleteStudentList")
    public ResponseMessage deleteStudentList(@RequestBody ReqDeleteClassStudent[] deleteClassStudents) throws ApplicationErrorException {
        if(deleteClassStudents!=null && deleteClassStudents.length>0){
            for(ReqDeleteClassStudent reqAddClassStudent:deleteClassStudents){
                deleteStudentByClass(reqAddClassStudent.getClassId(),reqAddClassStudent.getStudentId());
            }
        }
        return new ResponseMessage.Success<>();
    }
    @ApiOperation(value="????????????????????????")
    @PostMapping(value="/class/addClassCourseList")
    public ResponseMessage addClassCourseList(@RequestBody ReqAddClassCourse[] reqAddClassCourses) throws ApplicationErrorException {
        if(reqAddClassCourses!=null && reqAddClassCourses.length>0){
            for(ReqAddClassCourse reqAddClassCourse:reqAddClassCourses){
                classService.addClassCourse(reqAddClassCourse);
            }
        }
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value="????????????????????????")
    @PostMapping(value="/class/delClassCourseList")
    public ResponseMessage deleteClassCourseList(@RequestBody ReqDeleteClassCourse[] reqDeleteClassCourses) throws ApplicationErrorException {
        if(reqDeleteClassCourses!=null && reqDeleteClassCourses.length>0){
            for(ReqDeleteClassCourse reqDeleteClassCourse:reqDeleteClassCourses){
                classService.deleteClassCourse(reqDeleteClassCourse);
            }
        }
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value="??????moduleOrder")
    @PostMapping(value="/module/adjustModuleOrder")
    public ResponseMessage<Map> adjustModuleOrder(@RequestBody ReqAdjustModuleOrderClass reqEntity) throws ApplicationErrorException {
        Map<String,Object> result = moduleService.adjustModuleOrder(reqEntity);
        return new ResponseMessage.Success<>(result);
    }

    @ApiOperation(value="??????experimentOrder")
    @PostMapping(value="/experiment/adjustExperimentOrder")
    public ResponseMessage<Map> adjustExperimentOrder(@RequestBody ReqAdjustExperimentOrderClass reqEntity) throws ApplicationErrorException {
        Map<String,Object> resultMap = experimentService.adjustExperimentOrder(reqEntity);
        return new ResponseMessage.Success<>(resultMap);
    }


    @ApiOperation(value = "????????????key??????", notes = "???????????????")
    @GetMapping(value = "/getKey")
    public ResponseMessage<Map> getSysKey() throws ApplicationErrorException {
        String key = sysLicenseService.getSysKey();
        Map<String,Object> result = new HashMap<>();
        result.put("key",key);
        return new ResponseMessage.Success<>(result);
    }


    @ApiOperation(value = "???????????????license", notes = "??????????????????license?????????????????????????????????????????????")
    @GetMapping(value = "/isImportLicense")
    public ResponseMessage<Map> isImportLicense() throws ApplicationErrorException {
        Map<String,Object> result = sysLicenseService.decodeLicense();
        return new ResponseMessage.Success<>(result);
    }


    @ApiOperation(value = "???????????????license")
    @PostMapping(value = "/importOrUpdateLicense")
    public ResponseMessage<Map> importOrUpdateLicense(@RequestBody ReqImportOrUpdateLicense license) throws
            ApplicationErrorException{
        Map<String,Object> map = sysLicenseService.importOrUpdateLicense(license);
        return new ResponseMessage.Success<>(map);
    }
    @ApiOperation(value = "???????????????????????????")
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

    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/getPropertiesValue")
    public ResponseMessage<String> getPropertiesValue(@RequestParam String parameter) throws ApplicationErrorException{
        String value=environment.getProperty(parameter);
        return new ResponseMessage.Success<>(value);
    }



}


