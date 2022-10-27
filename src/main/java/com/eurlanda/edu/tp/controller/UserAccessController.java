package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.UserAccessRecordService;
import com.eurlanda.edu.tp.webdomain.request.ReqClouderaData;
import com.eurlanda.edu.tp.webdomain.request.ReqRecordUserOnline;
import com.eurlanda.edu.tp.webdomain.response.RepClouderaData;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by test on 2018/4/21.
 */
@CrossOrigin
@RestController
@RequestMapping("record")
public class UserAccessController {

    static Logger log = Logger.getLogger(UserAccessController.class.getName());


    @Autowired
    private UserAccessRecordService userAccessRecordService;

    @ApiOperation(value = "接收心跳检测", notes = "用户登录后每一分钟发送一次请求，记录更新数据")
    @PostMapping(value = "login")
    public ResponseMessage recordUserOnlineData(@RequestBody ReqRecordUserOnline record, HttpServletRequest request)throws
            ApplicationErrorException {
        String token = request.getHeader("authorization");
        if(token != null){
            if(token.indexOf("Bearer")>-1) {
                token = token.substring(token.indexOf("Bearer")+6).trim();
            }else {
                log.error("recordUserOnlineData获取token为null");
            }
        }else {
            log.error("recordUserOnlineData获取token为null");
        }
        userAccessRecordService.recordUserOnlineData(record.getUserId(),token);
        return new ResponseMessage.Success<>();
    }


    @ApiOperation(value = "离线检查", notes = "用户退出时，检测是否被加入T出的Map中")
    @PostMapping(value = "logoutCheck")
    public ResponseMessage<Boolean> logoutCheck(@RequestBody ReqRecordUserOnline record)throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(userAccessRecordService.logoutCheck(record));
    }


    @ApiOperation(value = "更改用户状态", notes = "用户注销或者关掉客户端，将用户的状态改为下线")
    @GetMapping(value = "{userId}/logout/{type}")
    public ResponseMessage recordUserOfflineData(@PathVariable("userId") int userId, @PathVariable("type") int type, HttpServletRequest request) throws
            ApplicationErrorException {
        String token = request.getHeader("authorization");
        if(token != null){
            if(token.indexOf("Bearer")>-1) {
                token = token.substring(token.indexOf("Bearer")+6).trim();
                ReqRecordUserOnline record = new ReqRecordUserOnline();
                record.setToken(token);
                record.setUserId(userId);
                //判断是当前正常登录状态用户操作
                boolean isAdd = userAccessRecordService.logoutCheck(record);
                if(isAdd){
                    type = 1;//正常退出
                }else {
                    type = 2;//被T用户，或者被重置密码
                }
            }else {
                log.info("recordUserOfflineData获取token为null");
            }
        }else {
            type = 1;
            log.info("recordUserOfflineData获取token为null");
        }

        userAccessRecordService.recordUserOfflineData(userId,type);
        return new ResponseMessage.Success<>();
    }

    @ApiOperation(value = "获取cdh报表数据", notes = "代理接口，获取cdh报表数据，再返回给前段")
    @PostMapping (value = "getClouderaData")
    public ResponseMessage<RepClouderaData> getClouderaData(@RequestBody ReqClouderaData reqClouderaData) throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(userAccessRecordService.getClouderaData(reqClouderaData));
    }



    @ApiOperation(value = "获取用户在实时在线时长相关数据", notes = "实时在线人数")
    @PostMapping (value = "getRealTimeOnlineData")
    public ResponseMessage<Map> getRealTimeOnlineData() throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(userAccessRecordService.getRealTimeOnlineData());
    }


    @ApiOperation(value = "累计使用时长", notes = "累计使用时长")
    @PostMapping (value = "getAccumulativeOnlineData")
    public ResponseMessage<Map> getAccumulativeOnlineData() throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(userAccessRecordService.getAccumulativeOnlineData());
    }

    @ApiOperation(value = "平均访问人数，在线时长", notes = "累计使用时长")
    @PostMapping (value = "getDayAndWeekAndMonthOnlineAvgData")
    public ResponseMessage<List<Map>> getDayAndWeekAndMonthOnlineAvgData() throws
            ApplicationErrorException {
        List<Map> result = userAccessRecordService.getDayAndWeekAndMonthOnlineAvgData(new HashMap<>());
        return new ResponseMessage.Success<>(result);
    }

    @ApiOperation(value = "字符云报表", notes = "课程内容，实验环境数量")
    @PostMapping (value = "getCharacterCloudDashboardData")
    public ResponseMessage<List<Map>> getCharacterCloudDashboardData() throws
            ApplicationErrorException {
        List<Map> result = userAccessRecordService.getCharacterCloudDashboardData();
        return new ResponseMessage.Success<>(result);
    }


    @ApiOperation(value = "实时登陆信息", notes = "查询出最近5分钟登录的用户信息")
    @PostMapping (value = "getRecentlyLoginUserData")
    public ResponseMessage<List<String>> getRecentlyLoginUserData(@RequestBody Map<String,Object> map) throws
            ApplicationErrorException {
        List<String> result = userAccessRecordService.selectRecentlyLoginUserData(map);
        return new ResponseMessage.Success<>(result);
    }



    @ApiOperation(value = "获取系统时间", notes = "")
    @PostMapping (value = "getSystemTime")
    public ResponseMessage<Map> getSystemTime() throws
            ApplicationErrorException {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = month + "";
        if(month < 10){
            monthStr = "0" + monthStr;
        }
        int date = calendar.get(Calendar.DATE);
        String dateStr = date + "";
        if(date < 10){
            dateStr = "0" + dateStr;
        }
        String yMd = year+ "-" + monthStr  + "-" + dateStr;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String hourStr = hour + "";
        if(hour < 10){
            hourStr = "0" + hour;
        }
        int min = calendar.get(Calendar.MINUTE);
        String minStr = min + "";
        if (min < 10){
            minStr = "0" + minStr;
        }
        int second = calendar.get(Calendar.SECOND);
        String secondStr = second + "";
        if (second < 10){
            secondStr = "0" + secondStr;
        }
        //String hMs = hour + ":" + min + ":" +second;
        String hMs = hourStr + ":" + minStr + ":" + secondStr;
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String week = weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        Map<String,Object> map = new HashMap<>();
        String yearMonthDateHourMin = yMd + " " + hMs;
        map.put("nYr_hMs",yearMonthDateHourMin);
        map.put("week",week);
        return new ResponseMessage.Success<>(map);
    }
















}




