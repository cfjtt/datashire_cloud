package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.cloudwareDomain.ResCloudware;
import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.mapper.CourseMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.RancherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class RancherController {

    @Value("${rancher.secret}")
    private String secret;

    @Autowired
    private RancherService rancherService;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 恢复教师为tony的数据
     */
    @ApiOperation(value = "创建共享文件夹", notes = "")
    @GetMapping(value = "/recoveryVolumes")
    public void recovery(){
        //恢复tony 的个人的数据  (userId=10298)
        ResCloudware cloudware = createVolume(10298,0);
        //恢复tony 的课程的数据
       /* List<Course> courseList = courseMapper.selectCourseByTeacherId(10298);
        if(courseList != null && courseList.size()>0){
            for(Course course : courseList){
                int courseId = course.getId();
                createVolume(courseId,1);
            }
        }*/
    }
    @ApiOperation(value = "创建共享文件夹", notes = "")
    @PostMapping(value = "/volumes")
    public ResCloudware createVolume(int userId,int type){
        ResCloudware cloudware = rancherService.createVolumes(secret,userId,type);
        return cloudware;
    }

    /**
     * 开启云件
     * @return
     */
    @ApiOperation(value = "开启云件", notes = "")
    @PostMapping(value = "/services")
    public ResponseMessage startService(String secret,String cloudware_type,int user_id,int experimentId) throws ApplicationErrorException {
        ResponseMessage message = null;
        synchronized (this){
            message = rancherService.startServices(secret,cloudware_type,user_id,experimentId);
        }
        return message;
    }

    @ApiOperation(value = "删除云件", notes = "")
    @PostMapping(value = "/homeworks")
    public ResponseMessage deleteCloudWare(String pulsarId,String serviceName,String serviceId){
        return rancherService.deleteCloudWare(pulsarId,serviceName,serviceId);
    }

    @ApiOperation(value = "terminal", notes = "")
    @PostMapping(value = "/terminals")
    public ResponseMessage terminals(String cloudwareId){
        return rancherService.terminals(cloudwareId);
    }
}
