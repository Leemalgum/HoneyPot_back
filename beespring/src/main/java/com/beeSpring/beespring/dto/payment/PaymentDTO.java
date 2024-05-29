package com.beeSpring.beespring.dto.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private String merchantUid; // order_id
    private String serialNumber;
    private String pg;
    private String payMethod;
    private boolean escrow;
    private String name; // 상품이름
    private int amount;
    private String buyerName;
    private LocalDate dateAdded;
    private String status;
    private boolean success;
    private String receiptUrl;
    private String productId;

    public PaymentDTO() {

    }
}
