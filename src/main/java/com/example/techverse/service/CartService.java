package com.example.techverse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.Model.AddItemRequest;
import com.example.techverse.Model.Cart;
import com.example.techverse.Model.CartItem;
import com.example.techverse.Model.Product;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.CartRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.ProductException;

 

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ProductService productService;  
	@Autowired
	private UserRepository userRepository;
	
	 
	public Cart createCart(User user) {   
		
		Cart cart=new Cart();
		cart.setUser(user);
		
		
		return cartRepository.save(cart);
	}

	 
	public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
		Cart cart=cartRepository.findByUserId(userId);
		
		Product product=productService.findProductById(req.getProductId());
		
		CartItem isPresent=cartItemService.isCartItemExist(cart, product, req.getSize(), userId);
		
		if(isPresent==null) {
			CartItem cartItem=new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
			Long price=0L;
			 
				  price= (long) (req.getQuantity()*product.getPrice());
				 
			cartItem.setPrice(price);
			cartItem.setSize("");
			
			CartItem createdCartItem=cartItemService.createCartitem(cartItem);
			cart.getCartItems().add(createdCartItem);
			
		}
		
		
		return "Item Add To Cart";
	}

 
	public Cart findUserCart(Long userId) { 
		
		Cart cart=cartRepository.findByUserId(userId);
		
		 if(cart==null) {
			 cart=createCart(userRepository.getById(userId));
		 }
		
		Long totalPrice=0L;
		Long totalDiscountedPrice=0L;
		int totalItem=0;
		
		for(CartItem cartItem:cart.getCartItems()) {
			totalPrice =totalPrice+cartItem.getPrice();
			totalDiscountedPrice =0L;
			totalItem =totalItem+cartItem.getQuantity();
		}
		
		
		cart.setTotalDicountedPrice(totalDiscountedPrice);
		cart.setTotalItem(totalItem);
		cart.setTotalPrice(totalPrice);
		cart.setDiscounte(totalPrice-totalDiscountedPrice);
		
		
		return cartRepository.save(cart);
	}

}
