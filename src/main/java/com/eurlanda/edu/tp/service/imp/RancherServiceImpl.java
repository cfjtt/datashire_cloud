package com.eurlanda.edu.tp.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.eurlanda.edu.tp.Util.HttpClientUtils;
import com.eurlanda.edu.tp.cloudwareDomain.ResCloudware;
import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.ImageConfig;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.enums.CloudwareTypeEnum;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperimentCloudware;
import com.eurlanda.edu.tp.webdomain.response.ResExperimentInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RancherServiceImpl implements RancherService{
    @Value("${rancher.username}")
    private String username;
    @Value("${rancher.password}")
    private String password;
    @Value("${rancher.endpoint}")
    private String endpoint;
    @Value("${rancher.wsaddr}")
    private String wsaddr;
    @Value("${rancher.env}")
    private String env;
    @Value("${rancher.lbid}")
    private String lbid;
    @Value("${rancher.secret}")
    private String secret;
    @Value("${cloudware.volumeurl}")
    private String volumeUrl;
    @Value("${rancher.stackId}")
    private String stackId;
    @Value("${file.shareFileUrl}")
    private String shareFileUrl;
    @Value("${cloudware.volumnShareBasePath}")
    private String volumeSharePath;
    @Value("${cloudware.volumnShareCoursePath}")
    private String volumeCoursePath;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private ImageConfigService configService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentExperimentService studentExperimentService;

    // 全局共享锁,用来控制LB转发规则的
    private static Object LB_LOCK = new Object();

    protected final Log logger = LogFactory.getLog(this.getClass());
    /**
     * 验证秘钥
     * @param secret
     * @return
     */
    public  boolean verifyToken(String secret){
        boolean isValid = false;
        if(this.secret.equals(secret)){
            isValid = true;
        }
        return isValid;
    }


    public String openContainer(int userId,int type){
        Map<String,Object> tmpData = new HashMap<>();
        tmpData.put("instanceTriggeredStop","stop");
        tmpData.put("startOnCreate",true);
        tmpData.put("publishAllPorts",false);
        tmpData.put("privileged",true);
        tmpData.put("stdinOpen",true);
        tmpData.put("tty",true);
        tmpData.put("readOnly",false);
        tmpData.put("runInit",false);
        tmpData.put("networkMode","managed");
        tmpData.put("type","container");
        //tmpData.put("requestedHostId","1h1");
        tmpData.put("secrets",new String[]{});
        tmpData.put("dataVolumes",new String[]{userId+":/data"});
        tmpData.put("dataVolumesFrom",new String[]{});
        tmpData.put("dns",new String[]{});
        tmpData.put("dnsSearch",new String[]{});
        tmpData.put("capAdd",new String[]{});
        tmpData.put("capDrop",new String[]{});
        tmpData.put("devices",new String[]{});
        Map<String,Object> logConfigMap = new HashMap<>();
        logConfigMap.put("driver","");
        logConfigMap.put("config",new HashMap<>());
        tmpData.put("logConfig",logConfigMap);
        tmpData.put("dataVolumesFromLaunchConfigs",new String[]{});
        tmpData.put("imageUuid","docker:busybox");
        tmpData.put("ports",new String[]{});
        tmpData.put("instanceLinks",new HashMap<>());
        Map<String,Object> labelMap = new HashMap<>();
        labelMap.put("container_type","cloudware");
        tmpData.put("labels",labelMap);
        if(type==0) {
            tmpData.put("name", userId);
        } else {
            tmpData.put("name","test"+userId);
        }
        tmpData.put("count",null);
        tmpData.put("createIndex",null);
        tmpData.put("created",null);
        tmpData.put("deploymentUnitUuid",null);
        tmpData.put("description",null);
        tmpData.put("externalId",null);
        tmpData.put("firstRunning",null);
        tmpData.put("healthState",null);
        tmpData.put("hostname",null);
        tmpData.put("kind",null);
        tmpData.put("memoryReservation",null);
        tmpData.put("milliCpuReservation",null);
        tmpData.put("removed",null);
        tmpData.put("startCount",null);
        tmpData.put("uuid",null);
        tmpData.put("volumeDriver",null);
        tmpData.put("workingDir",null);
        tmpData.put("user","root");
        tmpData.put("domainName",null);
        tmpData.put("memorySwap",null);
        tmpData.put("memory",null);
        tmpData.put("cpuSet",null);
        tmpData.put("cpuShares",null);
        tmpData.put("pidMode",null);
        tmpData.put("blkioWeight",null);
        tmpData.put("cgroupParent",null);
        tmpData.put("usernsMode",null);
        tmpData.put("pidsLimit",null);
        tmpData.put("diskQuota",null);
        tmpData.put("cpuCount",null);
        tmpData.put("cpuPercent",null);
        tmpData.put("ioMaximumIOps",null);
        tmpData.put("ioMaximumBandwidth",null);
        tmpData.put("cpuPeriod",null);
        tmpData.put("cpuQuota",null);
        tmpData.put("cpuSetMems",null);
        tmpData.put("isolation",null);
        tmpData.put("kernelMemory",null);
        tmpData.put("memorySwappiness",null);
        tmpData.put("shmSize",null);
        tmpData.put("uts",null);
        tmpData.put("ipcMode",null);
        tmpData.put("stopSignal",null);
        tmpData.put("oomScoreAdj",null);
        tmpData.put("ip",null);
        tmpData.put("ip6",null);
        tmpData.put("healthInterval",null);
        tmpData.put("healthTimeout",null);
        tmpData.put("healthRetries",null);
        String result = null;
        try {
            result =  HttpClientUtils.doPostByJsonWithAuth(endpoint+"/projects/"+env+"/container",JSON.toJSONString(tmpData,SerializerFeature.WriteMapNullValue),username,password);
            logger.info("opencontainer---" + result);
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }
    @Override
    public ResCloudware createVolumes(String secret, int userId,int type) {
        boolean isValid = verifyToken(secret);
        ResCloudware cloudware = new ResCloudware();
        if(!isValid){
            cloudware.setErrorCode(ErrorCode.InvalidSecret.getCode());
            return cloudware;
        }
        Map<String,Object> data = new HashMap<>();
        data.put("type","volume");
        data.put("driver","rancher-nfs");
        data.put("name",userId);
        Map<String,Object> map = new HashMap<>();
        if(type==0) {
            //用户共享文件夹
            map.put("exportBase", volumeSharePath);
        } else if(type==1){
            map.put("exportBase",volumeCoursePath);
        }
        map.put("host",volumeUrl);
        data.put("driverOpts",map);
        try {
            String result = HttpClientUtils.doPostByJsonWithAuth(endpoint+"/projects/"+env+"/volume", JSON.toJSONString(data,SerializerFeature.WriteMapNullValue),username,password);
            logger.info("createVolumes--" + result);
            if(result != null){
                //获取volume  id便于以后进行删除操作
                Map<String,Object> resultMap =(Map<String, Object>) JSON.parse(result);
                String volumeId = resultMap.get("id").toString();
                if(type==0){
                    //用户
                    User user = userService.getUserInfo(userId);
                    if(user != null){
                        user.setVolumeId(volumeId);
                        userService.updateUser(user);
                    }
                } else {
                    //课程
                    Course course = courseService.getCourseById(userId);
                    if(course != null){
                        course.setVolumeId(volumeId);
                        courseService.updateCourseWithOutSync(course);
                    }
                }
            }
            //openContainer(userId,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cloudware.setErrorCode(0);
        return cloudware;
    }

    @Override
    public synchronized ResponseMessage startServices(String secret,String cloudwareType, int userId,int experimentId) throws ApplicationErrorException {
        boolean isValid = verifyToken(secret);
        if(!isValid){
            return new ResponseMessage.Fail(ErrorCode.InvalidSecret.getCode(),ErrorCode.InvalidSecret.getErrorStringFormat());
        }
        //实验类型是否合法
        isValid = CloudwareTypeEnum.isExistTypeEnum(cloudwareType);
        if(!isValid || CloudwareTypeEnum.SHU_LIE_YUN.getEn().equals(cloudwareType)){
            return new ResponseMessage.Fail(ErrorCode.InvalidCloudwareType.getCode(),ErrorCode.InvalidCloudwareType.getErrorStringFormat());
        }
        //查询该用户是否存在
        User user = userService.getUserInfo(userId);
        if(user==null){
            return new ResponseMessage.Fail(ErrorCode.UserNotExist.getCode(),ErrorCode.UserNotExist.getErrorStringFormat());
        }

        //查询出课程id
        ResExperimentInfo resExperimentInfo = null;
        try {
            resExperimentInfo = experimentService.getExperiment(experimentId);
        } catch (ApplicationErrorException e) {
            logger.error("该课程不存在",e);
        }
        //创建一个随机名字
        String serviceName = null;
        synchronized (this) {
            serviceName = UUID.randomUUID().toString().replaceAll("-", "");
        }
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("scale",1);
        dataMap.put("assignServiceIpAddress",false);
        dataMap.put("startOnCreate",true);
        dataMap.put("type","service");
        dataMap.put("stackId",stackId);
        Map<String,Object> launchConfigMap = new HashMap<>();
        launchConfigMap.put("instanceTriggeredStop","stop");
        launchConfigMap.put("kind","container");
        launchConfigMap.put("networkMode","managed");
        launchConfigMap.put("privileged",true);
        launchConfigMap.put("publishAllPorts",false);
        launchConfigMap.put("readOnly",false);
        launchConfigMap.put("runInit",false);
        launchConfigMap.put("startOnCreate",true);
        launchConfigMap.put("stdinOpen",true);
        launchConfigMap.put("tty",true);
        launchConfigMap.put("vcpu",1);
        launchConfigMap.put("type","launchConfig");
        Map<String,Object> labelMap = new HashMap<>();
        labelMap.put("io.rancher.scheduler.affinity:host_label","cloudware=true");
        launchConfigMap.put("labels",labelMap);
        Map<String,Object> restartMap = new HashMap<>();
        restartMap.put("name","always");
        launchConfigMap.put("restartPolicy",restartMap);
        launchConfigMap.put("secrets",new String[]{});
        //管理员不需要创建共享文件夹
        if(user.getRole() != RoleEnum.MANAGER.getCode()) {
            launchConfigMap.put("dataVolumes", new String[]{userId+":/root/Desktop/myFile",resExperimentInfo.getCourseId()+":/root/Desktop/course:ro"});
        } else {
            launchConfigMap.put("dataVolumes", new String[]{resExperimentInfo.getCourseId()+":/root/Desktop/course"});
        }
        launchConfigMap.put("dns",new String[]{});
        launchConfigMap.put("dnsSearch",new String[]{});
        launchConfigMap.put("capAdd",new String[]{});
        launchConfigMap.put("capDrop",new String[]{});
        launchConfigMap.put("devices",new String[]{});
        Map<String,Object> logConfigMap = new HashMap<>();
        logConfigMap.put("driver","");
        logConfigMap.put("config",new HashMap<>());
        launchConfigMap.put("logConfig",logConfigMap);
        launchConfigMap.put("dataVolumesFromLaunchConfigs",new String[]{});
        launchConfigMap.put("imageUuid","docker:cloudwarelabs/xfce4-min");
        launchConfigMap.put("ports",new String[]{});
        launchConfigMap.put("blkioWeight",null);
        launchConfigMap.put("cgroupParent",null);
        launchConfigMap.put("count",null);
        launchConfigMap.put("cpuCount",null);
        launchConfigMap.put("cpuPercent",null);
        launchConfigMap.put("cpuPeriod",null);
        launchConfigMap.put("cpuQuota",null);
        launchConfigMap.put("cpuSet",null);
        launchConfigMap.put("cpuSetMems",null);
        launchConfigMap.put("cpuShares",null);
        launchConfigMap.put("createIndex",null);
        launchConfigMap.put("created",null);
        launchConfigMap.put("deploymentUnitUuid",null);
        launchConfigMap.put("description",null);
        launchConfigMap.put("diskQuota",null);
        launchConfigMap.put("domainName",null);
        launchConfigMap.put("externalId",null);
        launchConfigMap.put("firstRunning",null);
        launchConfigMap.put("healthInterval",null);
        launchConfigMap.put("healthRetries",null);
        launchConfigMap.put("healthState",null);
        launchConfigMap.put("healthTimeout",null);
        launchConfigMap.put("hostname",null);
        launchConfigMap.put("ioMaximumBandwidth",null);
        launchConfigMap.put("ioMaximumIOps",null);
        launchConfigMap.put("ip",null);
        launchConfigMap.put("ip6",null);
        launchConfigMap.put("ipcMode",null);
        launchConfigMap.put("isolation",null);
        launchConfigMap.put("kernelMemory",null);
        launchConfigMap.put("memory",null);
        launchConfigMap.put("memoryMb",null);
        launchConfigMap.put("memoryReservation",null);
        launchConfigMap.put("memorySwap",null);
        launchConfigMap.put("memorySwappiness",null);
        launchConfigMap.put("milliCpuReservation",null);
        launchConfigMap.put("oomScoreAdj",null);
        launchConfigMap.put("pidMode",null);
        launchConfigMap.put("pidsLimit",null);
        launchConfigMap.put("removed",null);
        launchConfigMap.put("requestedIpAddress",null);
        launchConfigMap.put("shmSize",null);
        launchConfigMap.put("startCount",null);
        launchConfigMap.put("stopSignal",null);
        launchConfigMap.put("user","root");
        launchConfigMap.put("userdata",null);
        launchConfigMap.put("usernsMode",null);
        launchConfigMap.put("uts",null);
        launchConfigMap.put("uuid",null);
        launchConfigMap.put("volumeDriver",null);
        launchConfigMap.put("workingDir",null);
        launchConfigMap.put("networkLaunchConfig",null);
        dataMap.put("launchConfig",launchConfigMap);
        dataMap.put("secondaryLaunchConfigs",new String[]{});
        dataMap.put("name",serviceName);
        dataMap.put("createIndex",null);
        dataMap.put("created",null);
        dataMap.put("description",null);
        dataMap.put("externalId",null);
        dataMap.put("healthState",null);
        dataMap.put("kind",null);
        dataMap.put("removed",null);
        dataMap.put("selectorContainer",null);
        dataMap.put("selectorLink",null);
        dataMap.put("uuid",null);
        dataMap.put("vip",null);
        dataMap.put("fqdn",null);
        ResponseMessage responseMessage = cloudwareCreate(dataMap, cloudwareType, serviceName);

        if(responseMessage.getErrorCode() == 0) {
            //returnMap.put("ws", serviceName);
//            returnMap.put("service_name", serviceName);
//            returnMap.put("service_id", serviceId);
//            returnMap.put("pulsar_id", containerId);

            Map<String,Object> info = ((Map<String,Object>)responseMessage.getData());
            ReqStudentExperimentCloudware reqStudentExperimentCloudware = new ReqStudentExperimentCloudware();
            reqStudentExperimentCloudware.setStudentId(userId);
            reqStudentExperimentCloudware.setExperimentId(experimentId);
            reqStudentExperimentCloudware.setServiceName(serviceName);
            reqStudentExperimentCloudware.setServiceId(info.get("service_id").toString());
            reqStudentExperimentCloudware.setPulsarId(info.get("pulsar_id").toString());
            reqStudentExperimentCloudware.setWebSocket(info.get("ws").toString());
            studentExperimentService.createStudentExperimentCloudware(reqStudentExperimentCloudware);
        }

        return responseMessage;
    }

    /**
     * cloudware开启实验
     * @param dataMap
     * @param cloudwareType
     * @param serviceName
     */
    public ResponseMessage cloudwareCreate(Map<String,Object> dataMap,String cloudwareType,String serviceName) throws ApplicationErrorException {
        Map<String,Object> lanuchConfigMap = (Map<String, Object>) dataMap.get("launchConfig");
        List<ImageConfig> configList = configService.selectImageAddressByCloudware(CloudwareTypeEnum.parse(cloudwareType).getCode());
        String path = null;
        if(configList.size()>0){
            path = configList.get(0).getAddress();
        }
        if(path == null){
            throw new ApplicationErrorException(ErrorCode.GeneralError);
        }
        // 设置docker镜像
        lanuchConfigMap.put("imageUuid",path);
//        switch (CloudwareTypeEnum.parse(cloudwareType)){
//            case PYTHON:
//                lanuchConfigMap.put("imageUuid","docker:deeplabs/jupyter:v4.1");
//                break;
//            case BASE:
//                lanuchConfigMap.put("imageUuid","docker:cloudwarelabs/base:v2.0");
//                break;
//            case RSTUDIO:
//                //lanuchConfigMap.put("imageUuid","docker:cloudwarelabs/rstudio:v1.0");
//                lanuchConfigMap.put("imageUuid","docker:deeplabs/rstudio:v4.0");
//                //lanuchConfigMap.put("imageUuid","docker:deeplabs/testbase:v1.0");
//                break;
//            case HADOOP:
//                lanuchConfigMap.put("imageUuid","docker:deeplabs/hadoop:v4.0");
//                break;
//            default:
//                lanuchConfigMap.put("imageUuid","docker:cloudwarelabs/base:v2.0");
//                break;
//        }
        //lanuchConfigMap.put("entryPoint",new String[]{"startxfce4"});
        lanuchConfigMap.put("entryPoint",null);
        lanuchConfigMap.put("vcpu",4);
        lanuchConfigMap.put("cpuCount",4);

        String result = HttpClientUtils.doPostByJsonWithAuth(endpoint+"/projects/"+env+"/service",JSON.toJSONString(dataMap,SerializerFeature.WriteMapNullValue),username,password);
        if(result != null) {
            Map<String,Object> resultMap = (Map<String, Object>) JSON.parse(result);
            if(resultMap.containsKey("type") && !resultMap.get("type").equals("error")){
                String id = resultMap.get("id")+"";
                // 获取容器id
                String containerId = getContainerId(id,0);
                if(containerId == null){
                    logger.error("-----未获取到容器ID----" + id + "," + cloudwareType + "," + serviceName +",result--" + result);
                    return new ResponseMessage.Fail(ErrorCode.GeneralError.getCode(),"获取容器id失败");
                }
                //获取主机id
//                String hostId = getHostId(containerId);
//                if(hostId==null){
//                    return new ResponseMessage.Fail(ErrorCode.GeneralError.getCode(),"获取hostId失败");
//                }
                //创建容器
                //Map<String,Object> returnMap = createPulsarContainer(serviceName,containerId,hostId,id);
                Map<String,Object> returnMap = getNONVCContainer(serviceName,containerId,id);
                if(resultMap != null) {
                    return new ResponseMessage.Success<>(returnMap);
                }
            }
        } else {
            logger.error("创建容器异常------" + cloudwareType + "," + serviceName);
        }
        return new ResponseMessage.Fail(ErrorCode.GeneralError.getCode(),"开始云件失败请稍后重试");
    }

    /**
     * loadBalance获得可以访问的地址
     * @param serviceName
     * @param containerId
     * @param serviceId
     * @return
     */
    public synchronized Map<String,Object> getNONVCContainer(String serviceName,String containerId,String serviceId){
        logger.debug("--------添加lb规则---------" + serviceName + "----" + containerId + "" + serviceId);
        if(StringUtils.isEmpty(serviceName)) {
            logger.error("设置Loadbalance时,不允许servicename为空");
            return null;
        }

        Map<String,Object> returnMap = new HashMap<>();
        String lbUrl = endpoint + "/projects/" + env + "/loadbalancerservices/" + lbid;
        synchronized (LB_LOCK) {
            String result = null;
            try {
                result = HttpClientUtils.doGetWithAuth(
                                lbUrl, new HashMap<>(), username, password);
            } catch (Exception e) {
                logger.error("获取loadbalance 规则列表异常", e);
                return null;
            }
            if (result != null) {
                Map<String, Object> loadbalanceServiceMap = (Map) JSON.parse(result);
                Map<String, Object>
                        lbConfigMap =
                        (Map<String, Object>) loadbalanceServiceMap.get("lbConfig");
                List portRulesList = (List) lbConfigMap.get("portRules");
                Map<String, Object> portRoulesMap = new HashMap<>();
                portRoulesMap.put("backendName", null);
                portRoulesMap.put("hostname", null);
                portRoulesMap.put("selector", null);
                portRoulesMap.put("protocol", "http");
                portRoulesMap.put("type", "portRule");
                portRoulesMap.put("path", "/" + serviceName);
                portRoulesMap.put("priority", "12");
                portRoulesMap.put("serviceId", serviceId);
                portRoulesMap.put("sourcePort", 8001);
                portRoulesMap.put("targetPort", 6901);
                portRulesList.add(JSONObject.parseObject(
                        JSON.toJSONString(portRoulesMap, SerializerFeature.WriteMapNullValue)));

                Map<String, Object>
                        launchConfigMap =
                        (Map) loadbalanceServiceMap.get("launchConfig");
                List<String> portsList = (List<String>) launchConfigMap.get("ports");
                if (!portsList.contains("8001:8001/tcp")) {
                    portsList.add("8001:8001/tcp");
                }

                //发起loadbalance请求
                result =
                        HttpClientUtils.doPutByJsonWithAuth(
                                lbUrl, JSON.toJSONString(loadbalanceServiceMap,
                                        SerializerFeature.WriteMapNullValue), username, password);
                //返回信息
                returnMap.put("errorCode", 0);
                logger.debug("返回信息:" + wsaddr + "/" + serviceName + "--result--" + result);
                returnMap.put("ws", serviceName);
                returnMap.put("service_name", serviceName);
                returnMap.put("service_id", serviceId);
                returnMap.put("pulsar_id", containerId);
                return returnMap;
            } else {
                logger.error("--------获取loadbalance 规则列表为空-------");
            }
        }
        return null;
    }
    /**
     * 获取容器Id
     * @param serviceId
     * @return
     */
    public String getContainerId(String serviceId,int index){

        //定时器开启实验
        String xfce4Id = null;
        index++;
        if(index > 20){
            // 启动超时,杀掉该服务
            String serviceUrl = endpoint+"/projects/"+env+"/services/";
            try {
                String r = HttpClientUtils.doDelete(serviceUrl + serviceId, username, password);
                if(r == null) {
                    logger.error("删除容器失败" + serviceId);
                }
            } catch (Exception e) {
                logger.error("启动容器超时,删除容器异常", e);
            }
            return null;
        }
        // 给rancher一点时间
        if(index > 1) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        try {
            String result = HttpClientUtils.doGetWithAuth(endpoint+"/projects/"+env+"/services/"+serviceId,new HashMap<>(),username,password);
            if(result != null ){
                Map<String,Object> resultMap = (Map<String, Object>) JSON.parse(result);
                if("error".equals(resultMap.get("type")+"") || resultMap.get("instanceIds") == null){
                    return getContainerId(serviceId,index);
                } else {
                    List instanceIds = (List) resultMap.get("instanceIds");
                    if(instanceIds.size()==0){
                        return getContainerId(serviceId,index);
                    } else {
                        //获得对应的容器id
                        xfce4Id = instanceIds.get(0)+"";
                        if(xfce4Id == null || xfce4Id.trim().length()==0){
                            return getContainerId(serviceId,index);
                        }
                    }
                }
            } else {
                return getContainerId(serviceId,index);
            }
        } catch (Exception e) {
            logger.error("getContainerId异常", e);
        }
        return xfce4Id;
    }

    /**
     * 获取容器对一个的主机id
     * @return
     */
    public String getHostId(String containerId){
        //定时器开启实验
        String hostId = null;
        try {
            String result = HttpClientUtils.doGetWithAuth(endpoint+"/projects/"+env+"/containers/"+containerId,new HashMap<>(),username,password);
            if(result != null ){
                Map<String,Object> resultMap = (Map<String, Object>) JSON.parse(result);
                if(resultMap.get("hostId")==null){
                    return getHostId(containerId);
                } else {
                    hostId = resultMap.get("hostId").toString();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostId;
    }


    /**
     * 从loadbalance中移除代理规则
     * @param serviceName 异常的service的名称
     * @return true/false  成功/失败
     */
    private boolean removeRuleToLoadbalance(String serviceName) {

        String lbUrl = endpoint+"/projects/"+env+"/loadbalancerservices/"+lbid;
        // 并发删除
        synchronized (LB_LOCK) {
            String result = null;
            try {
                result = HttpClientUtils.doGetWithAuth(
                        lbUrl,
                        new HashMap<>(), username, password);
            } catch (Exception e) {
                logger.error("获取loadbalance异常", e);
                return false;
            }
            if (result != null) {
                Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(result);
                Map<String, Object>
                        lbCongigMap =
                        (Map<String, Object>) resultMap.get("lbConfig");
                List<Map<String, Object>>
                        portRulesList =
                        (List<Map<String, Object>>) lbCongigMap.get("portRules");
                Iterator<Map<String, Object>> portRulesIterator = portRulesList.iterator();
                while (portRulesIterator.hasNext()) {
                    Map<String, Object> portMap = portRulesIterator.next();
                    if (portMap.get("path") != null
                            && portMap.get("path").toString().indexOf(serviceName) > 0) {
                        portRulesIterator.remove();
                        break;
                    }

                }

                //发起请求，删除容器
                result = HttpClientUtils.doPutByJsonWithAuth(lbUrl,
                        JSON.toJSONString(resultMap, SerializerFeature.WriteMapNullValue),
                        username, password);
                if(result == null) {
                    logger.debug("------------设置Lb 规则结果为空-----" + serviceName);
                    return false;
                }
                logger.debug("------------设置Lb 规则结果-----" + result);
                return true;
            } else {
                logger.debug("移除loadbalance中规则异常" + serviceName);
                return false;
            }
        }
    }

    /**
     * cloudware实验类型删除实验
     * @param pulsarId
     * @param serviceName
     * @param serviceId
     * @param serviceUrl
     * @param containerUrl
     */
    public ResponseMessage deleteCloureWare(String pulsarId,String serviceName,String serviceId, String serviceUrl,String containerUrl){

        String result = null;
        boolean flag = removeRuleToLoadbalance(serviceName);
        if(flag) {
            //删除service
            result = HttpClientUtils.doDelete(serviceUrl+serviceId, username, password);
            //删除container
            if(pulsarId != null) {
                result = HttpClientUtils.doDelete(containerUrl+pulsarId, username, password);
            }
            if(result != null) {
                return new ResponseMessage.Success();
            }
        }
        return new ResponseMessage.Fail(ErrorCode.GeneralError.getCode(),"删除云件失败");
    }

    /**
     * 其他类型的删除云件
     * @param serviceName
     * @param serviceId
     * @param serviceUrl
     * @param containerUrl
     * @return
     */
    public ResponseMessage deleteJupyterWare(String serviceName,String serviceId, String serviceUrl,String containerUrl){

        String result = null;
        // 并发删除
        boolean flag = removeRuleToLoadbalance(serviceName);
        if(flag) {
            //删除
            result = HttpClientUtils.doDelete(serviceUrl+serviceId,username,password);
            if(result != null){
//                Map<String,Object> deleteMap = (Map<String, Object>) JSON.parse(result);
                return new ResponseMessage.Success<>();
            }
        }
        return new ResponseMessage.Fail(ErrorCode.GeneralError.getCode(),"删除云件失败");
    }
    @Override
    public ResponseMessage deleteCloudWare(String pulsarId, String serviceName, String serviceId) {
        String serviceUrl = endpoint+"/projects/"+env+"/services/";
        String containerUrl = endpoint+"/projects/"+env+"/containers/";
        ResponseMessage responseMessage = null;

        if (pulsarId != null && pulsarId.trim().length() > 0) {
            // cloudware删除
            responseMessage =
                    deleteCloureWare(pulsarId, serviceName, serviceId, serviceUrl, containerUrl);
        } else {
            //其他所有的删除
            responseMessage =
                    deleteJupyterWare(serviceName, serviceId, serviceUrl, containerUrl);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage terminals(String cloudwareId) {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("attachStdin",true);
        dataMap.put("attachStdout",true);
        dataMap.put("tty",true);
        dataMap.put("command",new String[]{"/bin/sh","-c","TERM=xterm-256color; export TERM; [ -x /bin/bash ] && ([ -x /usr/bin/script ] && /usr/bin/script -q -c \"/bin/bash\" /dev/null || exec /bin/bash) || exec /bin/sh"});
        String result = HttpClientUtils.doPostByJson(endpoint+"/projects/"+env+"/containers/"+cloudwareId+"/?action=execute",JSON.toJSONString(dataMap));
        if(result!=null){
            return new ResponseMessage.Success();
        }
        return new ResponseMessage.Fail(ErrorCode.GeneralError.getCode(),"terminal接口失败");
    }

    /**
     * 删除volume(405 method not allow)
     * @param volumeId
     * @return
     */
    @Override
    public ResponseMessage deleteVolume(String volumeId) {
        String volumeUrl = endpoint+"/projects/"+env+"/volumes/"+volumeId;
        String result = HttpClientUtils.doDelete(volumeUrl,username,password);

        return new ResponseMessage.Success();
    }
}
