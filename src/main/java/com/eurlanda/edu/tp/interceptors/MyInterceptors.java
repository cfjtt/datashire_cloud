package com.eurlanda.edu.tp.interceptors;

import com.eurlanda.edu.tp.authentication.auth.AuthController;
import com.eurlanda.edu.tp.authentication.security.JwtTokenUtil;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.dao.entity.UserStatus;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.service.UserService;
import com.eurlanda.edu.tp.service.UserStatusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class MyInterceptors implements HandlerInterceptor{

    static Logger log = Logger.getLogger(MyInterceptors.class.getName());
    @Autowired
    private UserStatusService statusService;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //非管理员用户，需要判断是否重置过密码
        String token = httpServletRequest.getHeader("authorization");
        if(token != null){
            if(token.indexOf("Bearer")>-1) {
                token = token.substring(token.indexOf("Bearer")+6).trim();
            }
        }else {
            return true;
        }
        String username = null;
        try{
            //username = tokenUtil.getUsernameFromToken(token);
        }catch (Exception e){
            e.printStackTrace();
            log.error("getUsernameFromToken发生错误：" + e.getMessage());
            return false;
        }
       /* boolean isRepeat = false;
        boolean isOffline = false;
        for (String key : AuthController.repeatLoginUserMap.keySet()){
            String mapName = AuthController.repeatLoginUserMap.get(key) + "";
            if(mapName.equals(username)){
                isRepeat = true;
                if(token.equals(key)){
                    isOffline = true;
                }
            }
        }
        //加入被T缓存中的用户
        if(isOffline){
            httpServletResponse.getWriter().write("alreadyResetPassword");
            return false;
        }

        //查询出用户的id
        User user = userService.getUserInfo(username);
        if(user.getRole() != RoleEnum.MANAGER.getCode()){
            //检查是否重置过密码
            List<UserStatus> userStatuses = statusService.selectUserStatusByUserId(user.getId());
            if(userStatuses.size() > 0){
                if(isRepeat){
                    if(isOffline){
                        httpServletResponse.getWriter().write("alreadyResetPassword");
                        return false;
                    }
                }else {
                    httpServletResponse.getWriter().write("alreadyResetPassword");
                    return false;
                }
            }

        }*/
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
