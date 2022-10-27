package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.cloudwareDomain.ResCloudware;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ResponseMessage;

public interface RancherService {
    /**
     * 创建共享文件夹
     * @param secret
     * @param userId userId,courseId
     * @param type 0:用户 1:课程
     * @return
     */
    public ResCloudware createVolumes(String secret, int userId,int type);

    /**
     * 开始云件
     * @param cloudwareType
     * @param userId
     * @return  ws,serviceName,serviceId,pulsarId
     */
    public ResponseMessage startServices(String secret,String cloudwareType,int userId,int experimentId) throws ApplicationErrorException;

    /**
     * 删除云件
     * pulsarId(可选)
     * serviceName
     * serviceId
     */
    public ResponseMessage deleteCloudWare(String pulsarId,String serviceName,String serviceId);

    /**
     * 开启对应的terminal
     * @param serviceId
     * @return
     */
    public ResponseMessage terminals(String serviceId);

    public ResponseMessage deleteVolume(String volumeId);
}
