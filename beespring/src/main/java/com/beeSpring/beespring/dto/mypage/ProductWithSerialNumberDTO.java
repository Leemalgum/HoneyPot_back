package com.beeSpring.beespring.dto.mypage;

import com.beeSpring.beespring.domain.bid.StorageStatus;
import com.beeSpring.beespring.domain.shipping.DeliveryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    private Integer price;
    // 배송 상태를 문자열로 반환하는 메서드, null 검사를 추가함
    public String getDeliveryStatusAsString() {
        return deliveryStatus != null ? deliveryStatus.toString() : "N/A";
    }
}
