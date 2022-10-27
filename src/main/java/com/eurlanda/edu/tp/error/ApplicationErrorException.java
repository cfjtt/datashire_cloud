package com.eurlanda.edu.tp.error;

public class ApplicationErrorException extends Exception{
    private final ErrorCode errorCode;
    private final Object[] params;

    public ApplicationErrorException(ErrorCode code, Object... params){
        this.errorCode = code;
        this.params = params;
    }

    public int getErrorCode(){
        return errorCode.getCode();
    }

    @Override
    public String getMessage() {
        String messageFormat = errorCode.getErrorStringFormat();
        return String.format(messageFormat, params);
    }
}
