package com.beeSpring.beespring.domain.payment;

import com.beeSpring.beespring.dto.payment.PaymentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @Column(name = "order_id")
    private String merchantUid;

    @Column(name = "serialNumber")
    private String serialNumber;

    @Column(name = "pg")
    private String pg;

    @Column(name = "payMethod")
    private String payMethod;

    @Column(name = "escrow")
    private boolean escrow;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private int amount;

    @Column(name = "buyerName")
    private String buyerName;


    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "status")
    private String status;

    @Column(name = "success")
    private boolean success;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @Column(name = "product_Id")  // Ensure this is the correct column name
    private String productId;

    public Payment(PaymentDTO paymentDTO) {
        this.merchantUid = paymentDTO.getMerchantUid();
        this.serialNumber = paymentDTO.getSerialNumber();
        this.pg = paymentDTO.getPg();
        this.payMethod = paymentDTO.getPayMethod();
        this.escrow = paymentDTO.isEscrow();
        this.name = paymentDTO.getName();
        this.amount = paymentDTO.getAmount();
        this.buyerName = paymentDTO.getBuyerName();
        this.dateAdded = paymentDTO.getDateAdded();
        this.status = paymentDTO.getStatus();
        this.success = paymentDTO.isSuccess();
        this.receiptUrl = paymentDTO.getReceiptUrl();
        this.productId = paymentDTO.getProductId();
    }



}

