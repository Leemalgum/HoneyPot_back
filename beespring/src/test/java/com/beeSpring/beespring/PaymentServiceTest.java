package com.beeSpring.beespring;

import com.beeSpring.beespring.domain.bid.Bid;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.payment.Payment;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.bid.BidLogRepository;
import com.beeSpring.beespring.repository.payment.PaymentRepository;
import com.beeSpring.beespring.service.bid.BidLogServiceImpl;
import com.beeSpring.beespring.service.payment.PaymentServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class PaymentServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceTest.class);


    @Autowired
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;
    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(paymentRepository);
    }
    @Test
    @Transactional
    void testInsertPayment() throws InterruptedException, ExecutionException {
        logger.info("Starting insert payment");

        Payment payment = new Payment();
        payment.setMerchantUid("1");
        payment.setSerialNumber("123");
        payment.setPg("inicis");
        payment.setPayMethod("card");
        payment.setEscrow(true);
        payment.setName("estpa");
        payment.setAmount(1234);
        payment.setBuyerName("aespa");
        payment.setDateAdded(LocalDate.now());
        payment.setStatus("paid");
        payment.setSuccess(true);
        payment.setReceiptUrl("eeee");
        payment.setProductId("A02");

        // when
        paymentService.insertPayment(payment);

    }
}
