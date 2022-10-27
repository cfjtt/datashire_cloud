package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.enums.CloudwareTypeEnum;
import com.eurlanda.edu.tp.enums.GenderEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ErrorMessage;
import com.eurlanda.edu.tp.excel.ExcelUtil;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.webdomain.request.*;
import com.eurlanda.edu.tp.webdomain.response.*;
import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.excel.domain.ExcelStudent;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;

@Service
public class StudentServiceImp implements StudentService {
    private static Logger logger = LoggerFactory.getLogger(StudentServiceImp.class);


    @Value("${default.password}")
    private String defaultPassword;
    @Value("${file.shareFileUrl}")
    private String shareFileUrl;
    @Value("${file.baseDir}")
    private String baseDir;

    @Value("${file.imageDir}")
    private String imageDir;
    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private ClassService classService;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EduApi eduApi;
    @Autowired
    private  UserService userService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private RancherService rancherService;

    @Autowired
    private ClassCourseService classCourseService;
    @Autowired
    private JobScoreMapper jobScoreMapper;
    @Autowired
    private ExperimentMapper experimentMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TProjectUserService projectUserService;
    @Override
    public List<StudentClass> getAllStudentClassByUserId(int studentID) {
        return studentClassMapper.selectByStudentUserId(studentID);
    }

    @Override
    public Student getStudentById(int id) {
        return studentMapper.selectByPrimaryKey(id);
    }
    @Override
    public Student selectByUserId(int userId) {
        return studentMapper.selectByUserId(userId);
    }

    @Override
    public Integer selectExistsCourseByClassIdAndCourseIdAndStuId(Map<String,Object> map) {
        return studentMapper.selectExistsCourseByClassIdAndCourseIdAndStuId(map);
    }

    @Override
    public int findSlyByNotClassAndStuId(Map<String, Object> map) {
        return studentMapper.findSlyByNotClassAndStuId(map);
    }

    @Override
    public ResClassStudents getAllStudentInfo() {
        ResClassStudents resClassStudents = new ResClassStudents();
        List<ResClassStudents.ResClassStudent> resClassStudentList = new ArrayList<>();
        resClassStudents.setStudentList(resClassStudentList);
        for (Map studentInfo : studentMapper.getAllStudentInfo()){
            ResClassStudents.ResClassStudent resClassStudent = new ResClassStudents().new ResClassStudent();
            resClassStudent.setId((int)studentInfo.get("userId"));
            resClassStudent.setStudentNo((String) studentInfo.get("sno"));
            resClassStudent.setStudentName((String)studentInfo.get("name"));
            resClassStudent.setGender((int)studentInfo.get("gender"));
            resClassStudent.setPassword(studentInfo.get("password")+"");
            resClassStudent.setGrade((String) studentInfo.get("grade"));
            resClassStudent.setPhone((String) studentInfo.get("phone"));
            resClassStudentList.add(resClassStudent);
        }
        resClassStudents.setStudentList(resClassStudentList);
        return resClassStudents;

    }

    @Override
    public void addStudentInfo(ReqAddStudent reqAddStudent)throws  ApplicationErrorException {
        Student student=new Student();
        student.setName(reqAddStudent.getStudentName());
        student.setGender(reqAddStudent.getGender());
        student.setGrade(reqAddStudent.getGrade());
        student.setPhone(reqAddStudent.getPhone());
        student.setSno(reqAddStudent.getStudentNo());
        student.setBirthday("");
       try{
           createStudent(student);
       }catch (ApplicationErrorException e){
           throw  e;
       }
    }


