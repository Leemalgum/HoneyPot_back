package com.beeSpring.beespring.controller.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.service.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @GetMapping("/oauth2/loginSuccess")
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

        tokenService.saveToken(user.getSerialNumber(), accessToken, refreshToken, accessTokenExpiration, refreshTokenExpiration);

        return "redirect:/";
    }

    @GetMapping("/oauth2/loginFailure")
    public String loginFailure() {
        // 로그인 실패 시 처리 로직 추가 (예: 로그 기록, 사용자에게 오류 메시지 전달 등)
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout() {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().getAttribute("userId");

        // Redis에서 토큰 삭제
        tokenService.deleteToken(userId);

        // 세션 무효화
        SecurityContextHolder.clearContext();

        return "redirect:/login?logout";
    }

    @PostMapping("/signupSuccess")
    public String signupSuccess(@RequestBody User newUser) {
        if (userRepository.existsByUserId(newUser.getUserId())) {
            return "redirect:/signup?error=userexists";
        }
        newUser.setRegistrationDate(LocalDateTime.now());
        userRepository.save(newUser);
        return "redirect:/signup?success";
    }

    @GetMapping("/signupFailure")
    public String signupFailure() {
        // 회원가입 실패 시 처리 로직 추가
        return "redirect:/signup?error";
    }
}

