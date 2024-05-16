package com.beeSpring.beespring.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 로그인 기능
 * @AllArgsConstructor 를 쓰고 다른 데서 시간을 입력할지 직접 생성자를 만들고 localDate.now() 를 쓸지 생각해보자 
 * */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="User")
@Entity
public class User {
    @Id
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;
    @Column(name = "user_id")
    private String userId;
    @Column(name="role_id")
    private int roleId;
    @Column(name="role")
    private String role; // enum 이 아니라 string 으로 하는게 맞는지?
    private String password;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    private String nickname;
    private String email;
    @Column(name="mobile_number")
    private String mobileNumber;
    @Column(name="registration_date")
    private LocalDateTime registrationDate;
    private LocalDate birthdate;
    @Column(name="reported_cnt")
    private int reportedCnt;
    @Enumerated(EnumType.STRING)
    private State state;
    private String reason;
    private int suspended;
    @Column(name="mod_date")
    private LocalDateTime modDate;
    @Column(name="refresh_token")
    private String refreshToken;
    @Column(name="profile_image")
    private String profileImage;
    private String tag1;
    private String tag2;
    private String tag3;

    @Override
    public String toString() {
        return "User{" +
                "serialNumber='" + serialNumber + '\'' +
                ", userId='" + userId + '\'' +
                ", roleId=" + roleId +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", registrationDate=" + registrationDate +
                ", birthdate=" + birthdate +
                ", reportedCnt=" + reportedCnt +
                ", state=" + state +
                ", reason='" + reason + '\'' +
                ", suspended=" + suspended +
                ", modDate=" + modDate +
                ", refreshToken='" + refreshToken + '\'' +
                ", profileImage=" + profileImage +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", tag3='" + tag3 + '\'' +
                '}';
    }
}
