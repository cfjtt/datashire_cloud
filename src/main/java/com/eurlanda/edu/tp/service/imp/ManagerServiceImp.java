package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.dao.mapper.UserMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.Util.EncryptionUtil;
import com.eurlanda.edu.tp.cloudwareDomain.ReqCreateVolume;
import com.eurlanda.edu.tp.cloudwareDomain.ResCloudware;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.ManagerService;
import com.eurlanda.edu.tp.service.RancherService;
import com.eurlanda.edu.tp.webdomain.request.ReqResetPassword;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ManagerServiceImp implements ManagerService {

    static Logger log = Logger.getLogger(ManagerServiceImp.class.getName());

    @Value("${default.password}")
    private String defaultPassword;

    @Value("${cloudware.createVolumeUrl}")
    private String createVolumeUrl;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RancherService rancherService;

    @Override
    public int resetPassword(ReqResetPassword reqResetPassword) throws ApplicationErrorException {
        User user = new User();
        user.setId(reqResetPassword.getUserId());
        //判断用户是否存在
        User validateUser = userMapper.selectByPrimaryKey(reqResetPassword.getUserId());
        if(validateUser == null){
            throw new ApplicationErrorException(ErrorCode.UserNotExist);
        }

        user.setPassword(EncryptionUtil.encryptPassword(defaultPassword));
        user.setPassword2(defaultPassword);
        userMapper.updateByPrimaryKeySelective(user);

        return 0;
    }

    @Override
    public User createUser(String username, RoleEnum role) throws ApplicationErrorException {
        User user = new User();

        user.setUsername(username);
        user.setRole(role.getCode());
        user.setPassword(EncryptionUtil.encryptPassword(defaultPassword));
        user.setPassword2(defaultPassword);
        userMapper.insert(user);

        log.info(String.format("Start to create volume for user %d...", user.getId()));

        /**
         * 创建共享文件夹(暂时去掉,等自己部署在放开)
         */
        ReqCreateVolume reqCreateVolume = new ReqCreateVolume();
        reqCreateVolume.setUserId(user.getId());
        reqCreateVolume.setSecret("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmb28iOiJiYXIiLCJpYXQiOjE1MDU4MTM0NTd9.Ftw1yHeUrqdNvymFZcIpuEoS0RHBFZqu4MfUZON9Zm0");

        try {
            //直接调用方法即可
            ResCloudware resCloudware = rancherService.createVolumes(reqCreateVolume.getSecret(),reqCreateVolume.getUserId(),0);
            if (resCloudware.getErrorCode() != 0) {
                log.error(String.format("Creating volume failed. Error code returned %d.", resCloudware.getErrorCode()));
                throw new ApplicationErrorException(ErrorCode.GeneralError);
            }
        }catch (RestClientException e){
            log.error(String.format("Creating volume failed. Error message:%s", e.getMessage()));
            log.log(Level.ERROR, e.getMessage(), e);
            throw new ApplicationErrorException(ErrorCode.GeneralError);
        }
        log.info("Creating volume succeeded.");
        return user;
    }
}
