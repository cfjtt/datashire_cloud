package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.enums.SemesterEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.webdomain.request.*;
import com.eurlanda.edu.tp.webdomain.response.ResClassDetail;
import com.eurlanda.edu.tp.webdomain.response.ResClassInfos;
import com.eurlanda.edu.tp.webdomain.response.ResClassStudents;
import com.eurlanda.edu.tp.webdomain.response.ResStudentClassList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClassServiceImp implements ClassService {
    private static Logger logger = LoggerFactory.getLogger(ClassServiceImp.class);
    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentService studentService;


    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseResourceMapper courseResourceMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private StudentExperimentService experimentService;
    @Autowired
    private EduApi eduApi;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassService classService;
    @Autowired
    private RancherService rancherService;
    @Autowired
    private ClassCourseMapper classCourseMapper;
    @Autowired
    private JobScoreMapper jobScoreMapper;

    @Override
    public Clazz getClassById(int classID) throws ApplicationErrorException {

        Clazz clazz = clazzMapper.selectByPrimaryKey(classID);
        if (clazz == null) {
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }
        return clazz;
    }

    /**
     *
     * @param reqDeleteClassStudent
     * @param type 0 ????????????????????????    1 ?????????????????????
     * @return
     * @throws ApplicationErrorException
     */
    @Override
    public int deleteClassStudent(ReqDeleteClassStudent reqDeleteClassStudent,int type) throws ApplicationErrorException {
        if(!clazzMapper.isStudentInClass(reqDeleteClassStudent.getStudentId(), reqDeleteClassStudent.getClassId())){
            throw new ApplicationErrorException(ErrorCode.StudentNotInClass);
        }
        studentClassMapper.deleteByClassIdAndStudentId(reqDeleteClassStudent.getClassId(), reqDeleteClassStudent.getStudentId());
        synchronized (this){
            Clazz clazz = clazzMapper.selectByPrimaryKey(reqDeleteClassStudent.getClassId());
            clazz.setStudentNum(clazz.getStudentNum() - 1);
            clazzMapper.updateByPrimaryKey(clazz);
        }
        if(type==1){
            //????????????????????????????????????????????????????????????????????????????????????
            ResStudentClassList studentClassList = studentService.getAllStudentClassInfoByUserId(reqDeleteClassStudent.getStudentId());
            if(studentClassList ==null || studentClassList.getStudentClassList().size()==0){
                //??????????????????????????????
                List<ReqStudentExperiment> studentExperiments = experimentService.getStudentAllExperiments(reqDeleteClassStudent.getStudentId());
                if(studentExperiments != null){
                    for(ReqStudentExperiment studentExperiment : studentExperiments){
                        experimentService.deleteStudentExperiment(studentExperiment.getExperimentId(),studentExperiment.getStudentId());
                    }
                }
                //????????????
                studentMapper.deleteStudentByUserId(reqDeleteClassStudent.getStudentId());
                User user = userService.getUserInfo(reqDeleteClassStudent.getStudentId());
                String volumeId = user.getVolumeId();
                //??????????????????volume
                if(volumeId != null){
                    rancherService.deleteVolume(volumeId);
                }
                //??????????????????
                userMapper.deleteByPrimaryKey(reqDeleteClassStudent.getStudentId());

            }
        }
        return 0;
    }

    @Override
    public int addClassStudent(ReqAddClassStudent reqAddClassStudent) throws ApplicationErrorException {
        if(clazzMapper.selectByPrimaryKey(reqAddClassStudent.getClassId()) == null){
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }

        Student student = studentMapper.selectByStudentNo(reqAddClassStudent.getStudentNo());
        if(student != null){
            if(clazzMapper.isStudentInClass(student.getUserId(), reqAddClassStudent.getClassId())){
                throw new ApplicationErrorException(ErrorCode.StudentAlreadyInClass);
            }
            //?????????????????????????????????????????????????????????name?????????????????????(?????????????????????)?????????????????????????????????????????????????????????
            int addGender = reqAddClassStudent.getGender();
            String addName = reqAddClassStudent.getStudentName();
            boolean isSamePerson = false;
            if(addGender == student.getGender() && addName.equals(student.getName())){
                isSamePerson = true;
            }
            if(!reqAddClassStudent.isOverride() && !isSamePerson){
                String gender = null;
                if(student.getGender() == 1){
                    gender = "??????";
                }else if(student.getGender() == 2){
                    gender = "??????";
                }
                throw new ApplicationErrorException(ErrorCode.DuplicateStudentNoFound, student.getSno(), gender,student.getName());
            }
            student.setGender(reqAddClassStudent.getGender());
            student.setName(reqAddClassStudent.getStudentName());
            studentService.updateStudent(student);
        } else {
            User user = userMapper.selectByUserName(reqAddClassStudent.getStudentNo());
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
                        reqAddClassStudent.getStudentNo(), reqAddClassStudent.getStudentName(),
                        reqAddClassStudent.getGender(), "",reqAddClassStudent.getGrade(),reqAddClassStudent.getPhone());
                studentService.createStudent(student);
            }

          /*  if(userMapper.selectByUserName(reqAddClassStudent.getStudentNo()) == null) {
                student = new Student(
                        reqAddClassStudent.getStudentNo(), reqAddClassStudent.getStudentName(),
                        reqAddClassStudent.getGender(), "");
                studentService.createStudent(student);
            } else {
                throw new ApplicationErrorException(ErrorCode.UserAlreadyExists);
            }*/
        }

        StudentClass studentClass = new StudentClass();
        studentClass.setClassId(reqAddClassStudent.getClassId());
        studentClass.setStudentId(student.getUserId());
        studentClassMapper.insert(studentClass);
        Clazz clazz=classService.getClassById(reqAddClassStudent.getClassId());
        clazz.setStudentNum(clazz.getStudentNum()+1);
        clazzMapper.updateByPrimaryKeySelective(clazz);
        return 0;
    }

    @Override
    public ResClassStudents getAllClassStudentInfo(int classId) throws ApplicationErrorException {
        if (clazzMapper.selectByPrimaryKey(classId) == null) {
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }
        ResClassStudents resClassStudents = new ResClassStudents();

        List<ResClassStudents.ResClassStudent> resClassStudentList = new ArrayList<>();
        resClassStudents.setStudentList(resClassStudentList);
        for (Map studentInfo : studentClassMapper.getAllStudentByClassId(classId)){
            ResClassStudents.ResClassStudent resClassStudent = new ResClassStudents().new ResClassStudent();
            resClassStudent.setId((int)studentInfo.get("userId"));
            resClassStudent.setStudentNo((String) studentInfo.get("sno"));
            resClassStudent.setStudentName((String)studentInfo.get("name"));
            resClassStudent.setGender((int)studentInfo.get("gender"));
            resClassStudent.setGrade((String) studentInfo.get("grade"));
            resClassStudent.setPhone((String) studentInfo.get("phone"));
            resClassStudent.setPassword(studentInfo.get("password")+"");
            resClassStudentList.add(resClassStudent);
        }

        resClassStudents.setStudentList(resClassStudentList);

        return resClassStudents;
    }

    @Override
    public int deleteClass(ReqDeleteClass reqDeleteClass) throws ApplicationErrorException {
        int classId = reqDeleteClass.getClassId();
        if (clazzMapper.selectByPrimaryKey(classId) == null) {
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }
        List<StudentClass> studentClasses = studentClassMapper.selectByClassID(classId);
//        if (homeworkMapper.isClassUsedByHomework(classId)) {
//            throw new ApplicationErrorException(ErrorCode.ClassIsUsedByHomework);
//        }
//        if (studentClassMapper.isClassUsedByStudentClass(classId)) {
//            throw new ApplicationErrorException(ErrorCode.ClassIsUsedByStudentClass);
//        }

        for(Homework homework : homeworkMapper.selectByClassId(classId)){
            homeworkService.deleteHomework(new ReqDeleteHomework(homework.getId()));
        }
        /**
         * ??????????????????
         */
        try{
            //?????????????????????????????????????????????????????????
            int slyCount=findSlyByClassId(reqDeleteClass.getClassId());
            if(slyCount>0){
                List<Integer> userIdList=new ArrayList<>();
                //???????????????????????????????????????
                List<ClassCourse> classCourses=classCourseMapper.findClassCourseByClassId(classId);
                if(classCourses!=null && classCourses.size()>0){
                    for(ClassCourse classCourse:classCourses) {
                        //??????????????????????????????
                        if (studentClasses != null && studentClasses.size() > 0) {
                            for (StudentClass studentClass : studentClasses) {
                                //????????????????????????????????????????????????????????????
                                Map<String, Object> map = new HashMap<>();
                                map.put("classId", classId);
                                map.put("studentId", studentClass.getStudentId());
                                map.put("courseId", classCourse.getCourseId());
                                int count = studentService.findSlyByNotClassAndStuId(map);
                                if (count > 0) {
                                    continue;
                                }
                                userIdList.add(studentClass.getStudentId());
                            }
                        }
                        //??????????????????????????????????????????????????????????????????????????????????????????
                        Map<String,Object> map=new HashMap<>();
                        map.put("classId",classId);
                        map.put("teacherId",classCourse.getTeacherId());
                        map.put("courseId",classCourse.getCourseId());
                        int teacherCount=findSlyByTeaIdAndClassIdAndCourseId(map);
                        //?????????????????????????????????????????????
                        if(teacherCount==0){
                            //??????????????? ????????????????????????????????????????????? ??????????????????????????????????????????????????????
                            userIdList.add(classCourse.getTeacherId());
                        }

                        if(userIdList!=null && userIdList.size()>0){
                            //???????????????
                            map=new HashMap<>();
                            map.put("idList",userIdList);
                            List<String> userList=userService.findUserNameListByUserIdList(map);
                            if(userList!=null && userList.size()>0){
                                map=new HashMap<>();
                                map.put("userList",userList);
                                Course course=courseMapper.selectByPrimaryKey(classCourse.getCourseId());
                                map.put("courseName",course.getName());
                                eduApi.delUser(map);
                            }
                        }

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //????????????????????????
        jobScoreMapper.deleteScoreByClassId(classId);
        //?????????????????????????????????
        classCourseMapper.deleteByClassId(classId);
        studentClassMapper.deleteByClassId(classId);
        clazzMapper.deleteByPrimaryKey(classId);
        logger.info("??????????????????"+classId);
        //??????????????????????????????????????????????????????
//        if(studentClasses != null){
//            for(StudentClass studentClass : studentClasses){
//                //????????????????????????????????????????????????????????????????????????????????????
//                //???????????????????????????
//
//                ResStudentClassList studentClassList = studentService.getAllStudentClassInfoByUserId(studentClass.getStudentId());
//                if(studentClassList ==null || studentClassList.getStudentClassList().size()==0){
//                    //??????????????????????????????
//                    List<ReqStudentExperiment> studentExperiments = experimentService.getStudentAllExperiments(studentClass.getStudentId());
//                    if(studentExperiments != null){
//                        for(ReqStudentExperiment studentExperiment : studentExperiments){
//                            experimentService.deleteStudentExperiment(studentExperiment.getExperimentId(),studentExperiment.getStudentId());
//                        }
//                    }
//
//                    //????????????
//                    studentMapper.deleteStudentByUserId(studentClass.getStudentId());
//                    //??????????????????
//                    userMapper.deleteByPrimaryKey(studentClass.getStudentId());
//                }
//            }
//        }





        return 0;
    }

    @Override
    public int updateClassInfo(ReqUpdateClass reqUpdateClass) throws ApplicationErrorException {
        if(clazzMapper.selectByPrimaryKey(reqUpdateClass.getClassId()) == null){
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }

        Clazz clazz = new Clazz();
        clazz.setId(reqUpdateClass.getClassId());
        clazz.setName(reqUpdateClass.getClassName());
        //clazz.setCourseId(reqUpdateClass.getCourseId());
        //clazz.setTermId(reqUpdateClass.getTermId());
        //clazz.setTeacherId(reqUpdateClass.getTeacherId());
        Clazz beforeClazz = clazzMapper.selectByPrimaryKey(reqUpdateClass.getClassId());
        clazz.setStudentNum(beforeClazz.getStudentNum());
        validateClass(clazz);

        clazzMapper.updateByPrimaryKeySelective(clazz);
        return 0;
    }

    @Override
    public int createClass(ReqAddClass reqAddClass) throws ApplicationErrorException {
        Clazz clazz = new Clazz();
        clazz.setName(reqAddClass.getClassName());
        clazz.setCourseId(reqAddClass.getCourseId());
        clazz.setTermId(reqAddClass.getTermId());
        clazz.setTeacherId(reqAddClass.getTeacherId());
        clazz.setDate(new Date());
        validateClass2(clazz);
        clazzMapper.insertSelective(clazz);
        return 0;
    }

    @Override
    public ResClassInfos getAllClassInfo() {
        ResClassInfos resClassInfos = new ResClassInfos();
        List<ResClassInfos.ResClassInfo> resClassInfoList = new ArrayList<>();

        for (Map classInfo : clazzMapper.selectAllClass()){
            ResClassInfos.ResClassInfo resClassInfo = new ResClassInfos.ResClassInfo();

            resClassInfo.setClassId((int)classInfo.get("classId"));
            resClassInfo.setClassName((String) classInfo.get("className"));
            /*resClassInfo.setCourseId((int)classInfo.get("courseId"));

            resClassInfo.setCourseName((String) classInfo.get("courseName"));
            resClassInfo.setCourseDes((String) classInfo.get("description"));

            resClassInfo.setTeacherId((int)classInfo.get("teacherId"));
            resClassInfo.setTeacherName((String) classInfo.get("teacherName"));
            resClassInfo.setTeacherContact((String) classInfo.get("email"));

            resClassInfo.setTerm(new Term(
                    (String) classInfo.get("year"),
                    (int) classInfo.get("semester")
            ).getDescription());

            resClassInfo.setTermId(Integer.valueOf(classInfo.get("term_id")+""));
            resClassInfo.setCourseImage((String) classInfo.get("url"));
            resClassInfo.setCourseDate(Utility.formatDate((Date) classInfo.get("classDate")));
           resClassInfo.setDuration((String) classInfo.get("duration"));*/
            resClassInfo.setStudentNum(((Integer) classInfo.get("studentNum")).intValue());


            resClassInfoList.add(resClassInfo);
        }

        resClassInfos.setClassInfoList(resClassInfoList);

        return resClassInfos;
    }

    private void validateClass(Clazz clazz) throws ApplicationErrorException {


        Integer count = clazzMapper.selectCountByClassNameAndCourse(clazz);
        if(count > 0){
            throw new ApplicationErrorException(ErrorCode.ClassAlreadyExisted);
        }


       /* if(termMapper.selectByPrimaryKey(clazz.getTermId()) == null){
            throw new ApplicationErrorException(ErrorCode.TermNotExists);
        }

        if(courseMapper.selectByPrimaryKey(clazz.getCourseId()) == null){
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }

        if(teacherMapper.selectByUserId(clazz.getTeacherId()) == null){
            throw new ApplicationErrorException(ErrorCode.TeacherNotExists);
        }*/

    }
    private void validateClass2(Clazz clazz) throws ApplicationErrorException {

        Integer count = clazzMapper.selectCountByClassName(clazz);
        if(count > 0){
            throw new ApplicationErrorException(ErrorCode.ClassAlreadyExisted);
        }


       /* if(termMapper.selectByPrimaryKey(clazz.getTermId()) == null){
            throw new ApplicationErrorException(ErrorCode.TermNotExists);
        }

        if(courseMapper.selectByPrimaryKey(clazz.getCourseId()) == null){
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }

        if(teacherMapper.selectByUserId(clazz.getTeacherId()) == null){
            throw new ApplicationErrorException(ErrorCode.TeacherNotExists);
        }
*/
    }

    /**
     * ???????????????????????????????????????
     * @param   class_id,cloudware_type
     * @return
     */
    public boolean selectIsShulieyunCourseByClassId(int class_id,int cloudware_type){
        Map<String,Object> map = new HashMap<>();
        map.put("class_id",class_id);
        map.put("cloudware_type",cloudware_type);
        return clazzMapper.selectIsShulieyunCourseByClassId(map);
    }

    @Override
    public boolean isTeacherUsedByClass(int teacherId) {
        return clazzMapper.isTeacherUsedByClass(teacherId);
    }

    @Override
    public List<Clazz> selectByCourseId(int courseId) {
        return clazzMapper.selectByCourseId(courseId);
    }

    @Override
    public Integer selectCountByClassName(Clazz clazz) {
        return clazzMapper.selectCountByClassNameAndCourse(clazz);
    }

    @Override
    public int findSlyByClassId(int classId) {
        return clazzMapper.findSlyByClassId(classId);
    }

    @Override
    public int findSlyByTeaIdAndClassIdAndCourseId(Map<String, Object> map) {
        return clazzMapper.findSlyByTeaIdAndClassIdAndCourseId(map);
    }

    @Override
    public ResClassStudents getStudentByNotThisClass(int classId) throws ApplicationErrorException {
        if(clazzMapper.selectByPrimaryKey(classId) == null){
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }
        List<Map> mapListStu=clazzMapper.findStuByNotThisClass(classId);
        ResClassStudents resClassStudents = new ResClassStudents();
        List<ResClassStudents.ResClassStudent> resClassStudentList = new ArrayList<>();
        resClassStudents.setStudentList(resClassStudentList);
        for (Map studentInfo : mapListStu){
            ResClassStudents.ResClassStudent resClassStudent = new ResClassStudents().new ResClassStudent();
            resClassStudent.setId((int)studentInfo.get("userId"));
            resClassStudent.setStudentNo((String) studentInfo.get("studentNo"));
            resClassStudent.setStudentName((String)studentInfo.get("studentName"));
            resClassStudent.setGender((int)studentInfo.get("gender"));
            resClassStudent.setGrade((String) studentInfo.get("grade"));
            resClassStudent.setPhone((String) studentInfo.get("phone"));
            resClassStudent.setPassword(studentInfo.get("password")+"");
            resClassStudentList.add(resClassStudent);
        }
        resClassStudents.setStudentList(resClassStudentList);
        return resClassStudents;
    }

    @Override
    public List<ResClassDetail> getClassDetailByClassId(int classId) {
        List<ResClassDetail> resClassDetails=new ArrayList<>();
        List<Map> mapList=clazzMapper.getClassDetailByClassId(classId);
        if(mapList!=null && mapList.size()>0){
            for(Map map:mapList){
                ResClassDetail resClassDetail=new ResClassDetail();
                resClassDetail.setClassId(classId);
                resClassDetail.setTeacherName((String) map.get("teacherName"));
                resClassDetail.setCourseName((String)map.get("courseName"));
                resClassDetail.setCourseId(Integer.parseInt(map.get("courseId").toString()));
                //resClassDetail.setTeacherId(Integer.parseInt(map.get("teacherId").toString()));
                resClassDetail.setSchoolYear((String)map.get("schoolYear"));
                if(map.get("semester")!=null && !map.get("semester").equals("")){
                    resClassDetail.setSemester(Integer.parseInt((String)map.get("semester")));
                }
             /*   Integer semester=Integer.parseInt(map.get("semester").toString());
                String year=(String)map.get("year");
                SemesterEnum semesterEnum = SemesterEnum.fromInt(semester);
                if(year.indexOf("-")>0){
                    resClassDetail.setTerm(year+semesterEnum.getZh());
                }else{
                    String term=year+"-"+(Integer.parseInt(year)+1)+semesterEnum.getZh();
                    resClassDetail.setTerm(term);
                }*/
                resClassDetails.add(resClassDetail);
            }
        }
        return resClassDetails;
    }

    @Override
    public void addClassCourse(ReqAddClassCourse reqAddClassCourse) throws ApplicationErrorException {
        if(courseMapper.selectByPrimaryKey(reqAddClassCourse.getCourseId())==null){
            new ApplicationErrorException(ErrorCode.CourseNotExists);
        }
        //??????????????????????????????????????????
        Map<String,Object> map=new HashMap<>();
        map.put("classId",reqAddClassCourse.getClassId());
        map.put("courseId",reqAddClassCourse.getCourseId());
        ClassCourse oldCourse=classCourseMapper.findClassCourseByClassIdAndCourseId(map);
        if(oldCourse!=null){
            new ApplicationErrorException(ErrorCode.CourseAlreadyExisted);
        }
        //????????????????????????
        ClassCourse classCourse=new ClassCourse();
        classCourse.setClassId(reqAddClassCourse.getClassId());
        classCourse.setCourseId(reqAddClassCourse.getCourseId());
        classCourse.setTeacherId(reqAddClassCourse.getTeacherId());
        classCourse.setSchoolYear(reqAddClassCourse.getSchoolYear());
        classCourse.setSemester(reqAddClassCourse.getSemester());
        classCourse.setDate(new Date());
        classCourseMapper.insertSelective(classCourse);
        //?????????????????????????????????????????????????????????????????????????????????
        boolean isSync = courseService.selectCourseExistShulieyun(reqAddClassCourse.getCourseId());
        if(isSync){
            //???????????????????????????
            Map<String, Object> addUserMap = new HashMap<>();
            List<Map<String, Object>> addUserList = new ArrayList<>();
            List<Map> stuList=studentClassMapper.getAllStudentByClassId(reqAddClassCourse.getClassId());
            if(stuList!=null && stuList.size()>0){
                for(Map mapStr:stuList){
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("phone", mapStr.get("sno"));
                    userMap.put("password", mapStr.get("password"));
                    addUserList.add(userMap);
                }

            }
            User user = userService.getUserInfo(reqAddClassCourse.getTeacherId());
            if(user!=null) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("phone", user.getUsername());
                userMap.put("password", user.getPassword2());
                addUserList.add(userMap);
            }
            if(addUserList!=null && addUserList.size()>0){
                addUserMap.put("coursename", reqAddClassCourse.getCourseName());
                addUserMap.put("userList", addUserList);
                eduApi.addUser(addUserMap);
            }
        }
    }

    @Override
    public void deleteClassCourse(ReqDeleteClassCourse reqDeleteClassCourse) throws ApplicationErrorException {
        if(courseMapper.selectByPrimaryKey(reqDeleteClassCourse.getCourseId())==null){
            new ApplicationErrorException(ErrorCode.CourseNotExists);
        }

        //?????????????????????????????????????????????????????????????????????????????????
        boolean isSync = courseService.selectCourseExistShulieyun(reqDeleteClassCourse.getCourseId());
        if(isSync){
            List<StudentClass> studentClasses = studentClassMapper.selectByClassID(reqDeleteClassCourse.getClassId());
            List<Integer> userIdList = new ArrayList<>();
            if(studentClasses!=null && studentClasses.size()>0) {
                for (StudentClass studentClass : studentClasses) {
                    //???????????????????????????????????????????????????
                    Map<String, Object> map = new HashMap<>();
                    map.put("classId", reqDeleteClassCourse.getClassId());
                    map.put("courseId", reqDeleteClassCourse.getCourseId());
                    map.put("studentId", studentClass.getStudentId());
                    int count = classCourseMapper.findCourseByNotClassAndStuId(map);
                    if (count > 0) {
                        continue;
                    }
                    userIdList.add(studentClass.getStudentId());
                }
            }
                //????????????????????????????????????????????????????????????????????????
                Map<String,Object> teaMap=new HashMap<>();
                teaMap.put("classId",reqDeleteClassCourse.getClassId());
                teaMap.put("courseId",reqDeleteClassCourse.getCourseId());
                teaMap.put("teacherId",reqDeleteClassCourse.getTeacherId());
                int count=classCourseMapper.findCourseByNotClassAndTeacherId(teaMap);
                if(count==0){
                    userIdList.add(reqDeleteClassCourse.getTeacherId());
                }
                if(userIdList!=null && userIdList.size()>0){
                    //???????????????
                    teaMap=new HashMap<>();
                    teaMap.put("idList",userIdList);
                    List<String> userList=userService.findUserNameListByUserIdList(teaMap);
                    if(userList!=null && userList.size()>0){
                        //???????????????????????????????????????
                       Course course=courseMapper.selectByPrimaryKey(reqDeleteClassCourse.getCourseId());
                        if(course!=null){
                            Map<String,Object> map=new HashMap<>();
                            map.put("userList",userList);
                            map.put("courseName",course.getName());
                            eduApi.delUser(map);
                        }
                    }
                }
        }
        //??????????????????????????????
        Map map=new HashMap();
        map.put("courseId",reqDeleteClassCourse.getCourseId());
        map.put("classId",reqDeleteClassCourse.getClassId());
        map.put("teacherId",reqDeleteClassCourse.getTeacherId());
        map.put("semester",reqDeleteClassCourse.getSemester());
        map.put("schoolYear",reqDeleteClassCourse.getSchoolYear());
        classCourseMapper.deleteByClassIdAndCourseId(map);
        //?????????????????????
        map=new HashMap();
        map.put("classId",reqDeleteClassCourse.getClassId());
        map.put("courseId",reqDeleteClassCourse.getCourseId());
        jobScoreMapper.deleteByCidAndCouid(map);
    }

    @Override
    public Clazz findClassByNameAndCId(ReqUpdateClass reqUpdateClass) {
        Map<String,Object> map=new HashMap<>();
        map.put("className",reqUpdateClass.getClassName());
        map.put("classId",reqUpdateClass.getClassId());
        return clazzMapper.findClassByNameAndCId(map);

    }

    @Override
    public ResClassInfos getClassByTeacherId(int teacherId) {
     List<Map> mapList=classCourseMapper.getClassAndCourseByTeacher(teacherId);
     ResClassInfos resClassInfos=new ResClassInfos();
        List<ResClassInfos.ResClassInfo> resClassInfoList = new ArrayList<>();
     if(mapList!=null && mapList.size()>0){
         for(Map map:mapList){
             ResClassInfos.ResClassInfo resClassInfo = new ResClassInfos.ResClassInfo();
             resClassInfo.setClassId(Integer.parseInt(map.get("classId").toString()));
             resClassInfo.setClassName((String)map.get("className"));
             resClassInfo.setStudentNum(Integer.parseInt(map.get("studentNum").toString()));
             resClassInfoList.add(resClassInfo);
         }
         resClassInfos.setClassInfoList(resClassInfoList);
     }
        return resClassInfos;
    }

    @Override
    public Clazz findByClassName(String className) {
        Clazz clazz=clazzMapper.getClassByClassName(className);
        return clazz;
    }
}
