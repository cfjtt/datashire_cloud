package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.Clazz;
import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.Homework;
import com.eurlanda.edu.tp.dao.entity.HomeworkResource;
import com.eurlanda.edu.tp.dao.entity.Module;
import com.eurlanda.edu.tp.dao.entity.Student;
import com.eurlanda.edu.tp.dao.entity.StudentHomework;
import com.eurlanda.edu.tp.dao.entity.Teacher;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.response.ResClassHomework;
import com.eurlanda.edu.tp.webdomain.response.ResHomeworkDetail;
import com.eurlanda.edu.tp.webdomain.response.ResHomeworkSubmissionList;
import com.eurlanda.edu.tp.webdomain.response.ResStudentHomeworkDetail;
import com.eurlanda.edu.tp.webdomain.response.ResStudentHomeworkList;
import com.eurlanda.edu.tp.webdomain.response.ResTeacherHomeworkList;
import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.enums.CloudwareTypeEnum;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.HomeworkService;
import com.eurlanda.edu.tp.service.StudentHomeworkService;
import com.eurlanda.edu.tp.webdomain.request.ReqCreateHomework;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteHomework;
import com.eurlanda.edu.tp.webdomain.request.ReqUpdateHomework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeworkServiceImp implements HomeworkService {

    @Value("${notice.limit}")
    private int noticeLimit;

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentHomeworkMapper studentHomeworkMapper;

    @Autowired
    private StudentHomeworkService studentHomeworkService;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private HomeworkResourceMapper homeworkResourceMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CloudwareMapper cloudwareMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Override
    public ResHomeworkDetail getHomeworkDetail(int homeworkID) throws ApplicationErrorException {
        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkID);
        if(homework == null){
            throw new ApplicationErrorException(ErrorCode.HomeworkNotExists);
        }

        ResHomeworkDetail homeworkDetail = new ResHomeworkDetail();

        Module module = moduleMapper.selectByPrimaryKey(homework.getModuleId());
        Course course = courseMapper.selectByPrimaryKey(module.getCourseId());

        homeworkDetail.setCourseName(course.getName());
        homeworkDetail.setModuleName(module.getName());
        homeworkDetail.setHomeworkName(homework.getName());
        homeworkDetail.setHomeworkDes(homework.getDescription());
        homeworkDetail.setClassId(homework.getClassId());
        CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt(homework.getCloudwareType());
        homeworkDetail.setCloudwareType(cloudwareType == null ? "" : cloudwareType.toString());
        homeworkDetail.setDueDate(Utility.formatDate(homework.getDeadlineDate()));
        homeworkDetail.setPublishDate(Utility.formatDate(homework.getPublishDate()));

        return homeworkDetail;
    }

    @Override
    public ResHomeworkSubmissionList getAllHomeworkSubmissionByModuleId(int moduleId, int classId) throws ApplicationErrorException {
        Module module = moduleMapper.selectByPrimaryKey(moduleId);
        Clazz clazz = clazzMapper.selectByPrimaryKey(classId);
        if(module == null){
            throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
        }
        if(clazz == null){
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }

        Course course = courseMapper.selectByPrimaryKey(module.getCourseId());
        List<Homework> homeworks = homeworkMapper.selectByModuleIdAndClassId(moduleId, classId);
        ResHomeworkSubmissionList submissionList = new ResHomeworkSubmissionList();
        List<ResHomeworkSubmissionList.ResHomeworkList> homeworkList =
                new ArrayList<>();
        submissionList.setHomeworkList(homeworkList);
        submissionList.setModuleName(module.getName());
        submissionList.setCourseName(course.getName());
        submissionList.setClassName(clazz.getName());

        for(Homework homework : homeworks){
            homeworkList.add(this.getHomeworkSubmissionDetail(homework));
        }

        return submissionList;
    }

    @Override
    public ResStudentHomeworkDetail getStudentHomeworkDetailByHomeworkIdAndStudentId(int homeworkId, int studentId) throws ApplicationErrorException {
        StudentHomework
                studentHomework = studentHomeworkMapper.selectByStudentIdAndHomeworkId(studentId, homeworkId);

        if(studentHomework == null){
            throw new ApplicationErrorException(ErrorCode.StudentHomeworkNotExists);
        }

        return getStudentHomeworkDetailById(studentHomework.getId());
    }

    @Override
    public ResStudentHomeworkDetail getStudentHomeworkDetailById(int studentHomeworkId) throws ApplicationErrorException {
        StudentHomework studentHomework = studentHomeworkService.getStudentHomeworkById(studentHomeworkId);

        ResStudentHomeworkDetail homeworkDetail = new ResStudentHomeworkDetail();

        Homework homework = homeworkMapper.selectByPrimaryKey(studentHomework.getHomeworkId());
        Module module = moduleMapper.selectByPrimaryKey(homework.getModuleId());
        Course course = courseMapper.selectByPrimaryKey(module.getCourseId());
        Student student = studentMapper.selectByUserId(studentHomework.getStudentId());

        homeworkDetail.setCourseName(course.getName());
        homeworkDetail.setModuleName(module.getName());
        homeworkDetail.setHomeworkName(homework.getName());
        homeworkDetail.setStudentId(student.getUserId());
        homeworkDetail.setStudentName(student.getName());
        homeworkDetail.setCloudwareUrl(studentHomework.getCloudwareUrl());
        homeworkDetail.setHomeworkUrl(studentHomework.getHomeworkUrl());
        homeworkDetail.setScore(studentHomework.getScore());
        homeworkDetail.setComment(studentHomework.getComment());

        return homeworkDetail;
    }

    @Override
    public int deleteHomework(ReqDeleteHomework reqDeleteHomework) throws ApplicationErrorException {
        int homeworkID = reqDeleteHomework.getHomeworkId();

        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkID);
        if (homework == null) {
            throw new ApplicationErrorException(ErrorCode.HomeworkNotExists);
        }

