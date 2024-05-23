package com.beeSpring.beespring.controller.user;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.shipping.ShippingAddressDTO;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.response.CustomApiResponse;
import com.beeSpring.beespring.response.ResponseCode;
import com.beeSpring.beespring.security.jwt.JwtTokenProvider;
import com.beeSpring.beespring.service.mypage.MypageServiceImpl;
import com.beeSpring.beespring.service.shipping.ShippingService;
import com.beeSpring.beespring.service.user.UserIdolService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(MypageServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AmazonS3 s3Client;
    private final UserIdolService userIdolService;
    private final ShippingService shippingService;

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
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> signup(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("nickname") String nickname,
            @RequestParam("mobileNumber") String mobileNumber,
            @RequestParam("email") String email,
            @RequestParam("postcode") String postcode,
            @RequestParam("roadAddress") String roadAddress,
            @RequestParam("detailAddress") String detailAddress,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("selectedGender") String selectedGender) {

        log.debug("Entering signup method");
        try {
            log.debug("Checking if user ID exists: {}", username);
            if (userRepository.existsByUserId(username)) {
                log.warn("User ID already exists: {}", username);
                return ResponseEntity.badRequest().body("User ID already exists");
            }
            log.debug("Creating new user: {}", username);
            User user = new User();
            if (profileImage != null && !profileImage.isEmpty()) {
                String profileImageUrl = storeProfileImage(profileImage);
                user.setProfileImage(profileImageUrl);
            }
            user.setProvider("service");
            String serialNumber = generateUniqueSerialNumber();
            user.setSerialNumber(serialNumber);
            user.setUserId(username);
            user.setRoleId(1);
            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName(name);
            user.setNickname(nickname);
            user.setMobileNumber(mobileNumber);
            user.setEmail(email);
            user.setRegistrationDate(LocalDateTime.now());
            if (birthdate != null && !birthdate.isEmpty()) {
                user.setBirthdate(LocalDate.parse(birthdate));
            } else {
                user.setBirthdate(LocalDate.of(0, 12, 25)); // 기본값 설정
            }
            user.setGender(selectedGender);
            log.debug("Saving user to repository");
            userRepository.save(user);

            ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO();
            shippingAddressDTO.setSerialNumber(serialNumber);
            shippingAddressDTO.setAddressName("기본 배송지");
            shippingAddressDTO.setRecipientName(name);
            shippingAddressDTO.setPostCode(postcode);
            shippingAddressDTO.setRoadAddress(roadAddress);
            shippingAddressDTO.setDetailAddress(detailAddress);
            shippingAddressDTO.setRecipientPhone(mobileNumber);
            shippingService.saveAddress(shippingAddressDTO);

            log.debug("User registered successfully: {}", username);

            return ResponseEntity.ok(serialNumber);
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

    @GetMapping("/user-info")
    public ResponseEntity<CustomApiResponse<HashMap<String, String>>> getUserInfo(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomApiResponse.error("Invalid access token", 401));
        }

        if (!jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomApiResponse.error("Invalid access token", 401));
        }

        String username = jwtTokenProvider.getUsername(accessToken);
        Optional<User> userOptional = userRepository.findByProviderAndUserId("service", username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomApiResponse.error("User not found", 404));
        }

        User user = userOptional.get();
        HashMap<String, String> response = new HashMap<>();
        response.put("username", user.getUserId());
        response.put("serialNumber", user.getSerialNumber());

        return ResponseEntity.ok(CustomApiResponse.success(response, "User info fetched successfully"));
    }

    @PostMapping("/user-idol")
    public ResponseEntity<?> saveUserIdols(@RequestBody Map<String, Object> request) {
        String serialNumber = (String) request.get("serialNumber");
        List<Integer> idolIds = (List<Integer>) request.get("idolIds");

        if (serialNumber == null || idolIds == null || idolIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        try {
            userIdolService.saveUserIdols(serialNumber, idolIds);
            return ResponseEntity.ok("Idols selected successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving idol selection");
        }
    }

    @Transactional
    @PostMapping("/change-password")
    public ResponseEntity<CustomApiResponse<String>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            Optional<User> userOptional = userRepository.findBySerialNumber(changePasswordRequest.getSerialNumber());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomApiResponse.error("User not found", 404));
            }
            User user = userOptional.get();

            // 현재 비밀번호 검증
            if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomApiResponse.error("Invalid current password", 401));
            }

            // 새로운 비밀번호 설정
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);

            return ResponseEntity.ok(CustomApiResponse.success("Password changed successfully", "Password changed successfully"));
        } catch (Exception e) {
            log.error("Error changing password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomApiResponse.error("Password change failed: " + e.getMessage(), 500));
        }
    }

    public String storeProfileImage(MultipartFile file) throws IOException {
        String objectName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {

            PutObjectRequest putRequest = new PutObjectRequest("beespring-bucket/profile", objectName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putRequest);
            logger.info("File uploaded successfully to S3. URL: {}", s3Client.getUrl("beespring-bucket", objectName).toString());
            return s3Client.getUrl("beespring-bucket/profile", objectName).toString();
        } catch (AmazonServiceException e) {
            logger.error("Failed to upload file to S3. AWS error message: {}", e.getErrorMessage());
            throw new IOException("Failed to upload file to S3.", e);
        } catch (SdkClientException e) {
            logger.error("Failed to upload file to S3. SDK error message: {}", e.getMessage());
            throw new IOException("Failed to upload file to S3.", e);
        }
    }
}
