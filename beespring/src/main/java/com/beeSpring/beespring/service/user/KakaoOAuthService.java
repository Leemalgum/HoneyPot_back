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
            System.out.println("WebClient가 문제일까?");
            /*String userInfo = webClient.get()
                    .uri("/v2/user/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();*/
            System.out.println("WebClient가 문제 맞구나");
            // 사용자 정보에서 userId 추출 로직 추가
            // 예제: JSON 파싱하여 userId 추출
//            return extractUserIdFromUserInfo(userInfo);

            System.out.println("서비스에서 UserData 출력 : " + userData.toString());
            String email = (String) userData.get("email");
            String profileImage = (String) userData.get("profileImage");

            String userId = "";
            if (email != null && email.contains("@")) {
                userId = email.substring(0, email.indexOf("@"));
            }

            System.out.println("레포지토리 되니?");
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
            System.out.println("레포지토리 된대");
            
            return userId;
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                // 클라이언트 오류 처리
                return null;
            } else {
                // 서버 오류 또는 기타 오류 처리
                throw new RuntimeException("Failed to retrieve user info from Kakao API", e);
            }
        }
    }

    private String extractUserIdFromUserInfo(String userInfo) {
        // JSON 파싱 로직 추가
        return "${spring.security.oauth2.client.registration.kakao.client-id}"; // 실제 파싱된 userId 반환
    }

    /*    @Transactional(readOnly = true)
    public String isSignedUp(String token) {
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        User user = userRepository.findByProviderAndUserId("kakao",userInfo.getId().toString()).orElseThrow(() -> new UserNotFoundException(ResponseCode.USER_NOT_FOUND));

        return user.getUserId();
    }

    @Transactional
    public String handleKakaoLogin(String accessToken) {
        // Get user info from Kakao
        RestTemplate restTemplate = new RestTemplate();
        String userInfoEndpointUri = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);

        Map<String, Object> userAttributes = response.getBody();

        String providerId = String.valueOf(userAttributes.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) userAttributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        User user = userRepository.findByProviderAndUserId("kakao", providerId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setProvider("kakao");
                    newUser.setUserId(providerId);
                    newUser.setAccessToken(accessToken);
                    newUser.setEmail(email);
                    newUser.setNickname(nickname);
                    newUser.setProfileImage(profileImage);
                    return userRepository.save(newUser);
                });

        user.setUserId(providerId);
        user.setAccessToken(accessToken);
        user.setEmail(email);
        user.setNickname(nickname);
        user.setProfileImage(profileImage);
        userRepository.save(user);

        return jwtTokenProvider.createToken(user.getUserId());
    }*/
}

