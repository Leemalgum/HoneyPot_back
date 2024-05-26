package com.beeSpring.beespring.dto.bid;

import com.beeSpring.beespring.domain.bid.BidResultStatus;
import com.beeSpring.beespring.domain.bid.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BidResultDTO {
    private Long bidResultId;
    private String orderId;
    private Product product;
    private int paymentStatus;
    private BidResultStatus result;
    private LocalDateTime endTime;
    private LocalDateTime modTime;
    private LocalDateTime completeDate;
    private LocalDateTime enrolledTime;
    private String customerId;

    public BidResultDTO() {
    }
}