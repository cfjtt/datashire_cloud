package com.eurlanda.edu.tp.authentication.security;

import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.error.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomHTTP403Filter implements AuthenticationEntryPoint {
    static Logger log = Logger.getLogger(CustomHTTP403Filter.class.getName());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn(String.format("User tries to access url '%s' without authentication. The user needs to log on.", request.getRequestURI()));

        Utility.writeError(request, response, HttpStatus.OK, ErrorCode.NeedAuthentication);
    }
}
