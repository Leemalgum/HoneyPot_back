package com.beeSpring.beespring.controller.shipping;

import com.beeSpring.beespring.controller.mypage.MypageController;
import com.beeSpring.beespring.service.shipping.ShippingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ShippingController {

    private static final Logger log = LoggerFactory.getLogger(MypageController.class);
    private final ShippingService shippingService;
    @PostMapping(value ="/shopping/{productId}")
    public ResponseEntity<String> confirmShipping(
            @PathVariable String productId) {
        try {
            log.info("Confirmation Starts with follow productId: " + productId);
            shippingService.updateOrderConfirm(productId);
            return ResponseEntity.ok("Profile order confirmation done");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to confirm order: " + e.getMessage());
        }
    }

    @GetMapping(value = "/shopping/{productId}")
    public ResponseEntity<Boolean> fetchOrderConfirmation(
            @PathVariable String productId) {
        try {
            log.info("Fetching order confirmation for productId: " + productId);
            boolean response = shippingService.getOrderConfirm(productId);
            log.info("Here is the OrderConfirm status " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
