package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.StudentExperiment;
import com.eurlanda.edu.tp.dao.mapper.CloudwareMapper;
import com.eurlanda.edu.tp.dao.mapper.ExperimentMapper;
import com.eurlanda.edu.tp.dao.mapper.UserMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.CloudwareService;
import com.eurlanda.edu.tp.service.StudentExperimentService;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperiment;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperimentCloudware;
import com.eurlanda.edu.tp.dao.entity.Cloudware;
import com.eurlanda.edu.tp.dao.mapper.StudentExperimentMapper;
import com.eurlanda.edu.tp.webdomain.response.ResStudentLastExperiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentExperimentServiceImp implements StudentExperimentService {

    @Autowired
    private StudentExperimentMapper studentExperimentMapper;

    @Autowired
    private CloudwareMapper cloudwareMapper;

    @Autowired
    private CloudwareService cloudwareService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Override
    public Cloudware getStudentExperimentCloudware(int experimentId, int studentId) throws
            ApplicationErrorException {
        List<StudentExperiment>
                studentExperimentList = studentExperimentMapper.selectByStudentIdAndExperimentId(studentId, experimentId);
        if(studentExperimentList == null || studentExperimentList.size()==0){
            throw new ApplicationErrorException(ErrorCode.StudentExperimentNotFound);
        }
        StudentExperiment studentExperiment = studentExperimentList.get(0);
        if(studentExperiment.getCloudwareId() == null){
            throw new ApplicationErrorException(ErrorCode.CloudwareNotExist);
        }

        return cloudwareMapper.selectByPrimaryKey(studentExperiment.getCloudwareId());
    }

    @Override
    public void createStudentExperimentCloudware(ReqStudentExperimentCloudware reqStudentExperimentCloudware) throws ApplicationErrorException {
        validateStudentExperiment(
                reqStudentExperimentCloudware.getStudentId(),
                reqStudentExperimentCloudware.getExperimentId()
        );
        List<StudentExperiment> studentExperimentList =
                studentExperimentMapper.selectByStudentIdAndExperimentId(reqStudentExperimentCloudware.getStudentId(),
                        reqStudentExperimentCloudware.getExperimentId());
        StudentExperiment studentExperiment = null;
        if(studentExperimentList.size()>0) {
            studentExperiment = studentExperimentList.get(0);
        }
        Cloudware cloudware = new Cloudware(reqStudentExperimentCloudware.getWebSocket(),
                reqStudentExperimentCloudware.getServiceId(),
                reqStudentExperimentCloudware.getInstanceId(),
                reqStudentExperimentCloudware.getServiceName(),
                reqStudentExperimentCloudware.getPulsarId());
        cloudwareMapper.insert(cloudware);

        if(studentExperiment != null){
            studentExperiment.setCloudwareId(cloudware.getId());
            studentExperimentMapper.updateByPrimaryKey(studentExperiment);
        } else {

            studentExperiment = new StudentExperiment(
                    reqStudentExperimentCloudware.getStudentId(),
                    reqStudentExperimentCloudware.getExperimentId(),
                    cloudware.getId()
            );
            studentExperimentMapper.insert(studentExperiment);
        }
    }

    @Override
    public void deleteStudentExperiment(int studentExperimentId) throws ApplicationErrorException {

        StudentExperiment studentExperiment = studentExperimentMapper.selectByPrimaryKey(studentExperimentId);
        if(studentExperiment == null){
            throw new ApplicationErrorException(ErrorCode.StudentExperimentNotFound);
        }
        studentExperimentMapper.deleteByPrimaryKey(studentExperimentId);
        if(studentExperiment.getCloudwareId() != null){
            cloudwareService.deleteCloudware(studentExperiment.getCloudwareId());
        }
    }

    @Override
    public void deleteStudentExperiment(int experimentId, int studentId) throws ApplicationErrorException {
        List<StudentExperiment> studentExperimentList =
                studentExperimentMapper.selectByStudentIdAndExperimentId(studentId, experimentId);

        if(studentExperimentList == null || studentExperimentList.size()==0){
            throw new ApplicationErrorException(ErrorCode.StudentExperimentNotFound);
        }
        for(StudentExperiment studentExperiment : studentExperimentList) {
            studentExperimentMapper.deleteByPrimaryKey(studentExperiment.getId());
            if (studentExperiment.getCloudwareId() != null) {
                cloudwareService.deleteCloudware(studentExperiment.getCloudwareId());
            }
        }
    }

    @Override
    public ResStudentLastExperiment getStudentLastExperiment(int studentId) throws ApplicationErrorException {
        if(userMapper.selectByPrimaryKey(studentId) == null){
            throw new ApplicationErrorException(ErrorCode.UserNotExist);
        }

        List<Map> lastExperimentInfo = studentExperimentMapper.selectLastExperimentByUserId(studentId);
        if(lastExperimentInfo == null || lastExperimentInfo.size()==0){
            return new ResStudentLastExperiment();
        }

        return new ResStudentLastExperiment(
                (int)lastExperimentInfo.get(0).get("experimentId"),
                (String)lastExperimentInfo.get(0).get("experimentName"),
                (String)lastExperimentInfo.get(0).get("moduleName")
        );
    }

    @Override
    public List<ReqStudentExperiment> getStudentAllExperiments(int studentId) throws ApplicationErrorException {
        if(userMapper.selectByPrimaryKey(studentId) == null){
            throw new ApplicationErrorException(ErrorCode.UserNotExist);
        }
        List<ReqStudentExperiment> experiments = studentExperimentMapper.selectExperimentByStudentId(studentId);
        return experiments;
    }

    private void validateStudentExperiment(int studentId, int experimentId) throws ApplicationErrorException {
        if(userMapper.selectByPrimaryKey(studentId) == null){
            throw new ApplicationErrorException(ErrorCode.UserNotExist);
        }

        if(experimentMapper.selectByPrimaryKey(experimentId) == null){
            throw new ApplicationErrorException(ErrorCode.ExperimentNotFound);
        }
    }
}
