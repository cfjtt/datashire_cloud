package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.Teacher;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteTeacher;
import com.eurlanda.edu.tp.webdomain.request.ReqHomeworkGrade;
import com.eurlanda.edu.tp.webdomain.request.ReqUpdateTeacher;
import com.eurlanda.edu.tp.webdomain.response.ResBatchAddTeacher;
import com.eurlanda.edu.tp.webdomain.response.ResTeacherClassList;
import com.eurlanda.edu.tp.webdomain.response.ResTeacherList;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TeacherService {
    Teacher getTeacherByUserId(int teacherID) throws ApplicationErrorException;

    ResTeacherClassList getAllTeacherClassInfoByUserId(int teacherId) throws ApplicationErrorException;

    void gradeHomework(ReqHomeworkGrade homeworkGrade) throws ApplicationErrorException;

    ResTeacherList getAllTeacherList() throws ApplicationErrorException;

    void updateTeacher(ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException;

    void createTeacher(ReqUpdateTeacher reqUpdateTeacher) throws ApplicationErrorException;

    void deleteTeacherByTeacherUserId(ReqDeleteTeacher reqDeleteTeacher) throws ApplicationErrorException;

    ResBatchAddTeacher batchTeacherCreation(MultipartFile file) throws ApplicationErrorException, IOException;

    int getCount();

    /**
     * 获取老师是否是数猎云的课程的实验老师
     * @param cloudware_type,teacher_id
     * @return
     */
    List<Map<String,Object>> selectTeacherIsShulieyunByUserId(int cloudware_type, int teacher_id);
    Boolean checkTnoIsExist(ReqUpdateTeacher reqUpdateTeacher);

    /**
     * 获取老师是否是数猎云的课程的实验老师
     * @param cloudware_type,teacher_id
     * @return
     */
    List<Map<String,Object>> selectTeacherIsShulieyunByTeacherId(int cloudware_type, int teacher_id);

    /**
     * 分页查询老师信息
     * @param map  start,end
     * @return
     */
    ResTeacherList getAllTeachersBylimit(int page,int limit);

    List<Course> getAllTeacherAssociateCourses(Integer teacherId)throws ApplicationErrorException;



}
