package com.beeSpring.beespring.repository.bid;

import com.beeSpring.beespring.domain.bid.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p, i.idolName FROM Product p JOIN p.idol i")
    List<Object[]> findAllWithIdolName();

    //    @Query("SELECT p.productId, br.paymentStatus, br.completeDate, sh.deliveryStatus, u.serialNumber, p.productName, p.image1, p.priceUnit, p.startPrice, p.bidCnt, u.nickname FROM Shipping sh JOIN sh.bidResult br JOIN br.product p JOIN p.user u WHERE u.serialNumber = :serialNumber\n")
    @Query("SELECT p.productId, br.paymentStatus, br.completeDate, sh.deliveryStatus, u.serialNumber, p.productName, p.image1, p.priceUnit, p.startPrice, p.bidCnt, u.nickname " +
            "FROM Shipping sh " +
            "JOIN sh.bidResult br " +
            "JOIN br.product p " +
            "JOIN p.user u " +
            "WHERE u.serialNumber = :serialNumber")
    List<Object[]> findBySerialNumber(String serialNumber);

//    @Query("SELECT Product.productId, Product.serialNumber, Product.productName, Product.priceUnit, Product.startPrice, Product.bidCnt, Shipping_address.addressName, Shipping_address.recipientName, Shipping_address.postCode, Shipping_address.roadAddress, Shipping_address.detailAddress, Shipping_address.recipientPhone FROM Product JOIN User ON Product.serialNumber = User.serialNumber JOIN Shipping_address ON Product.serialNumber = Shipping_address.serialNumber WHERE Product.serialNumber = '123456789' AND Product.productId = 'A02'")
//    Object findByProductId(String serialNumber, String productId);
//    @Query("SELECT p.productId, p.serialNumber, p.productName, p.priceUnit, p.startPrice, p.bidCnt, sa.recipientName, sa.postCode, sa.roadAddress, sa.detailAddress, sa.recipientPhone FROM Product p JOIN p.user u JOIN p.shippingAddress sa WHERE p.serialNumber = :serialNumber AND p.productId = :productId")
//    Object findByProductId(@Param("serialNumber") String serialNumber, @Param("productId") String productId);

//    @Query("SELECT p.productId, p.serialNumber, p.productName, p.priceUnit, p.startPrice, p.bidCnt, sa.recipientName, sa.postCode, sa.roadAddress, sa.detailAddress, sa.recipientPhone FROM Product p JOIN p.user u JOIN p.shippingAddress sa WHERE p.serialNumber = :serialNumber AND p.productId = :productId")
//    Object findByProductId(@Param("serialNumber") String serialNumber, @Param("productId") String productId);

//    @Query("SELECT p.productId, p.serialNumber, p.productName, p.priceUnit, p.startPrice, p.bidCnt, " +
//            "sa.recipientName, sa.postCode, sa.roadAddress, sa.detailAddress, sa.recipientPhone " +
//            "FROM Product p JOIN p.user u JOIN u.shippingAddresses sa " +
//            "WHERE p.serialNumber = :serialNumber AND p.productId = :productId")

    @Query("SELECT p.productId, u.serialNumber, p.productName, p.priceUnit, p.startPrice, p.bidCnt, " +
            "sa.recipientName, sa.postCode, sa.roadAddress, sa.detailAddress, sa.recipientPhone " +
            "FROM Product p JOIN p.user u JOIN u.shippingAddresses sa " +
            "WHERE u.serialNumber = :serialNumber AND p.productId = :productId")
    List<Object[]> findByProductId(@Param("serialNumber") String serialNumber, @Param("productId") String productId);


}
