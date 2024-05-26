package com.beeSpring.beespring.response;

public enum ResponseCode {
    SUCCESS( "Success", 200),
    USER_LOGIN_SUCCESS("User login successful", 200),
    USER_LOGIN_FAILURE("User login failed", 401),
    USER_NOT_FOUND("User not found", 404),
    ERROR("Internal Server Error", 500),
    SEND_MMS_SUCCESS("Send mms successful", 200);

    private final String message;
    private final int code;

    ResponseCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
