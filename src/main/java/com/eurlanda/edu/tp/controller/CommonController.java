package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.SysLicense;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.webdomain.request.ReqImportOrUpdateLicense;
import com.eurlanda.edu.tp.webdomain.response.*;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("common")
public class CommonController {

    @Value("${file.baseDir}")
    private String baseDir;

    @Value("${file.imageDir}")
    private String imageDir;

    @Value("${file.defaultImgPath}")
    private String defaultImgPath;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private FileService fileService;



    static Logger log = Logger.getLogger(CommonController.class.getName());
    @ApiOperation(value = "获取热门课程信息", notes = "")
    @GetMapping(value = "hotCourses/all")
    public ResponseMessage<ResHotCourseList> getHotCourses() {
        return new ResponseMessage.Success<>(courseService.getHotCourses());
    }
    @ApiOperation(value = "图片预览", notes = "")
    @GetMapping(value = "image/preview")
    public void previewImage(int courseId, HttpServletResponse response){
        //查询出课程的图片信息
        try {
            Course course = courseService.getCourseById(courseId);
            String imgurl = null;
            if(course.getImageUrl()==null || course.getImageUrl().trim().length()==0){
                //图片不存在，展示默认图片
                imgurl= baseDir+imageDir+"/"+defaultImgPath;
            } else {
                imgurl = course.getImageUrl();
            }
            if(imgurl.startsWith(baseDir + imageDir)){
                fileService.previewImage(imgurl, response);
            }else {
                fileService.previewImage(baseDir + imageDir + "/" + imgurl,response);
            }
        } catch (ApplicationErrorException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "获取课程实验信息", notes = "")
    @GetMapping(value = "course/{courseId}/experiments")
    public ResponseMessage<ResCourseModuleExperiments> getCourseExperiments(@PathVariable int courseId) throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getCourseModuleExperiments(courseId));
    }

    @ApiOperation(value = "获取课程实验信息", notes = "")
    @GetMapping(value = "course/{classId}/{courseId}/{userId}/getExperimentAndAnswer")
    public ResponseMessage<ResCourseModuleExperiments> getCourseExperimentDetailAndAnswer(@PathVariable Integer courseId,
    @PathVariable Integer userId,@PathVariable Integer classId) throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getCourseModuleExperimentsAndAnswer(courseId,userId,classId));
    }

    @ApiOperation(value = "获取实验信息", notes = "")
    @GetMapping(value = "experiment/{experimentId}")
    public ResponseMessage<ResExperimentInfo> getExperiment(@PathVariable int experimentId) throws ApplicationErrorException {
        return new ResponseMessage.Success(experimentService.getExperiment(experimentId));
    }

    @ApiOperation(value = "获取课程相关信息", notes = "包含老师姓名，班级数以及班级人数")
    @GetMapping(value = "course/{courseId}/detail")
    public ResponseMessage<ResCommonCourseDetail> getCourseCommonDetail(@PathVariable int courseId) throws ApplicationErrorException {
        return new ResponseMessage.Success<>(courseService.getCommonCourseDetail(courseId));
    }

    @ApiOperation(value = "获取网站统计信息", notes = "包含学生，课程，实验，老师数量")
    @GetMapping(value = "statistics")
    public ResponseMessage<ResStatistics> getStatistics() throws ApplicationErrorException {
        ResStatistics statistics = new ResStatistics(
                studentService.getCount(),
                teacherService.getCount(),
                courseService.getCount(),
                experimentService.getCount()
        );
        return new ResponseMessage.Success<>(statistics);
    }








}
