package com.beeSpring.beespring.controller.user;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String serialNumber;
    private String currentPassword;
    private String newPassword;
}
