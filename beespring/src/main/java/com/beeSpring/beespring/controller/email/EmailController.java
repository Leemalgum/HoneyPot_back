package com.beeSpring.beespring.controller.email;

import com.beeSpring.beespring.controller.user.AuthController;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.user.ForgotPasswordRequest;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.service.email.EmailService;
import com.beeSpring.beespring.service.user.PasswordResetTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordResetTokenServiceImpl passwordResetTokenService;

    @PostMapping("/notifyEndingSoon")
    public void notifyAuctionEndingSoon(@RequestParam String productId) {
//        emailService.sendBidClosingReminderEmail(productId);
        emailService.sendBidClosingReminderEmail();

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        System.out.println(request.toString());
        Optional<User> user = userRepository.findByUserIdAndMobileNumberAndEmail(request.getUserId(), request.getMobileNumber(), request.getEmail());
        System.out.println(user.toString());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        } else {
            String token = passwordResetTokenService.createPasswordResetTokenForUser(user.get());
            String resetUrl = String.format("%s?token=%s", "http://http://223.130.153.93:3000//resetPassword", token);
            System.out.println("token : " + token);
            System.out.println("resetUrl : " + resetUrl);
            emailService.sendPasswordResetEmail(request.getEmail(), resetUrl);

            return ResponseEntity.ok("비밀번호 재설정 이메일이 발송되었습니다.");
        }
    }
}