    public Student getStudentByNo(String no){
        Student student = studentMapper.selectByStudentNo(no);
        return student;
    }
    @Override
    public ResStudentClassList getAllStudentClassInfoByUserId(int studentID) throws
            ApplicationErrorException {
        if(studentMapper.selectByUserId(studentID) == null){
            throw new ApplicationErrorException(ErrorCode.StudentNotExists);
        }

        List<Map> classDetailList = studentClassMapper.getStudentClassInfoByUserId(studentID);
        ResStudentClassList resStudentClassInfoList = new ResStudentClassList();

        List<ResClassDetail> studentClassList = new ArrayList<>(classDetailList.size());
        resStudentClassInfoList.setStudentClassList(studentClassList);
        int index = 0;
        for (Map classDetail : classDetailList){
            ResClassDetail resClassDetail = new ResClassDetail();
            studentClassList.add(index++, resClassDetail);

            resClassDetail.setClassId((int)classDetail.get("classId"));
            resClassDetail.setClassName((String) classDetail.get("className"));
            resClassDetail.setSchoolYear((String)classDetail.get("schoolYear"));
            if(classDetail.get("semester")!=null && !classDetail.get("semester").equals("")){
                resClassDetail.setSemester(Integer.parseInt(classDetail.get("semester").toString()));
            }
           /* resClassDetail.setTerm( new Term((String) classDetail.get("year"),
                                    (int) classDetail.get("semester")).getDescription());*/
            resClassDetail.setImage((String)classDetail.get("url"));
            resClassDetail.setDuration((String)classDetail.get("duration"));
            resClassDetail.setStudentNumber((int)classDetail.get("studentNum"));
            resClassDetail.setCourseId((int)classDetail.get("courseId"));
            resClassDetail.setCourseName((String) classDetail.get("courseName"));
            resClassDetail.setCourseDescription((String) classDetail.get("description"));
            resClassDetail.setClassDate(Utility.formatDate((Date)classDetail.get("classDate")));
            resClassDetail.setTeacherName((String) classDetail.get("teacherName"));
            resClassDetail.setTeacherContract((String) classDetail.get("teacherContact"));
        }

        return resStudentClassInfoList;
    }

    @Override
    public int updateStudentInfo(ReqUpdateStudent reqUpdateStudent) throws ApplicationErrorException {
        Student studentToUpdate = new Student(
                0, reqUpdateStudent.getId(), reqUpdateStudent.getStudentNo(), reqUpdateStudent.getStudentName(),
                reqUpdateStudent.getGender(), "",reqUpdateStudent.getGrade(),reqUpdateStudent.getPhone());

        this.updateStudent(studentToUpdate);

        return 0;
    }

    @Override
    public ResBatchCreateStudent batchCreateStudent(MultipartFile file) throws ApplicationErrorException,IOException{
        ResBatchCreateStudent resBatchCreateStudent=new ResBatchCreateStudent();
        List<ResBatchCreateStudent.FailureReason> failureReasonList = new ArrayList<>();
        int success = 0;
        int failure = 0;
        ExcelStudent excelStudent = ExcelUtil.studentExcelAnalysis(file);
        for (ExcelStudent.ExcelStudentElement excelStudentElement : excelStudent.getExcelStudentElementList()) {
            try {
                ReqAddStudent reqAddStudent = new ReqAddStudent();
                // 校验编号
                if(excelStudentElement.getStudentNum()==null || excelStudentElement.getStudentNum().equals("")){
                    throw  new ApplicationErrorException(ErrorCode.STUDENTNoTNULLNo);
                }
                if(excelStudentElement.getStudentNum().trim().length()>45){
                    throw  new ApplicationErrorException(ErrorCode.STUDENTNOLENGTH,45);
                }
                if(!excelStudentElement.getStudentNum().matches("[0-9]{1,45}")){
                    throw  new ApplicationErrorException(ErrorCode.STUDENTInvalid);
                }
                reqAddStudent.setStudentNo(excelStudentElement.getStudentNum());
                if(excelStudentElement.getStudentName() == null || excelStudentElement.getStudentName().trim().length()==0){
                    throw new ApplicationErrorException(ErrorCode.NameIsNULL);
                }
                if(excelStudentElement.getStudentName() != null && excelStudentElement.getStudentName().trim().length()>45){
                    throw new ApplicationErrorException(ErrorCode.NameLENGTH,45);
                }
                reqAddStudent.setStudentName(excelStudentElement.getStudentName());
                try{
                    GenderEnum genderEnum=GenderEnum.parse(excelStudentElement.getGenderStr());
                    if(genderEnum!=null){
                        reqAddStudent.setGender(genderEnum.getCode());
                    }else{
                        throw new ApplicationErrorException(ErrorCode.InvalidGender);
                    }
                }catch (Exception e){
                    throw new ApplicationErrorException(ErrorCode.InvalidGender);
                }
                //年级
                if(excelStudentElement.getGrade()!=null && !excelStudentElement.getGrade().equals("")){
                    if(excelStudentElement.getGrade().trim().length()>45){
                        throw new ApplicationErrorException(ErrorCode.GRADELENGTH,45);
                    }
                    reqAddStudent.setGrade(excelStudentElement.getGrade());
                }else{
                    throw new ApplicationErrorException(ErrorCode.NoTNULLGRADE);
                }


                //校验老师联系方式
                if(!excelStudentElement.getPhone().matches("[A-Za-z0-9\\u4e00-\\u9fa5+\\.+\\-+\\_]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+")
                        && !excelStudentElement.getPhone().matches("1\\d{10}")){
                    throw new ApplicationErrorException(ErrorCode.CONCAT_IS_VALID);
                }else{
                    if(excelStudentElement.getPhone().trim().length()>45){
                        throw new ApplicationErrorException(ErrorCode.PhoneLength,45);
                    }
                }
                reqAddStudent.setPhone(excelStudentElement.getPhone());

                reqAddStudent.setOverride(false);
                //导入
                importStudent(reqAddStudent);
                success += 1;
            } catch (ApplicationErrorException ex) {
                ResBatchCreateStudent.FailureReason failureReason = new ResBatchCreateStudent().new FailureReason();
                failureReason.setStudentNum(excelStudentElement.getStudentNum());
                failureReason.setStudentName(excelStudentElement.getStudentName());
                failureReason.setGender(excelStudentElement.getGender());
                failureReason.setReason(ex.getMessage());
                failureReason.setErrorCode(ex.getErrorCode());
                failureReasonList.add(failureReason);
                failure += 1;
            }
        }
        resBatchCreateStudent.setSuccess(success);
        resBatchCreateStudent.setFailure(failure);
        resBatchCreateStudent.setFailureReasonList(failureReasonList);
        return resBatchCreateStudent;
    }

