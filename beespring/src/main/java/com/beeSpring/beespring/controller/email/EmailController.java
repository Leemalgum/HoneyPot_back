package com.beeSpring.beespring.controller.email;

import com.beeSpring.beespring.controller.user.AuthController;
import com.beeSpring.beespring.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;


    @PostMapping("/notifyEndingSoon")
    public void notifyAuctionEndingSoon(@RequestParam String productId) {
        emailService.sendBidClosingReminderEmail(productId);

    }
}
