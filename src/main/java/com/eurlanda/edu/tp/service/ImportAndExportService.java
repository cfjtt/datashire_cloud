package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by test on 2018/5/5.
 */
public interface ImportAndExportService {


    OutputStream exportCourseData(Integer courseId,OutputStream out) throws Exception;

    void importCourseData(MultipartFile file) throws Exception;
}