    @Override
    public void importStudent(ReqAddStudent reqAddStudent) throws ApplicationErrorException {
        Student student = studentMapper.selectByStudentNo(reqAddStudent.getStudentNo());
        if(student != null){
            //学生已经存在
            throw new ApplicationErrorException(ErrorCode.StudentAlreadyExists,student.getSno());
        }else{
            User user = userMapper.selectByUserName(reqAddStudent.getStudentNo());
            if(user!=null){
                if(user.getRole()==RoleEnum.TEACHER.getCode()){
                    throw new ApplicationErrorException(ErrorCode.USERISNOTSTUDENT);
                }else if(user.getRole()==RoleEnum.STUDENT.getCode()){
                    throw new ApplicationErrorException(ErrorCode.StudentAlreadyExists,user.getUsername());
                }else{
                    throw new ApplicationErrorException(ErrorCode.UserAlreadyExists);
                }
            }else{
                student = new Student(
                        reqAddStudent.getStudentNo(), reqAddStudent.getStudentName(),
                        reqAddStudent.getGender(), "",reqAddStudent.getGrade(),reqAddStudent.getPhone());
                createStudent(student);
            }
        }
    }

    @Override
    public void updateStu(ReqUpdateStudent reqUpdateStudent) throws ApplicationErrorException {
        Student student=getStudentByNo(reqUpdateStudent.getStudentNo());
        if(student!=null){
            student.setSno(reqUpdateStudent.getStudentNo());
            student.setName(reqUpdateStudent.getStudentName());
            student.setGender(reqUpdateStudent.getGender());
            student.setGrade(reqUpdateStudent.getGrade());
            student.setPhone(reqUpdateStudent.getPhone());
            student.setId(reqUpdateStudent.getId());
            updateStudent(student);
        }else{
            throw new ApplicationErrorException(ErrorCode.StudentNotExists);
        }
    }

    @Override
    public void deleteStuByStuNotClassAndCourse(Student student) {
        User user = userService.getUserInfo(student.getUserId());
        //删除学生
        studentMapper.deleteStudentByUserId(student.getUserId());
        if(user!=null){
            String volumeId = user.getVolumeId();
            //将该学生删除
            userMapper.deleteByPrimaryKey(student.getUserId());
            //删除该学生的volume
            if(volumeId != null){
                rancherService.deleteVolume(volumeId);
            }
            //删除项目相关信息
            projectUserService.deleteByUserId(user.getId());

        }

    }

