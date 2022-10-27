package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.ClazzMapper;
import com.eurlanda.edu.tp.dao.mapper.CourseMapper;
import com.eurlanda.edu.tp.dao.mapper.StudentHomeworkMapper;
import com.eurlanda.edu.tp.dao.mapper.TeacherMapper;
import com.eurlanda.edu.tp.dao.mapper.UserMapper;
import com.eurlanda.edu.tp.enums.GenderEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.webdomain.response.ResBatchAddTeacher;
import com.eurlanda.edu.tp.webdomain.response.ResClassDetail;
import com.eurlanda.edu.tp.webdomain.response.ResTeacherClassList;
import com.eurlanda.edu.tp.webdomain.response.ResTeacherInfo;
import com.eurlanda.edu.tp.webdomain.response.ResTeacherList;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.enums.TitleEnum;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.excel.ExcelUtil;
import com.eurlanda.edu.tp.excel.domain.ExcelTeacher;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteTeacher;
import com.eurlanda.edu.tp.webdomain.request.ReqHomeworkGrade;
import com.eurlanda.edu.tp.webdomain.request.ReqUpdateTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3.x2000.x09.xmldsig.ObjectType;

import java.io.IOException;
import java.util.*;

@Service
public class TeacherServiceImp implements TeacherService {

    @Value("${default.password}")
    private String defaultPassword;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private ClassService classService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentHomeworkMapper studentHomeworkMapper;

    @Autowired
    private StudentHomeworkService studentHomeworkService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private CloudwareService cloudwareService;

    @Autowired
    private RancherService rancherService;
    @Override
    public Teacher getTeacherByUserId(int teacherID) throws ApplicationErrorException {

        Teacher teacher = teacherMapper.selectByUserId(teacherID);
        if(teacher == null){
            throw new ApplicationErrorException(ErrorCode.TeacherNotExists);
        }

        return teacher;
    }

    @Override
    public ResTeacherClassList getAllTeacherClassInfoByUserId(int teacherUserId) throws ApplicationErrorException {
        if(teacherMapper.selectByUserId(teacherUserId) == null){
            throw new ApplicationErrorException(ErrorCode.TeacherNotExists);
        }

        ResTeacherClassList resTeacherClassList = new ResTeacherClassList();
        List<ResClassDetail> resTeacherClasses = new ArrayList<>();
        resTeacherClassList.setTeacherClassList(resTeacherClasses);

        for (Map clazzInfo : clazzMapper.selectAllClassInfoByTeacherUserId(teacherUserId)) {
            ResClassDetail resClassDetailInfo = new ResClassDetail();
            resTeacherClasses.add(resClassDetailInfo);

            resClassDetailInfo.setClassId((int)clazzInfo.get("classId"));
            resClassDetailInfo.setClassName((String) clazzInfo.get("className"));
           /* resClassDetailInfo.setTerm(new Term(
                    (String) clazzInfo.get("year"),
                    (int) clazzInfo.get("semester")
            ).getDescription());*/
            resClassDetailInfo.setSchoolYear((String)clazzInfo.get("schoolYear"));
            if(clazzInfo.get("semester")!=null && !clazzInfo.get("semester").equals("")){
                resClassDetailInfo.setSemester(Integer.parseInt(clazzInfo.get("semester").toString()));
            }
            resClassDetailInfo.setImage((String) clazzInfo.get("url"));
            resClassDetailInfo.setDuration((String) clazzInfo.get("duration"));
            resClassDetailInfo.setStudentNumber((int) clazzInfo.get("studentNum"));
            resClassDetailInfo.setCourseDescription((String) clazzInfo.get("description"));
            resClassDetailInfo.setCourseId((int) clazzInfo.get("courseId"));
            resClassDetailInfo.setCourseName((String) clazzInfo.get("courseName"));
            resClassDetailInfo.setTeacherContract((String) clazzInfo.get("email"));
            resClassDetailInfo.setTeacherName((String) clazzInfo.get("teacherName"));
            resClassDetailInfo.setClassDate(Utility.formatDate((Date) clazzInfo.get("classDate")));
        }

        return resTeacherClassList;
    }

