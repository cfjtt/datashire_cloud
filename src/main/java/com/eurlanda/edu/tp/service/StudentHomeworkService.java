package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentHomeworkCloudware;
import com.eurlanda.edu.tp.dao.entity.Cloudware;
import com.eurlanda.edu.tp.dao.entity.StudentHomework;
import com.eurlanda.edu.tp.webdomain.request.ReqHomeworkSubmission;

public interface StudentHomeworkService {
    void submitStudentHomework(ReqHomeworkSubmission homeworkSubmission) throws
            ApplicationErrorException;

    void validStudentHomeWork(int studentId, int homeworkId) throws ApplicationErrorException;

    StudentHomework getStudentHomeworkById(int studentHomeworkId) throws ApplicationErrorException;

    Cloudware getStudentHomeworkCloudware(int homeworkId, int studentId) throws ApplicationErrorException;

    void createStudentHomeworkCloudware(ReqStudentHomeworkCloudware reqStudentHomeworkCloudware) throws ApplicationErrorException;

    void deleteStudentHomework(int studentHomeworkId) throws ApplicationErrorException;
}
