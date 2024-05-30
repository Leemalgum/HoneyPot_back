package com.beeSpring.beespring.dto.user;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String userId;
    private String mobileNumber;
    private String email;
}
