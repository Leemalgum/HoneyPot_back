package com.beeSpring.beespring.repository.shipping;

import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    @Query("SELECT sa FROM ShippingAddress sa JOIN sa.user u WHERE u.serialNumber = :serialNumber")
    List<ShippingAddress> findBySerialNumber(@Param("serialNumber") String serialNumber);


}