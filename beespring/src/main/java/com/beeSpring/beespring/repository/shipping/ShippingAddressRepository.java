package com.beeSpring.beespring.repository.shipping;

import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    List<ShippingAddress> findBySerialNumber(Long serialNumber);

}