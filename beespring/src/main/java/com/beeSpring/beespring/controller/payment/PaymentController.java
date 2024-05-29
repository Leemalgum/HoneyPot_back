package com.beeSpring.beespring.controller.payment;


import com.beeSpring.beespring.domain.payment.Payment;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.dto.payment.PaymentDTO;
import com.beeSpring.beespring.repository.payment.PaymentRepository;
import com.beeSpring.beespring.service.mypage.MypageServiceImpl;
import com.beeSpring.beespring.service.payment.PaymentServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @GetMapping  // Handle GET requests to /payment
//    @CrossOrigin(origins = {"http://localhost:3000", "http://223.130.153.93:8011"})
//    public ResponseEntity<PaymentDTO> receivePaymentData(@RequestParam MultiValueMap<String, String> params) {
//        log.info("data received: {}", params.toString());
//        PaymentDTO paymentDTO = new PaymentDTO();
//        // Extract payment data from request parameters
//        // Process payment data
//        log.info("Received payment data: {}", paymentDTO);
//        paymentServiceImpl.processPayment(paymentDTO);
//        return ResponseEntity.ok(paymentDTO);
//    }

    @GetMapping("/payments/{productId}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://223.130.153.93:8011"})
    public ResponseEntity<ProductWithSerialNumberDTO> getPaymentStatus(@PathVariable("productId") String productIds) {
        int status = paymentServiceImpl.getPaymentStatus(productIds).getPaymentStatus();


        ProductWithSerialNumberDTO product = new ProductWithSerialNumberDTO();
        product.setPaymentStatus(status);
        product.setSerialNumber(paymentServiceImpl.getPaymentStatus(productIds).getSerialNumber());

        if (product != null) {
            // 상품 데이터가 존재할 경우 200 OK 응답과 함께 데이터를 반환합니다.
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            // 상품 데이터가 존재하지 않을 경우 404 Not Found 응답을 반환합니다.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping  // Handle POST requests to /payment
    @CrossOrigin(origins = {"http://localhost:3000", "http://223.130.153.93:8011"})
    public ResponseEntity<PaymentDTO> processPaymentData(@RequestBody PaymentDTO paymentDTO) {
        log.info("Received Payment Data: {}", paymentDTO);

        if (paymentDTO != null) {
            log.info("===============Received payment data: {}", paymentDTO);
//            paymentServiceImpl.processPayment(paymentDTO);
            // method1: payment DB 에 값 넣어주기
            // method2: shipping 에 order_confirm(구매확정여부) 에 0이 였던 값을 1로 업데이트
            paymentServiceImpl.processPayment(paymentDTO);
            return ResponseEntity.ok(paymentDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/payments/list")
    public ResponseEntity<List<PaymentDTO>> getAllPayment() {

        List<PaymentDTO> paymentList = paymentServiceImpl.findAllPayment();

        if (paymentList != null) {
            // 상품 데이터가 존재할 경우 200 OK 응답과 함께 데이터를 반환합니다.
            return new ResponseEntity<>(paymentList, HttpStatus.OK);
        } else {
            // 상품 데이터가 존재하지 않을 경우 404 Not Found 응답을 반환합니다.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
