package com.beeSpring.beespring.controller.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.response.CustomApiResponse;
import com.beeSpring.beespring.response.ResponseCode;
import com.beeSpring.beespring.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse<HashMap<String, String>>> login(@RequestBody AuthRequest authRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("1 여기까지 되니?");
            //System.out.println(authRequest.getUsername() + " " + authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            System.out.println("2 여기까지 되니?");
            Optional<User> userOptional = userRepository.findByProviderAndUserId("service", authRequest.getUsername());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            User user = userOptional.get();

            // 비밀번호 검증
            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomApiResponse.error("Invalid username/password", 401));
            }

            System.out.println("3 여기까지 되니?");

            String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());
            System.out.println("로그인할 때 액세스 토큰 만들어지니? " + accessToken);
            System.out.println("로그인할 때 리프레쉬 토큰 만들어지니? " + refreshToken);


            // 토큰에서 사용자 이름 추출
            String username = jwtTokenProvider.getUsername(refreshToken);
            System.out.println("Username: " + username);

            // 토큰에서 만료 날짜 추출
            LocalDateTime accessTokenExpiration = jwtTokenProvider.getExpirationDate(accessToken);
            System.out.println("accessExpiration : " + accessTokenExpiration);
            LocalDateTime refreshTokenExpiration = jwtTokenProvider.getExpirationDate(refreshToken);
            System.out.println("refreshExpiration : " + refreshTokenExpiration);

            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            user.setAccessTokenExpiration(accessTokenExpiration);
            user.setRefreshTokenExpiration(refreshTokenExpiration);
            userRepository.save(user);

            System.out.println("레포지토리 save 되니.........?");

            HashMap<String, String> map = new HashMap<>();
            map.put("provider", "service");
            map.put("userId", username);
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            map.put("accessTokenExpiration", String.valueOf(accessTokenExpiration));
            map.put("refreshTokenExpiration", String.valueOf(refreshTokenExpiration));
            map.put("redirectUrl", "http://localhost:3000");

            // 토큰을 세션에 저장
            HttpSession session = request.getSession();
            session.setAttribute("JWT_TOKEN", accessToken);
            System.out.println("세션 저장 되니?");

            // 리다이렉트 설정
//            response.sendRedirect("http://localhost:3000/index");
//            System.out.println("리다이렉트 되니....?");
            return ResponseEntity.ok(CustomApiResponse.success(map, ResponseCode.USER_LOGIN_SUCCESS.getMessage()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username/password");
        /*} catch (IOException e) {
            log.error("Redirect failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();*/
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        System.out.println("signup에 오긴 하니?");
        log.debug("Entering signup method");
        try {
            log.debug("Checking if user ID exists: {}", signupRequest.getUsername());
            if (userRepository.existsByUserId(signupRequest.getUsername())) {
                log.warn("User ID already exists: {}", signupRequest.getUsername());
                return ResponseEntity.badRequest().body("User ID already exists");
            }
            log.debug("Creating new user: {}", signupRequest.getUsername());
            User user = new User();
            user.setProfileImage(signupRequest.getProfileImage());
            user.setProvider("service");
            user.setSerialNumber(generateUniqueSerialNumber());
            user.setUserId(signupRequest.getUsername());
            user.setRoleId(1);
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setFirstName(signupRequest.getUsername());
            user.setNickname(signupRequest.getNickname());
            user.setMobileNumber(signupRequest.getMobileNumber());
            user.setEmail(signupRequest.getEmail());
            user.setRegistrationDate(LocalDateTime.now());
            user.setPostcode(signupRequest.getPostcode());
            user.setRoadAddress(signupRequest.getRoadAddress());
            user.setDetailAddress(signupRequest.getDetailAddress());
            user.setBirthdate(signupRequest.getBirthdate());
            user.setGender(signupRequest.getSelectedGender());
            log.debug("Saving user to repository");
            userRepository.save(user);
            log.debug("User registered successfully: {}", signupRequest.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            log.error("Signup error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup failed: " + e.getMessage());
        }
    }

    private String generateUniqueSerialNumber() {
        String serialNumber;
        do {
            serialNumber = new BigInteger(130, new SecureRandom()).toString(32);
        } while (userRepository.existsBySerialNumber(serialNumber));
        return serialNumber;
    }

    // 필요한 경우 로그아웃 엔드포인트도 추가할 수 있습니다.
    @Transactional
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody HashMap<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        Optional<User> userOptional = userRepository.findByProviderAndUserId("service", username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        User user = userOptional.get();
        if (!refreshToken.equals(user.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getUserId());
        LocalDateTime newAccessTokenExpiration = jwtTokenProvider.getExpirationDate(newAccessToken);

        user.setAccessToken(newAccessToken);
        user.setAccessTokenExpiration(newAccessTokenExpiration);
        userRepository.save(user);

        HashMap<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("accessTokenExpiration", newAccessTokenExpiration.toString());

        return ResponseEntity.ok(CustomApiResponse.success(response, "Access token refreshed successfully"));
    }
}
