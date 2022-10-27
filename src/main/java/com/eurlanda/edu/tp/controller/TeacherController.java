package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.webdomain.request.ReqHomeworkGrade;
import com.eurlanda.edu.tp.webdomain.response.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private ClassService classService;
    @Autowired
    private JobScoreService jobScoreService;

    @ApiOperation(value = "选课列表", notes = "列出所有该教师的班级列表")
    @GetMapping(value = "{teacherId}/course/all")
    public ResponseMessage<ResTeacherClassList> getAllTeacherCourses(@PathVariable int teacherId) throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(teacherService.getAllTeacherClassInfoByUserId(teacherId));
    }


    @ApiOperation(value = "查询与老师关联课程", notes = "")
    @GetMapping(value = "{teacherId}/course/associate")
    public ResponseMessage<List<Course>> getAllTeacherAssociateCourses(@PathVariable int teacherId) throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(teacherService.getAllTeacherAssociateCourses(teacherId));
    }


    @ApiOperation(value = "课程详情", notes = "列出所有该课程的课时以及属于这些课时的所有实验内容")
    @GetMapping(value = "course/{classId}")
    public ResponseMessage<ResCourseModuleExperiments> getCourseExperiments(@PathVariable int classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getClassModuleExperiments(classId));
    }

    @ApiOperation(value = "所有学生作业详情", notes = "列出该课时该班级下所有学生作业的完成情况")
    @GetMapping(value = "course/{moduleId}/{classId}/homework")
    public ResponseMessage<ResHomeworkSubmissionList> getAllHomeworkSubmissionByModuleId(
            @PathVariable int moduleId, @PathVariable int classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(homeworkService.getAllHomeworkSubmissionByModuleId(moduleId, classId));
    }

    @ApiOperation(value = "单个学生作业详情", notes = "")
    @GetMapping(value = "course/homework/{studentHomeworkId}")
    public ResponseMessage<ResStudentHomeworkDetail> getStudentHomeworkDetailById(@PathVariable int studentHomeworkId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(homeworkService.getStudentHomeworkDetailById(studentHomeworkId));
    }

    @ApiOperation(value = "批改作业", notes = "")
    @PostMapping(value = "course/homework/grade")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage gradeHomework(@RequestBody ReqHomeworkGrade homeworkGrade) throws ApplicationErrorException {
        teacherService.gradeHomework(homeworkGrade);
        return new ResponseMessage.Success();
    }

    @ApiOperation(value = "获取老师所在班级的作业列表", notes = "")
    @GetMapping(value = "course/homework/all/{teacherId}")
    public ResponseMessage<ResTeacherHomeworkList> getHomeworkListByTeacherId(@PathVariable int teacherId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(homeworkService.getHomeworkListByTeacherId(teacherId));
    }

    @ApiOperation(value = "获取老师所在的班级", notes = "")
    @GetMapping(value = "class/all/{teacherId}")
    public ResponseMessage<ResClassInfos> getClassByTeacherId(@PathVariable int teacherId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(classService.getClassByTeacherId(teacherId));
    }
    @ApiOperation(value = "获取老师所在的班级对应课程及实验", notes = "")
    @PostMapping(value = "class/course")
    public ResponseMessage<List<Map<String, List<Map<String,Object>>>>> getCouExpByTeaIdAndCId(@RequestParam("teacherId")int teacherId, @RequestParam("classId")int classId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getCouExpByTeaIdAndCId(teacherId,classId));
    }
    @ApiOperation(value = "老师查看班级成绩", notes = "")
    @PostMapping(value = "class/score")
    public ResponseMessage<ResJobScoreList> findClassScore(@RequestParam("classId")int classId, @RequestParam("courseId")int courseId,@RequestParam("experimentId")int experimentId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(jobScoreService.findScoreByCidAndCouidAndExpid(classId,courseId,experimentId));
    }
    @ApiOperation(value = "根据课程名称查询课程信息", notes = "")
    @PostMapping(value = "course/courseInfo")
    public ResponseMessage<Course> findCourseByName(@RequestParam("courseName")String courseName) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.selectByCourseName(courseName));
    }

    @ApiOperation(value = "导出班级学生成绩", notes = "")
    @GetMapping(value = "class/export")
    public void exportClassScore(HttpServletResponse response, @RequestParam("classId")int classId,
                                                    @RequestParam("courseId")int courseId,
                                                    @RequestParam("experimentId")int experimentId,
                                                    @RequestParam("className")String className,
                                                    @RequestParam("courseName")String courseName,
                                                    @RequestParam("moduleName")String moduleName,
                                                    @RequestParam("experimentName")String experimentName) throws ApplicationErrorException {
        OutputStream outPutStream = null;
        OutputStreamWriter osw=null;
        try {
            outPutStream = response.getOutputStream();
             osw = new OutputStreamWriter(outPutStream,"UTF-8");
            String csvName =className+"/"+courseName+"/"+moduleName+"/"+experimentName+".csv";
            String filename = new String(csvName.getBytes("utf-8"), "ISO-8859-1");
            response.setContentType("application/force-download");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
            response.setCharacterEncoding("UTF-8");
            //查询班级成绩
            ResJobScoreList resJobScoreList=jobScoreService.findScoreByCidAndCouidAndExpid(classId,courseId,experimentId);
            osw.write("姓名");
            osw.write(",");
            osw.write("学号");
            osw.write(",");
            osw.write("性别");
            osw.write(",");
            osw.write("提交时间");
            osw.write(",");
            osw.write("分数");
            osw.write("\r\n");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            if(resJobScoreList!=null && resJobScoreList.getJobScoreList()!=null && resJobScoreList.getJobScoreList().size()>0){
                try{
                    if(resJobScoreList.getJobScoreList()!=null && resJobScoreList.getJobScoreList().size()>0){
                        Collections.sort(resJobScoreList.getJobScoreList(), new Comparator<ResJobScore>() {
                            @Override
                            public int compare(ResJobScore o1, ResJobScore o2) {
                                long i =Long.parseLong( o1.getSno()) - Long.parseLong(o2.getSno());
                                if(i == 0){
                                    return o1.getScore() - o2.getScore();
                                }
                                int returnValue = 0;
                                if(i>0){
                                    returnValue = 1;
                                } else if(i<0){
                                    returnValue = -1;
                                }
                                return returnValue;
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                for(ResJobScore resJobScore:resJobScoreList.getJobScoreList()){
                    osw.write(String.valueOf(resJobScore.getStudentName()));
                    osw.write(",");
                    osw.write(String.valueOf(resJobScore.getSno()));
                    osw.write(",");
                    if(resJobScore.getGender()==1){
                        osw.write("男");
                    }else if(resJobScore.getGender()==2){
                        osw.write("女");
                    }else{
                        osw.write("未知");
                    }
                    osw.write(",");
                    if(resJobScore.getCreateTime()==null){
                        osw.write("未提交");
                    }else{
                        String date=sdf.format(resJobScore.getCreateTime());
                        osw.write(date);
                    }
                    osw.write(",");
                    if(resJobScore.getCreateTime()!=null && resJobScore.getScore()==null){
                        osw.write("批改中");
                    }else if(resJobScore.getCreateTime()==null){
                        osw.write("0");
                    }else{
                        osw.write(String.valueOf(resJobScore.getScore()));
                    }
                    osw.write("\r\n");
                    osw.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(osw!=null){
                try{
                    osw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(outPutStream!=null){
                try{
                    outPutStream.close();
                }catch (Exception e){
                }

            }
        }
    }
}
