package com.eurlanda.edu.tp.authentication.auth;

import com.eurlanda.edu.tp.Util.RecordUtils;
import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.authentication.security.JwtAuthenticationRequest;
import com.eurlanda.edu.tp.authentication.security.JwtAuthenticationResponse;
import com.eurlanda.edu.tp.authentication.security.JwtTokenUtil;
import com.eurlanda.edu.tp.authentication.security.JwtUpdatePasswordRequest;
import com.eurlanda.edu.tp.dao.entity.SysLicense;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.dao.entity.UserAccessRecord;
import com.eurlanda.edu.tp.dao.entity.UserStatus;
import com.eurlanda.edu.tp.dao.mapper.UserAccessRecordMapper;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.SysLicenseService;
import com.eurlanda.edu.tp.service.UserAccessRecordService;
import com.eurlanda.edu.tp.service.UserService;
import com.eurlanda.edu.tp.service.UserStatusService;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 2017/6/3.
 */

@CrossOrigin
@RestController
@RequestMapping("auth")
public class AuthController {

    static Logger log = Logger.getLogger(AuthController.class.getName());

    public static Map<String, Object> loginUserMap = new ConcurrentHashMap<>();
    //确认需要T掉的用户
    public static Map<String, Object> repeatLoginUserMap = new ConcurrentHashMap<>();
    //重复登录的用户，还不确定是否要T掉
    public static Map<String, Object> temporaryRepeatLoginUserMap = new ConcurrentHashMap<>();

    @Value("${token.tokenHeader}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private EduApi eduApi;

    @Autowired
    private UserStatusService statusService;

    @Autowired
    private SysLicenseService sysLicenseService;

    @Autowired
    private UserAccessRecordService userAccessRecordService;

    @Autowired
    private UserAccessRecordMapper userAccessRecordMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @ApiOperation(value = "登录", notes = "登录接口")
    @PostMapping(value = "login")
    public ResponseMessage<ResponseEntity<?>> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        log.info(String.format("User '%s' tries to login.", authenticationRequest.getUsername()));


       /* User user = userService.getUserInfo(authenticationRequest.getUsername());
        String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());*/
        //登录前校验
        /*authService.licenseValidate();
        SysLicense sysLicense = sysLicenseService.selectLicenseData();
        if (sysLicense != null) {
        }*/
        User user = userService.getUserInfo(authenticationRequest.getUsername());
        /*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user == null || !encoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            //去除修改过密码的用户的缓存

            return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.BadCredential));

        }
        if (!AuthServiceImpl.isHaveLicense) {
            //没有license,只允许管理员登录
            if (user.getRole() != RoleEnum.MANAGER.getCode()) {
                //throw new AuthenticationException();
                return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.SystemIsNotActivated));
            }
        }

        //结束时间为null则为不限时长
        if (AuthServiceImpl.expireMillisecond != null) {

            if (System.currentTimeMillis() > AuthServiceImpl.expireMillisecond) {
                //过期
                if (user.getRole() != RoleEnum.MANAGER.getCode()) {
                    //throw new AuthenticationException();
                    return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.SystemAlreadyExpired));
                }
            }

        }



        String token = null;
        boolean flag = false;
        synchronized (this) {
            User validateUser = userService.selectByPrimaryKey2(user.getId());
            Integer isRemind = validateUser.getIsRemind();//是否提醒过用户重复登录
            Long tokenCreateTime = validateUser.getTokenCreateTime();//当前登录用户token创建的时间戳
            String dbToken = validateUser.getToken();//当前登录用户token创建的时间戳
            UserAccessRecord userAccessRecord = new UserAccessRecord();
            userAccessRecord.setUserId(user.getId());
            Date curDate = RecordUtils.getYearMonthDay(new Date());
            userAccessRecord.setAccessDate(curDate);
            UserAccessRecord record = userAccessRecordMapper.selectUserRecordByUserIdAndDate(userAccessRecord);
            boolean isLogin = false;//(tokenCreateTime != null && tokenCreateTime.intValue() != -1);//token创建时间已经存在，说明该用户已经登录
            if(tokenCreateTime != null){
                //判断是否重置密码
                if(tokenCreateTime == -1){
                    //判断用户是否在线
                    if(record != null && record.getStatus().intValue() == 1){
                        //用户在线，判定为重复登录
                        isLogin = true;
                    }else {
                        //用户不在线，判定正常登录
                        isLogin = false;
                    }
                }else {
                    //token创建时间不等于null，又不等于-1
                    isLogin = true;
                }
            }
            if (isLogin && !user.getUsername().equals("manager")) {
                //重复登录
                if (((isRemind == null || isRemind.intValue() == 0) || !authenticationRequest.getIsOffline())) {
                    //temporaryRepeatLoginUserMap.put(user.getUsername(), loginUserMap.get(user.getUsername()));
                    User paraUser = new User();
                    paraUser.setIsRemind(1);
                    paraUser.setTokenCreateTime(tokenCreateTime);
                    paraUser.setId(user.getId());
                    paraUser.setToken(dbToken);
                    userService.updateByPrimaryKey2(paraUser);//修改为已提醒
                    return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.RepeatLogin));

                } else if ((isRemind.intValue() == 1) && authenticationRequest.getIsOffline()) {
                    //踢掉已经登录的用户

                    //重新生成token
                    token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
                    //将重复登录的用户加入确定要t掉的Map中
                    Date createdDate = jwtTokenUtil.getCreatedDateFromToken(token);
                    User paraUser = new User();
                    paraUser.setIsRemind(0);//重置是否通知
                    if(tokenCreateTime != null && tokenCreateTime.intValue() == -1){
                        //重复登录时候被重置密码，不予登录
                        return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.AlreadyResetPassword));
                        //paraUser.setTokenCreateTime(-1l);
                    }else {
                        paraUser.setTokenCreateTime(createdDate.getTime());
                    }
                    paraUser.setToken(token);
                    paraUser.setId(user.getId());
                    userService.updateByPrimaryKey2(paraUser);
                    //去除已经T掉用户的记录
                    flag = true;
                } else {
                    //不踢掉已经登录的用户
                    //token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
                    return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.NoOffline));
                }
                //return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.Offline));
            } else {
                token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
                Date createdDate = jwtTokenUtil.getCreatedDateFromToken(token);
                User paraUser = new User();
                paraUser.setIsRemind(0);//重置是否通知
                paraUser.setTokenCreateTime(createdDate.getTime());
                paraUser.setId(user.getId());
                paraUser.setToken(token);
                userService.updateByPrimaryKey2(paraUser);
            }
        }

        Integer currentCount = userAccessRecordMapper.selectCurrentOnlineUserCount();
        //验证是否超过最大同时在线人数
        Integer maxCount = currentCount;//AuthController.loginUserMap.size() - 1;;//userAccessRecordMapper.selectCurrentOnlineUserCount();
        //管理员不算同时在线人数
        if (maxCount >= AuthServiceImpl.maxMeanwhileOnlineUserCount && !flag) {
            if (user.getRole() != RoleEnum.MANAGER.getCode()) {
                //修改已经记录的登录信息
                User paraUser = new User();
                paraUser.setIsRemind(0);//重置是否通知
                paraUser.setTokenCreateTime(null);
                paraUser.setId(user.getId());
                paraUser.setToken(null);
                userService.updateByPrimaryKey2(paraUser);
                return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.ExceedTheMaximumNumberOfOnline));
            }
        }

        log.info(String.format("User '%s' logs on successfully.", authenticationRequest.getUsername()));*/
        String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return new ResponseMessage.Success<>(ResponseEntity.ok(
                new JwtAuthenticationResponse(user.getId(), user.getRole(),
                        authenticationRequest.getUsername(), token)));
    }

