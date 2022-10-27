package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.mapper.CourseMapper;
import com.eurlanda.edu.tp.dao.mapper.ExperimentMapper;
import com.eurlanda.edu.tp.dao.mapper.ModuleMapper;
import com.eurlanda.edu.tp.enums.CloudwareTypeEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.StudentExperimentService;
import com.eurlanda.edu.tp.webdomain.request.ReqAdjustExperimentOrderClass;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteExperiment;
import com.eurlanda.edu.tp.webdomain.request.ReqExperiment;
import com.eurlanda.edu.tp.webdomain.response.ResExperimentInfo;
import com.eurlanda.edu.tp.dao.entity.Experiment;
import com.eurlanda.edu.tp.dao.entity.Module;
import com.eurlanda.edu.tp.dao.mapper.StudentExperimentMapper;
import com.eurlanda.edu.tp.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExperimentServiceImp implements ExperimentService {

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentExperimentMapper studentExperimentMapper;

    @Autowired
    private StudentExperimentService studentExperimentService;

    @Override
    public int deleteExperiment(ReqDeleteExperiment reqDeleteExperiment) throws
            ApplicationErrorException {

        //查询该实验的所属的modul是否存在
        Integer count = experimentMapper.selectModuleIsExistByExperiment(reqDeleteExperiment.getId());
        if(count < 1){
            throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
        }

        if(experimentMapper.selectByPrimaryKey(reqDeleteExperiment.getId()) == null){
            throw new ApplicationErrorException(ErrorCode.ExperimentNotFound);
        }

//        if(experimentMapper.isExperimentUsedByStudentExperiment(reqDeleteExperiment.getId())){
//            throw new ApplicationErrorException(ErrorCode.ExperimentUsedByStudentExperiment);
//        }

        for(int studentExperimentId :
                studentExperimentMapper.selectStudentExperimentByExperimentId(reqDeleteExperiment.getId())){
            studentExperimentService.deleteStudentExperiment(studentExperimentId);
        }
        //调整删除后的实验顺序
        synchronized (this){
            Experiment experiment = experimentMapper.selectByPrimaryKey(reqDeleteExperiment.getId());
            List<Experiment> experiments = experimentMapper.selectInfluenceOrderIdByModuleId(experiment);
            for (Experiment influenceExperiment : experiments){
                Integer beforeOrderId = influenceExperiment.getOrderId();
                influenceExperiment.setOrderId(beforeOrderId - 1);
                experimentMapper.updateByPrimaryKeySelective(influenceExperiment);
            }
        }

        experimentMapper.deleteByPrimaryKey(reqDeleteExperiment.getId());

        return 0;
    }

    @Override
    public int createExperiment(ReqExperiment reqExperiment) throws ApplicationErrorException {
        CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt(reqExperiment.getCloudwareType());
        if(cloudwareType == null){
            throw new ApplicationErrorException(ErrorCode.InvalidCloudwareType);
        }

        Experiment experiment = new Experiment();
        experiment.setName(reqExperiment.getExperimentName());
        experiment.setModuleId(reqExperiment.getModuleId());
        experiment.setCloudwareType(reqExperiment.getCloudwareType());
        experiment.setPublishDate(reqExperiment.getExperimentCreateDate());
        experiment.setDeadlineDate(reqExperiment.getExperimentDueDate());
        experiment.setExperimentContent(reqExperiment.getExperimentContent());
        experiment.setDescription(reqExperiment.getExperimentDes());
        validateExperiment(experiment);
        //保证查询到的orderId是最新的
        synchronized (this){
            Integer maxOrderId = experimentMapper.selectMaxOrderIdByModuleId(reqExperiment.getModuleId());
            if(maxOrderId == null){
                maxOrderId = 1;
            }else {
                maxOrderId += 1;
            }
            experiment.setOrderId(maxOrderId);
            try{
                experimentMapper.insert(experiment);
            }catch (Exception e){
                String msg = e.getMessage();
                if(msg.contains("experiment_content")){
                    throw new ApplicationErrorException(ErrorCode.EncodingError);
                }else {
                    throw  e;
                }
            }
        }

        return 0;
    }

    @Override
    public int updateExperiment(ReqExperiment reqExperiment) throws ApplicationErrorException {

        Experiment experiment = experimentMapper.selectByPrimaryKey(reqExperiment.getId());

        //查询该实验的所属的modul是否存在
        Integer count = experimentMapper.selectModuleIsExistByExperiment(reqExperiment.getId());
        if(count < 1){
            throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
        }

        if(experiment == null){
            throw new ApplicationErrorException(ErrorCode.ExperimentNotFound);
        }

        experiment.setName(reqExperiment.getExperimentName());
        experiment.setCloudwareType(reqExperiment.getCloudwareType());
        experiment.setExperimentContent(reqExperiment.getExperimentContent());
        experiment.setDescription(reqExperiment.getExperimentDes());

        validateExperiment(experiment);
        try{
            experimentMapper.updateByPrimaryKeySelective(experiment);
        }catch (Exception e){
            String msg = e.getMessage();
            if(msg.contains("experiment_content")){
                throw new ApplicationErrorException(ErrorCode.EncodingError);
            }else {
                throw  e;
            }
        }

        return 0;
    }

    @Override
    public ResExperimentInfo getExperiment(int experimentId) throws ApplicationErrorException {
        Experiment experiment = experimentMapper.selectByPrimaryKey(experimentId);
        if(experiment == null){
            throw new ApplicationErrorException(ErrorCode.ExperimentNotFound);
        }

        Module module = moduleMapper.selectByPrimaryKey(experiment.getModuleId());
        Course course = courseMapper.selectByPrimaryKey(module.getCourseId());
        ResExperimentInfo experimentInfo = new ResExperimentInfo(experiment);
        experimentInfo.setModuleName(module.getName());
        experimentInfo.setCourseName(course.getName());
        experimentInfo.setCourseId(course.getId());
        return experimentInfo;
    }

    @Override
    public int getCount() {
        return experimentMapper.getCount();
    }

    private void validateExperiment(Experiment experiment) throws ApplicationErrorException {
        if(moduleMapper.selectCourseCountByModuleId(experiment.getModuleId()) < 1){
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }


        if(moduleMapper.selectByPrimaryKey(experiment.getModuleId()) == null){
            throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
        }

        if(experimentMapper.selectCountByModuleIdAndName(experiment) > 0){
            throw new ApplicationErrorException(ErrorCode.ExperimentAlreadyExisted);
        }


        CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt(experiment.getCloudwareType());
        if(cloudwareType == null){
            throw new ApplicationErrorException(ErrorCode.InvalidCloudwareType);
        }
    }

    @Override
    public Course getCourseByModuleId(int moduleId) {
        return experimentMapper.getCourseByModuleId(moduleId);
    }

    @Override
    public Integer selectModuleIsExistByExperiment(Integer id) {
        return experimentMapper.selectModuleIsExistByExperiment(id);
    }

    @Override
    public boolean experimentIsExist(Experiment experiment) {
        List<Experiment> experimentList = experimentMapper.selectExperiment(experiment);
        if(experimentList != null && experimentList.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public Map<String,Object> adjustExperimentOrder(ReqAdjustExperimentOrderClass entity) throws ApplicationErrorException{
        Integer ex1 = entity.getExperimentOne();
        Integer o1 = entity.getOrderOne();
        Integer ex2 = entity.getExperimentTwo();
        Integer o2 = entity.getOrderTwo();
        Integer moduleId = entity.getModuleId();
        Integer courseId = entity.getCourseId();

        if(ex1 == null || ex2 == null || moduleId == null){
            throw new ApplicationErrorException(ErrorCode.InvalidParam);
        }

        //验证所属模块是否存在
        Module module = moduleMapper.selectByPrimaryKey(moduleId);
        if(module == null){
            throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
        }
        //验证所属课程是否存在
        Course course = courseMapper.selectByPrimaryKey(module.getCourseId());
        if(course == null){
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }
        //交换实验的OrderId
        Experiment experiment1 = null;
        Experiment experiment2 = null;
        synchronized (this){
            //验证实验是否存在
            experiment1 = experimentMapper.selectByPrimaryKey(ex1);
            experiment2 = experimentMapper.selectByPrimaryKey(ex2);
            if(experiment1 == null || experiment2 == null){
                throw new ApplicationErrorException(ErrorCode.ExperimentNotFound);
            }
            Experiment paraExperiment = new Experiment();
            paraExperiment.setId(ex1);
            Integer orderId2 = experiment2.getOrderId();
            if(orderId2 == null){
                orderId2 = 0;
            }
            paraExperiment.setOrderId(orderId2);
            experimentMapper.updateByPrimaryKeySelective(paraExperiment);
            paraExperiment.setId(ex2);
            Integer orderId1 = experiment1.getOrderId();
            if(orderId1 == null){
                orderId1 = 0;
            }
            paraExperiment.setOrderId(orderId1);
            experimentMapper.updateByPrimaryKeySelective(paraExperiment);
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("" + ex1,experiment2.getOrderId());
        resultMap.put("" + ex2,experiment1.getOrderId());
        return resultMap;
    }

    @Override
    public Experiment findExpById(Integer id) {
        return experimentMapper.selectByPrimaryKey(id);
    }
}
