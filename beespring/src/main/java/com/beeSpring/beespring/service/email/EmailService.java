package com.beeSpring.beespring.service.email;

import jakarta.mail.internet.MimeMessage;

import java.util.List;

public interface EmailService {

    List<Object[]> sendBidClosingReminderEmail(String productId);
    MimeMessage createMail(String nickname, String email, String productName, String productId);
    MimeMessage sendPasswordResetEmail(String to, String resetUrl);
}
