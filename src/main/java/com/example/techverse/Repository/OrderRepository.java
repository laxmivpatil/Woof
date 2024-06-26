package com.example.techverse.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.techverse.Model.Order;

 

public interface OrderRepository extends JpaRepository<Order,String>{
	
	
	@Query("SELECT o FROM Order o WHERE o.user.id=:userId AND(o.orderStatus='PLACED' OR o.orderStatus='CONFIRMED' OR o.orderStatus='SHIPPED' OR o.orderStatus='DELIVERED' )")
	public List<Order> getUsersOreders(@Param("userId") Long userId);
	

}
