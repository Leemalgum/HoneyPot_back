package com.beeSpring.beespring.dto.bid;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductDTO {
    private String productId;
    private int idolId;
    private int ptypeId;
    private String serialNumber;
    private String productName;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String productInfo;
    private int price;
    private int priceUnit;
    private int buyNow;
    private LocalDateTime timeLimit;
    private long view;
    private int startPrice;
    private LocalDateTime registrationDate;
    private int bidCnt;
    private String requestId;
    private LocalDateTime requestTime;
    private String storageStatus;
}

