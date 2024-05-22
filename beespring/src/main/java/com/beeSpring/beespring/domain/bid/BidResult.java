package com.beeSpring.beespring.domain.bid;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Bid_result")
@Getter
@NoArgsConstructor
public class BidResult {
    @Id
    @Column(name = "bid_result_id")
    private Long bidResultId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "payment_status")
    private int paymentStatus;

    @Enumerated(EnumType.STRING)
    private BidResultStatus result;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "mod_time")
    private LocalDateTime modTime;

    @Column(name = "complete_date")
    private LocalDateTime completeDate;

    @Column(name = "enrolled_time")
    private LocalDateTime enrolledTime;

    @Column(name = "customer_id")
    private String customerId;

}
