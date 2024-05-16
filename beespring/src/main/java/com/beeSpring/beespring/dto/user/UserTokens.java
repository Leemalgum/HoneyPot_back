package com.beeSpring.beespring.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash("UserTokens")
public class UserTokens implements Serializable {
    private String userId;
    private String provider;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime accessTokenExpiration;
    private LocalDateTime refreshTokenExpiration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

