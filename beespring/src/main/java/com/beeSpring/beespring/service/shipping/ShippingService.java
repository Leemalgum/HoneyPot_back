package com.beeSpring.beespring.service.shipping;

import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import com.beeSpring.beespring.dto.shipping.ShippingAddressDTO;

import java.util.List;
import java.util.Optional;

public interface ShippingService {
    Optional<ShippingAddress> getAddressById(Long id);

    ShippingAddress saveAddress(ShippingAddressDTO addressDTO);

    void deleteAddress(Long id);

    List<ShippingAddress> getAddressesBySerialNumber(Long serialNumber);
}
