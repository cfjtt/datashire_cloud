package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperiment;
import com.eurlanda.edu.tp.webdomain.response.ResExperimentInfo;

/**
 * Created by Administrator on 2017/10/19.
 */
public interface CloudwareService {

    String getStudentExperiment(ReqStudentExperiment reqStudentExperiment) throws
            ApplicationErrorException;

    ResExperimentInfo getExperiment(int id) throws ApplicationErrorException;

    void deleteCloudware(int cloudwareId);
}
