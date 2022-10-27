package com.eurlanda.edu.tp.authentication.auth;

import com.eurlanda.edu.tp.authentication.authUser.AuthUserInfo;
import com.eurlanda.edu.tp.authentication.authUser.AuthUserInfoService;
import com.eurlanda.edu.tp.authentication.security.JwtTokenUtil;
import com.eurlanda.edu.tp.authentication.security.JwtUser;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.SysLicenseService;
import com.eurlanda.edu.tp.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Created by Justin on 2017/6/3.
 */

@Service
public class AuthServiceImpl implements AuthService {

    static Logger log = Logger.getLogger(AuthServiceImpl.class.getName());

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private AuthUserInfoService authUserInfoService;
    private UserService userService;

    public static boolean isHaveLicense = false;
    public static volatile boolean isCheck = false;//是否解析license信息
    public static Long startMillisecond = 0l;
    public static Long expireMillisecond = 0l;
    public static Integer maxMeanwhileOnlineUserCount = 1000;

    @Value("${token.tokenHeader}")
    private String tokenHead;

    @Value("${token.initRole}")
    private String initRole;

    @Autowired
    private SysLicenseService sysLicenseService;

//    @Autowired
//    private SessionRegistry sessionRegistry;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            AuthUserInfoService authUserInfoService,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authUserInfoService = authUserInfoService;
        this.userService = userService;
    }

    @Override
    public AuthUserInfo register(AuthUserInfo authUserInfoToAdd) {
        final String username = authUserInfoToAdd.getUsername();
        if (authUserInfoService.getUserInfo(username) != null) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = authUserInfoToAdd.getPassword();
        authUserInfoToAdd.setPassword2(rawPassword);
        authUserInfoToAdd.setPassword(encoder.encode(rawPassword));
        authUserInfoToAdd.setLastPasswordResetDate(new Date());
        authUserInfoToAdd.setRoles(Collections.singletonList(initRole));
        return authUserInfoService.addUserInfo(authUserInfoToAdd);
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

//    public String loginOut(String username){
//        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
//        if(allPrincipals != null){
//            for(Object pricipal : allPrincipals){
//                if(pricipal instanceof UserDetails){
//                    String userNameAuth = ((UserDetails) pricipal).getUsername();
//                    if(userNameAuth.equals(username)){
//
//                    }
//                }
//            }
//        }
//        return null;
//    }
    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        try{
            if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
                return jwtTokenUtil.refreshToken(token);
            }
        }catch (ApplicationErrorException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updatePassword(int userId, String oldPassword, String newPassword, String confirmPassword) throws
            ApplicationErrorException {
        User user = userService.getUserInfo(userId);
        if(user == null){
            throw new ApplicationErrorException(ErrorCode.UserNotExist);
        }

        if(!newPassword.equals(confirmPassword)){
            throw new ApplicationErrorException(ErrorCode.NewPasswordsNotTheSame);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(oldPassword, user.getPassword())){
            throw new ApplicationErrorException(ErrorCode.IncorrectOldPassword);
        }

        user.setPassword(encoder.encode(newPassword));
        user.setPassword2(newPassword);
        userService.updateUser(user);
    }

    /**
     * 验证license
     */
    @Override
    public void licenseValidate() {

        Boolean isHaveLicense = true;
        Integer maxUserCount = 1;
        Map<String,Object> map = null;
        try{
            map = sysLicenseService.decodeLicense();
        }catch (ApplicationErrorException e){
            if(e.getErrorCode() == ErrorCode.NoImportLicense.getCode() || e.getErrorCode() == ErrorCode.InvalidLicense.getCode()){
                AuthServiceImpl.isHaveLicense = false;
            }
            return;

        }
        String maxOnlineNum = map.get("maxOnlineNum") + "";
        String startTime = map.get("startTime") + "";
        String endTime = map.get("endTime") + "";
        if("null".equals(endTime)){
            AuthServiceImpl.expireMillisecond = null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date start = sdf.parse(startTime);
            AuthServiceImpl.startMillisecond = start.getTime();
            if(!("null".equals(endTime))){
                Date expire = sdf.parse(endTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(expire);
                calendar.add(Calendar.DATE,1);
                AuthServiceImpl.expireMillisecond = calendar.getTime().getTime();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("licenseValidate"  + e.getMessage());
        }

        maxUserCount = Integer.parseInt(maxOnlineNum);
        AuthServiceImpl.isHaveLicense = isHaveLicense;
        AuthServiceImpl.maxMeanwhileOnlineUserCount = maxUserCount;
        AuthServiceImpl.isCheck = true;

    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        System.out.println(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DATE,1);
        System.out.println(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        System.out.println(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_WEEK,1);
        System.out.println(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH,1);
        System.out.println(sdf.format(calendar.getTime()));

    }
}
