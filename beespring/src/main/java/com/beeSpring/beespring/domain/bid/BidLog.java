package com.beeSpring.beespring.domain.bid;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Bid_log")
@Getter
@NoArgsConstructor
public class BidLog {
    @Id
    @Column(name = "bid_log_id")
    private Integer bidLogId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "price")
    private int price;

    @Column(name = "bid_time")
    private LocalDateTime bidTime;
}
