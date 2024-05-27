package com.beeSpring.beespring.dto.payment;

import lombok.Data;

@Data
public class PaymentDTO {
    private String order_id;
    private String pg;
    private String payMethod;
    private boolean escrow;
    private String vbankDue; // 가상계좌 입금 기간 설정(필요x)
    private String bizNum;
    private String quota;
    private String merchantUid;
    private String name;
    private int amount;
    private String buyerName;
    private String buyerPhone;
    private String buyerEmail;
    private String buyerAddr;
    private String buyerPostcode;

    private String imp_uid;
    private String paid_amount;
    private String paid_at;
    private String pay_method;
    private String status;
    private String receipt_url;
    private String pg_provider;
    private String pg_tid;
    private String card_name;
    private String card_number;

}
