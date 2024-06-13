package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.OrderItem;
 
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	
	

}
