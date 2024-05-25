package com.beeSpring.beespring.repository.bid;
import com.beeSpring.beespring.domain.bid.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p, i.idolName, pt.ptypeName FROM Product p JOIN p.idol i JOIN p.productType pt")
    List<Object[]> findAllWithIdolName();
    //    @Query("SELECT p.productId, br.paymentStatus, br.completeDate, sh.deliveryStatus, u.serialNumber, p.productName, p.image1, p.priceUnit, p.startPrice, p.bidCnt, u.nickname FROM Shipping sh JOIN sh.bidResult br JOIN br.product p JOIN p.user u WHERE u.serialNumber = :serialNumber\n")
//    @Query("SELECT p.productId, br.paymentStatus, br.completeDate, sh.deliveryStatus, u.serialNumber, p.productName, p.image1, p.priceUnit, p.startPrice, p.bidCnt, u.nickname " +
//            "FROM Shipping sh " +
//            "JOIN sh.bidResult br " +
//            "JOIN br.product p " +
//            "JOIN p.user u " +
//            "WHERE u.serialNumber = :serialNumber")
    @Query("SELECT p.productId, br.paymentStatus, br.completeDate, sh.deliveryStatus, p.user.serialNumber, p.productName, p.image1, p.priceUnit, p.startPrice, p.bidCnt, u.nickname, p.storageStatus " +
            "FROM Shipping sh " +
            "JOIN sh.bidResult br " +
            "RIGHT JOIN br.product p " +
            "JOIN p.user u " +
            "WHERE p.user.serialNumber = :serialNumber")
    List<Object[]> findBySerialNumber(String serialNumber);
    @Query("SELECT p.productId, br.paymentStatus, br.completeDate, sh.deliveryStatus, u.serialNumber, " +
            "p.productName, p.image1, p.priceUnit, p.startPrice, p.bidCnt, u.nickname " +
            "FROM Shipping sh " +
            "JOIN sh.bidResult br " +
            "JOIN br.product p " +
            "JOIN p.user u " +
            "WHERE u.serialNumber = :serialNumber")
    List<Object[]> findByCustomerId(String serialNumber);
    @Query("SELECT p.productId, u.serialNumber, p.productName, p.priceUnit, p.startPrice, p.bidCnt, " +
            "sa.addressId, sa.addressName, sa.recipientName, sa.recipientPhone, sa.postCode, sa.roadAddress, sa.detailAddress " +
            "FROM Product p JOIN p.user u JOIN u.shippingAddresses sa " +
            "WHERE u.serialNumber = :serialNumber AND p.productId = :productId")
    List<Object[]> findByProductId(@Param("serialNumber") String serialNumber, @Param("productId") String productId);
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.price = :price WHERE p.productId = :productId")
    void updateProductPrice(String productId, int price);
    @Modifying
    @Query("UPDATE Product p SET p.view = p.view + 1 WHERE p.id = :productId")
    void incrementViewCount(@Param("productId") String productId);
}