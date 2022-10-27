package com.eurlanda.edu.tp.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by test on 2018/4/21.
 */
public class RecordUtils {

    /**
     * 从年月日时分秒中获取年月日
     * @param date
     * @return
     */
    public static Date getYearMonthDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String ss = sdf.format(date);
            Date lastdayformat = sdf.parse(ss);
            return lastdayformat;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 今天加减几天天
     * @param daycount 正数表示加天，负数表示减天
     * @return
     */
    public static Date addDay(Date date,int daycount) {
        Calendar yestodaycal = Calendar.getInstance();
        yestodaycal.setTime(date);
        yestodaycal.set(Calendar.DATE, yestodaycal.get(Calendar.DATE) + daycount);
        Date yestoday = yestodaycal.getTime();
        return yestoday;
    }


}
