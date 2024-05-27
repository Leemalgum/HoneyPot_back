package com.beeSpring.beespring.service.payment;

import com.beeSpring.beespring.domain.payment.Payment;
import com.beeSpring.beespring.dto.payment.PaymentDTO;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    public void processPayment(PaymentDTO paymentDTO) {
        // Convert PaymentDTO to Payment if necessary
        Payment payment = convertToPayment(paymentDTO);

        // Process the payment using Payment
        // Example:
        // paymentGateway.processPayment(payment);
    }

    public Payment convertToPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        // Populate Payment properties from PaymentDTO
        payment.setPg(paymentDTO.getPg());
        payment.setPayMethod(paymentDTO.getPayMethod());
        payment.setEscrow(paymentDTO.isEscrow());
        // Set other properties similarly
        return payment;
    }
}