    @Override
    public int submitHomeworkOfShuLie(ReqSubmitHomeworkOfShuLie reqSubmitHomeworkOfShuLie)throws ApplicationErrorException, IOException {
        User user=userService.getUserInfo(reqSubmitHomeworkOfShuLie.getUserId());
        Course course=courseMapper.selectByPrimaryKey(reqSubmitHomeworkOfShuLie.getCourseId());
        //查询实验作业答案是否存在
        Experiment experiment=experimentMapper.selectByPrimaryKey(reqSubmitHomeworkOfShuLie.getExperimentId());
        if(experiment!=null){
            if(experiment.getIsAnswer()==1){
                //判断表是否存在
                Map<String,Object> isMap=eduApi.checkCloudTableIsExists(null,course.getName(),experiment.getAnswerTableName(),1);
                if(isMap!=null && isMap.size()>0){
                    String isExists=(String)isMap.get("isExists");
                    if(isExists.equals("3")){
                        throw  new ApplicationErrorException(ErrorCode.ANSWERNOTEXISTS);
                    }
                }
            }else{
                throw  new ApplicationErrorException(ErrorCode.ANSWERNOTEXISTS);
            }
        }
            if(user!=null){
                //查询表是否存在
                Map<String,Object> isMap=eduApi.checkCloudTableIsExists(user.getUsername(),course.getName(),reqSubmitHomeworkOfShuLie.getTableName(),2);
                if(isMap!=null && isMap.size()>0){
                    String isExists=(String)isMap.get("isExists");
                    if(isExists.equals("1")){
                        //查询班级信息
                        if(reqSubmitHomeworkOfShuLie.getClassName()!=null && !reqSubmitHomeworkOfShuLie.getClassName().equals("")){
                            Clazz clazz= clazzMapper.getClassByClassName(reqSubmitHomeworkOfShuLie.getClassName());
                            if(clazz!=null){
                                JobScore jobScore=new JobScore();
                                jobScore.setClassId(clazz.getId());
                                jobScore.setCourseId(reqSubmitHomeworkOfShuLie.getCourseId());
                                jobScore.setExperimentId(reqSubmitHomeworkOfShuLie.getExperimentId());
                                jobScore.setCreateTime(new Date());
                                jobScore.setStudentId(reqSubmitHomeworkOfShuLie.getUserId());
                                jobScore.setTableName(reqSubmitHomeworkOfShuLie.getTableName());
                                jobScore.setScore(null);
                                jobScore.setFileUrl(null);
                                return  jobScoreMapper.insertSelective(jobScore);
                            }
                        }
                    }else{
                        //表不存在
                        throw  new ApplicationErrorException(ErrorCode.DBTABLENOTEXISTS);
                    }
                }else{
                    //表不存在
                    throw  new ApplicationErrorException(ErrorCode.DBTABLENOTEXISTS);
                }
            }
        return 0;
    }

