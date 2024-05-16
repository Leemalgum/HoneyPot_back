package com.beeSpring.beespring.domain.shipping;


import com.beeSpring.beespring.domain.bid.BidResult;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Shipping")
public class Shipping {
    @Id
    @Column(name = "shipping_id")
    private String shippingId;

    @ManyToOne
    @JoinColumn(name = "bid_result_id")
    private BidResult bidResult;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "deliver_time")
    private LocalDateTime deliverTime;
}
