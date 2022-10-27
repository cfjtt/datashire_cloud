package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.webdomain.response.ResJobScoreList;

import java.util.Map;

public interface JobScoreService {
    ResJobScoreList findScoreByCidAndCouidAndExpid(int classId,int courseId,int experimentId);
    void correctingHomework();
    String findStudentIsSubAnswer(Integer classId,Integer courseId,Integer experimentId,Integer userId);

    int deleteBystuIdAndCidAndCouId(Integer classId,Integer userId);

}
