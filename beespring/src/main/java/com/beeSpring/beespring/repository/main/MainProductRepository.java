package com.beeSpring.beespring.repository.main;

import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.category.Idol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainProductRepository  extends JpaRepository<Product, String> {

    //회원 serialNumber 불러오기


    //회원별 관심 idol 목록 불러오기
//    @Query("SELECT i.idolName FROM UserIdol ui JOIN Idol i ON ui.idolId = i.idolId JOIN User u ON ui.serialNumber = u.serialNumber")
//    List<String> findIdolName(String serialNumber);
////    List<String> findIdolsByUserId(@Param("serialNumber") String serialNumber);



    //idol별 상품 목록 불러오기
    @Query("SELECT p, i.idolName, t.ptypeName, u.userId " +
            "FROM Product p " +
            "JOIN Idol i ON p.idolId = i.idolId " +
            "JOIN ProductType t ON p.ptypeId  = t.ptypeId " +
            "JOIN UserIdol ui ON ui.idolId = p.idolId " +
            "JOIN User u ON ui.serialNumber = u.serialNumber " +
            "ORDER BY rand() DESC LIMIT 4")

//    List<Object[]> findByCategory(List<String> idolName);
    List<Object[]> findByCategory();


    @Query("SELECT p, i.idolName,  t.ptypeName, u.userId " +
            "            FROM Product p\n" +
            "            JOIN Idol i ON p.idolId = i.idolId\n" +
            "            JOIN ProductType t ON p.ptypeId  = t.ptypeId\n" +
            "            JOIN User u ON p.serialNumber  = u.serialNumber\n" +
            "            ORDER BY p.view DESC limit 12")

    List<Object[]> findByView();

    @Query("SELECT p, i.idolName,  t.ptypeName, u.userId " +
            "            FROM Product p\n" +
            "            JOIN Idol i ON p.idolId = i.idolId\n" +
            "            JOIN ProductType t ON p.ptypeId  = t.ptypeId\n" +
            "            JOIN User u ON p.serialNumber  = u.serialNumber\n" +
            "            ORDER BY p.timeLimit DESC limit 12")
    List<Object[]> findByDeadLine();


    @Query("SELECT p, i.idolName, t.ptypeName, u.userId "+
            "            FROM Product p\n" +
            "            JOIN Idol i ON p.idolId = i.idolId\n" +
            "            JOIN ProductType t ON p.ptypeId  = t.ptypeId\n" +
            "            JOIN User u ON p.serialNumber  = u.serialNumber\n" +
            "            ORDER BY p.registrationDate ASC limit 12")
    List<Object[]> findByLatest();


    @Query("SELECT u.userId " +
            "FROM Product p " +
            "JOIN User u ON p.serialNumber = u.serialNumber "+
            "WHERE p.productId = ?1"
    )
    String findUserIdByProductId(String productId);
}