    @ApiOperation(value = "修改密码", notes = "")
    @PostMapping(value = "updatePassword")
    public ResponseMessage updatePassword(@RequestBody JwtUpdatePasswordRequest updatePasswordRequest) throws
            ApplicationErrorException {
        log.info(String.format("User '%d' tries to update password.", updatePasswordRequest.getUserId()));

        authService.updatePassword(
                updatePasswordRequest.getUserId(), updatePasswordRequest.getOldPassword(),
                updatePasswordRequest.getNewPassword(), updatePasswordRequest.getConfirmPassword());


        log.info(String.format("User '%d' updates password successfully.", updatePasswordRequest.getUserId()));

        //同步数猎云
        User user = userService.getUserInfo(updatePasswordRequest.getUserId());
        if (user != null) {
            Map<String, Object> paramMap = new HashMap<>();
            List<Map<String, Object>> userList = new ArrayList<>();
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("phone", user.getUsername());
            userMap.put("password", updatePasswordRequest.getNewPassword());
            userList.add(userMap);
            paramMap.put("userList", userList);
            eduApi.updatePassword(paramMap);
        }
        return new ResponseMessage.Success();
    }

//    @RequestMapping(value = "refresh", method = RequestMethod.GET)
//    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException {
//        String token = request.getHeader(tokenHeader);
//        String refreshedToken = authService.refresh(token);
//        if (refreshedToken == null) {
//            return ResponseEntity.badRequest().body(null);
//        } else {
//            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
//        }
//        return null;
//    }
//
//    @RequestMapping(value = "register", method = RequestMethod.POST)
//    public ResponseEntity<?> register(@RequestBody AuthUserInfo addedUserInfo) throws AuthenticationException{
//        return ResponseEntity.ok(authService.register(addedUserInfo));
//    }

}
