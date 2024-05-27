package com.beeSpring.beespring.controller.payment;


import com.beeSpring.beespring.dto.payment.PaymentDTO;
import com.beeSpring.beespring.service.payment.PaymentServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Log4j2
public class PaymentController {

    private final PaymentServiceImpl paymentServiceImpl;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentServiceImpl) {
        log.info("hiho payment controller");
        this.paymentServiceImpl = paymentServiceImpl;
    }

    @GetMapping  // Handle GET requests to /payment
    @CrossOrigin(origins = {"http://localhost:3000", "http://223.130.153.93:8011"})
    public ResponseEntity<PaymentDTO> receivePaymentData(@RequestParam MultiValueMap<String, String> params) {
        log.info("data received: {}", params.toString());
        PaymentDTO paymentDTO = new PaymentDTO();
        // Extract payment data from request parameters
        // Process payment data
        log.info("Received payment data: {}", paymentDTO);
        paymentServiceImpl.processPayment(paymentDTO);
        return ResponseEntity.ok(paymentDTO);
    }

    @PostMapping  // Handle POST requests to /payment
    @CrossOrigin(origins = {"http://localhost:3000", "http://223.130.153.93:8011"})
    public ResponseEntity<PaymentDTO> processPaymentData(@RequestBody PaymentDTO paymentDTO) {
        log.info("Received Payment Data: {}", paymentDTO);

        if (paymentDTO != null) {
            log.info("===============Received payment data: {}", paymentDTO);
            paymentServiceImpl.processPayment(paymentDTO);
            return ResponseEntity.ok(paymentDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
