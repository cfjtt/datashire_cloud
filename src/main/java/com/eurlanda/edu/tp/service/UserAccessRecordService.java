package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.UserAccessRecord;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqClouderaData;
import com.eurlanda.edu.tp.webdomain.request.ReqRecordUserOnline;
import com.eurlanda.edu.tp.webdomain.response.RepClouderaData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by test on 2018/4/21.
 */

public interface UserAccessRecordService {


    void recordUserOnlineData(Integer userId,String token) throws ApplicationErrorException;


    void recordUserOfflineData(Integer userId,Integer type) throws ApplicationErrorException;


    void updateAbnormalOfflineUserRecordStatus() throws ApplicationErrorException;

    RepClouderaData getClouderaData(ReqClouderaData reqClouderaData) throws ApplicationErrorException;


    Map<String,Object> getRealTimeOnlineData() throws ApplicationErrorException;

    Map<String,Object> getAccumulativeOnlineData() throws ApplicationErrorException;

    List<Map> getDayAndWeekAndMonthOnlineAvgData(Map<String,Object> paraMap) throws ApplicationErrorException;

    List<Map> getCharacterCloudDashboardData();


    UserAccessRecord selectUserRecordByUserIdAndDate(UserAccessRecord record)throws ApplicationErrorException;


    int updateUserRecordByUserIdAndDate(UserAccessRecord record)throws ApplicationErrorException;



    int deleteExpireRecord()throws ApplicationErrorException;

    List<String> selectRecentlyLoginUserData(Map<String,Object> map ) throws ApplicationErrorException;


    Boolean logoutCheck(ReqRecordUserOnline recordUserOnline);


}
