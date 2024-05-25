package com.beeSpring.beespring.response;

public class CustomApiResponse<T> {
    private String message;
    private T data;
    private int statusCode;
    private boolean success; // 성공 여부 필드 추가

    public CustomApiResponse(String message, T data, int statusCode, boolean success) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static <T> CustomApiResponse<T> success(T data, String message) {
        return new CustomApiResponse<>(message, data, 200, true);
    }

    public static <T> CustomApiResponse<T> error(String message, int statusCode) {
        return new CustomApiResponse<>(message, null, statusCode, false);
    }
}
