package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.dao.entity.Clazz;
import com.eurlanda.edu.tp.dao.entity.Cloudware;
import com.eurlanda.edu.tp.dao.mapper.ClazzMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.webdomain.request.*;
import com.eurlanda.edu.tp.webdomain.response.*;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.mapreduce.v2.app.webapp.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private StudentHomeworkService studentHomeworkService;

    @Autowired
    private StudentExperimentService studentExperimentService;

    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private JobScoreService jobScoreService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ClassService classService;

    @ApiOperation(value = "选课列表", notes = "列出所有该学生的班级列表")
    @GetMapping(value = "course/all/{studentId}")
    public ResponseMessage<ResStudentClassList> getAllStudentCourses(@PathVariable int studentId) throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(studentService.getAllStudentClassInfoByUserId(studentId));
    }

    @ApiOperation(value = "课程详情", notes = "列出所有该课程的课时以及属于这些课时的所有实验内容")
    @GetMapping(value = "course/{classId}/detail")
    public ResponseMessage<ResCourseModuleExperiments> getCourseExperiments(@PathVariable int classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getClassModuleExperiments(classId));
    }

    @ApiOperation(value = "班级详情", notes = "列出该班级所属课程的的课时以及属于这些课时的所有作业")
    @GetMapping(value = "course/{classId}/{studentId}/homework")
    public ResponseMessage<ResCourseHomeworks> getCourseHomeworks(@PathVariable int classId, @PathVariable int studentId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getCourseHomeworks(classId, studentId));
    }

    @ApiOperation(value = "作业详情", notes = "")
    @GetMapping(value = "homework/{homeworkId}")
    public ResponseMessage<ResHomeworkDetail> getHomeworkDetail(@PathVariable int homeworkId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(homeworkService.getHomeworkDetail(homeworkId));
    }

    @ApiOperation(value = "提交作业", notes = "")
    @PostMapping(value = "homework/submission")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage submitHomeWork(@RequestBody ReqHomeworkSubmission homeworkSubmission) throws ApplicationErrorException {
        studentHomeworkService.submitStudentHomework(homeworkSubmission);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "获取学生作业云件", notes = "")
    @GetMapping(value = "homework/{homeworkId}/{studentId}/cloudware")
    public ResponseMessage<Cloudware> getStudentHomeworkCloudware(@PathVariable int homeworkId, @PathVariable int studentId) throws ApplicationErrorException {
        return new ResponseMessage.Success(studentHomeworkService.getStudentHomeworkCloudware(homeworkId, studentId));
    }

    @ApiOperation(value = "获取学生实验云件", notes = "")
    @GetMapping(value = "experiment/{experimentId}/{studentId}/cloudware")
    public ResponseMessage<Cloudware> getStudentExperimentCloudware(@PathVariable int experimentId, @PathVariable int studentId) throws ApplicationErrorException {
        return new ResponseMessage.Success(studentExperimentService.getStudentExperimentCloudware(experimentId, studentId));
    }

    @ApiOperation(value = "创建学生作业以及云件", notes = "")
    @PostMapping(value = "homework/creation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage createStudentHomeworkCloudware(@RequestBody
    ReqStudentHomeworkCloudware reqStudentHomeworkCloudware) throws ApplicationErrorException {
        studentHomeworkService.createStudentHomeworkCloudware(reqStudentHomeworkCloudware);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "创建学生实验以及云件", notes = "")
    @PostMapping(value = "experiment/creation")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage createStudentExperimentCloudware(@RequestBody
    ReqStudentExperimentCloudware reqStudentExperimentCloudware) throws ApplicationErrorException {
        studentExperimentService.createStudentExperimentCloudware(reqStudentExperimentCloudware);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "单个学生作业详情", notes = "")
    @GetMapping(value = "course/homework/{homeworkId}/{studentId}")
    public ResponseMessage<ResStudentHomeworkDetail> getStudentHomeworkDetailById(@PathVariable int homeworkId, @PathVariable int studentId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(homeworkService.getStudentHomeworkDetailByHomeworkIdAndStudentId(homeworkId, studentId));
    }

    @ApiOperation(value = "获取单个学生作业列表", notes = "")
    @GetMapping(value = "course/homework/all/{studentId}")
    public ResponseMessage<ResStudentHomeworkList> getStudentHomeworkListById(@PathVariable int studentId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(homeworkService.getStudentHomeworkListById(studentId));
    }

    @ApiOperation(value = "删除学生实验信息及云件", notes = "")
    @PostMapping(value = "experiment/delete")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteStudentExperiment(@RequestBody ReqStudentExperiment reqStudentExperiment) throws ApplicationErrorException {
        studentExperimentService.deleteStudentExperiment(reqStudentExperiment.getExperimentId(),
                                                         reqStudentExperiment.getStudentId());
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "获取学生上次未完成实验信息", notes="为了通知用户一次只能开启一个实验容器")
    @GetMapping(value = "experiment/last/{studentId}")
    public ResponseMessage<ResStudentLastExperiment> getStudentLastExperiment(@PathVariable int studentId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(studentExperimentService.getStudentLastExperiment(studentId));
    }

    @ApiOperation(value="查询是否有编程类实现")
    @PostMapping(value="/selectCourseExistOtherExperimentByUserId")
    public ResponseMessage<Boolean> selectCourseExistOtherExperimentByUserId(@RequestParam Integer userId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.selectCourseExistOtherExperimentByUserId(userId));
    }

    @ApiOperation(value="学生提交数猎作业")
    @PostMapping(value="/submitHomeworkOfShuLie")
    public ResponseMessage submitHomeworkOfShuLie(@RequestBody ReqSubmitHomeworkOfShuLie reqSubmitHomeworkOfShuLie) throws ApplicationErrorException,IOException {
        try{
             studentService.submitHomeworkOfShuLie(reqSubmitHomeworkOfShuLie);
        }catch (ApplicationErrorException e){
            if(e.getErrorCode() == ErrorCode.DBTABLENOTEXISTS.getCode()){
                return new ResponseMessage.Fail(ErrorCode.DBTABLENOTEXISTS.getCode(),e.getMessage());
            }else if(e.getErrorCode() == ErrorCode.ANSWERNOTEXISTS.getCode()){
                return new ResponseMessage.Fail(ErrorCode.ANSWERNOTEXISTS.getCode(),e.getMessage());
            }else{
                throw e;
            }
        }
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value="学生提交非数猎作业")
    @PostMapping(value="/submitHomeworkOfOther")
    public ResponseMessage submitHomeworkOfOther(@RequestBody ReqSubmitHomeworkOfOther reqSubmitHomeworkOfOther) throws ApplicationErrorException,IOException {
        try {
            studentService.submitHomeworkOfOther(reqSubmitHomeworkOfOther);
        }catch (ApplicationErrorException e){
            if(e.getErrorCode() == ErrorCode.FileIsNotExist.getCode()){
                return new ResponseMessage.Fail(ErrorCode.FileIsNotExist.getCode(),e.getMessage());
            }else if(e.getErrorCode() == ErrorCode.ANSWERNOTEXISTS.getCode()){
                return new ResponseMessage.Fail(ErrorCode.ANSWERNOTEXISTS.getCode(),e.getMessage());
            }else{
                throw e;
            }
        }
        return new ResponseMessage.Success<>();
    }
    @ApiOperation(value="查询学生提交作业列表")
    @PostMapping(value="/getStudentSubmitHomework")
    public ResponseMessage<List<Map<String, Object>>> getStudentSubmitHomework(@RequestParam("className") String className,@RequestParam("courseId") Integer courseId,@RequestParam("experimentId") Integer experimentId,@RequestParam("userId") Integer userId,@RequestParam("type") Integer type) throws ApplicationErrorException {
        Clazz clazz=clazzMapper.getClassByClassName(className);
        if(clazz!=null){
            return new ResponseMessage.Success<>(studentService.getStudentSubmitHomework(clazz.getId(),courseId,experimentId,type,userId));
        }else{
            return new ResponseMessage.Success<>(new ArrayList<>());
        }

    }

    @ApiOperation(value="查询学生是否提交答案")
    @PostMapping(value="/getStudentIsSubAnswer")
    public ResponseMessage<String> getStudentIsSubAnswer(@RequestParam("className") String className,@RequestParam("courseId") Integer courseId,@RequestParam("experimentId") Integer experimentId,@RequestParam("userId") Integer userId) throws ApplicationErrorException {
        Clazz clazz=clazzMapper.getClassByClassName(className);
        if(clazz!=null){
            return new ResponseMessage.Success<>(jobScoreService.findStudentIsSubAnswer(clazz.getId(),courseId,experimentId,userId));
        }else{
            return new ResponseMessage.Success<>("");
        }

    }
    @ApiOperation(value="查询学生共享文件夹文件")
    @PostMapping(value="/getJobShareFile")
    public ResponseMessage<List<Map<String, Object>>> getJobShareFile(@RequestParam("userId") Integer userId,@RequestParam("path") String path,@RequestParam("type") Integer type) throws ApplicationErrorException,IOException {
        return new ResponseMessage.Success<>(fileService.getJobShareList(userId,path,type));

    }

    @ApiOperation(value="查询学生共享文件夹文件")
    @PostMapping(value="/findClassName")
    public ResponseMessage<Clazz> findClassName(@RequestParam("className") String className) throws ApplicationErrorException,IOException {
        return new ResponseMessage.Success<>(classService.findByClassName(className));

    }





}
