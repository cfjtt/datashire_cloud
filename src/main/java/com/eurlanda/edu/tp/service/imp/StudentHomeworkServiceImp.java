package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.entity.Cloudware;
import com.eurlanda.edu.tp.dao.entity.Homework;
import com.eurlanda.edu.tp.dao.entity.Resource;
import com.eurlanda.edu.tp.dao.entity.StudentHomework;
import com.eurlanda.edu.tp.dao.entity.StudentHomeworkResource;
import com.eurlanda.edu.tp.dao.mapper.ClazzMapper;
import com.eurlanda.edu.tp.dao.mapper.CloudwareMapper;
import com.eurlanda.edu.tp.dao.mapper.HomeworkMapper;
import com.eurlanda.edu.tp.dao.mapper.ResourceMapper;
import com.eurlanda.edu.tp.dao.mapper.StudentHomeworkMapper;
import com.eurlanda.edu.tp.dao.mapper.StudentHomeworkResourceMapper;
import com.eurlanda.edu.tp.dao.mapper.StudentMapper;
import com.eurlanda.edu.tp.enums.ResourceTypeEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.CloudwareService;
import com.eurlanda.edu.tp.service.StudentHomeworkService;
import com.eurlanda.edu.tp.webdomain.request.ReqHomeworkSubmission;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentHomeworkCloudware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StudentHomeworkServiceImp implements StudentHomeworkService {
    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentHomeworkMapper studentHomeworkMapper;

    @Autowired
    private CloudwareMapper cloudwareMapper;

    @Autowired
    private CloudwareService cloudwareService;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private StudentHomeworkResourceMapper studentHomeworkResourceMapper;

    @Override
    public StudentHomework getStudentHomeworkById(int studentHomeworkId) throws
            ApplicationErrorException {
        StudentHomework studentHomework = studentHomeworkMapper.selectByPrimaryKey(studentHomeworkId);

        if(studentHomework == null){
            throw new ApplicationErrorException(ErrorCode.StudentHomeworkNotExists);
        }

        return studentHomework;
    }

    @Override
    public Cloudware getStudentHomeworkCloudware(int homeworkId, int studentId) throws ApplicationErrorException {
        StudentHomework studentHomework = studentHomeworkMapper.selectByStudentIdAndHomeworkId(studentId, homeworkId);
        if(studentHomework == null){
            throw new ApplicationErrorException(ErrorCode.StudentHomeworkNotExists);
        }
        if(studentHomework.getCloudwareId() == null){
            throw new ApplicationErrorException(ErrorCode.CloudwareNotExist);
        }

        return cloudwareMapper.selectByPrimaryKey(studentHomework.getCloudwareId());
    }

    @Override
    public void submitStudentHomework(ReqHomeworkSubmission homeworkSubmission) throws ApplicationErrorException {
        this.validStudentHomeWork(homeworkSubmission.getStudentId(), homeworkSubmission.getHomeworkId());
        StudentHomework studentHomework = studentHomeworkMapper.selectByStudentIdAndHomeworkId(
                homeworkSubmission.getStudentId(), homeworkSubmission.getHomeworkId());

        if(studentHomework == null){
            insertStudentHomeWork(homeworkSubmission);
        } else {
            updateStudentHomeWork(studentHomework, homeworkSubmission);
        }
    }

    @Override
    public void validStudentHomeWork(int studentId, int homeworkId) throws ApplicationErrorException {
        if(studentMapper.selectByUserId(studentId) == null){
            throw new ApplicationErrorException(ErrorCode.StudentNotExists);
        }

        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkId);
        if(homework == null){
            throw new ApplicationErrorException(ErrorCode.HomeworkNotExists);
        }

        if(!clazzMapper.isStudentInClass(studentId, homework.getClassId())){
            throw new ApplicationErrorException(ErrorCode.StudentNotInClass);
        }
    }

    @Override
    public void createStudentHomeworkCloudware(ReqStudentHomeworkCloudware reqStudentHomeworkCloudware) throws ApplicationErrorException {
        validStudentHomeWork(reqStudentHomeworkCloudware.getStudentId(), reqStudentHomeworkCloudware.getHomeworkId());
        StudentHomework studentHomework =
                studentHomeworkMapper.selectByStudentIdAndHomeworkId(reqStudentHomeworkCloudware.getStudentId(),
                                                                     reqStudentHomeworkCloudware.getHomeworkId());

        Cloudware cloudware = new Cloudware(reqStudentHomeworkCloudware.getWebSocket(),
                                            reqStudentHomeworkCloudware.getServiceId(),
                                            reqStudentHomeworkCloudware.getInstanceId(),
                                            reqStudentHomeworkCloudware.getServiceName(),
                                            reqStudentHomeworkCloudware.getPulsarId());

        cloudwareMapper.insert(cloudware);

        if(studentHomework == null) {
            studentHomework = new StudentHomework(
                    reqStudentHomeworkCloudware.getStudentId(),
                    reqStudentHomeworkCloudware.getHomeworkId(),
                    cloudware.getId(),
                    "",
                    0,
                    null,
                    null,
                    null,
                    null
            );

            studentHomeworkMapper.insert(studentHomework);
        } else {
            studentHomework.setCloudwareId(cloudware.getId());
            studentHomeworkMapper.updateByPrimaryKey(studentHomework);
        }
    }

    @Override
    public void deleteStudentHomework(int studentHomeworkId) throws ApplicationErrorException {
        StudentHomework studentHomework = studentHomeworkMapper.selectByPrimaryKey(studentHomeworkId);
        if(studentHomework == null){
            throw new ApplicationErrorException(ErrorCode.StudentHomeworkNotExists);
        }

        StudentHomeworkResource studentHomeworkResource =
                studentHomeworkResourceMapper.selectByStudentHomeworkIdAndType(studentHomeworkId, ResourceTypeEnum.HOMEWORK.getCode());

        if(studentHomeworkResource != null){
            studentHomeworkResourceMapper.deleteByPrimaryKey(studentHomeworkResource.getId());
            resourceMapper.deleteByPrimaryKey(studentHomeworkResource.getResourceId());
        }

        studentHomeworkMapper.deleteByPrimaryKey(studentHomeworkId);

        if(studentHomework.getCloudwareId() != null){
            cloudwareService.deleteCloudware(studentHomework.getCloudwareId());
        }
    }

    protected void insertStudentHomeWork(ReqHomeworkSubmission homeworkSubmission) {

        StudentHomework studentHomework = new StudentHomework(
                homeworkSubmission.getStudentId(),
                homeworkSubmission.getHomeworkId(),
                null,
                "",
                0,
                new Date(),
                new Date(),
                null,
                null
        );
        studentHomeworkMapper.insert(studentHomework);

        Resource resource = new Resource(
                "", homeworkSubmission.getHomework_url(), 0, 0);
        resourceMapper.insert(resource);

        StudentHomeworkResource studentHomeworkResource = new StudentHomeworkResource(
                studentHomework.getId(),
                resource.getId(),
                ResourceTypeEnum.HOMEWORK.getCode());
        studentHomeworkResourceMapper.insert(studentHomeworkResource);
    }

    protected void updateStudentHomeWork(StudentHomework studentHomework, ReqHomeworkSubmission homeworkSubmission){

        if(studentHomework.getSubmissionDate() == null){
            studentHomework.setSubmissionDate(new Date());
        }
        studentHomework.setLastEditDate(new Date());
        studentHomeworkMapper.updateByPrimaryKey(studentHomework);

        StudentHomeworkResource studentHomeworkResource =
                studentHomeworkResourceMapper.selectByStudentHomeworkIdAndType(
                        studentHomework.getId(), ResourceTypeEnum.HOMEWORK.getCode());

        if(studentHomeworkResource != null) {
            Resource resource = resourceMapper.selectByPrimaryKey(studentHomeworkResource.getResourceId());
            resource.setUrl(homeworkSubmission.getHomework_url());
            resourceMapper.updateByPrimaryKey(resource);
        } else {
            Resource resource = new Resource(
                    "", homeworkSubmission.getHomework_url(), 0, 0);
            resourceMapper.insert(resource);

            studentHomeworkResource = new StudentHomeworkResource(
                    studentHomework.getId(),
                    resource.getId(),
                    ResourceTypeEnum.HOMEWORK.getCode());
            studentHomeworkResourceMapper.insert(studentHomeworkResource);
        }
    }
}
