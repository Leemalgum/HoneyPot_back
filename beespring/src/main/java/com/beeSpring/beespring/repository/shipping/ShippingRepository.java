package com.beeSpring.beespring.repository.shipping;

import com.beeSpring.beespring.domain.shipping.Shipping;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    //TODO:: 프론트에서 주는 productID 를 가지고 BidResult 에서 bid_result_id 를 받고
    @Transactional
    @Modifying
    @Query("UPDATE Shipping s SET s.orderConfirm = true WHERE s.bidResult.bidResultId = :bidResultId")
    void updateOrderConfirm(int bidResultId);

    @Query("SELECT br.bidResultId From BidResult br where br.product.productId = :productId")
    int findBigResultId(String productId);

    @Query("SELECT s.orderConfirm From Shipping s where s.bidResult.bidResultId = :bidResultId")
    boolean findOrderConfirm(int bidResultId);
}
