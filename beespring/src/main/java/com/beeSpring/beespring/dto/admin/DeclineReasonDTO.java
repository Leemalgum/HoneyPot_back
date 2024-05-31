package com.beeSpring.beespring.dto.admin;

import lombok.Data;

@Data
public class DeclineReasonDTO {
    private String productId;
    private String serialNumber;
    private String productName;
    private String declineReason;
    private String storageStatus;
}
