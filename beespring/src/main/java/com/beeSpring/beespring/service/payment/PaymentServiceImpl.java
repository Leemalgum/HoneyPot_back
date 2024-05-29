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

import java.util.ArrayList;
import java.util.List;

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
        // Process the payment using Payment
        // Example:
        // paymentGateway.processPayment(payment);
    }

    @Override
    public List<PaymentDTO> findAllPayment() {
        List<Payment> paymentList = paymentRepository.findAll();
        List<PaymentDTO> dtoList = new ArrayList<>();

        paymentList.stream().forEach(payment -> {
            PaymentDTO paymentDTO = new PaymentDTO().builder()
                    .merchantUid(payment.getMerchantUid())
                    .serialNumber(payment.getSerialNumber())
                    .pg(payment.getPg())
                    .payMethod(payment.getPayMethod())
                    .escrow(payment.isEscrow())
                    .name(payment.getName())
                    .amount(payment.getAmount())
                    .buyerName(payment.getBuyerName())
                    .dateAdded(payment.getDateAdded())
                    .status(payment.getStatus())
                    .success(payment.isSuccess())
                    .receiptUrl(payment.getReceiptUrl())
                    .productId(payment.getProductId())
                    .build();
            dtoList.add(paymentDTO);
        });
        return dtoList;
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