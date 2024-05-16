package com.beeSpring.beespring.service.jwt;

import com.beeSpring.beespring.dto.user.UserTokens;

import java.time.LocalDateTime;

public interface TokenService {
    void saveToken(String userId, String accessToken, String refreshToken, LocalDateTime accessTokenExpiration, LocalDateTime refreshTokenExpiration);
    UserTokens getToken(String userId);
    void deleteToken(String userId);
}
