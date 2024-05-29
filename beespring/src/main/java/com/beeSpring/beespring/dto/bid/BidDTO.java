package com.beeSpring.beespring.dto.bid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
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

