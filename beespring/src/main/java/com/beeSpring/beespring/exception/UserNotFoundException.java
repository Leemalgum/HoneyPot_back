package com.beeSpring.beespring.exception;

import com.beeSpring.beespring.response.ResponseCode;

public class UserNotFoundException extends RuntimeException {
    private final ResponseCode responseCode;

    public UserNotFoundException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}

