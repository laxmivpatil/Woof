package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.techverse.Model.Cart;
import com.example.techverse.Model.CartItem;
import com.example.techverse.Model.Product;

 

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
	
	@Query("SELECT ci from CartItem ci Where ci.cart=:cart And ci.product=:product And ci.userId=:userId")
	public CartItem isCartItemExist(@Param("cart")Cart cart,@Param("product")Product product,
			@Param("userId")Long userId);

}