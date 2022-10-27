package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.cloudwareDomain.ResCloudware;
import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.Experiment;
import com.eurlanda.edu.tp.dao.mapper.CloudwareMapper;
import com.eurlanda.edu.tp.dao.mapper.CourseMapper;
import com.eurlanda.edu.tp.dao.mapper.ExperimentMapper;
import com.eurlanda.edu.tp.dao.mapper.ModuleMapper;
import com.eurlanda.edu.tp.dao.mapper.StudentExperimentMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.CloudwareService;
import com.eurlanda.edu.tp.service.RancherService;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteCloudware;
import com.eurlanda.edu.tp.webdomain.response.ResExperimentInfo;
import com.eurlanda.edu.tp.dao.entity.Cloudware;
import com.eurlanda.edu.tp.dao.entity.Module;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperiment;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2017/10/19.
 */
@Service
public class CloudwareServiceImp implements CloudwareService {

    static Logger log = Logger.getLogger(CloudwareServiceImp.class.getName());

    @Value("${cloudware.deleteCloudwareUrl}")
    private String deleteCloudwareUrl;

    @Autowired
    private StudentExperimentMapper studentExperimentMapper;

    @Autowired
    private CloudwareMapper cloudwareMapper;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RancherService rancherService;

    @Override
    public String getStudentExperiment(ReqStudentExperiment reqStudentExperiment) throws
            ApplicationErrorException {

        int cloudwareId = studentExperimentMapper.selectCloudwareIdByStudentIdAndExperimentId(reqStudentExperiment.getStudentId(), reqStudentExperiment.getExperimentId());

        return cloudwareMapper.selectWebSocketById(cloudwareId);
    }

    @Override
    public ResExperimentInfo getExperiment(int id) throws ApplicationErrorException{


        Experiment experiment = experimentMapper.selectByPrimaryKey(id);
        Module module = moduleMapper.selectByPrimaryKey(experiment.getModuleId());
        Course course = courseMapper.selectByPrimaryKey(module.getCourseId());

        ResExperimentInfo resExperiment = new ResExperimentInfo(experiment);

        resExperiment.setCourseName(course.getName());
        resExperiment.setModuleName(module.getName());

        return resExperiment;
    }

    @Override
    public void deleteCloudware(int cloudwareId) {
        log.info(String.format("Start to delete cloudware for cloudware id %d...", cloudwareId));

        Cloudware cloudware = cloudwareMapper.selectByPrimaryKey(cloudwareId);
        ReqDeleteCloudware reqDeleteCloudware = new ReqDeleteCloudware(cloudware);

        try {
            ResponseMessage responseMessage = rancherService.deleteCloudWare(reqDeleteCloudware.getPulsarId(),reqDeleteCloudware.getServiceName(),reqDeleteCloudware.getServiceId());
            if (responseMessage.getErrorCode() != 0) {
                log.error(String.format("Deleting cloudware failed. Error code returned %d.", responseMessage.getErrorCode()));
            } else {
                log.info("delete cloudware succeeded.");
            }
        } catch (RestClientException e) {
            log.error("删除rancher容器异常", e);
        }

        cloudwareMapper.deleteByPrimaryKey(cloudwareId);
    }
}
