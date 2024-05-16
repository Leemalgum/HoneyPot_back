package com.beeSpring.beespring.security.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.beeSpring.beespring.service.user.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.service.jwt.TokenService;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName(); // Assuming userId is the username
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            user = new User();
            user.setUserId(userId);
            user.setNickname(authentication.getName()); // This is just an example, modify as needed
            user.setProfileImage("default.png"); // Default profile image
            user.setRegistrationDate(LocalDateTime.now());
            userRepository.save(user);
        }

        String accessToken = JWT.create().withSubject("jwtToken").withExpiresAt(new Date(System.currentTimeMillis()+3600000)) // 3600000밀리초 = 1시간
                .withClaim("userId", userId).sign(Algorithm.HMAC512("${spring.jwt.secret}"));
        String refreshToken = JWT.create().withSubject("jwtToken").withExpiresAt(new Date(System.currentTimeMillis()+(1000*60*60))) // 3600000밀리초 = 1시간
                .sign(Algorithm.HMAC512("${spring.jwt.secret}"));
        LocalDateTime accessTokenExpiration = LocalDateTime.now().plusMinutes(10);
        LocalDateTime refreshTokenExpiration = LocalDateTime.now().plusDays(30);

        tokenService.saveToken(user.getSerialNumber(), accessToken, refreshToken, accessTokenExpiration, refreshTokenExpiration);

        response.sendRedirect("/index"); // Redirect to the homepage or other success page
    }

    public Authentication getAuthentication(User user) {
        PrincipalDetail principalDetail = new PrincipalDetail(user);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), principalDetail.getAuthorities()));
    }

    public void setSecuritySession(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
