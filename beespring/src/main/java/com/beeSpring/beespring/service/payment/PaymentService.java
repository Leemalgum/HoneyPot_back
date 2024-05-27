package com.beeSpring.beespring.service.payment;

import com.beeSpring.beespring.domain.payment.Payment;
import com.beeSpring.beespring.dto.payment.PaymentDTO;

public interface PaymentService {
    public void processPayment(PaymentDTO paymentDTO);
    public Payment convertToPayment(PaymentDTO paymentDTO);
}
