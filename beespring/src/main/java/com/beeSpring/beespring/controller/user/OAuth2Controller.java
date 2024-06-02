package com.beeSpring.beespring.controller.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.response.CustomApiResponse;
import com.beeSpring.beespring.response.ResponseCode;
import com.beeSpring.beespring.security.jwt.JwtTokenProvider;
import com.beeSpring.beespring.service.user.KakaoOAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    private final UserRepository userRepository;
    private final KakaoOAuthService kakaoOAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String redirectUri;

    // 카카오 로그인을 위해 회원가입 여부 확인, 이미 회원이면 Jwt 토큰 발급
    @Operation(summary = "KakaoLogin endpoint", description = "Authenticate user and provide token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/kakaologin")
    public ResponseEntity<CustomApiResponse<HashMap<String, String>>> authCheck(@RequestBody Map<String, Object> userData, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(userData.toString());
        String accessToken = (String) userData.get("accessToken");
        try {
            if (accessToken == null || accessToken.isEmpty()) {
                throw new IllegalArgumentException("Access token is null or empty.");
            }
            logger.info("Received accessToken: {}", accessToken);

            String email = (String) userData.get("email");
            String profileImage = (String) userData.get("profileImage");
            Integer expiresIn = (Integer) userData.get("expiresIn");
            String refreshToken = (String) userData.get("refreshToken");
            Integer refreshTokenExpiresIn = (Integer) userData.get("refreshTokenExpiresIn");

            String userId = kakaoOAuthService.isSignedUp(userData);
            System.out.println("서비스에서 받아온 UserId : " + userId);
            if (userId == null) {
                throw new IllegalArgumentException("User not signed up with provided access token.");
            }

            User user = userRepository.findByProviderAndUserId("kakao", userId)
                    .orElse(new User());

            String jwtAccessToken = jwtTokenProvider.createAccessToken(userId, "kakao");
            String jwtRefreshToken = jwtTokenProvider.createRefreshToken(userId);
            LocalDateTime jwtAccessTokenExpiration = jwtTokenProvider.getExpirationDate(jwtAccessToken);
            LocalDateTime jwtRefreshTokenExpiration = jwtTokenProvider.getExpirationDate(jwtRefreshToken);

            if (user.getUserId() == null) {
                user.setSerialNumber(generateUniqueSerialNumber());
            }
            user.setUserId(userId);
            user.setProvider("kakao");
            user.setEmail(email);
            user.setProfileImage(profileImage);
            user.setRegistrationDate(LocalDateTime.now());
            user.setRoleId(1);

/*            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            long accessTokenExpirationMillis = System.currentTimeMillis() + (expiresIn * 1000L);
            long refreshTokenExpirationMillis = System.currentTimeMillis() + (refreshTokenExpiresIn * 1000L);

            LocalDateTime accessTokenExpiration = Instant.ofEpochMilli(accessTokenExpirationMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime refreshTokenExpiration = Instant.ofEpochMilli(refreshTokenExpirationMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            user.setAccessTokenExpiration(accessTokenExpiration);

            user.setRefreshTokenExpiration(refreshTokenExpiration);*/
            user.setAccessToken(jwtAccessToken);
            user.setRefreshToken(jwtRefreshToken);
            user.setAccessTokenExpiration(jwtAccessTokenExpiration);
            user.setRefreshTokenExpiration(jwtRefreshTokenExpiration);

            userRepository.save(user);

            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("accessToken", jwtAccessToken);
            map.put("refreshToken", jwtRefreshToken);
            map.put("accessTokenExpiration", String.valueOf(jwtAccessTokenExpiration));
            map.put("refreshTokenExpiration", String.valueOf(jwtRefreshTokenExpiration));
            map.put("redirectUrl", "http://localhost:3000/");

            logger.info("Login successful for userId: {}", userId);
            return ResponseEntity.ok(CustomApiResponse.success(map, ResponseCode.USER_LOGIN_SUCCESS.getMessage()));
        } catch (Exception e) {
            logger.error("Login failed with accessToken: {}", accessToken, e);
            return ResponseEntity.status(ResponseCode.USER_LOGIN_FAILURE.getCode())
                    .body(CustomApiResponse.error(ResponseCode.USER_LOGIN_FAILURE.getMessage(), ResponseCode.USER_LOGIN_FAILURE.getCode()));
        }
    }

    @GetMapping("/kakaologinSuccess")
    public String loginSuccess(OAuth2AuthenticationToken authentication) {
        String userId = authentication.getPrincipal().getAttribute("userId");
        String nickname = authentication.getPrincipal().getAttribute("name");
        String profileImage = authentication.getPrincipal().getAttribute("profileImage");

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            user = new User();
            user.setUserId(userId);
            user.setNickname(nickname);
            user.setProfileImage(profileImage);
            userRepository.save(user);
        }

        String accessToken = "generatedAccessToken"; // 실제 토큰 발급 로직 추가
        String refreshToken = "generatedRefreshToken"; // 실제 토큰 발급 로직 추가
        LocalDateTime accessTokenExpiration = LocalDateTime.now().plusMinutes(10); // 예시로 10분 설정
        LocalDateTime refreshTokenExpiration = LocalDateTime.now().plusDays(30); // 예시로 30일 설정

        return "redirect:/";
    }

    @GetMapping("/oauth2/kakaologinFailure")
    public String loginFailure() {
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout() {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().getAttribute("userId");

        SecurityContextHolder.clearContext();

        return "redirect:/login?logout";
    }

    private String generateUniqueSerialNumber() {
        String serialNumber;
        do {
            serialNumber = new BigInteger(130, new SecureRandom()).toString(32);
        } while (userRepository.existsBySerialNumber(serialNumber));
        return serialNumber;
    }
}
