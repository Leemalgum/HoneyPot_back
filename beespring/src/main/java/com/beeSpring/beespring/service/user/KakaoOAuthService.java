package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.controller.user.AuthController;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class KakaoOAuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /*    private final KakaoUserInfo kakaoUserInfo; */
    private final JwtTokenProvider jwtTokenProvider;
    private final WebClient webClient;
    private final UserRepository userRepository;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    public KakaoOAuthService(JwtTokenProvider jwtTokenProvider, WebClient.Builder webClientBuilder, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.webClient = webClientBuilder.baseUrl(kakaoApiUrl).build();
        this.userRepository = userRepository;

    }

    public String isSignedUp(Map<String, Object> userData) {
        try {
            String email = (String) userData.get("email");
            String profileImage = (String) userData.get("profileImage");

            String userId = "";
            if (email != null && email.contains("@")) {
                userId = email.substring(0, email.indexOf("@"));
            }

            Optional<User> user = userRepository.findByProviderAndUserId("kakao", userId);
            if (user.isPresent()) {
                userId = user.get().getUserId();
            } else {
                User newUser = new User();
                newUser.setProvider("kakao");
                newUser.setUserId(userId);
                newUser.setEmail(email);
                newUser.setProfileImage(profileImage);
            }
            return userId;
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return null;
            } else {
                throw new RuntimeException("Failed to retrieve user info from Kakao API", e);
            }
        }
    }

    private String extractUserIdFromUserInfo(String userInfo) {
        return "${spring.security.oauth2.client.registration.kakao.client-id}"; // 실제 파싱된 userId 반환
    }
}

