package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.service.utils.JwtUtil;
import com.beeSpring.beespring.domain.user.OAuthUser;
import com.beeSpring.beespring.repository.user.OAuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoOAuthService {
    @Autowired
    private OAuthUserRepository oAuthUserRepository;

    @Autowired
    private JwtUtil jwtUtil;

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
        String username = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        OAuthUser oAuthUser = oAuthUserRepository.findByProviderAndProviderId("kakao", providerId)
                .orElseGet(() -> {
                    OAuthUser newUser = new OAuthUser();
                    newUser.setProvider("kakao");
                    newUser.setProviderId(providerId);
                    newUser.setAccessToken(accessToken);
                    newUser.setEmail(email);
                    newUser.setUsername(username);
                    newUser.setProfileImage(profileImage);
                    return oAuthUserRepository.save(newUser);
                });

        oAuthUser.setAccessToken(accessToken);
        oAuthUser.setEmail(email);
        oAuthUser.setUsername(username);
        oAuthUser.setProfileImage(profileImage);
        oAuthUserRepository.save(oAuthUser);

        return jwtUtil.generateToken(oAuthUser.getUsername());
    }
}

