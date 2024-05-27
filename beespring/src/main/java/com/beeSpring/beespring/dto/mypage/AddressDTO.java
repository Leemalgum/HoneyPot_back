package com.beeSpring.beespring.dto.mypage;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long addressId;
    private String addressName;
    private String recipientName;
    private String recipientPhone;
    private String postCode;
    private String roadAddress;
    private String detailAddress;
}
