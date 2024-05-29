package com.beeSpring.beespring.repository.bid;

import com.beeSpring.beespring.domain.bid.*;
import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface BidResultRepository  extends JpaRepository<BidResult, Long> {
    @Query("SELECT b " +
            "FROM Bid b "+
            "WHERE b.product.productId = :productId " +
            "ORDER BY b.product.price DESC LIMIT 1")
    Bid findHighestBidByProductId(String productId);

    @Query("SELECT p FROM Product p WHERE p.deadline <= :now AND p.storageStatus = :status")
    List<Product> findProductsByDeadlineBeforeAndStorageStatus(LocalDateTime now, StorageStatus status);


    @Query("UPDATE Product p SET p.storageStatus = :status where p.productId = :productId")
    void updateProductStatus(StorageStatus status, String productId);
}