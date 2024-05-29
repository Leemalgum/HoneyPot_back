package com.beeSpring.beespring.repository.payment;

import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.payment.Payment;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.dto.payment.PaymentDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
//    @Transactional
//    @Modifying
//    @Query("update Product p SET p.storageStatus = :storageStatus where p.productId = :productId")
//    void updateProductStatusAfterConfirm(String productId, String storageStatus);
//
//    @Query("SELECT status, serialNumber FROM Payment WHERE productId = :productId")
//    ProductWithSerialNumberDTO getPaymentStatus(String productId);

//    @Transactional
//    @Modifying
//    @Query("UPDATE BidResult SET paymentStatus = true WHERE BidResult.product.productId = :productId")
//    void updatePaymentStatus(String productId);



}
