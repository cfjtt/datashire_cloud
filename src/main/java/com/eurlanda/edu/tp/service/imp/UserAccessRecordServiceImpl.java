package com.eurlanda.edu.tp.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.eurlanda.edu.tp.Util.HttpClientUtils;
import com.eurlanda.edu.tp.Util.RecordUtils;
import com.eurlanda.edu.tp.authentication.security.JwtTokenUtil;
import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.enums.CloudwareTypeEnum;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.UserAccessRecordService;
import com.eurlanda.edu.tp.service.UserStatusService;
import com.eurlanda.edu.tp.webdomain.request.ReqClouderaData;
import com.eurlanda.edu.tp.webdomain.request.ReqRecordUserOnline;
import com.eurlanda.edu.tp.webdomain.response.RepClouderaData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by test on 2018/4/21.
 */
@Service
public class UserAccessRecordServiceImpl implements UserAccessRecordService {

    @Autowired
    private UserAccessRecordMapper userAccessRecordMapper;

    @Autowired
    private RealTimeOnlineUserMapper realTimeOnlineUserMapper;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private UserStatusService statusService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${cdhAPI}")
    private String url;
    @Value("${cdhUsername}")
    private String username;
    @Value("${cdhPassword}")
    private String password;


    static Logger log = Logger.getLogger(UserAccessRecordServiceImpl.class.getName());

