package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqAddCourse;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteCourse;
import com.eurlanda.edu.tp.webdomain.response.ResCommonCourseDetail;
import com.eurlanda.edu.tp.webdomain.response.ResCourseHomeworks;
import com.eurlanda.edu.tp.webdomain.response.ResCourseList;
import com.eurlanda.edu.tp.webdomain.response.ResCourseModuleExperiments;
import com.eurlanda.edu.tp.webdomain.response.ResHotCourseList;

import java.util.List;
import java.util.Map;

public interface CourseService {
    Course getCourseById(int courseID) throws ApplicationErrorException;

    ResCourseModuleExperiments getCourseModuleExperiments(int classId) throws ApplicationErrorException;
    ResCourseModuleExperiments getCourseModuleExperimentsAndAnswer(Integer courseId,Integer userId,Integer classId) throws ApplicationErrorException;

    ResCourseModuleExperiments getClassModuleExperiments(int classId) throws ApplicationErrorException;

    ResCourseHomeworks getCourseHomeworks(int classID, int studentId) throws ApplicationErrorException;

    ResCourseList getAllCourses() throws ApplicationErrorException;

    ResHotCourseList getHotCourses();

    void createCourse(ReqAddCourse reqAddCourse) throws ApplicationErrorException;

    void updateCourse(Course course) throws ApplicationErrorException;

    void updateCourseWithOutSync(Course course) throws ApplicationErrorException;

    void deleteCourse(ReqDeleteCourse reqDeleteCourse) throws ApplicationErrorException;

    ResCommonCourseDetail getCommonCourseDetail(int courseId) throws ApplicationErrorException;

    int getCount();
    List<Course> getCourseByClassId(int classId);
    boolean isTeacherUsedByCourse(int teacherId);
    boolean selectCourseExistShulieyun(int courseId);
    boolean selectCourseExistOtherExperiment(int courseId);
    boolean selectCourseExistOtherExperimentByUserId(int userId);
    Integer selectCountByCourseName(Course course);

    List<Course> selectByCourseName(Course course);
    List<Course> selectAllCourses();
    Course selectByCourseName(String courseName);

    Map<String,Object> findExperimentCountByCouseId(int courseId);

    List<String> findStuByCourseId(int courseId);

    List<String> findTeaByCourseId(int courseId);

    Map<String,Object> findModelCountByCouseId(int courseId);


    List<Course> findShulieyunCourseById(int courseId);

    Course selectCourseByCourseId(Integer courseId);


    List<Course> findCourseByStuId(Map<String,Object> map);

    Course selectCourseByCourseName(String courseName);

    List<Map<String, List<Map<String,Object>>>> getCouExpByTeaIdAndCId(int teacherId,int classId);



}