    @Override
    public int submitHomeworkOfOther(ReqSubmitHomeworkOfOther reqSubmitHomeworkOfOther) throws ApplicationErrorException, IOException{
        //查询实验作业答案是否存在
        Experiment experiment=experimentMapper.selectByPrimaryKey(reqSubmitHomeworkOfOther.getExperimentId());
        if(experiment!=null){
            if(experiment.getIsAnswer()==1){
                //判断答案源是否存在
                Integer courseId = reqSubmitHomeworkOfOther.getCourseId();
                Course course = courseMapper.selectByPrimaryKey2(courseId);
                if(course == null){
                    throw new ApplicationErrorException(ErrorCode.CourseNotExists);
                }
                String folderName = course.getFolderName();
                if(folderName == null){
                    course.setFolderName(System.currentTimeMillis() + "");
                    courseMapper.updateByPrimaryKeySelective(course);
                }

                String filePath = baseDir + imageDir + "/" + course.getFolderName()  + "/" + experiment.getAnswerUrl();
                File file=new File(filePath);
                if(!file.exists()){
                    throw  new ApplicationErrorException(ErrorCode.ANSWERNOTEXISTS);
                }
            }else{
                throw  new ApplicationErrorException(ErrorCode.ANSWERNOTEXISTS);
            }
        }

        //查询文件是否存在
        File file=new File(shareFileUrl+reqSubmitHomeworkOfOther.getUserId()+"/"+reqSubmitHomeworkOfOther.getHomeworkPath());
        if(!file.exists()){
                throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
        if (reqSubmitHomeworkOfOther.getClassName() != null && !reqSubmitHomeworkOfOther.getClassName().equals("")) {
            Clazz clazz = clazzMapper.getClassByClassName(reqSubmitHomeworkOfOther.getClassName());
            if (clazz != null) {
                JobScore jobScore = new JobScore();
                jobScore.setClassId(clazz.getId());
                jobScore.setCourseId(reqSubmitHomeworkOfOther.getCourseId());
                jobScore.setExperimentId(reqSubmitHomeworkOfOther.getExperimentId());
                jobScore.setCreateTime(new Date());
                jobScore.setStudentId(reqSubmitHomeworkOfOther.getUserId());
                jobScore.setFileUrl(reqSubmitHomeworkOfOther.getHomeworkPath());
                jobScore.setScore(null);
                jobScore.setTableName(null);
                return jobScoreMapper.insertSelective(jobScore);
            }
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> getStudentSubmitHomework(int classId, int courseId, int experimentId,int type,int userId) {
        List<Map<String, Object>> mapList=new ArrayList<>();
        //查询成绩表
        Map<String,Object> selectMap=new HashMap<>();
        selectMap.put("classId",classId);
        selectMap.put("courseId",courseId);
        selectMap.put("experimentId",experimentId);
        selectMap.put("studentId",userId);
        List<JobScore> jobScore=jobScoreMapper.selectStuScoreByMap(selectMap);
        if(jobScore!=null && jobScore.size()>0){
            Map<String,Object> map=new HashMap<>();
            //数猎云
            if(type==1){
                map.put("fileName",jobScore.get(0).getTableName());
                map.put("size","");
                mapList.add(map);
            }else if(type==0){//编程
                //根据文件地址获取大小
                String filePath=shareFileUrl+userId+"/"+jobScore.get(0).getFileUrl();
                File baseFile = new File(filePath);
                if (baseFile.exists()) {
                    map.put("fileName", baseFile.getName());
                    map.put("size", baseFile.length());
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }


    @Override
    public ResBatchAddStudent batchStudentCreation(int classId, MultipartFile file) throws ApplicationErrorException, IOException {
       /* ResBatchAddStudent resBatchAddStudent = new ResBatchAddStudent();
        List<ResBatchAddStudent.FailureReason> failureReasonList = new ArrayList<>();

        int success = 0;
        int failure = 0;

        if (clazzMapper.selectByPrimaryKey(classId) == null) {
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }
        ExcelStudent excelStudent = ExcelUtil.studentExcelAnalysis(file);
        //校验信息是否正确
        //查询出该班级所对应的课程的名字
        Clazz clazz = classService.getClassById(classId);
        Course course = courseService.getCourseById(clazz.getCourseId());
        //判断该课程是否是数猎云实训课程
        boolean issynchronized = false;
        List<Map<String,Object>> teacherList = teacherService.selectTeacherIsShulieyunByUserId(CloudwareTypeEnum.SHU_LIE_YUN.getCode(),course.getTeacherId());
        if(teacherList != null && teacherList.size()>0){
            issynchronized = true;
        }
        String courseName = null;
        if(course!=null){
            courseName = course.getName();
        }
        Map<String,Object> addUserMap = new HashMap<>();
        List<Map<String,Object>> userList = new ArrayList<>();
        for (ExcelStudent.ExcelStudentElement excelStudentElement : excelStudent.getExcelStudentElementList()) {
            try {
                ReqAddClassStudent newClassStudent = new ReqAddClassStudent();
                newClassStudent.setClassId(classId);
                // 校验编号
                if(excelStudentElement.getStudentNum()==null || excelStudentElement.getStudentNum().equals("")){
                    throw  new ApplicationErrorException(ErrorCode.STUDENTNoTNULLNo);
                }
                if(excelStudentElement.getStudentNum().trim().length()>45){
                    throw  new ApplicationErrorException(ErrorCode.STUDENTNOLENGTH,45);
                }
                if(!excelStudentElement.getStudentNum().matches("[0-9]{1,45}")){
                    throw  new ApplicationErrorException(ErrorCode.STUDENTInvalid);
                }
                newClassStudent.setStudentNo(excelStudentElement.getStudentNum());
                if(excelStudentElement.getStudentName() == null || excelStudentElement.getStudentName().trim().length()==0){
                    throw new ApplicationErrorException(ErrorCode.NameIsNULL);
                }
                if(excelStudentElement.getStudentName() != null &&  excelStudentElement.getStudentName().trim().length()>45){
                    throw new ApplicationErrorException(ErrorCode.NameLENGTH,45);
                }
                newClassStudent.setStudentName(excelStudentElement.getStudentName());
                //校验性别
                *//*if(!excelStudentElement.getGenderStr().matches("1|2")){
                    throw new ApplicationErrorException(ErrorCode.InvalidGender);
                }*//*
                try{
                    GenderEnum genderEnum=GenderEnum.parse(excelStudentElement.getGenderStr());
                    if(genderEnum!=null){
                        newClassStudent.setGender(genderEnum.getCode());
                    }else{
                        throw new ApplicationErrorException(ErrorCode.InvalidGender);
                    }
                }catch (Exception e){
                    throw new ApplicationErrorException(ErrorCode.InvalidGender);
                }
                //年级
                if(excelStudentElement.getGrade()!=null && !excelStudentElement.getGrade().equals("")){
                    if(excelStudentElement.getGrade().trim().length()>45){
                        throw new ApplicationErrorException(ErrorCode.GRADELENGTH,45);
                    }
                    newClassStudent.setGrade(excelStudentElement.getGrade());
                }else{
                    throw new ApplicationErrorException(ErrorCode.NoTNULLGRADE);
                }
                //校验学生联系方式
                if(!excelStudentElement.getPhone().matches("[A-Za-z0-9\\u4e00-\\u9fa5+\\.+\\-+\\_]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+")
                        && !excelStudentElement.getPhone().matches("1\\d{10}")){
                    throw new ApplicationErrorException(ErrorCode.CONCAT_IS_VALID);
                }else{
                    if(excelStudentElement.getPhone().trim().length()>45){
                        throw new ApplicationErrorException(ErrorCode.PhoneLength,45);
                    }
                }
                newClassStudent.setPhone(excelStudentElement.getPhone());

                newClassStudent.setOverride(false);
                //放到集合中，等待批量插入
               classService.addClassStudent(newClassStudent);
                if(issynchronized) {
                    String phone = excelStudentElement.getStudentNum();
                    String password = null;
                    //查找出学生的no和password信息
                    Student student = studentMapper.selectByStudentNo(excelStudentElement.getStudentNum());
                    if (student != null) {
                        User user = userMapper.selectByPrimaryKey(student.getUserId());
                        if (user != null) {
                            password = user.getPassword2();
                            Map<String, Object> map = new HashMap<>();
                            map.put("phone", phone);
                            map.put("password", password);
                            userList.add(map);
                        }
                    }
                }
                success += 1;
            } catch (ApplicationErrorException ex) {
                ResBatchAddStudent.FailureReason failureReason = new ResBatchAddStudent().new FailureReason();
                failureReason.setClassId(classId);
                failureReason.setStudentNum(excelStudentElement.getStudentNum());
                failureReason.setStudentName(excelStudentElement.getStudentName());
                failureReason.setGender(excelStudentElement.getGender());
                failureReason.setReason(ex.getMessage());
                failureReason.setErrorCode(ex.getErrorCode());
                failureReasonList.add(failureReason);

                failure += 1;
            }
        }
        resBatchAddStudent.setSuccess(success);
        resBatchAddStudent.setFailure(failure);
        resBatchAddStudent.setFailureReasonList(failureReasonList);
        //增加班级人数


        //判断该课程是否是数猎云相关课程，如果是，将用户同步到数猎云
        if(issynchronized) {
            addUserMap.put("coursename", courseName);
            addUserMap.put("userList", userList);
            eduApi.addUser(addUserMap);
        }*/
        return null;
    }

    @Override
    public void createStudent(Student student) throws ApplicationErrorException {
        if(userMapper.selectByUserName(student.getSno()) != null) {
            //判断是否是老师学号
            User user=userMapper.selectByUserName(student.getSno());
                if(user.getRole()==RoleEnum.TEACHER.getCode()){
                    throw new ApplicationErrorException(ErrorCode.USERISNOTSTUDENT);
                }
            throw new ApplicationErrorException(ErrorCode.StudentAlreadyExists, student.getSno());
        }
        User user = managerService.createUser(student.getSno(), RoleEnum.STUDENT);
        student.setUserId(user.getId());
        studentMapper.insert(student);
    }

    @Override
    public void updateStudent(Student studentToUpdate) throws ApplicationErrorException {
        Student originalStudent = studentMapper.selectByUserId(studentToUpdate.getUserId());
        if(originalStudent == null){
            throw new ApplicationErrorException(ErrorCode.StudentNotExists);
        }
        if( !originalStudent.getSno().equals(studentToUpdate.getSno())){
            //If student No is changed
            if(userMapper.selectByUserName(studentToUpdate.getSno()) != null){
                throw new ApplicationErrorException(ErrorCode.StudentAlreadyExists, studentToUpdate.getSno());
            }
        }

        studentMapper.updateByUserId(studentToUpdate);
        User user = userMapper.selectByPrimaryKey(studentToUpdate.getUserId());
        user.setUsername(studentToUpdate.getSno());
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int getCount() {
        return studentMapper.getCount();
    }

    @Override
    public List<Map<String, Object>> getAllStudentByCourseId(int courseId) {
        return studentClassMapper.getAllStudentByCourseId(courseId);
    }
}
