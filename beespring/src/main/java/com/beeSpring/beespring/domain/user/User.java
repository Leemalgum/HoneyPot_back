package com.beeSpring.beespring.domain.user;

import com.beeSpring.beespring.domain.bid.Bid;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 로그인 기능
 * @AllArgsConstructor 를 쓰고 다른 데서 시간을 입력할지 직접 생성자를 만들고 localDate.now() 를 쓸지 생각해보자
 * */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="User")
@Entity
@Builder
public class User {
    @Id
    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "provider")
    private String provider;

    @Column(name="role_id")
    private int roleId;

    @Column(name = "password")
    private String password;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name="mobile_number")
    private String mobileNumber;

    @Column(name="registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name="reported_cnt")
    private int reportedCnt;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "reason")
    private String reason;

    @Column(name = "suspended")
    private int suspended;

    @Column(name="mod_date")
    private LocalDateTime modDate;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name="refresh_token")
    private String refreshToken;

    @Column(name = "access_token_expiration")
    private LocalDateTime accessTokenExpiration;

    @Column(name = "refresh_token_expiration")
    private LocalDateTime refreshTokenExpiration;

    @Column(name="profile_image")
    private String profileImage;

    @Column(name = "gender")
    private String gender;

    @Column(name = "user_account")
    private String userAccount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShippingAddress> shippingAddresses;

    @OneToMany(mappedBy = "user")
    private List<UserIdol> userIdols;

    @OneToMany(mappedBy = "user")
    private List<Product> products;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Bid> bids;

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
                '}';
    }
}
