package com.beeSpring.beespring.repository.bid;

import com.beeSpring.beespring.domain.bid.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("select p, i.idolName from Product p join Idol i on p.idolId = i.idolId")
    List<Object[]> findAllWithIdolName();

    //    @Query("SELECT br.productId, br.paymentStatus, br.completeDate, sh.deliveryStatus, p.serialNumber, p.productName, p.image1, p.priceUnit, p.startPrice, p.bidCnt FROM BidResult br JOIN Shipping sh ON br.bidResultId = sh.bidResult.bidResultId JOIN Product p ON br.productId = p.productId")
    @Query("SELECT br.productId, br.paymentStatus, br.completeDate, sh.deliveryStatus, p.serialNumber, p.productName, p.image1, p.priceUnit, p.startPrice, p.bidCnt, u.nickname FROM BidResult br JOIN Shipping sh ON br.bidResultId = sh.bidResult.bidResultId JOIN Product p ON br.productId = p.productId JOIN User u ON p.serialNumber = u.serialNumber")
    List<Object[]> findBySerialNumber(String serialNumber);
}