    /**
     * ??????????????????????????????????????????
     * 1.???????????????????????????????????????
     * 2.??????-->???????????????????????????????????????????????????????????????-->??????????????????
     *
     * @param userId
     * @return
     */
    @Override
    public void recordUserOnlineData(Integer userId, String token) throws ApplicationErrorException {
        if (userId.intValue() == 0) {
            throw new ApplicationErrorException(ErrorCode.GeneralError);
        }
        //1.???????????????????????????????????????
        UserAccessRecord paraRecord = new UserAccessRecord();
        paraRecord.setUserId(userId);
        Date curDate = RecordUtils.getYearMonthDay(new Date());
        paraRecord.setAccessDate(curDate);

        UserAccessRecord record = userAccessRecordMapper.selectUserRecordByUserIdAndDate(paraRecord);
        if (record == null) {
            //??????????????????????????????????????????
            UserAccessRecord newRecord = new UserAccessRecord();
            newRecord.setUserId(userId);
            newRecord.setAccessDate(curDate);
            newRecord.setDuration(1);//??????
            newRecord.setLastTime(System.currentTimeMillis());//????????????????????????
            newRecord.setStatus(1);//??????
            newRecord.setLoginTime(new Date());//????????????
            userAccessRecordMapper.insertUserAccessRecord(newRecord);
        } else {
            //????????????????????????,?????????????????????????????????
            Integer status = record.getStatus();
            if (status.intValue() == 0) {
                //????????????????????????????????????????????????????????????????????????????????????last_time
                record.setLastTime(System.currentTimeMillis());
                record.setStatus(1);
                record.setDuration(null);
                record.setLoginTime(new Date());//??????????????????
            } else {
                //?????????????????????????????????????????????????????????last_time
                Long curMis = System.currentTimeMillis();
                Long lastMis = record.getLastTime();
                Double add = (curMis - lastMis) / (1000 * 60.0);
                BigDecimal bigDecimal = new BigDecimal(add);
                bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
                Integer duration = record.getDuration();
                //????????????????????????????????????????????????????????????????????????????????????last_time????????????????????????????????????
                //???????????????????????????????????????????????????last_time
                if (bigDecimal.intValue() >= 1) {
                    record.setLastTime(curMis);
                    record.setDuration(duration + bigDecimal.intValue());//????????????
                }
            }
            userAccessRecordMapper.updateUserRecordByUserIdAndDate(record);
        }
        User user = userMapper.selectByPrimaryKey2(userId);
        String username = user.getUsername();
        Long tokenCreateTime = user.getTokenCreateTime();

        //Object token = AuthController.loginUserMap.get(username);
        //System.out.println(Thread.currentThread().getName() + " username???" + username + " token : " + token);
        if (token == null) {
            return;
            //throw new ApplicationErrorException(ErrorCode.GeneralError);
        }

        Date createTime = jwtTokenUtil.getCreatedDateFromToken(token);
        if (tokenCreateTime == null) {
            if(user.getRole() == RoleEnum.MANAGER.getCode()){
                return;
            }
            log.info("?????????????????????token????????????null,token????????????,username: " + username);
            //throw new ApplicationErrorException(ErrorCode.TokenExpired);
            return;
        }
        if (tokenCreateTime == -1) {
            //???????????????,??????????????????
            throw new ApplicationErrorException(ErrorCode.AlreadyResetPassword);
        }


        String tokenUsername = jwtTokenUtil.getUsernameFromToken(token);
        if (tokenCreateTime != createTime.getTime() && !user.getUsername().equals("manager") && tokenUsername.equals(username)) {
            throw new ApplicationErrorException(ErrorCode.Offline);
        }


    }


    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param userId
     * @throws ApplicationErrorException
     */
    @Override
    public void recordUserOfflineData(Integer userId, Integer type) throws ApplicationErrorException {
        if (userId.intValue() == 0) {
            throw new ApplicationErrorException(ErrorCode.GeneralError);
        }
        //???T????????????????????????
        if (type == 2) {
            return;
        }
        Date curDate = RecordUtils.getYearMonthDay(new Date());
        UserAccessRecord paraRecord = new UserAccessRecord();
        paraRecord.setUserId(userId);
        paraRecord.setAccessDate(curDate);
        UserAccessRecord record = userAccessRecordMapper.selectUserRecordByUserIdAndDate(paraRecord);
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (record == null) {
            UserAccessRecord newRecord = new UserAccessRecord();
            newRecord.setAccessDate(curDate);
            newRecord.setUserId(userId);
            newRecord.setDuration(1);
            newRecord.setLastTime(System.currentTimeMillis());
            newRecord.setStatus(0);
            newRecord.setLoginTime(new Date());
            userAccessRecordMapper.insertUserAccessRecord(newRecord);
        } else {
            //????????????
            Long currentTimeMillis = System.currentTimeMillis();
            Long lastTime = record.getLastTime();
            Double add = (currentTimeMillis - lastTime) / (1000 * 60.0);
            BigDecimal bigDecimal = new BigDecimal(add);
            bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
            record.setStatus(0);
            record.setDuration(record.getDuration() + bigDecimal.intValue());
            record.setLastTime(currentTimeMillis);
            userAccessRecordMapper.updateUserRecordByUserIdAndDate(record);
        }

        //???????????????????????????map?????????????????????????????????
        User user = userMapper.selectByPrimaryKey2(userId);
        if (user != null && type == 1) {
            User paraUser = new User();
            if (user.getTokenCreateTime() != null && user.getTokenCreateTime().intValue() == -1) {
                paraUser.setTokenCreateTime(-1l);
            } else {
                paraUser.setTokenCreateTime(null);
            }
            paraUser.setIsRemind(0);
            paraUser.setId(userId);
            paraUser.setToken(null);
            log.info("recordUserOfflineData????????????,userId: " + userId);
            userMapper.updateByPrimaryKey2(paraUser);
            //????????????
            /*if(AuthController.temporaryRepeatLoginUserMap.containsKey(userName)){
                String existToken = AuthController.loginUserMap.get(userName) + "";
                String tmpToken = AuthController.temporaryRepeatLoginUserMap.get(userName) + "";
                if(existToken.equals(tmpToken)){
                    AuthController.temporaryRepeatLoginUserMap.remove(userName);
                }
            }
            AuthController.loginUserMap.remove(userName);*/
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @throws ApplicationErrorException
     */
    //@Scheduled(cron = "0 0/5 * * * ? ")
    @Override
    public void updateAbnormalOfflineUserRecordStatus() throws ApplicationErrorException {
        Map<String, Object> map = new HashMap<>();
        int pageSize = 300;
        map.put("pageSize", pageSize);
        Double start = System.currentTimeMillis() - 5.0 * 60 * 1000;
        map.put("start", start);
        for (int i = 1; true; i++) {
            map.put("pageIndex", (i - 1) * pageSize);
            List<UserAccessRecord> records = userAccessRecordMapper.selectNotUpdatedUserRecordPaged(map);
            if (records == null || records.size() < 1) {
                break;
            }
            //??????????????????
            for (UserAccessRecord record : records) {
                record.setStatus(0);
                record.setDuration(null);
                record.setLastTime(null);
                userAccessRecordMapper.updateUserRecordByUserIdAndDate(record);
                //??????????????????????????????
                Integer userId = record.getUserId();
                User user = userMapper.selectByPrimaryKey2(userId);
                if(user == null){
                    continue;
                }
                Long tokenCreateTime = user.getTokenCreateTime();
                if (tokenCreateTime != null) {
                    User paraUser = new User();
                    paraUser.setId(userId);
                    paraUser.setTokenCreateTime(null);
                    paraUser.setIsRemind(0);
                    paraUser.setToken(null);
                    userMapper.updateByPrimaryKey2(paraUser);
                    log.info("updateAbnormalOfflineUserRecordStatus???username:" + record.getUserId());
                } else {
                    log.error("?????????????????????loginUserMap????????????????????????username:");
                }
            }
        }

    }

    @Override
    public UserAccessRecord selectUserRecordByUserIdAndDate(UserAccessRecord record) throws ApplicationErrorException {
        return userAccessRecordMapper.selectUserRecordByUserIdAndDate(record);
    }

    @Override
    public int updateUserRecordByUserIdAndDate(UserAccessRecord record) throws ApplicationErrorException {
        return userAccessRecordMapper.updateUserRecordByUserIdAndDate(record);
    }

    /**
     * ????????????????????????????????????????????????????????????????????????T???????????????
     *
     * @param recordUserOnline
     * @return
     */
    @Override
    public Boolean logoutCheck(ReqRecordUserOnline recordUserOnline) {
        Integer userId = recordUserOnline.getUserId();
        String token = recordUserOnline.getToken();
        if (token == null) {
            log.error("logoutCheck???token???null");
            return false;
        }
        User user = userMapper.selectByPrimaryKey2(userId);
        if (user == null) {
            log.error("logoutCheck????????????????????????????????????");
            return false;
        }
        String username = user.getUsername();
        String dbToken = user.getToken();
        if (dbToken == null) {
            return true;//?????????????????????
        }
        String tokenUsername = jwtTokenUtil.getUsernameFromToken(token);
        if (tokenUsername.equals(username)) {
            if (dbToken.equals(token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @return
     */

    @Override
    public int deleteExpireRecord() throws ApplicationErrorException {
        return userAccessRecordMapper.deleteExpireRecord();
    }

    @Override
    public List<String> selectRecentlyLoginUserData(Map<String, Object> map) throws ApplicationErrorException {
        List<String> resultList = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("pageIndex", 0);
        paraMap.put("pageSize", map.get("pageSize"));
        paraMap.put("difference", 10);
        paraMap.put("date", new Date());
        List<Map> records = userAccessRecordMapper.selectRecentlyLoginUser(paraMap);
        StringBuilder sb = null;
        for (Map record : records) {
            sb = new StringBuilder("");
            String loginTime = record.get("login_time") + "";
            if ("null".equals(loginTime)) {
                continue;
            }

            sb.append(loginTime);
            String userIdStr = record.get("user_id") + "";
            Integer userId = Integer.parseInt(userIdStr);
            User user = userMapper.selectByPrimaryKey(userId);
            int role = user.getRole();
            String name = "";
            String roleStr = "";
            if (role == 1) {
                //??????
                Student student = studentMapper.selectByUserId(userId);
                if (student == null) {
                    continue;
                }
                name = student.getName();
                roleStr = "??????";
            } else if (role == 2) {
                Teacher teacher = teacherMapper.selectByUserId(userId);
                if (teacher == null) {
                    continue;
                }
                name = teacher.getName();
                roleStr = "??????";
            } else if (role == 3) {
                roleStr = "?????????";
                name = user.getUsername();
            }
            sb.append(roleStr + " ").append(name + " ").append("????????????");
            resultList.add(sb.toString());
        }
        return resultList;
    }

    @Override
    public RepClouderaData getClouderaData(ReqClouderaData reqClouderaData) throws ApplicationErrorException {
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("query", URLEncoder.encode(reqClouderaData.getQuery()));
        TimeZone zone = TimeZone.getTimeZone("GMT+0");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(zone);
        String from = df.format(System.currentTimeMillis() - 30 * 60 * 1000);
        String to = df.format(System.currentTimeMillis());
        paraMap.put("from", from);
        paraMap.put("to", to);
        String result = null;
        RepClouderaData repClouderaData = new RepClouderaData();
        try {
            result = HttpClientUtils.doGetWithAuth(url, paraMap, username, password);
            Map obj = JSONObject.parseObject(result, Map.class);
            Object items = obj.get("items");
            for (Object map2 : (List) items) {
                Map map = (Map) map2;
                Object timeSeries = map.get("timeSeries");
                List<Map> maps = (List) timeSeries;
                for (Map timeSerieMap : maps) {
                    Map metadataMap = (Map) timeSerieMap.get("metadata");
                    Map<String, Object> dataMap = new HashMap<>();
                    List<Object> timeAxisData = new ArrayList<>();
                    List<Object> yAxisData = new ArrayList<>();
                    String metricName = metadataMap.get("metricName") + "";
                    String entityName = metadataMap.get("entityName") + "";
                    List dataList = (List) timeSerieMap.get("data");
                    processingData(dataList, timeAxisData, yAxisData, metricName);
                    dataMap.put("name", entityName);
                    dataMap.put("timeAxisData", timeAxisData);
                    dataMap.put("yAxisData", yAxisData);
                    if ("cpu_percent".equals(metricName)) {
                        repClouderaData.getCpuData().add(dataMap);
                    } else if ("physical_memory_buffers".equals(metricName)) {
                        repClouderaData.getMemoryData().add(dataMap);
                    } else if ("total_bytes_receive_rate_across_network_interfaces".equals(metricName)) {
                        repClouderaData.getNetworkData().add(dataMap);
                    } else if ("total_write_ios_rate_across_disks".equals(metricName)) {
                        repClouderaData.getDisksData().add(dataMap);
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return repClouderaData;
    }

    //
    public void processingData(List dataList, List timeAxisData, List yAxisData, String name) {
        for (Object data : dataList) {
            Map dataMap = (Map) data;
            String timestamp = dataMap.get("timestamp") + "";
            String time = convertISO8601Date(timestamp);
            timeAxisData.add(time);
            Object value = dataMap.get("value");
            Object resultValue = null;
            if (name.equals("physical_memory_buffers")) {
                //byte?????????Mb
                Double bytes = Double.parseDouble(value + "");
                resultValue = bytes / 1024 / 1024;

            } else if (name.equals("total_bytes_receive_rate_across_network_interfaces")) {
                //byte??????Kb
                Double bytes = Double.parseDouble(value + "");
                resultValue = bytes / 1024;

            } else {
                resultValue = value;
            }
            //??????????????????
            BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(resultValue + ""));
            double result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            yAxisData.add(result);
        }
    }

    //????????????????????????????????????????????????
    public String convertISO8601Date(String ISO8601DateStr) {
        String result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date ISO8601Date = sdf.parse(ISO8601DateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ISO8601Date);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
            Date newDate = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result = format.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Date> loadAWeekDate() {
        List<Date> date = new ArrayList<>();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(System.currentTimeMillis() - 24 * 3600 * 1000);
        rightNow.add(Calendar.DATE, -6);
        for (int i = 0; i < 7; i++) {
            Date lastday = rightNow.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            String dataStr = sdf.format(lastday);
            Timestamp result = Timestamp.valueOf(dataStr);
            rightNow.add(Calendar.DATE, 1);
            date.add(result);
        }
        return date;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @throws ApplicationErrorException
     */
    @Override
    public Map<String, Object> getRealTimeOnlineData() throws ApplicationErrorException {

        //1.??????????????????
        Map<String, Object> realTimeOnlineUserMap = getRealTimeOnlineUser(new HashMap<>());
        return realTimeOnlineUserMap;

    }

    @Override
    public Map<String, Object> getAccumulativeOnlineData() throws ApplicationErrorException {
        //2.??????????????????
        Map<String, Object> map = new HashMap<>();
        map.put("difference", 1);
        map.put("start", 4);
        map.put("end", 15);
        Map<String, Object> accumulativeOnlineMap = getAccumulativeOnlineData(map);
        return accumulativeOnlineMap;
    }


    public Map<String, Object> getCloudwareTypeCount() {
        List<Map> maps = experimentMapper.selectCloudwareTypeCount();
        List<Map> resultMap = new ArrayList<>();
        List<Map> nameMaps = new ArrayList<>();
        Map<String, Object> hashMap = new HashMap<>();
        Map<String, Object> validateNameMap = new HashMap<>();

        for (Map map : maps) {
            Integer type = Integer.parseInt(map.get("type") + "");
            String count = map.get("count") + "";
            //Integer count = studentClassMapper.selectNumsOfClass(ids.split(","));
            Map<String, Object> newMap = new HashMap<>();
            Map<String, Object> nameMap = new HashMap<>();
            newMap.put("symbolSize", count);
            String name = null;
            if (type == 7) {
                name = "?????????";
            } else {
                name = CloudwareTypeEnum.getEnFromCode(type);
            }
            validateNameMap.put(name, type);
            newMap.put("name", name);
            newMap.put("category", name);
            newMap.put("draggable", true);
            nameMap.put("name", name);
            nameMaps.add(nameMap);
            resultMap.add(newMap);
        }
        String[] nameArray = {"RStudio", "Python", "Base", "Hadoop", "JupyterPython", "IdeJava", "?????????",};
        for (String name : nameArray) {
            if (!validateNameMap.containsKey(name)) {
                Map<String, Object> data = new HashMap<>();
                Map<String, Object> nameMap = new HashMap<>();
                nameMap.put("name", name);
                nameMaps.add(nameMap);
                data.put("name", name);
                data.put("category", name);
                data.put("draggable", true);
                data.put("symbolSize", 1);
                resultMap.add(data);
            }
        }


        Collections.sort(resultMap, new Comparator<Map>() {

            @Override
            public int compare(Map o1, Map o2) {

                String valueStr1 = o1.get("symbolSize") + "";
                int value1 = Integer.parseInt(valueStr1);
                String valueStr2 = o2.get("symbolSize") + "";
                int value2 = Integer.parseInt(valueStr2);
                return -(value1 - value2);
            }
        });
        hashMap.put("data", resultMap);
        zoomData(resultMap);
        hashMap.put("nameMap", nameMaps);
        hashMap.put("name", "experiment");
        return hashMap;
    }

    public Map<String, Object> getCourseContextData() {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map> maps = courseMapper.selectCourseBySelectedCount();
        List<Map> nameMaps = new ArrayList<>();
        List<Map> result = new ArrayList<>();
        for (Map map : maps) {
            String name = map.get("name") + "";
            String value = map.get("value") + "";
            Map<String, Object> newMap = new HashMap<>();
            newMap.put("name", name);
            Map nameMap = new HashMap<>();
            nameMap.put("name", name);
            nameMaps.add(nameMap);
            newMap.put("symbolSize", value);
            newMap.put("category", name);
            newMap.put("draggable", true);
            result.add(newMap);
        }
        zoomData(result);
        resultMap.put("data", result);
        resultMap.put("nameMap", nameMaps);
        resultMap.put("name", "course");
        return resultMap;
    }

    /**
     * ????????????????????????
     *
     * @param
     * @return
     */
    public void zoomData(List<Map> maps) {
        //????????????value??????????????????
        Map firstMap = maps.get(0);
        String fistValueStr = firstMap.get("symbolSize") + "";
        Integer firstValue = Integer.parseInt(fistValueStr);
        Double maxValue = 60.0;
        if (firstValue > maxValue) {
            double factor = firstValue / maxValue;
            for (Map map : maps) {
                String valueStr = map.get("symbolSize") + "";
                Integer value = Integer.parseInt(valueStr);
                map.put("symbolSize", value / factor);
            }
        }

    }


    @Override
    public List<Map> getCharacterCloudDashboardData() {
        List<Map> resultMap = new ArrayList<>();
        Map<String, Object> exp = getCloudwareTypeCount();
        Map<String, Object> course = getCourseContextData();
        resultMap.add(exp);
        resultMap.add(course);
        return resultMap;
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param paraMap
     * @return
     */
    public List<Map> getDayAndWeekAndMonthOnlineAvgData(Map<String, Object> paraMap) throws ApplicationErrorException {
        //???
        Map<String, Object> dayMap = getDayOnlineAvgData();
        Map<String, Object> weekMap = getWeekOnlineAvgData();
        Map<String, Object> monthMap = getMonthOnlineAvgData();
        List<Map> result = new ArrayList<>();
        result.add(dayMap);
        result.add(weekMap);
        result.add(monthMap);
        return result;
    }


    public Map<String, Object> getDayOnlineAvgData() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        map.put("difference", 7);
        map.put("unit", "DAY");
        List<Map> daysData = userAccessRecordMapper.selectDayOnlineAvgDataByAccessDate(map);
        List<Date> dayXaxisData = loadAWeekDate();
        List<String> dayXaxisData2 = new ArrayList<>();
        List<Object> dayUvYaxisData = new ArrayList<>();
        List<Object> dayDurationYaxisData = new ArrayList<>();
        for (int i = 0; i < dayXaxisData.size(); i++) {
            dayUvYaxisData.add(0);
            dayDurationYaxisData.add(0);
        }
        for (int i = 0; i < dayXaxisData.size(); i++) {
            Date date = dayXaxisData.get(i);
            for (int j = 0; j < daysData.size(); j++) {
                Map data = daysData.get(j);
                Date accessDate = (Date) data.get("accessDate");
                if (date.getTime() == accessDate.getTime()) {
                    String idsStr = data.get("idsStr") + "";
                    String duration = data.get("duration") + "";
                    Double doubleDuration = Double.parseDouble(duration);
                    String[] idsArr = idsStr.split(",");
                    BigDecimal bigDecimal = null;
                    if (idsArr.length != 0) {
                        bigDecimal = new BigDecimal(doubleDuration / idsArr.length);
                    } else {
                        bigDecimal = new BigDecimal(doubleDuration / 1);
                    }
                    Double result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    dayUvYaxisData.set(i, idsArr.length);
                    dayDurationYaxisData.set(i, result);
                }

            }
        }
        for (Date obj : dayXaxisData) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String v = format.format(obj);
            dayXaxisData2.add(v);
        }
        resultMap.put("xAxisData", dayXaxisData2);
        resultMap.put("uvYaxisData", dayUvYaxisData);
        resultMap.put("durationYaxisData", dayDurationYaxisData);
        resultMap.put("name", "day");
        return resultMap;
    }


    public Map<String, Object> getWeekOnlineAvgData() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        List<Object> weekXaxisData = new ArrayList<>();
        List<Object> weekUvYaxisData = new ArrayList<>();
        List<Object> weekDurationYaxisData = new ArrayList<>();
        map.put("difference", 6);
        map.put("unit", "WEEK");
        List<Map> weeksData = userAccessRecordMapper.selectWeekOnlineAvgDataByAccessDate(map);
        getWeekNum(weekXaxisData);
        for (int i = 0; i < weekXaxisData.size(); i++) {
            weekUvYaxisData.add(0);
            weekDurationYaxisData.add(0);
        }
        for (int i = 0; i < weekXaxisData.size(); i++) {
            String xData = weekXaxisData.get(i) + "";
            for (int j = 0; j < weeksData.size(); j++) {
                Map data = weeksData.get(j);
                String weekNum = data.get("weekNum") + "";
                if (xData.equals(weekNum)) {
                    String idsStr = data.get("idsStr") + "";
                    String[] idsArr = idsStr.split(",");
                    String duration = data.get("duration") + "";
                    Double doubleDuration = Double.parseDouble(duration);
                    BigDecimal bigDecimal = null;
                    if (idsArr.length != 0) {
                        bigDecimal = new BigDecimal(doubleDuration / idsArr.length);
                    } else {
                        bigDecimal = new BigDecimal(doubleDuration / 1);
                    }
                    Double result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    weekUvYaxisData.set(i, idsArr.length);
                    weekDurationYaxisData.set(i, result);
                }

            }
        }

        resultMap.put("xAxisData", weekXaxisData);
        resultMap.put("uvYaxisData", weekUvYaxisData);
        resultMap.put("durationYaxisData", weekDurationYaxisData);
        resultMap.put("name", "week");
        return resultMap;
    }


    public Map<String, Object> getMonthOnlineAvgData() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        List<Object> monthXaxisData = new ArrayList<>();
        List<Object> monthUvYaxisData = new ArrayList<>();
        List<Object> monthDurationYaxisData = new ArrayList<>();
        map.put("difference", 6);
        map.put("unit", "MONTH");
        List<Map> monthsData = userAccessRecordMapper.selectMonthOnlineAvgDataByAccessDate(map);
        getMonthNum(monthXaxisData);
        for (int i = 0; i < monthXaxisData.size(); i++) {
            monthUvYaxisData.add(0);
            monthDurationYaxisData.add(0);
        }
        for (int i = 0; i < monthXaxisData.size(); i++) {
            String xData = monthXaxisData.get(i) + "";
            for (int j = 0; j < monthsData.size(); j++) {
                Map data = monthsData.get(j);
                String weekNum = data.get("monthNum") + "";
                if (xData.equals(weekNum)) {
                    String idsStr = data.get("idsStr") + "";
                    String[] idsArr = idsStr.split(",");
                    String duration = data.get("duration") + "";
                    Double doubleDuration = Double.parseDouble(duration);
                    BigDecimal bigDecimal = null;
                    if (idsArr.length != 0) {
                        bigDecimal = new BigDecimal(doubleDuration / idsArr.length);
                    } else {
                        bigDecimal = new BigDecimal(doubleDuration / 1);
                    }
                    Double result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    monthUvYaxisData.set(i, idsArr.length);
                    monthDurationYaxisData.set(i, result);
                }

            }
        }

        resultMap.put("xAxisData", monthXaxisData);
        resultMap.put("uvYaxisData", monthUvYaxisData);
        resultMap.put("durationYaxisData", monthDurationYaxisData);
        resultMap.put("name", "month");
        return resultMap;
    }


    public void getMonthNum(List<Object> months) {
        Calendar calendar = Calendar.getInstance();
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endYear = calendar.get(Calendar.YEAR);
        String pre = endYear + "-";
        //??????
        if (endMonth > startMonth) {
            for (int i = endMonth + 1; i <= 12; i++) {
                if (i < 10) {
                    months.add(pre + "0" + i);
                } else {
                    months.add(pre + i);
                }
            }
            endMonth = 0;
            pre = startYear + "-";
        }
        for (int i = endMonth + 1; i <= startMonth; i++) {
            if (i < 10) {
                months.add(pre + "0" + i);
            } else {
                months.add(pre + i);
            }
        }
    }


    public void getWeekNum(List<Object> weeks) {
        Calendar calendar = Calendar.getInstance();
        int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int startYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - 6);
        int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int endYear = calendar.get(Calendar.YEAR);
        String pre = endYear + "";
        //??????
        if (endWeek > startWeek) {
            for (int i = endWeek + 1; i <= 52; i++) {
                weeks.add(pre + i);
            }
            endWeek = 0;
            pre = startYear + "-";
        }
        for (int i = endWeek + 1; i <= startWeek; i++) {

            weeks.add(pre + i);
        }
    }


    /**
     * ????????????????????????????????????
     *
     * @param paraMap
     * @return
     */
    public Map<String, Object> getAccumulativeOnlineData(Map<String, Object> paraMap) {

        List<Map> records = userAccessRecordMapper.selectAccumulativeOnlineUserByTimeRange(paraMap);
        List<Object> xAxisData = new ArrayList<>();
        List<Object> yAxisData = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        getAccumulativeOnlineDataXaxisData(xAxisData, paraMap);
        for (int i = 0; i < xAxisData.size(); i++) {
            yAxisData.add(0);
        }
        Integer start = Integer.parseInt(paraMap.get("start") + "");
        Integer end = Integer.parseInt(paraMap.get("end") + "");


        for (Map map : records) {
            Integer duration = Integer.parseInt(map.get("duration") + "");
            Integer count = Integer.parseInt(map.get("count") + "");
            if (duration < start) {
                yAxisData.set(0, (int) yAxisData.get(0) + count);
            } else if (duration > end) {
                yAxisData.set(yAxisData.size() - 1, (int) yAxisData.get(yAxisData.size() - 1) + count);
            }
        }

        for (int j = 0; j < records.size(); j++) {
            Map<String, Object> record = records.get(j);
            Integer duration = Integer.parseInt(record.get("duration") + "");
            Integer count = Integer.parseInt(record.get("count") + "");
            for (int i = 1; i < xAxisData.size() - 1; i++) {
                Object obj = xAxisData.get(i);
                int curDuration = (int) obj;
                if (curDuration == duration) {
                    yAxisData.set(i, (int) yAxisData.get(i) + count);
                }
            }
        }
        result.put("name", "accumulativeOnlineData");
        result.put("xAxisData", xAxisData);
        result.put("yAxisData", yAxisData);
        return result;
    }

    /**
     * @param xAxisData
     * @param map
     */

    public void getAccumulativeOnlineDataXaxisData(List<Object> xAxisData, Map<String, Object> map) {
        Integer start = Integer.parseInt(map.get("start") + "");
        Integer end = Integer.parseInt(map.get("end") + "");
        xAxisData.add("<" + start);
        for (int i = start; i <= end; i++) {
            xAxisData.add(i);
        }
        xAxisData.add(">" + end);
    }


    /**
     * endTime ????????????????????????????????????(?????????????????????)
     * accessDate ????????????????????????
     *
     * @param map
     * @return
     */

    public Map<String, Object> getRealTimeOnlineUser(Map<String, Object> map) {

        Map<String, Object> paraMap = new HashMap<>();
        //???????????????????????????
        paraMap.put("endTime", System.currentTimeMillis() - 30 * 60 * 1000);
        List<RealTimeOnlineUser> onlineUsers = realTimeOnlineUserMapper.selectOnlineUserCountByTimeRange(paraMap);
        List<Date> timeAxisData = new ArrayList<>();
        List<String> timeAxisData2 = new ArrayList<>();
        List<Object> yAxisData = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        getRealTimeOnlineUserXaxisData(timeAxisData, onlineUsers.get(0).getOnlineTimeStamp());
        for (int i = 0; i < timeAxisData.size(); i++) {
            yAxisData.add(0);//?????????????????????0
        }
        for (int j = 0; j < timeAxisData.size(); j++) {
            Object time = timeAxisData.get(j);
            Date date = (Date) time;//?????????????????????
            Long xMillis = date.getTime();//??????????????????
            for (RealTimeOnlineUser onlineUser : onlineUsers) {
                Long recordMillis = onlineUser.getOnlineTimeStamp();
                if (xMillis - recordMillis < 10) {
                    //??????????????????????????????????????????10ms??????????????????????????????
                    yAxisData.set(j, onlineUser.getOnlineCount());
                }
            }

        }
        for (Date date : timeAxisData) {
            timeAxisData2.add(date.toString());
        }
        resultMap.put("timeAxisData", timeAxisData2);
        resultMap.put("yAxisData", yAxisData);
        resultMap.put("name", "realTimeUserCountOnline");
        return resultMap;
    }

    //???????????????????????????????????????????????????30s
    public void getRealTimeOnlineUserXaxisData(List<Date> timeAxisData, Long curMillis) {
        //Long curMillis = System.currentTimeMillis();
        Long beforeMills = curMillis - 30 * 60 * 1000;
        for (int i = 0; i < 60; i++) {
            if (beforeMills > curMillis) {
                break;
            }
            beforeMills += (30 * 1000);
            Date date = new Date(beforeMills);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String result = sdf.format(date);
            Timestamp dateTamp = Timestamp.valueOf(result);
            timeAxisData.add(dateTamp);

        }

    }

    public void getRealTimeOnlineUserXaxisStringData(List<String> timeAxisData) {
        Long curMillis = System.currentTimeMillis();
        Long beforeMills = curMillis - 30 * 60 * 1000;
        for (; true; ) {
            beforeMills += (30 * 1000);
            Date date = new Date(beforeMills);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String result = sdf.format(date);
            timeAxisData.add(result);
            if (beforeMills > curMillis) {
                break;
            }
        }

    }

    public static void main(String[] args) {
    }


}
