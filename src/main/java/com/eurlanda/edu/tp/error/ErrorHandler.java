package com.eurlanda.edu.tp.error;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    static Logger log = Logger.getLogger(ErrorHandler.class.getName());

    @ExceptionHandler(ApplicationErrorException.class)
    @ResponseBody
    ResponseMessage handleApplicationErrorException(HttpServletRequest request, Throwable ex) {
        ApplicationErrorException appError = (ApplicationErrorException) ex;
        log.warn(String.format("Encountered ApplicationErrorException. Error code %d; Error message: %s.",
                appError.getErrorCode(), appError.getMessage()));
        return new ResponseMessage.Fail((ApplicationErrorException)ex);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    ResponseMessage handleDulicateKeyException(HttpServletRequest request, Throwable ex) {
        //todo: log original exception
        log.error(String.format("Encountered DuplicateKey Exception. Message: %s", ex.getMessage()));
        log.log(Level.ERROR, ex.getMessage(), ex);
        DuplicateKeyException duplicateException = (DuplicateKeyException)ex;
        String errorMessage = duplicateException.getMessage();

        if(errorMessage.contains("username_UNIQUE")){
            return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.UserAlreadyExists));
        }

        return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.EntityAlreadyExists));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    ResponseMessage handleAuthenticationException(HttpServletRequest request, Throwable ex){
        log.warn(String.format("Failed to authenticate user. Message: %s", ex.getMessage()));
        if(ex instanceof BadCredentialsException){
            return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.BadCredential));
        }

        return null;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseMessage handleThrowableException(HttpServletRequest request, Throwable ex) {
        //todo: log original exception
        log.error(String.format("Encountered server error. Message: %s", ex.getMessage()));
        log.log(Level.ERROR, ex.getMessage(), ex);
        return new ResponseMessage.Fail(new ApplicationErrorException(ErrorCode.GeneralError));
    }
}