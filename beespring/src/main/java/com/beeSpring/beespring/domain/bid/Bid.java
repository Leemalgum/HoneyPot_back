package com.beeSpring.beespring.domain.bid;

import com.beeSpring.beespring.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Bid")
@Getter
@NoArgsConstructor
public class Bid {
    @Id
    @Column(name = "bid_id")
    private Integer bidId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "serial_number")
    private User user;

    @Column(name = "price")
    private int price;

    @Column(name = "bid_time")
    private LocalDateTime bidTime;

    public Bid(Product product, User user, int price) {
        this.product = product;
        this.user = user;
        this.price = price;
        this.bidTime = LocalDateTime.now();
    }
}