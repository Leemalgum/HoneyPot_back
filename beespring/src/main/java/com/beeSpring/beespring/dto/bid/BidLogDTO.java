package com.beeSpring.beespring.dto.bid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BidLogDTO {
    private Integer bidId;
    private Integer productId;
    private Integer userId;
    private int price;
    private LocalDateTime bidTime;
}
