package com.beeSpring.beespring.controller.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            User user = userRepository.findByUserId(authRequest.getUsername());
            return jwtTokenProvider.createToken(user.getUserId());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username/password");
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest signupRequest) {
        User user = new User();
        user.setUserId(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    // 필요한 경우 로그아웃 엔드포인트도 추가할 수 있습니다.
}
