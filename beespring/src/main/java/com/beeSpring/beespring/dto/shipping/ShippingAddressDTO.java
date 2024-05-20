package com.beeSpring.beespring.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddressDTO {

    private Long addressId;
    private String addressName;
    private String detailAddress;
    private String postCode;
    private String recipientName;
    private String recipientPhone;
    private String roadAddress;
    private Long serialNumber;
}
