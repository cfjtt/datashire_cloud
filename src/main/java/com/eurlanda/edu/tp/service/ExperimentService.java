package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.Experiment;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqAdjustExperimentOrderClass;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteExperiment;
import com.eurlanda.edu.tp.webdomain.request.ReqExperiment;
import com.eurlanda.edu.tp.webdomain.response.ResExperimentInfo;

import java.util.Map;

public interface ExperimentService {

    int deleteExperiment(ReqDeleteExperiment reqDeleteExperiment) throws ApplicationErrorException;

    int createExperiment(ReqExperiment reqExperiment) throws ApplicationErrorException;

    int updateExperiment(ReqExperiment reqExperiment) throws ApplicationErrorException;

    ResExperimentInfo getExperiment(int experimentId) throws ApplicationErrorException;

    int getCount();
    Course getCourseByModuleId(int moduleId);

    Integer selectModuleIsExistByExperiment(Integer id);

    boolean experimentIsExist(Experiment experiment);


    Map<String,Object> adjustExperimentOrder(ReqAdjustExperimentOrderClass entity) throws ApplicationErrorException;

    Experiment findExpById(Integer id);
}