//        if (studentHomeworkMapper.isHomeworkUsedByStudentHomework(homeworkID)) {
//            throw new ApplicationErrorException(ErrorCode.HomeworkUsedByStudentHomework);
//        }

        for (Integer studentHomeworkId : studentHomeworkMapper.selectStudentHomeworkByHomeworkId(homeworkID)){
            studentHomeworkService.deleteStudentHomework(studentHomeworkId);
        }

        HomeworkResource homeworkResource = homeworkResourceMapper.selectByHomeworkId(homeworkID);
        if (homeworkResource != null) {
            homeworkResourceMapper.deleteByPrimaryKey(homeworkResource.getId());
            resourceMapper.deleteByPrimaryKey(homeworkResource.getResourceId());
        }
        homeworkMapper.deleteByPrimaryKey(homeworkID);
        return 0;
    }

    @Override
    public int updateHomework(ReqUpdateHomework reqUpdateHomework) throws ApplicationErrorException {
        Homework homework = homeworkMapper.selectByPrimaryKey(reqUpdateHomework.getHomeworkId());

        if(homework == null){
            throw new ApplicationErrorException(ErrorCode.HomeworkNotExists);
        }

        homework.setName(reqUpdateHomework.getHomeworkName());
        homework.setDescription(reqUpdateHomework.getHomeworkDes());
        if (reqUpdateHomework.getHomeworkCreateDate() != null) {
            homework.setPublishDate(reqUpdateHomework.getHomeworkCreateDate());
        }
        if (reqUpdateHomework.getHomeworkDueDate() != null) {
            homework.setDeadlineDate(reqUpdateHomework.getHomeworkDueDate());
        }

        homework.setCloudwareType(reqUpdateHomework.getCloudwareType());

        validate(homework);

        homeworkMapper.updateByPrimaryKeySelective(homework);
        return 0;
    }

    @Override
    public int createHomework(ReqCreateHomework reqCreateHomework) throws ApplicationErrorException {
        Homework homework = new Homework();
        homework.setClassId(reqCreateHomework.getClassId());
        homework.setModuleId(reqCreateHomework.getModuleId());
        homework.setName(reqCreateHomework.getHomeworkName());
        homework.setDescription(reqCreateHomework.getHomeworkDes());
        if (reqCreateHomework.getHomeworkCreateDate() != null) {
            homework.setPublishDate(reqCreateHomework.getHomeworkCreateDate());
        }
        if (reqCreateHomework.getHomeworkDueDate() != null) {
            homework.setDeadlineDate(reqCreateHomework.getHomeworkDueDate());
        }
        homework.setCloudwareType(reqCreateHomework.getCloudwareType());

        validate(homework);

        homeworkMapper.insertSelective(homework);
        return 0;
    }

    @Override
    public ResClassHomework getClassHomework(int classId) throws ApplicationErrorException {
        if(clazzMapper.selectByPrimaryKey(classId) == null){
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }

        ResClassHomework resClassHomework = new ResClassHomework();

        Clazz clazz = clazzMapper.selectByPrimaryKey(classId);
        Course course = courseMapper.selectByPrimaryKey(clazz.getCourseId());
        Teacher teacher = teacherMapper.selectByClassId(classId);
        resClassHomework.setClassId(classId);
        resClassHomework.setClassName(clazz.getName());
        resClassHomework.setCourseName(course.getName());
        ResClassHomework.ResClassHomeworkModule resClassHomeworkModule = new ResClassHomework().new ResClassHomeworkModule();

        List<ResClassHomework.ResClassHomeworkModule> modules = new ArrayList<>();
        resClassHomework.setModules(modules);

        for(Map moduleHomeworkInfo : homeworkMapper.selectHomeworkListByCourseAndClassId(course.getId(), classId)){
            int moduleId = (int)moduleHomeworkInfo.get("moduleId");
            if(moduleId != resClassHomeworkModule.getModuleId()){
                resClassHomeworkModule = new ResClassHomework().new ResClassHomeworkModule();
                modules.add(resClassHomeworkModule);
                resClassHomeworkModule.setModuleId(moduleId);
                resClassHomeworkModule.setModuleName((String)moduleHomeworkInfo.get("moduleName"));
                resClassHomeworkModule.setHomeworks(new ArrayList<>());
            }

            if(moduleHomeworkInfo.get("homeworkId") != null){
                ResClassHomework.ResClassHomeworkModuleHomework resClassHomeworkModuleHomework = new ResClassHomework().new ResClassHomeworkModuleHomework();
                resClassHomeworkModuleHomework.setHomeworkId((int)moduleHomeworkInfo.get("homeworkId"));
                resClassHomeworkModuleHomework.setHomeworkName((String)moduleHomeworkInfo.get("homeworkName"));
                resClassHomeworkModuleHomework.setHomeworkDes((String)moduleHomeworkInfo.get("homeworkDes"));
                resClassHomeworkModuleHomework.setHomeworkCreateDate(Utility.formatDate((Date) moduleHomeworkInfo.get("publishDate")));
                resClassHomeworkModuleHomework.setTeacherName(teacher.getName());
                resClassHomeworkModuleHomework.setHomeworkDueDate(Utility.formatDate((Date) moduleHomeworkInfo.get("dueDate")));
                CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt((int)moduleHomeworkInfo.get("cloudwareType"));
                resClassHomeworkModuleHomework.setCloudwareType(cloudwareType == null ? "" : cloudwareType.toString());

                resClassHomeworkModule.getHomeworks().add(resClassHomeworkModuleHomework);
            }
        }

        return resClassHomework;
    }

    @Override
    public ResStudentHomeworkList getStudentHomeworkListById(int studentId) throws ApplicationErrorException {
        ResStudentHomeworkList resStudentHomeworkList = new ResStudentHomeworkList();
        List<ResStudentHomeworkList.ResHomework> resHomeworkList = new ArrayList<>();

        for(Map homeworkInfo : studentClassMapper.getStudentHomeworkByStudentId(studentId)){
            ResStudentHomeworkList.ResHomework resHomework = new ResStudentHomeworkList().new ResHomework();
            resHomework.setId((int)homeworkInfo.get("homeworkId"));
            resHomework.setName((String)homeworkInfo.get("homeworkName"));
            resHomework.setDescription((String)homeworkInfo.get("homeworkDes"));
            CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt((int)homeworkInfo.get("cloudwareType"));
            resHomework.setCloudwareType(cloudwareType == null ? "" : cloudwareType.toString());
            resHomework.setPublishDate(Utility.formatDate((Date)homeworkInfo.get("publishDate")));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date)homeworkInfo.get("publishDate"));
            resHomework.setPublishMonth(calendar.get(Calendar.MONTH) + 1);
            resHomework.setPublishDay(calendar.get(Calendar.DATE));
            resHomework.setDeadlineDate(Utility.formatDate((Date)homeworkInfo.get("deadlineDate")));
            resHomework.setClassId((int)homeworkInfo.get("classId"));
            resHomework.setClassName((String)homeworkInfo.get("className"));
            resHomework.setTeacherName((String)homeworkInfo.get("teacherName"));

            boolean isCompleted = homeworkInfo.get("studentHomeworkId") != null &&
                    homeworkInfo.get("submissionDate") != null;
            resHomework.setComplete(isCompleted);

            resHomeworkList.add(resHomework);
        }

        resStudentHomeworkList.setResHomeworkList(resHomeworkList);
        return resStudentHomeworkList;
    }

    @Override
    public ResTeacherHomeworkList getHomeworkListByTeacherId(int teacherId) throws ApplicationErrorException {
        ResTeacherHomeworkList resTeacherHomeworkList = new ResTeacherHomeworkList();
        List<ResTeacherHomeworkList.ResHomework> resHomeworkList = new ArrayList<>();

        for(Map homeworkInfo : homeworkMapper.selectHomeworkDetailByTeacherId(teacherId)){
            ResTeacherHomeworkList.ResHomework resHomework = new ResTeacherHomeworkList().new ResHomework();
            resHomework.setClassId((int)homeworkInfo.get("classId"));
            resHomework.setClassName((String) homeworkInfo.get("className"));
            resHomework.setName((String) homeworkInfo.get("homeworkName"));
            resHomework.setDescription((String) homeworkInfo.get("description"));
            CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt((int) homeworkInfo.get("cloudwareType"));
            resHomework.setCloudwareType(cloudwareType == null ? "" : cloudwareType.toString());
            resHomework.setPublishDate(Utility.formatDate((Date) homeworkInfo.get("publishDate")));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) homeworkInfo.get("publishDate"));
            resHomework.setPublishMonth(calendar.get(Calendar.MONTH) + 1);
            resHomework.setPublishDay(calendar.get(Calendar.DATE));
            resHomework.setDeadlineDate(Utility.formatDate((Date) homeworkInfo.get("deadlineDate")));

            resHomeworkList.add(resHomework);
        }

        resTeacherHomeworkList.setResHomeworkList(resHomeworkList);
        return resTeacherHomeworkList;
    }

    private ResHomeworkSubmissionList.ResHomeworkList getHomeworkSubmissionDetail(
            Homework homework
    ){
        ResHomeworkSubmissionList.ResHomeworkList resHomeworkList = new ResHomeworkSubmissionList.ResHomeworkList();
        resHomeworkList.setHomeworkId(homework.getId());
        resHomeworkList.setHomeworkName(homework.getName());
        CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt(homework.getCloudwareType());
        resHomeworkList.setCloudwareType(cloudwareType == null ? "" : cloudwareType.toString());

        List<ResHomeworkSubmissionList.ResHomeworkSubmissionDetail> submissionDetails =
                new ArrayList<>();
        resHomeworkList.setHomeworkSubmissionList(submissionDetails);

        int completed = 0;
        int incomplete = 0;
        for(Map studentHomeworkInfo :
                studentClassMapper.getStudentHomeworkByClassAndHomeworkId(homework.getClassId(), homework.getId())){
            ResHomeworkSubmissionList.ResHomeworkSubmissionDetail submissionDetail =
                    new ResHomeworkSubmissionList.ResHomeworkSubmissionDetail();
            submissionDetails.add(submissionDetail);

            submissionDetail.setHomeworkId(homework.getId());
            submissionDetail.setStudentId((int)studentHomeworkInfo.get("studentId"));
            submissionDetail.setStudentNo((String) studentHomeworkInfo.get("sno"));
            submissionDetail.setStudentName((String) studentHomeworkInfo.get("studentName"));
            submissionDetail.setDueDate(Utility.formatDate(homework.getDeadlineDate()));

            if(studentHomeworkInfo.get("studentHomeworkId") == null ||
                    studentHomeworkInfo.get("submissionDate") == null){
                submissionDetail.setStudentHomeworkId(0);
                submissionDetail.setCompleted(false);
                incomplete++;
            } else {
                submissionDetail.setStudentHomeworkId((int)studentHomeworkInfo.get("studentHomeworkId"));
                submissionDetail.setCompleted(studentHomeworkInfo.get("submissionDate") != null);
                submissionDetail.setSubmissionDate(Utility.formatDate((Date)studentHomeworkInfo.get("submissionDate") ));
                submissionDetail.setLastEditDate(Utility.formatDate((Date)studentHomeworkInfo.get("lastEditDate")));
                submissionDetail.setScore((int)studentHomeworkInfo.get("score"));
                completed++;
            }
        }

        resHomeworkList.setCompletedCount(completed);
        resHomeworkList.setNonCompletedCount(incomplete);
        return resHomeworkList;
    }

    private void validate(Homework homework) throws ApplicationErrorException {
        CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt(homework.getCloudwareType());

        if(cloudwareType == null){
            throw new ApplicationErrorException(ErrorCode.InvalidCloudwareType);
        }

        Clazz clazz = clazzMapper.selectByPrimaryKey(homework.getClassId());
        if(clazz == null){
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }
        Module module = moduleMapper.selectByPrimaryKey(homework.getModuleId());
        if(module == null){
            throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
        }

        if(module.getCourseId() != clazz.getCourseId()){
            throw new ApplicationErrorException(ErrorCode.HomeworkModuleClassBelongsToDifferentCourse);
        }
    }
}
