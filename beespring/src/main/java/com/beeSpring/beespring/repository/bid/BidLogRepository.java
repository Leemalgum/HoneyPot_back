package com.beeSpring.beespring.repository.bid;

import com.beeSpring.beespring.domain.bid.Bid;
import com.beeSpring.beespring.domain.bid.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BidLogRepository extends JpaRepository<Bid, Integer> {
    @Query("SELECT MAX(b.price) FROM Bid b WHERE b.product = :product")
    Integer findMaxPriceByProduct(Product product);

    List<Bid> findByProduct(Product product);

    /**
     * 마이페이지->입찰 목록
     * @param serialNumber
     * @return
     */
    @Query("SELECT b, seller.nickname FROM Bid b " +
            "JOIN b.product p " +
            "JOIN User seller ON p.user.serialNumber = seller.serialNumber " +
            "WHERE b.user.serialNumber = :serialNumber " +
            "AND b.bidTime = (SELECT MAX(b2.bidTime) " +
            "FROM Bid b2 WHERE b2.product = b.product " +
            "AND b2.user.serialNumber = :serialNumber)" +
            "ORDER BY b.bidTime DESC")
    List<Object[]> findMostRecentBidsByUser(String serialNumber);

    @Query("SELECT u.nickname, u.email, p.productName, p.id " +
            "FROM Bid b " +
            "JOIN b.user u " +
            "JOIN b.product p " +
            "WHERE p.deadline BETWEEN :now AND :oneHourLater")
    List<Object[]> findUsersWithProductDeadlineWithinAnHour(@Param("now") LocalDateTime now, @Param("oneHourLater") LocalDateTime oneHourLater);

    @Query("SELECT COUNT(b) FROM Bid b WHERE b.bidTime >= :startOfDay AND b.bidTime < :endOfDay")
    int countBidsToday(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
