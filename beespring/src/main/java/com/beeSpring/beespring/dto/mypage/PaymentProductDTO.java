package com.beeSpring.beespring.dto.mypage;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProductDTO {
    private String productId;
    private String serialNumber;
    private String productName;
    private int priceUnit;
    private int bidCnt;
    private int startPrice;
    private String recipientName;
    private String postCode;
    private String roadAddress;
    private String detailAddress;
    private String recipientPhone;
}
