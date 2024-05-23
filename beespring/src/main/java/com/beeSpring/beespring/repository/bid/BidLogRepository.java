package com.beeSpring.beespring.repository.bid;

import com.beeSpring.beespring.domain.bid.Bid;
import com.beeSpring.beespring.domain.bid.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidLogRepository extends JpaRepository<Bid, Integer> {
    @Query("SELECT MAX(b.price) FROM Bid b WHERE b.product = :product")
    Integer findMaxPriceByProduct(Product product);
    List<Bid> findByProduct(Product product);
}
