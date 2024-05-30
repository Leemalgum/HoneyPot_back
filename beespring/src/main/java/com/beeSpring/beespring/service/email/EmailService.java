package com.beeSpring.beespring.service.email;

import com.beeSpring.beespring.dto.admin.DeclineReasonDTO;
import com.beeSpring.beespring.dto.bid.PendingProductsDTO;
import jakarta.mail.internet.MimeMessage;

import java.util.List;

public interface EmailService {

//    List<Object[]> sendBidClosingReminderEmail(String productId);
    void sendBidClosingReminderEmail();
    MimeMessage createMail(String nickname, String email, String productName, String productId);
    MimeMessage sendPasswordResetEmail(String to, String resetUrl);
    void sendReceiptRejectionEmail(String to, DeclineReasonDTO declineReasonDTO);
    void sendReceiptApprovementEmail(String to, PendingProductsDTO pendingProductsDTO);
    void sendRegisterApprovementEmail(String to, PendingProductsDTO pendingProductsDTO);
}
