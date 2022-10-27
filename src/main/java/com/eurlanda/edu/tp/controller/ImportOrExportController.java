package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.mapper.CourseMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.CourseService;
import com.eurlanda.edu.tp.service.ImportAndExportService;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by test on 2018/5/5.
 */
@CrossOrigin
@RestController
@RequestMapping("port")
public class ImportOrExportController {

    static Logger log = Logger.getLogger(ImportOrExportController.class.getName());


    @Autowired
    CourseService courseService;

    @Autowired
    private ImportAndExportService importAndExportService;

    @ApiOperation(value = "根据课程id导出课程对应的数据")
    @GetMapping(value = "/exportCourse")
    public ResponseMessage exportCourseData(String courseName, HttpServletResponse response) throws
            ApplicationErrorException {


        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + "course.deep");
        response.setCharacterEncoding("UTF-8");

        OutputStream out = null;
        OutputStream zipOut = null;
        try {
            out = response.getOutputStream();
            Course course = courseService.selectCourseByCourseName(courseName);
            Integer courseId = null;
            if(course == null){
                throw new ApplicationErrorException(ErrorCode.CourseNotExists);
            }else {
                courseId = course.getId();
            }
            zipOut = importAndExportService.exportCourseData(courseId, out);
            out.flush();
        } catch (Exception e) {
            response.setStatus(500);
            e.printStackTrace();
            log.error("exportCourseData错误：" + e.getMessage(),e);
            throw new ApplicationErrorException(ErrorCode.ExportError);
            //return new ResponseMessage.Fail(ErrorCode.ExportError.getCode(),ErrorCode.ExportError.getErrorStringFormat());
        } finally {
            try {
                if (zipOut != null) {
                    zipOut.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApplicationErrorException(ErrorCode.ExportError);

            }
        }
        return new ResponseMessage.Success<>();

    }


    @ApiOperation(value = "导入课程")
    @PostMapping(value = "/importCourse")
    public ResponseMessage importCourseData(@RequestParam("file") MultipartFile file) throws
            ApplicationErrorException{
        try {
            importAndExportService.importCourseData(file);
        }catch (Exception e){
            e.printStackTrace();
            log.error("importCourseData错误：" + e.getMessage(),e);
            throw new ApplicationErrorException(ErrorCode.ImportError);
        }
        return new ResponseMessage.Success<>();
    }
}
