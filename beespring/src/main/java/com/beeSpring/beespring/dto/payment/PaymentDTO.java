package com.beeSpring.beespring.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {


    private String pg;
    private String payMethod;
    private boolean escrow;
    private String vbankDue;
    private String bizNum;
    private String quota;
    private String merchantUid;
    private String name;
    private String amount;
    private String buyerName;
    private String buyerPhone;
    private String buyerEmail;
    private String buyerAddr;
    private String buyerPostcode;

// Getters and setters
}