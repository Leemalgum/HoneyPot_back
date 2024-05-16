package com.beeSpring.beespring.dto.mypage;

import com.beeSpring.beespring.domain.shipping.DeliveryStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithSerialNumberDTO {
    private String productId;
    private int paymentStatus;
    private DeliveryStatus deliveryStatus;
    private String serialNumber;
    private String productName;
    private String image1;
    private Integer priceUnit;
    private int startPrice;
    private int bidCnt;
    public String getDeliveryStatusAsString() {
        return deliveryStatus.toString();
    }
}
