package com.beeSpring.beespring.domain.bid;

import com.beeSpring.beespring.domain.category.Idol;
import com.beeSpring.beespring.domain.category.ProductType;
import com.beeSpring.beespring.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "Product")
@Getter
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "product_id", updatable = false, nullable = false)
    private String productId;

    @ManyToOne
    @JoinColumn(name = "idol_id", nullable = false)
    private Idol idol;

    @ManyToOne
    @JoinColumn(name = "ptype_id", nullable = false)
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "serial_number", nullable = false)
    private User user;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "image1")
    private String image1;

    @Column(name = "image2")
    private String image2;

    @Column(name = "image3")
    private String image3;

    @Column(name = "image4")
    private String image4;

    @Column(name = "image5")
    private String image5;

    @Column(name = "product_info")
    private String productInfo;

    @Column(name = "price")
    private int price;

    @Column(name = "price_unit")
    private Integer priceUnit;

    @Column(name = "buy_now")
    private Integer buyNow;

    @Column(name = "time_limit")
    private int timeLimit;

    @Column(name = "view")
    private Long view;

    @Column(name = "start_price")
    private int startPrice;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "bid_cnt")
    private int bidCnt;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_status")
    private StorageStatus storageStatus;

    public Product() {

    }

    // Setter for storageStatus
    public void updateStorageStatus(StorageStatus storageStatus) {
        this.storageStatus = storageStatus;
    }
}
