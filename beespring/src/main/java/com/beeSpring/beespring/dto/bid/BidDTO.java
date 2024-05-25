package com.beeSpring.beespring.dto.bid;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidDTO {
    private String productId;
    private String sellerId;
    private String productName;
    private String productInfo;
    private int price;
    private String image1;
    private LocalDateTime deadline;
    private int currentPrice;
    private String nickName;

}

