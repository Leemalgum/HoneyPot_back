package com.beeSpring.beespring.domain.shipping;

import com.beeSpring.beespring.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Shipping_address")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_phone")
    private String recipientPhone;

    @Column(name = "road_address")
    private String roadAddress;

//    @Column(name = "serial_number")
//    private Long serialNumber;

    @ManyToOne
    @JoinColumn(name = "serial_number", nullable = false)
    private User user;

}

