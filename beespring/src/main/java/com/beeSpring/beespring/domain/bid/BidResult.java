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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidResultId;

    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "payment_status")
    private int paymentStatus;

    @Column(name = "result")
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

    public BidResult(Product product, int paymentStatus, LocalDateTime endTime, LocalDateTime enrolledTime, String customerId, BidResultStatus result) {

        this.product = product;
        this.paymentStatus = paymentStatus;
        this.endTime = endTime;
        this.enrolledTime = enrolledTime;
        this.customerId = customerId;
        this.result = result;

    }

    @Override
    public String toString() {
        return "BidResult{" +
                "bidResultId=" + bidResultId +
                ", orderId='" + orderId + '\'' +
                ", product=" + product +
                ", paymentStatus=" + paymentStatus +
                ", result=" + result +
                ", endTime=" + endTime +
                ", modTime=" + modTime +
                ", completeDate=" + completeDate +
                ", enrolledTime=" + enrolledTime +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
