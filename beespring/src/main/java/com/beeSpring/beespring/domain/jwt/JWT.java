package com.beeSpring.beespring.domain.jwt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="JWT_refresh_token")
@Entity
public class JWT {
    @Id
    @Column(name="jwt_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jwt_id;

    @Column(name="token_name", nullable = false)
    private String tokenName;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="created_date")
    @CreationTimestamp
    private Timestamp created;
}
