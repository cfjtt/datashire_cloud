package com.eurlanda.edu.tp.error;

/**
 * Created by Dell on 2017/6/12.
 */
public class ResponseMessage<T> {
    private int errorCode;

    private String message;

    private T data;

    ResponseMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    ResponseMessage(int errorCode, String message, T data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static class Success<T> extends ResponseMessage<T>{
        public Success(){
            super(ErrorCode.Success.getCode(), ErrorCode.Success.getErrorStringFormat());
        }

        public Success(T data){
            super(ErrorCode.Success.getCode(), ErrorCode.Success.getErrorStringFormat(), data);
        }
    }

    public static class Fail extends ResponseMessage{
        public Fail(int errorCode, String message) {
            super(errorCode, message);
        }

        public Fail(ApplicationErrorException ex){
            super(ex.getErrorCode(), ex.getMessage());
        }
    }
}
