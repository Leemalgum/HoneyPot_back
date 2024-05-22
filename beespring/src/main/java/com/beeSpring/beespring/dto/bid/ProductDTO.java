package com.beeSpring.beespring.dto.bid;

import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.bid.StorageStatus;
import com.beeSpring.beespring.domain.category.Idol;
import com.beeSpring.beespring.domain.category.ProductType;
import com.beeSpring.beespring.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductDTO {
    private String productId;
    private int idolId;
    private int ptypeId;
    private String serialNumber;
    private String productName;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String productInfo;
    private int price;
    private Integer priceUnit;
    private int buyNow;
    private int timeLimit;
    private long view;
    private int startPrice;
    private LocalDateTime registrationDate;
    private int bidCnt;
    private LocalDateTime requestTime;
    private String storageStatus;

    public ProductDTO() {

    }

    //dto -> entity
    public Product toEntity(){
        return Product.builder()
                .productId(productId)
                .idol(Idol.builder().idolId(idolId).build())
                .productType(ProductType.builder().ptypeId(ptypeId).build())
                .user(User.builder().serialNumber(serialNumber).build())
                .productName(productName)
                .image1(image1)
                .image2(image2)
                .image3(image3)
                .image4(image4)
                .image5(image5)
                .productInfo(productInfo)
                .price(price)
                .priceUnit(priceUnit)
                .buyNow(buyNow)
                .timeLimit(timeLimit)
                .view(view)
                .startPrice(startPrice)
                .registrationDate(registrationDate)
                .bidCnt(bidCnt)
                .requestTime(requestTime)
                .storageStatus(StorageStatus.valueOf(storageStatus))
                .build();
    }
}

