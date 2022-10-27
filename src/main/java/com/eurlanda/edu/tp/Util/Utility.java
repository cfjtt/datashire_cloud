package com.eurlanda.edu.tp.Util;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utility {

    private Utility(){}

    private static Pattern pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    public static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    public static String shortDateFormat = "yyyy-MM-dd";
    public static String longDateFormat = "yyyyMMddHHmmss.sss";

    public static void validateEmail(String email) throws ApplicationErrorException {
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()){
            throw new ApplicationErrorException(ErrorCode.InvalidEmail, email);
        }
    }

    public static String formatDate(Date date){
        return formatDate(date, dateFormat);
    }

    public static String formatDate(Date date, String format){
        if(date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parseDate(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static Date parseShortDate(String date) throws ParseException {
        return parseDate(date, shortDateFormat);
    }

    public static void writeError(HttpServletRequest request, HttpServletResponse response,
                                  HttpStatus status, ErrorCode errorCode) throws IOException {
        response.setStatus(status.value());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(String.format("{\"errorCode\": %d,\n" +
                "\"message\": \"%s\"}", errorCode.getCode(), errorCode.getErrorStringFormat()));
    }

    public static String getCurrentUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
