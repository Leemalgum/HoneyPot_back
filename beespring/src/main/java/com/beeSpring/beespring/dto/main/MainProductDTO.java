package com.beeSpring.beespring.dto.main;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;



@Getter
@Setter
@AllArgsConstructor
@Builder
public class MainProductDTO {

    private String productId;

    private String productName;

    private String userId;

    private int idolId;

    private String idolName;

    private int ptypeId;

    private String ptypeName;

    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String productInfo;
    private String serialNumber;
    private Integer priceUnit;
    private int buyNow;
    private String storageStatus;
    private LocalDateTime requestTime;
    private int bidCnt;
    private int startPrice;
    private int price;
    private long view;
    private LocalDateTime timeLimit;
    private LocalDateTime registrationDate;


    public MainProductDTO() {}

}