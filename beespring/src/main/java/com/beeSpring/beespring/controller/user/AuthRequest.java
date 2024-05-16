package com.beeSpring.beespring.controller.user;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
