package com.beeSpring.beespring.dto.user;

import com.beeSpring.beespring.domain.user.RoleType;
import com.beeSpring.beespring.domain.user.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {
    private String serialNumber;
    private String userId;
    private int roleId;
    private String password;
    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String mobileNumber;
    private LocalDateTime registrationDate;
    private LocalDate birthdate;
    private int reportedCnt;
    private State state;
    private String reason;
    private int suspended;
    private LocalDateTime modDate;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime accessTokenExpiration;
    private LocalDateTime refreshTokenExpiration;
    private String profileImage;
    private String tag1;
    private String tag2;
    private String tag3;

//    public UserDTO(String profileImage, String nickname, String tag1, String tag2, String tag3, Object o) {
//    }

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
