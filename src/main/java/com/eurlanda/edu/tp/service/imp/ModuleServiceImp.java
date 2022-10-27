package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.Experiment;
import com.eurlanda.edu.tp.dao.entity.Homework;
import com.eurlanda.edu.tp.dao.entity.Module;
import com.eurlanda.edu.tp.dao.entity.ModuleResource;
import com.eurlanda.edu.tp.dao.entity.Resource;
import com.eurlanda.edu.tp.dao.mapper.CourseMapper;
import com.eurlanda.edu.tp.dao.mapper.ExperimentMapper;
import com.eurlanda.edu.tp.dao.mapper.HomeworkMapper;
import com.eurlanda.edu.tp.dao.mapper.ModuleMapper;
import com.eurlanda.edu.tp.dao.mapper.ModuleResourceMapper;
import com.eurlanda.edu.tp.dao.mapper.ResourceMapper;
import com.eurlanda.edu.tp.enums.ResourceTypeEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.ModuleService;
import com.eurlanda.edu.tp.webdomain.request.*;
import com.eurlanda.edu.tp.webdomain.response.ResModuleId;
import com.eurlanda.edu.tp.webdomain.response.ResModuleImages;
import com.eurlanda.edu.tp.service.ExperimentService;
import com.eurlanda.edu.tp.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleServiceImp implements ModuleService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private ModuleResourceMapper moduleResourceMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private HomeworkService homeworkService;

    @Override
    public ResModuleId createModule(Module module) throws ApplicationErrorException {
        Course course = courseMapper.selectByPrimaryKey(module.getCourseId());

        if(course == null){
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }
        //查找该模块是否存在
        List<Module> modules = moduleMapper.selectModuleSelective(module);
        if(modules !=null && modules.size()>0){
            throw new ApplicationErrorException(ErrorCode.ModuleIsExists);
        }
        synchronized (this){
            Integer orderId = moduleMapper.selectMaxOrderIdByCourseId(module.getCourseId());
            if(orderId == null){
                orderId = 1;
            }else {
                orderId += 1;
            }
            module.setOrderId(orderId);
            moduleMapper.insert(module);
        }
        return new ResModuleId(module.getId());
    }

    @Override
    public void deleteModule(ReqDeleteModule reqDeleteModule) throws ApplicationErrorException {
        int moduleId = reqDeleteModule.getModuleId();

        if(moduleMapper.selectCourseCountByModuleId(moduleId) < 1){
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }

        if(moduleMapper.selectByPrimaryKey(moduleId) == null){
            throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
        }

//        if(experimentMapper.isModuleUsedByExperiment(moduleId)){
//            throw new ApplicationErrorException(ErrorCode.ModuleUsedByExperiment);
//        }
//
//        if(homeworkMapper.isModuleUsedByHomework(moduleId)){
//            throw new ApplicationErrorException(ErrorCode.ModuleUsedByHomework);
//        }

        for(Experiment experiment : experimentMapper.selectByModuleId(moduleId)){
            experimentService.deleteExperiment(new ReqDeleteExperiment(experiment.getId()));
        }

        for(Homework homework : homeworkMapper.selectByModuleId(moduleId)){
            homeworkService.deleteHomework(new ReqDeleteHomework(homework.getId()));
        }

        for (ModuleResource moduleResource : moduleResourceMapper.selectByModuleId(moduleId)){
            moduleResourceMapper.deleteByPrimaryKey(moduleResource.getId());
            resourceMapper.deleteByPrimaryKey(moduleResource.getResourceId());
        }



        //删除module调整剩余module的顺序
        synchronized (this){
            Module module = moduleMapper.selectByPrimaryKey(moduleId);
            List<Module> modules = moduleMapper.selectInfluenceOrderIdByCourseId(module);
            for (Module influenceModule : modules){
                Integer beforeOrderId = influenceModule.getOrderId();
                influenceModule.setOrderId(beforeOrderId - 1);
                moduleMapper.updateByPrimaryKeySelective(influenceModule);
            }
        }
        moduleMapper.deleteByPrimaryKey(moduleId);
    }

    @Override
    public ResModuleImages getModuleImageUrls(int moduleId) {
        ResModuleImages resModuleImages = new ResModuleImages();
        List<ResModuleImages.ResModuleImage> urlList = new ArrayList<>();

        for (Map resource : moduleResourceMapper.selectAllByModuleIdAndType(moduleId, ResourceTypeEnum.IMAGE.getCode())){
            ResModuleImages.ResModuleImage moduleImage = new ResModuleImages.ResModuleImage(
                    (int)resource.get("resourceId"),
                    (String) resource.get("name"),
                    (String) resource.get("url"),
                    (int) resource.get("width"),
                    (int) resource.get("height")
            );
            urlList.add(moduleImage);
        }
        resModuleImages.setImageList(urlList);

        return resModuleImages;
    }

    @Override
    public Integer addModuleResource(ReqAddModuleResource reqAddModuleResource) throws ApplicationErrorException {
        if(moduleMapper.selectByPrimaryKey(reqAddModuleResource.getModuleId()) == null){
            throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
        }

        Resource resource = new Resource(
                reqAddModuleResource.getName(),
                reqAddModuleResource.getImageUrl(),
                reqAddModuleResource.getWidth(),
                reqAddModuleResource.getHeight()
        );

        resourceMapper.insert(resource);
        ModuleResource moduleResource = new ModuleResource(
                reqAddModuleResource.getModuleId(),
                resource.getId(),
                ResourceTypeEnum.IMAGE.getCode()
        );
        moduleResourceMapper.insert(moduleResource);
        return resource.getId();
    }

    @Override
    public void deleteModuleResource(ReqDeleteModuleResource reqDeleteModuleResource) throws ApplicationErrorException {
        ModuleResource moduleResource = moduleResourceMapper.selectByModuleIdAndResourceId(
                reqDeleteModuleResource.getModuleId(),
                reqDeleteModuleResource.getResourceId()
        );
        if(moduleResource == null){
            throw new ApplicationErrorException(ErrorCode.ModuleResourceNotFound);
        }

        moduleResourceMapper.deleteByPrimaryKey(moduleResource.getId());
        resourceMapper.deleteByPrimaryKey(moduleResource.getResourceId());
    }

    @Override
    public boolean moduleIsExist(Module module) throws ApplicationErrorException {
        List<Module> modules = moduleMapper.selectModuleSelective(module);
        if(modules !=null && modules.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> adjustModuleOrder(ReqAdjustModuleOrderClass entity) throws ApplicationErrorException {
        Integer o1 = entity.getOrderOne();
        Integer m1 = entity.getModuleIdOne();
        Integer o2 = entity.getOrderTwo();
        Integer m2 = entity.getModuleIdTwo();
        Integer courseId = entity.getCourseId();
        //参数异常
        if(o1 == null || o2 == null || courseId == null || m1 == null || m2 == null){
            throw new ApplicationErrorException(ErrorCode.InvalidParam);
        }

        //验证章节所属课程是否存在
        Course course = courseMapper.selectByPrimaryKey(courseId);
        if(course == null){
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }

        Module module1 = null;
        Module module2 = null;
        synchronized (this){
            //验证两个章节是否存在
            module1 = moduleMapper.selectByPrimaryKey(m1);
            module2 = moduleMapper.selectByPrimaryKey(m2);
            if(module1 == null || module2 == null){
                throw new ApplicationErrorException(ErrorCode.ModuleNotExists);
            }
            //交换orderId
            Module paraModule = new Module();
            paraModule.setId(m1);
            Integer orderId2 = module2.getOrderId();
            if(orderId2 == null){
                orderId2 = 0;
            }
            paraModule.setOrderId(orderId2);
            moduleMapper.updateByPrimaryKeySelective(paraModule);
            paraModule.setId(m2);
            Integer orderId1 = module1.getOrderId();
            if(orderId1 == null){
                orderId1 = 0;
            }
            paraModule.setOrderId(orderId1);
            moduleMapper.updateByPrimaryKeySelective(paraModule);
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put( "" + m1,module2.getOrderId());
        resultMap.put( "" + m2,module1.getOrderId());
        return resultMap;

    }

    @Override
    public void updateModule(Module module) throws ApplicationErrorException {
        Course course = courseMapper.selectByPrimaryKey(module.getCourseId());

        if(course == null){
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }
        //查找该模块是否被删除
        Module resultModule = moduleMapper.selectByPrimaryKey(module.getId());
        if(resultModule == null){
            throw new ApplicationErrorException(ErrorCode.ModuleIsExists);
        }
        //同一课程的下的模块不能同名
        //查找该模块是否存在
        List<Module> modules = moduleMapper.selectModuleIsExist(module);
        if(modules !=null && modules.size()>0){
            throw new ApplicationErrorException(ErrorCode.ModuleIsExists);
        }

        moduleMapper.updateByPrimaryKey(module);

    }

    @Override
    public Module getModuleInfo(Module module) throws ApplicationErrorException {
        return moduleMapper.selectByPrimaryKey(module.getId());
    }
}
