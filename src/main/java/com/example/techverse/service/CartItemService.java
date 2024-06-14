package com.example.techverse.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.Model.Cart;
import com.example.techverse.Model.CartItem;
import com.example.techverse.Model.Product;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.CartItemRepository;
import com.example.techverse.Repository.CartRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.CartItemException;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.exception.UserException;

 

@Service
public class CartItemService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	public CartItem createCartitem(CartItem cartItem) {
		 
		cartItem.setQuantity(1);
		Product p=cartItem.getProduct();
		 
		cartItem.setPrice((long) (cartItem.getProduct().getPrice()*cartItem.getQuantity()));
		 
		cartItem.setDiscountedPrice(0L*cartItem.getQuantity());
		
		
		CartItem createdCartItem=cartItemRepository.save(cartItem);
		
		return createdCartItem;
		
		
		
	}

 
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException,UnauthorizedAccessException  {
		 
		CartItem item=findCartItemById(id);
		 User user = userRepository.findById(item.getUserId()).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
	       
		 
		
		if(user.getId().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setPrice((long) (item.getQuantity()*item.getProduct().getPrice()));
			item.setDiscountedPrice(0L*item.getQuantity());
			
		}
		
		
		return cartItemRepository.save(item);
	}

	 
	public CartItem isCartItemExist(Cart cart, Product product,  Long userId) {
		 
		CartItem cartItem=cartItemRepository.isCartItemExist(cart, product, userId);
		
		
		
		return cartItem;
	}
 
	public void removeCartItem(User reqUser, Long cartItemId) throws CartItemException,UnauthorizedAccessException,UserException {
		
		CartItem cartItem=findCartItemById(cartItemId);
		
		 User user = userRepository.findById(cartItem.getUserId()).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
		    	
System.out.println(cartItem.getUserId());
		
		if(user.getId().equals(reqUser.getId())) {
			cartItemRepository.deleteById(cartItemId);
		}
		else {
			throw new UserException("you cant remove another users item");
		}
		
	}

	 
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem> cartItem=cartItemRepository.findById(cartItemId);
		
		if(cartItem.isPresent()) {
			return cartItem.get();
		}
		
		throw new CartItemException("cart item not found with id "+cartItemId);
	}

}
