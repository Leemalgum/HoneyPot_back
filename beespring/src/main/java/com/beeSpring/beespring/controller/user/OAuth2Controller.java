package com.beeSpring.beespring.controller.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.response.CustomApiResponse;
import com.beeSpring.beespring.response.ResponseCode;
import com.beeSpring.beespring.security.jwt.JwtTokenProvider;
//import com.beeSpring.beespring.service.jwt.TokenService;
import com.beeSpring.beespring.service.user.KakaoOAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final KakaoOAuthService kakaoOAuthService;
//    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

    /*@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String clientId;*/
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String redirectUri;
    /*@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String clientSecret;
*/
/*    // 카카오 로그인을 위해 회원가입 여부 확인, 이미 회원이면 Jwt 토큰 발급
    @Operation(summary = "KakaoLogin endpoint", description = "Authenticate user and provide token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/kakaologin")
    public ResponseEntity<CustomApiResponse<HashMap<String, String>>> authCheck(@RequestHeader String accessToken) {
        try {
            accessToken = accessToken.replace("Bearer ", "");
            logger.info("Received accessToken: {}", accessToken);
            String userId = kakaoOAuthService.isSignedUp(accessToken);
            if (userId == null) {
                throw new IllegalArgumentException("User not signed up with provided access token.");
            }

            // JWT 토큰 생성
            String token = jwtTokenProvider.createToken(userId);
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("token", token);

            logger.info("Login successful for userId: {}", userId);
            return ResponseEntity.ok(CustomApiResponse.success(map, ResponseCode.USER_LOGIN_SUCCESS.getMessage()));
        } catch (Exception e) {
            logger.error("Login failed with accessToken: {}", accessToken, e);
            return ResponseEntity.status(ResponseCode.USER_LOGIN_FAILURE.getCode())
                    .body(CustomApiResponse.error(ResponseCode.USER_LOGIN_FAILURE.getMessage(), ResponseCode.USER_LOGIN_FAILURE.getCode()));
        }
    }*/

    // 카카오 로그인을 위해 회원가입 여부 확인, 이미 회원이면 Jwt 토큰 발급
    @Operation(summary = "KakaoLogin endpoint", description = "Authenticate user and provide token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/kakaologin")
    public ResponseEntity<CustomApiResponse<HashMap<String, String>>> authCheck(@RequestBody Map<String, Object> userData) {
        System.out.println(userData.toString());
        String accessToken = (String) userData.get("accessToken");
        System.out.println("1 - 여기까지 되니?");
        try {
            if (accessToken == null || accessToken.isEmpty()) {
                throw new IllegalArgumentException("Access token is null or empty.");
            }
            logger.info("Received accessToken: {}", accessToken);
            // 여기서 받은 데이터를 이용해 추가적인 작업을 할 수 있습니다.
            String email = (String) userData.get("email");
            String profileImage = (String) userData.get("profileImage");
            Integer expiresIn = (Integer) userData.get("expiresIn");
            String refreshToken = (String) userData.get("refreshToken");
            Integer refreshTokenExpiresIn = (Integer) userData.get("refreshTokenExpiresIn");

            System.out.println("3 - 여기까지 되니?");
            String userId = kakaoOAuthService.isSignedUp(userData);
            System.out.println("서비스에서 받아온 UserId : " + userId);
            System.out.println("4 - 여기까지 되니?");
            if (userId == null) {
                throw new IllegalArgumentException("User not signed up with provided access token.");
            }
            System.out.println("5 - 여기까지 되니?");

            // JWT 토큰 생성
            String token = jwtTokenProvider.createAccessToken(userId);
            System.out.println("토큰 만들어지니? " + token);

            // DB에 저장할 User 객체 생성 및 저장
            User user = userRepository.findByProviderAndUserId("kakao", userId)
                    .orElse(new User());

            if (user.getUserId() == null) {
                user.setSerialNumber(generateUniqueSerialNumber());
            }
            user.setUserId(userId);
            user.setProvider("kakao");
            user.setEmail(email);
            user.setProfileImage(profileImage);
            user.setAccessToken(accessToken);
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
            user.setRefreshTokenExpiration(refreshTokenExpiration);
            userRepository.save(user);

            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("token", token);
            map.put("email", email);
            map.put("profileImage", profileImage);
            System.out.println("토큰 프로바이더 되니?");

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

//        tokenService.saveToken(user.getUserId(), accessToken, refreshToken, accessTokenExpiration, refreshTokenExpiration);

        return "redirect:/";
    }

    @GetMapping("/oauth2/kakaologinFailure")
    public String loginFailure() {
        // 로그인 실패 시 처리 로직 추가 (예: 로그 기록, 사용자에게 오류 메시지 전달 등)
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout() {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().getAttribute("userId");

        // Redis에서 토큰 삭제
//        tokenService.deleteToken(userId);

        // 세션 무효화
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
