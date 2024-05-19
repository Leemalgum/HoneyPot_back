package com.beeSpring.beespring.domain.bid;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "Product")
@Getter
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "product_id", updatable = false, nullable = false)
    private String productId;

    @Column(name = "idol_id")
    private int idolId;

    @Column(name = "ptype_id")
    private int ptypeId;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "image1")
    private String image1;

    @Column(name = "image2")
    private String image2;

    @Column(name = "image3")
    private String image3;

    @Column(name = "image4")
    private String image4;

    @Column(name = "image5")
    private String image5;

    @Column(name = "product_info")
    private String productInfo;

    @Column(name = "price")
    private int price;

    @Column(name = "price_unit")
    private Integer priceUnit;

    @Column(name = "buy_now")
    private Integer buyNow;

    @Column(name = "time_limit")
    private int timeLimit;

    @Column(name = "view")
    private Long view;

    @Column(name = "start_price")
    private int startPrice;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "bid_cnt")
    private int bidCnt;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Column(name = "storage_status")
    private String storageStatus;

    public Product() {

    }
}