package com.example.techverse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.Model.OrderItem;
import com.example.techverse.Repository.OrderItemRepository;

 

@Service
public class OrderItemService  {
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	 
	public OrderItem createOrderItem(OrderItem orderItem) {
		 
		return orderItemRepository.save(orderItem);
		 
	}
	
	

}