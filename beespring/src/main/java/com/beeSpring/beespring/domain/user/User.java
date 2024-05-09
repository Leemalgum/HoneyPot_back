package com.beeSpring.beespring.domain.user;

import jakarta.persistence.*;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="User")
public class User {
    @Id
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "user_id")
    private String userId;
    @Column(name="role_id")
    private int roleId;
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
    private Blob profileImage;
    private String tag1;
    private String tag2;
    private String tag3;

    protected User() {
    }
    public User(String serialNumber, String userId, int roleId, String password, String firstName, String lastName, String nickname, String email, String mobileNumber, LocalDateTime registrationDate, LocalDate birthdate, int reportedCnt, State state, String reason, int suspended, LocalDateTime modDate, String refreshToken, Blob profileImage, String tag1, String tag2, String tag3) {
        this.serialNumber = serialNumber;
        this.userId = userId;
        this.roleId = roleId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.registrationDate = registrationDate;
        this.birthdate = birthdate;
        this.reportedCnt = reportedCnt;
        this.state = state;
        this.reason = reason;
        this.suspended = suspended;
        this.modDate = modDate;
        this.refreshToken = refreshToken;
        this.profileImage = profileImage;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getUserId() {
        return userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public int getReportedCnt() {
        return reportedCnt;
    }

    public State getState() {
        return state;
    }

    public String getReason() {
        return reason;
    }

    public int getSuspended() {
        return suspended;
    }

    public LocalDateTime getModDate() {
        return modDate;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Blob getProfileImage() {
        return profileImage;
    }

    public String getTag1() {
        return tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public String getTag3() {
        return tag3;
    }

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
