package com.beeSpring.beespring.service.shipping;

import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.shipping.ShippingAddressDTO;
import com.beeSpring.beespring.repository.shipping.ShippingAddressRepository;
import com.beeSpring.beespring.repository.shipping.ShippingRepository;
import com.beeSpring.beespring.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ShippingServiceImpl implements ShippingService{

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ShippingAddress> getAllAddresses() {
        return shippingAddressRepository.findAll();
    }

    @Override
    public Optional<ShippingAddress> getAddressById(Long id) {
        return shippingAddressRepository.findById(id);
    }

    @Override
    public ShippingAddress saveAddress(ShippingAddressDTO addressDTO) {
        User user = userRepository.findById(addressDTO.getSerialNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user serial number"));
        ShippingAddress address = new ShippingAddress(
                addressDTO.getAddressId(),
                addressDTO.getAddressName(),
                addressDTO.getDetailAddress(),
                addressDTO.getPostCode(),
                addressDTO.getRecipientName(),
                addressDTO.getRecipientPhone(),
                addressDTO.getRoadAddress(),
//                addressDTO.getSerialNumber()
                user
        );
        return shippingAddressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long id) {
        shippingAddressRepository.deleteById(id);
    }

    @Override
    public List<ShippingAddress> getAddressesBySerialNumber(String serialNumber) {
        return shippingAddressRepository.findBySerialNumber(serialNumber);
    }

    @Override
    @Transactional
    public void updateOrderConfirm(String productId) {
        try {
            int bidResultId = shippingRepository.findBigResultId(productId);
            log.info("Update bidResultID is: " + bidResultId);
            shippingRepository.updateOrderConfirm(bidResultId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean getOrderConfirm(String productId) {
        try {
            int bidResultId = shippingRepository.findBigResultId(productId);
            log.info("Let's get OrderConfirm with bidResultID: " + bidResultId);
            boolean status = shippingRepository.findOrderConfirm(bidResultId);
            log.info("Here is OrderConfirm Status: " + status);
            return status;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
