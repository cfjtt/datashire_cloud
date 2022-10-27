package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperiment;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperimentCloudware;
import com.eurlanda.edu.tp.dao.entity.Cloudware;
import com.eurlanda.edu.tp.webdomain.response.ResStudentLastExperiment;

import java.util.List;

public interface StudentExperimentService {
    Cloudware getStudentExperimentCloudware(int experimentId, int studentId) throws
            ApplicationErrorException;

    void createStudentExperimentCloudware(ReqStudentExperimentCloudware reqStudentExperimentCloudware) throws ApplicationErrorException;

    void deleteStudentExperiment(int studentExperimentId) throws ApplicationErrorException;

    void deleteStudentExperiment(int experimentId, int studentId) throws ApplicationErrorException;

    ResStudentLastExperiment getStudentLastExperiment(int studentId) throws ApplicationErrorException;

    List<ReqStudentExperiment> getStudentAllExperiments(int studentId) throws ApplicationErrorException;
}
