package com.beeSpring.beespring.dto.shipping;

import com.beeSpring.beespring.domain.bid.BidResult;
import com.beeSpring.beespring.domain.shipping.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingDTO {
    private String shippingId;
    private BidResult bidResult;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime deliverTime;
    private Boolean orderConfirm;
}
