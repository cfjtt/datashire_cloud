package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.ClassCourse;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.response.ResClassDetail;

import java.util.List;
import java.util.Map;

public interface ClassCourseService {
    //修改班级课程关联表
    void updateClassCourseByClassIdAndCourseId(ClassCourse classCourse);

    //根据班级和课程编号查询关联表
    ClassCourse findClassCourseByClassIdAndCourseId(Map<String,Object> map);

    //根据班级编号删除关联表
    void deleteByClassId(Integer classId)throws ApplicationErrorException;

    //根据班级查询关
    List<ClassCourse> findClassCourseByClassId(Integer classId)throws ApplicationErrorException;

    //根据班级查询关
    List<ResClassDetail> findClassDetailByClassId(Integer classId)throws ApplicationErrorException;


}
