package com.beeSpring.beespring.controller.user;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.beeSpring.beespring.domain.category.Idol;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.domain.user.UserIdol;
import com.beeSpring.beespring.dto.shipping.ShippingAddressDTO;
import com.beeSpring.beespring.repository.category.IdolRepository;
import com.beeSpring.beespring.repository.shipping.ShippingAddressRepository;
import com.beeSpring.beespring.repository.user.UserIdolRepository;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.response.CustomApiResponse;
import com.beeSpring.beespring.response.ResponseCode;
import com.beeSpring.beespring.security.jwt.JwtTokenProvider;
import com.beeSpring.beespring.service.shipping.ShippingServiceImpl;
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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserIdolRepository userIdolRepository;
    private final IdolRepository idolRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AmazonS3 s3Client;
    private final UserIdolService userIdolService;
    private final ShippingAddressRepository shippingAddressRepository;
    private final TransactionTemplate transactionTemplate;
    private final ShippingServiceImpl shippingService;


    @Transactional
    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse<HashMap<String, String>>> login(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody AuthRequest authRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            String bearerToken = request.getHeader("Authorization");
            logger.debug("Login Authorization Header: " + bearerToken);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            Optional<User> userOptional = userRepository.findByProviderAndUserId("service", authRequest.getUsername());

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            User user = userOptional.get();

            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomApiResponse.error("Invalid username/password", 401));
            }

            String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), "service");
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());
            String username = jwtTokenProvider.getUsername(accessToken);

            LocalDateTime accessTokenExpiration = jwtTokenProvider.getExpirationDate(accessToken);
            System.out.println("accessExpiration : " + accessTokenExpiration);
            LocalDateTime refreshTokenExpiration = jwtTokenProvider.getExpirationDate(refreshToken);
            System.out.println("refreshExpiration : " + refreshTokenExpiration);

            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            user.setAccessTokenExpiration(accessTokenExpiration);
            user.setRefreshTokenExpiration(refreshTokenExpiration);
            userRepository.save(user);

            HashMap<String, String> map = new HashMap<>();
            map.put("provider", "service");
            map.put("userId", username);
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            map.put("accessTokenExpiration", String.valueOf(accessTokenExpiration));
            map.put("refreshTokenExpiration", String.valueOf(refreshTokenExpiration));
            map.put("redirectUrl", "http://localhost:3000");

            HttpSession session = request.getSession();
            session.setAttribute("JWT_TOKEN", accessToken);

            return ResponseEntity.ok(CustomApiResponse.success(map, ResponseCode.USER_LOGIN_SUCCESS.getMessage()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username/password");
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    user.setBirthdate(LocalDate.parse(birthdate, formatter));
                } catch (DateTimeParseException e) {
                    log.error("Invalid birthdate format: {}", birthdate, e);
                    return ResponseEntity.badRequest().body("Invalid birthdate format");
                }
            } else {
                user.setBirthdate(LocalDate.of(1992, 12, 25)); // 기본값 설정
            }
            user.setGender(selectedGender);
            log.debug("Saving user to repository");

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    userRepository.save(user);
                    saveShippingAddress(user.getSerialNumber(), detailAddress, postcode, name, mobileNumber, roadAddress);
                }
            });

            log.debug("Creating shipping address for user: {}", username);
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveShippingAddress(String serialNumber, String detailAddress, String postcode, String name, String mobileNumber, String roadAddress) {
        ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO();
        shippingAddressDTO.setSerialNumber(serialNumber);
        shippingAddressDTO.setAddressName("기본 배송지");
        shippingAddressDTO.setDetailAddress(detailAddress);
        shippingAddressDTO.setPostCode(postcode);
        shippingAddressDTO.setRecipientName(name);
        shippingAddressDTO.setRecipientPhone(mobileNumber);
        shippingAddressDTO.setRoadAddress(roadAddress);

        shippingService.saveAddress(shippingAddressDTO);
    }

    @Transactional
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody HashMap<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        String provider = jwtTokenProvider.getProvider(refreshToken);

        Optional<User> userOptional = userRepository.findByProviderAndUserId(provider, username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        User user = userOptional.get();
        if (!refreshToken.equals(user.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getProvider());
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
        String provider = jwtTokenProvider.getProvider(accessToken);
        System.out.println("사용자 이름 : " + username);
        System.out.println("프로바이더 : " + provider);

        Optional<User> userOptional = userRepository.findByProviderAndUserId(provider, username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomApiResponse.error("User not found", 404));
        }

        User user = userOptional.get();
        HashMap<String, String> response = new HashMap<>();
        response.put("username", user.getUserId());
        response.put("serialNumber", user.getSerialNumber());

        return ResponseEntity.ok(CustomApiResponse.success(response, "User info fetched successfully"));
    }

    @Transactional
    @PostMapping("/user-idol")
    public ResponseEntity<?> saveUserIdols(@RequestBody Map<String, Object> request) {
        String serialNumber = (String) request.get("serialNumber");
        List<Integer> idolIds = (List<Integer>) request.get("idolIds");

        if (serialNumber == null || idolIds == null || idolIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        User user = userRepository.findBySerialNumber(serialNumber)
                .orElseThrow();

        try {
            for (Integer idolId : idolIds) {
                Idol idol = idolRepository.findById(idolId).orElseThrow(() -> new RuntimeException("Idol not found: " + idolId));
                UserIdol userIdol = UserIdol.builder()
                        .user(user)
                        .idol(idol)
                        .build();
                userIdolRepository.save(userIdol);
            }
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
