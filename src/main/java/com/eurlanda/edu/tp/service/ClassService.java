package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.Clazz;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.*;
import com.eurlanda.edu.tp.webdomain.response.ResClassDetail;
import com.eurlanda.edu.tp.webdomain.response.ResClassInfos;
import com.eurlanda.edu.tp.webdomain.response.ResClassStudents;

import java.util.List;
import java.util.Map;

public interface ClassService {
    Clazz getClassById(int classID) throws ApplicationErrorException;

    int deleteClassStudent(ReqDeleteClassStudent reqDeleteClassStudent,int type) throws ApplicationErrorException;

    int addClassStudent(ReqAddClassStudent reqAddClassStudent) throws ApplicationErrorException;

    ResClassStudents getAllClassStudentInfo(int classId) throws ApplicationErrorException;

    int deleteClass(ReqDeleteClass reqDeleteClass) throws ApplicationErrorException;

    int updateClassInfo(ReqUpdateClass reqUpdateClass) throws ApplicationErrorException;

    int createClass(ReqAddClass reqAddClass) throws ApplicationErrorException;

    ResClassInfos getAllClassInfo();
    boolean selectIsShulieyunCourseByClassId(int class_id,int cloudware_type);
    boolean isTeacherUsedByClass(int teacherId);
    List<Clazz> selectByCourseId(int courseId);


    Integer selectCountByClassName(Clazz clazz);

    /**
     * 查询该班级对应的课程 是否和数列云有关系
     * @param classId
     * @return
     */
    int findSlyByClassId(int classId);

    /**
     * 根据班级编辑编号和课程编号
     * 查询该老师在其他班级中是否有相同课程的数列云实验
     * @param map
     * @return
     */
    int findSlyByTeaIdAndClassIdAndCourseId(Map<String,Object> map);

    ResClassStudents getStudentByNotThisClass(int classId) throws ApplicationErrorException;

    List<ResClassDetail> getClassDetailByClassId(int classId);

    void addClassCourse(ReqAddClassCourse reqAddClassCourse)throws ApplicationErrorException;

    void deleteClassCourse(ReqDeleteClassCourse reqDeleteClassCourse)throws ApplicationErrorException;

    Clazz findClassByNameAndCId(ReqUpdateClass reqUpdateClass);

    /**
     * 获取老师对应的所有班级
     */
    ResClassInfos getClassByTeacherId(int teacherId);

    Clazz findByClassName(String className);

}
