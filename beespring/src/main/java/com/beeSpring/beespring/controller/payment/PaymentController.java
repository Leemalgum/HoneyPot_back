package com.beeSpring.beespring.controller.payment;

import com.beeSpring.beespring.dto.payment.PaymentDTO;
import com.beeSpring.beespring.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
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

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

//    @GetMapping(path="/api/payment")
//    public ResponseEntity<PaymentDTO> processPaymentInfo(@PathVariable("paymentInfo") PaymentDTO paymentDTO) {
//        if (paymentDTO != null)
//            return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @GetMapping  // Use @GetMapping for handling HTTP GET requests
    public ResponseEntity<PaymentDTO> receivePaymentData(@RequestParam MultiValueMap<String, String> params) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPg(params.getFirst("pg"));
        paymentDTO.setPayMethod(params.getFirst("payMethod"));
        paymentDTO.setEscrow(Boolean.parseBoolean(params.getFirst("escrow")));
        paymentDTO.setVbankDue(params.getFirst("vbankDue"));
        paymentDTO.setBizNum(params.getFirst("bizNum"));
        paymentDTO.setQuota(params.getFirst("quota"));
        paymentDTO.setMerchantUid(params.getFirst("merchantUid"));
        paymentDTO.setName(params.getFirst("name"));
        paymentDTO.setAmount(params.getFirst("amount"));
        paymentDTO.setBuyerName(params.getFirst("buyerName"));
        paymentDTO.setBuyerPhone(params.getFirst("buyerPhone"));
        paymentDTO.setBuyerEmail(params.getFirst("buyerEmail"));
        paymentDTO.setBuyerAddr(params.getFirst("buyerAddr"));
        paymentDTO.setBuyerPostcode(params.getFirst("buyerPostcode"));

        log.info("Received payment data: {}", paymentDTO);
        paymentService.processPayment(paymentDTO);
        return ResponseEntity.ok(paymentDTO);
    }


}