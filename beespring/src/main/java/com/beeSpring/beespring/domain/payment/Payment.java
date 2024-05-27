package com.beeSpring.beespring.domain.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment", catalog = "BeeSpringDB")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private String order_id;

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

    private String imp_uid;
    private String paid_amount;
    private String paid_at;
//    private String pay_method;
    private String status;
    private String receipt_url;
    private String pg_provider;
    private String pg_tid;
    private String card_name;
    private String card_number;

}