package com.beeSpring.beespring.service.jwt;

import com.beeSpring.beespring.dto.user.UserTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisTemplate<String, UserTokens> redisTemplate;

    @Override
    public void saveToken(String userId, String accessToken, String refreshToken, LocalDateTime accessTokenExpiration, LocalDateTime refreshTokenExpiration) {
        UserTokens userTokens = new UserTokens();
        userTokens.setUserId(userId);
        userTokens.setAccessToken(accessToken);
        userTokens.setRefreshToken(refreshToken);
        userTokens.setAccessTokenExpiration(accessTokenExpiration);
        userTokens.setRefreshTokenExpiration(refreshTokenExpiration);

        redisTemplate.opsForValue().set("userTokens:" + userId, userTokens, 10, TimeUnit.MINUTES); // 예시로 10분 TTL 설정
    }

    public UserTokens getToken(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    @Override
    public void deleteToken(String userId) {
        redisTemplate.delete("userTokens:" + userId);
    }
}
