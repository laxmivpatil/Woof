package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.techverse.Model.ShippingAddress;

 

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
}