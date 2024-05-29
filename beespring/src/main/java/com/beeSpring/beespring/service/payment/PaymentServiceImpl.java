package com.beeSpring.beespring.service.payment;

import com.beeSpring.beespring.domain.payment.Payment;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.dto.payment.PaymentDTO;
import com.beeSpring.beespring.repository.bid.BidResultRepository;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import com.beeSpring.beespring.repository.payment.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void processPayment(PaymentDTO paymentDTO) {
        // Convert PaymentDTO to Payment if necessary
        log.info("paymentDTO: " + paymentDTO);
        Payment payment = new Payment(paymentDTO);
        log.info("Payment: " + payment.toString());
        paymentRepository.save(payment);
        // TODO:: 배송이 완료가 되고 구매확정을 누루면 SOLD 인건가? DB 에 A02 의 값이 SOLD로 되어 있어서
        // Process the payment using Payment
        // Example:
        // paymentGateway.processPayment(payment);
    }



    @Transactional
    public void insertPayment(Payment payment) {
        System.out.println("payment 정보" + payment.toString());
        paymentRepository.save(payment);
    }


    @Override
    @Transactional
    public ProductWithSerialNumberDTO getPaymentStatus(String productId) {
//        return paymentRepository.getPaymentStatus(productId);
        return null;
    }

}