    @Override
    public void gradeHomework(ReqHomeworkGrade homeworkGrade) throws ApplicationErrorException {
        StudentHomework studentHomework = studentHomeworkService.getStudentHomeworkById(
                homeworkGrade.getStudentHomeworkId());

        if(studentHomework == null){
            throw new ApplicationErrorException(ErrorCode.StudentHomeworkNotExists);
        }

        Integer cloudwareId = studentHomework.getCloudwareId();
        studentHomework.setComment(homeworkGrade.getComment());
        studentHomework.setScore(homeworkGrade.getGrade());
        studentHomework.setCloudwareId(null);
        studentHomeworkMapper.updateByPrimaryKey(studentHomework);

        if(cloudwareId != null){
            cloudwareService.deleteCloudware(cloudwareId);
        }
    }

    @Override
    public ResTeacherList getAllTeacherList() throws ApplicationErrorException {
        ResTeacherList teacherList = new ResTeacherList();
        List<ResTeacherInfo> teacherInfoList = new ArrayList<>();
        teacherList.setTeacherInfoList(teacherInfoList);

        for (Teacher teacher : teacherMapper.getAllTeachers()){
            ResTeacherInfo teacherInfo = new ResTeacherInfo(teacher);
            teacherInfoList.add(teacherInfo);
        }

        return teacherList;
    }

    @Override
    public ResTeacherList getAllTeachersBylimit(int page,int limit) {
        int start = (page-1)*limit;
        Map<String,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("limit",limit);
        List<Teacher> teachers = teacherMapper.getAllTeachersBylimit(map);
        List<ResTeacherInfo> teacherInfoList = new ArrayList<>();
        for(Teacher teacher : teachers){
            ResTeacherInfo teacherInfo = new ResTeacherInfo(teacher);
            teacherInfoList.add(teacherInfo);
        }
        int count = teacherMapper.getCount();
        ResTeacherList teacherList = new ResTeacherList();
        teacherList.setCount(count);
        teacherList.setTeacherInfoList(teacherInfoList);
        return teacherList;
    }


    @Override
    public List<Course> getAllTeacherAssociateCourses(Integer teacherId) throws ApplicationErrorException{
        ResTeacherClassList list = getAllTeacherClassInfoByUserId(teacherId);
        Set<Course> coursesSet = new HashSet<>();
        for (ResClassDetail detail :list.getTeacherClassList()){
            Course course = new Course();
            course.setId(detail.getCourseId());
            coursesSet.add(course);
        }
        //课程与老师解除关联
        /*List<Course> courses = courseMapper.selectCourseByTeacherId(teacherId);
        coursesSet.addAll(courses);*/
        List<Course> result = new ArrayList<>();
        result.addAll(coursesSet);
        return result;
    }

