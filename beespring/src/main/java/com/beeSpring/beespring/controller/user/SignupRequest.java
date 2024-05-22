package com.beeSpring.beespring.controller.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequest {
    private String profileImage;
    private String provider;
    private String username; //userId
    private String roleId;
    private String password;
    private String name;
    private String nickname;
    private String mobileNumber;
    private String email;
    private String postcode;
    private String roadAddress;
    private String detailAddress;
    private LocalDate birthdate;
    private String selectedGender;
}

