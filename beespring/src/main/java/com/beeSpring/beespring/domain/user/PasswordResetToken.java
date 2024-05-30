package com.beeSpring.beespring.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="prt_id")
    private Long prtId;

    @Column(name="token")
    private String token;

    @Column(name="user_id")
    private String userId;

    @Column(name="expiry_date")
    private Date expiryDate;

}

