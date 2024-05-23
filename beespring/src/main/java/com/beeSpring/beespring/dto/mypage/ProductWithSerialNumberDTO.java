package com.beeSpring.beespring.dto.mypage;

import com.beeSpring.beespring.domain.bid.StorageStatus;
import com.beeSpring.beespring.domain.shipping.DeliveryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithSerialNumberDTO {
    private String productId;
    private Integer paymentStatus;
    private LocalDateTime completeDate;
    private DeliveryStatus deliveryStatus;
    private String serialNumber;
    private String productName;
    private String image1;
    private Integer priceUnit;
    private Integer startPrice;
    private Integer bidCnt;
    private String nickName;
    private StorageStatus storageStatus;
    public String getDeliveryStatusAsString() {
        return deliveryStatus.toString();
    }
}
