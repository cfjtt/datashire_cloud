package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqCreateHomework;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteHomework;
import com.eurlanda.edu.tp.webdomain.request.ReqUpdateHomework;
import com.eurlanda.edu.tp.webdomain.response.ResClassHomework;
import com.eurlanda.edu.tp.webdomain.response.ResHomeworkDetail;
import com.eurlanda.edu.tp.webdomain.response.ResHomeworkSubmissionList;
import com.eurlanda.edu.tp.webdomain.response.ResStudentHomeworkDetail;
import com.eurlanda.edu.tp.webdomain.response.ResStudentHomeworkList;
import com.eurlanda.edu.tp.webdomain.response.ResTeacherHomeworkList;

public interface HomeworkService {
    ResHomeworkDetail getHomeworkDetail(int homeworkID) throws ApplicationErrorException;

    ResHomeworkSubmissionList getAllHomeworkSubmissionByModuleId(int moduleId, int classId) throws ApplicationErrorException;

    ResStudentHomeworkDetail getStudentHomeworkDetailById(int studentHomeworkId) throws ApplicationErrorException;

    ResStudentHomeworkDetail getStudentHomeworkDetailByHomeworkIdAndStudentId(int homeworkId, int studentId) throws ApplicationErrorException;

    int deleteHomework(ReqDeleteHomework reqDeleteHomework) throws ApplicationErrorException;

    int updateHomework(ReqUpdateHomework reqUpdateHomework) throws ApplicationErrorException;

    int createHomework(ReqCreateHomework reqCreateHomework) throws ApplicationErrorException;

    ResClassHomework getClassHomework(int classId) throws ApplicationErrorException;

    ResStudentHomeworkList getStudentHomeworkListById(int studentId) throws ApplicationErrorException;

    ResTeacherHomeworkList getHomeworkListByTeacherId(int teacherId) throws ApplicationErrorException;
}
