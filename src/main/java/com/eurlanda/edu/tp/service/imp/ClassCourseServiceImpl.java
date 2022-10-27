package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.ClassCourse;
import com.eurlanda.edu.tp.dao.mapper.ClassCourseMapper;
import com.eurlanda.edu.tp.dao.mapper.ClazzMapper;
import com.eurlanda.edu.tp.enums.SemesterEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.ClassCourseService;
import com.eurlanda.edu.tp.webdomain.response.ResClassDetail;
import com.sun.javafx.image.IntPixelGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class ClassCourseServiceImpl implements ClassCourseService {
    @Autowired
    private ClassCourseMapper classCourseMapper;
    @Autowired
    private ClazzMapper clazzMapper;
    @Override
    public void updateClassCourseByClassIdAndCourseId(ClassCourse classCourse) {
        classCourseMapper.updateByPrimaryKeySelective(classCourse);
    }

    @Override
    public ClassCourse findClassCourseByClassIdAndCourseId(Map<String,Object> map) {
        return classCourseMapper.findClassCourseByClassIdAndCourseId(map);
    }

    @Override
    public void deleteByClassId(Integer classId) throws ApplicationErrorException{
        if (clazzMapper.selectByPrimaryKey(classId) == null) {
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }
        classCourseMapper.deleteByClassId(classId);

    }

    @Override
    public List<ClassCourse> findClassCourseByClassId(Integer classId) throws ApplicationErrorException{
        if (clazzMapper.selectByPrimaryKey(classId) == null) {
            throw new ApplicationErrorException(ErrorCode.ClassNotExists);
        }
        return classCourseMapper.findClassCourseByClassId(classId);
    }

    @Override
    public List<ResClassDetail> findClassDetailByClassId(Integer classId) throws ApplicationErrorException {
            List<ResClassDetail> resClassDetails=new ArrayList<>();
            List<Map> mapList=classCourseMapper.findClassDetailByClassId(classId);
            if(mapList!=null && mapList.size()>0){
                for(Map map:mapList){
                    ResClassDetail resClassDetail=new ResClassDetail();
                    resClassDetail.setClassId(classId);
                    resClassDetail.setTeacherName((String) map.get("teacherName"));
                    resClassDetail.setCourseId(Integer.parseInt(map.get("courseId").toString()));
                    resClassDetail.setTeacherId(Integer.parseInt(map.get("teacherId").toString()));
                    resClassDetail.setCourseName((String)map.get("courseName"));
                    resClassDetail.setSchoolYear((String)map.get("schoolYear"));
                    if(map.get("semester")!=null && !map.get("semester").equals("")){
                        resClassDetail.setSemester(Integer.parseInt(map.get("semester").toString()));
                    }
                   /* Integer semester=Integer.parseInt(map.get("semester").toString());
                    String year=(String)map.get("year");
                    SemesterEnum semesterEnum = SemesterEnum.fromInt(semester);
                    if(year.indexOf("-")>0){
                        resClassDetail.setTerm(year+semesterEnum.getZh());
                    }else{
                        String term=year+"-"+(Integer.parseInt(year)+1)+semesterEnum.getZh();
                        resClassDetail.setTerm(term);
                    }*/
                    resClassDetails.add(resClassDetail);
                }
            }

        return resClassDetails;
    }
}
