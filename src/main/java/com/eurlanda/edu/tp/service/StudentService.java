package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.Student;
import com.eurlanda.edu.tp.dao.entity.StudentClass;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqAddStudent;
import com.eurlanda.edu.tp.webdomain.request.ReqSubmitHomeworkOfOther;
import com.eurlanda.edu.tp.webdomain.request.ReqSubmitHomeworkOfShuLie;
import com.eurlanda.edu.tp.webdomain.request.ReqUpdateStudent;
import com.eurlanda.edu.tp.webdomain.response.ResBatchCreateStudent;
import com.eurlanda.edu.tp.webdomain.response.ResClassStudents;
import com.eurlanda.edu.tp.webdomain.response.ResStudentClassList;
import com.eurlanda.edu.tp.webdomain.response.ResBatchAddStudent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface StudentService {
    List<StudentClass> getAllStudentClassByUserId(int studentID) throws ApplicationErrorException;

    ResStudentClassList getAllStudentClassInfoByUserId(int studentID) throws ApplicationErrorException;

    int updateStudentInfo(ReqUpdateStudent reqUpdateStudent) throws ApplicationErrorException;

    ResBatchAddStudent batchStudentCreation(int classId, MultipartFile file) throws ApplicationErrorException, IOException;

    void createStudent(Student student) throws ApplicationErrorException;

    void updateStudent(Student student) throws ApplicationErrorException;

    int getCount();
    List<Map<String,Object>> getAllStudentByCourseId(int courseId);

    Student getStudentByNo(String no);

    Student getStudentById(int id);

   Student selectByUserId(int userId);

   //查询学生除了该班级之外的班级里是否有该课程
    Integer selectExistsCourseByClassIdAndCourseIdAndStuId(Map<String,Object> map);


    int findSlyByNotClassAndStuId(Map<String,Object> map);

    ResClassStudents getAllStudentInfo();
    void addStudentInfo(ReqAddStudent reqAddStudent)throws  ApplicationErrorException;

    ResBatchCreateStudent batchCreateStudent(MultipartFile file) throws ApplicationErrorException, IOException;
    void importStudent(ReqAddStudent reqAddStudent)throws ApplicationErrorException;
    void updateStu(ReqUpdateStudent reqUpdateStudent) throws ApplicationErrorException;
    void deleteStuByStuNotClassAndCourse(Student student);
    int submitHomeworkOfShuLie(ReqSubmitHomeworkOfShuLie reqSubmitHomeworkOfShuLie)throws ApplicationErrorException, IOException;
    int submitHomeworkOfOther(ReqSubmitHomeworkOfOther reqSubmitHomeworkOfOther)throws ApplicationErrorException, IOException;;

    List<Map<String, Object>> getStudentSubmitHomework(int classId,int courseId,int experimentId,int type,int userId);

}