    @Override
    public void createTeacher(ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException {
        User user = userMapper.selectByUserName(reqUpdateTeacher.getTeacherNo());
        if(user != null){
            if(user.getRole()==RoleEnum.TEACHER.getCode()) {
                throw new ApplicationErrorException(ErrorCode.TeacherAlreadyExists, user.getUsername());
            } else {
                throw new ApplicationErrorException(ErrorCode.USERISNOTTEACHER);
            }
        }
        validateTeacher(reqUpdateTeacher);

        User newUser = managerService.createUser(reqUpdateTeacher.getTeacherNo(), RoleEnum.TEACHER);
        Teacher teacher = new Teacher();
        teacher.setUserId(newUser.getId());
        teacher.setTno(reqUpdateTeacher.getTeacherNo());
        teacher.setName(reqUpdateTeacher.getTeacherName());
        teacher.setTitle(reqUpdateTeacher.getTeacherTitleId());
        teacher.setGender(reqUpdateTeacher.getGender());
        teacher.setEmail(reqUpdateTeacher.getTeacherContact());

        teacherMapper.insert(teacher);
    }

    @Override
    public void updateTeacher(ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException {
        Teacher teacher = teacherMapper.selectByUserId(reqUpdateTeacher.getId());

        if(teacher == null){
            throw new ApplicationErrorException(ErrorCode.TeacherNotExists);
        }
        if( !teacher.getTno().equals(reqUpdateTeacher.getTeacherNo())){
            //If teacher No is changed
            if(userMapper.selectByUserName(reqUpdateTeacher.getTeacherNo()) != null){
                throw new ApplicationErrorException(ErrorCode.TeacherAlreadyExists, reqUpdateTeacher.getTeacherNo());
            }
        }

        validateTeacher(reqUpdateTeacher);

        teacher.setTitle(reqUpdateTeacher.getTeacherTitleId());
        teacher.setEmail(reqUpdateTeacher.getTeacherContact());
        teacher.setTno(reqUpdateTeacher.getTeacherNo());
        teacher.setName(reqUpdateTeacher.getTeacherName());
        teacher.setGender(reqUpdateTeacher.getGender());
        teacherMapper.updateByUserId(teacher);

        User user = userMapper.selectByPrimaryKey(teacher.getUserId());
        user.setUsername(reqUpdateTeacher.getTeacherNo());
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void deleteTeacherByTeacherUserId(ReqDeleteTeacher reqDeleteTeacher) throws ApplicationErrorException {
        int teacherId = reqDeleteTeacher.getTeacherId();

        Teacher teacher = teacherMapper.selectByUserId(teacherId);
        if (teacher == null)
            throw new ApplicationErrorException(ErrorCode.TeacherNotExists);
        //课程已和老师解除关联因此不再做检验
        /*if (courseMapper.isTeacherUsedByCourse(teacherId)) {
            throw new ApplicationErrorException(ErrorCode.TeacherIsUsedByCourse);
        }*/
        if (clazzMapper.isTeacherUsedByClass(teacherId)) {
            throw new ApplicationErrorException(ErrorCode.TeacherIsUsedByClass);
        }

        User user = userService.getUserInfo(reqDeleteTeacher.getTeacherId());
        String volumeId = user.getVolumeId();
        teacherMapper.deleteByUserId(teacherId);
        userService.deleteUserById(teacher.getUserId());
        if(volumeId != null){
            rancherService.deleteVolume(volumeId);
        }
    }

    @Override
    public ResBatchAddTeacher batchTeacherCreation(MultipartFile file) throws ApplicationErrorException, IOException {
        ResBatchAddTeacher resBatchAddTeacher = new ResBatchAddTeacher();
        List<ResBatchAddTeacher.FailureReason> failureReasonList = new ArrayList<>();

        int success = 0;
        int failure = 0;

        ExcelTeacher excelTeacher = ExcelUtil.teacherExcelAnalysis(file);
        for (ExcelTeacher.ExcelTeacherElement excelTeacherElement : excelTeacher.getExcelTeacherElementList()) {
            try {
                ReqUpdateTeacher updateTeacher = new ReqUpdateTeacher();
                // 校验编号
                if(excelTeacherElement.getTeacherNum()==null || excelTeacherElement.getTeacherNum().equals("")){
                    throw  new ApplicationErrorException(ErrorCode.TEACHERNoTNULLNo);
                }
                if(excelTeacherElement.getTeacherNum().trim().length()>45){
                    throw  new ApplicationErrorException(ErrorCode.TEACHERNOLENGTH,45);
                }
                if(!excelTeacherElement.getTeacherNum().matches("[0-9]{1,45}")){
                    throw  new ApplicationErrorException(ErrorCode.TEACHERNovalidNo);
                }
                updateTeacher.setTeacherNo(excelTeacherElement.getTeacherNum());
                if(excelTeacherElement.getTeacherName() == null || excelTeacherElement.getTeacherName().trim().length()==0){
                    throw  new ApplicationErrorException(ErrorCode.NameIsNULL);
                }
                if(excelTeacherElement.getTeacherName() != null && excelTeacherElement.getTeacherName().trim().length()>45){
                    throw  new ApplicationErrorException(ErrorCode.NameLENGTH,45);
                }
                updateTeacher.setTeacherName(excelTeacherElement.getTeacherName());
                //校验性
                try{
                    GenderEnum genderEnum=GenderEnum.parse(excelTeacherElement.getGenderStr());
                    if(genderEnum==null){
                        throw  new ApplicationErrorException(ErrorCode.InvalidGender);
                    }else{
                        updateTeacher.setGender(genderEnum.getCode());
                    }
                }catch (Exception e){
                    throw  new ApplicationErrorException(ErrorCode.InvalidGender);
                }
                //校验编号
                try{
                    TitleEnum titleEnum=TitleEnum.parse(excelTeacherElement.getTeacherTitleStr());
                    if(titleEnum==null){
                        throw new ApplicationErrorException(ErrorCode.InvalidTeacherTitle);
                    }else{
                        updateTeacher.setTeacherTitleId(titleEnum.getCode());
                    }
                }catch (Exception e){
                    throw new ApplicationErrorException(ErrorCode.InvalidTeacherTitle);
                }
                //校验老师联系方式
                if(!excelTeacherElement.getTeacherContact().matches("[A-Za-z0-9\\u4e00-\\u9fa5+\\.+\\-+\\_]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+")
                        && !excelTeacherElement.getTeacherContact().matches("1\\d{10}")){
                    throw new ApplicationErrorException(ErrorCode.CONCAT_IS_VALID);
                }else{
                    if(excelTeacherElement.getTeacherContact().trim().length()>45){
                        throw new ApplicationErrorException(ErrorCode.PhoneLength,45);
                    }
                }
                updateTeacher.setTeacherContact(excelTeacherElement.getTeacherContact());

                this.createTeacher(updateTeacher);

                success += 1;
            } catch (ApplicationErrorException e) {
                ResBatchAddTeacher.FailureReason failureReason = new ResBatchAddTeacher().new FailureReason();
                failureReason.setTeacherNum(excelTeacherElement.getTeacherNum());
                failureReason.setTeacherName(excelTeacherElement.getTeacherName());
                // todo
                failureReason.setReason(e.getMessage());
                failureReasonList.add(failureReason);

                failure += 1;
            }
        }
        resBatchAddTeacher.setSuccess(success);
        resBatchAddTeacher.setFailure(failure);
        resBatchAddTeacher.setFailureReasonList(failureReasonList);

        return resBatchAddTeacher;
    }

    @Override
    public int getCount() {
        return teacherMapper.getCount();
    }

    private void validateTeacher(ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException {

        TitleEnum title = TitleEnum.fromInt(reqUpdateTeacher.getTeacherTitleId());
        if(title == null){
            throw new ApplicationErrorException(ErrorCode.InvalidTitle);
        }

        //Utility.validateEmail(reqUpdateTeacher.getTeacherContact());
    }

    @Override
    public List<Map<String,Object>> selectTeacherIsShulieyunByUserId(int cloudware_type,int teacher_id) {
        Map<String,Object> map = new HashMap<>();
        map.put("cloudware_type",cloudware_type);
        map.put("teacher_id",teacher_id);
        return teacherMapper.selectTeacherIsShulieyunByUserId(map);
    }

    /**
     * 判断教师编号是否存在，存在 true  不存在false
     * @param reqUpdateTeacher
     * @return
     */
    @Override
    public Boolean checkTnoIsExist(ReqUpdateTeacher reqUpdateTeacher) {
        Teacher teacher = teacherMapper.selectByUserId(reqUpdateTeacher.getId());

        if(teacher == null){
            return true;
        }
        if( !teacher.getTno().equals(reqUpdateTeacher.getTeacherNo())){
            //If teacher No is changed
            if(userMapper.selectByUserName(reqUpdateTeacher.getTeacherNo()) != null){
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断该课程是否是数猎云课程
     * @param cloudware_type,teacher_id
     * @param teacher_id
     * @return
     */
    public List<Map<String,Object>> selectTeacherIsShulieyunByTeacherId(int cloudware_type, int teacher_id){
        Map<String,Object> map = new HashMap<>();
        map.put("cloudware_type",cloudware_type);
        map.put("teacher_id",teacher_id);
        return teacherMapper.selectTeacherIsShulieyunByTeacherId(map);
    }

}
