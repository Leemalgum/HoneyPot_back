package com.beeSpring.beespring.exception;

import com.beeSpring.beespring.response.CustomApiResponse;
import com.beeSpring.beespring.response.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<Object>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomApiResponse.error("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }*/

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace(); // 콘솔에 예외 출력
        logger.error("Unhandled exception caught", ex);
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        return ResponseEntity.status(responseCode.getCode())
                .body(CustomApiResponse.error(responseCode.getMessage(), responseCode.getCode()));
    }
}
