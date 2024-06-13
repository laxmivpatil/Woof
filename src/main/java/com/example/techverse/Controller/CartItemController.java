package com.example.techverse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.DTO.ApiResponse1;
import com.example.techverse.Model.CartItem;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.CartItemException;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.exception.UserException;
import com.example.techverse.service.CartItemService;
import com.example.techverse.service.UserService;

 

@RestController
@RequestMapping("api/cart_items")
public class CartItemController {
	
	
	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	
	
	@DeleteMapping("/{cartItemId}/{entityType}/{entityId}")
	public ResponseEntity<ApiResponse1> deleteCartItem(@PathVariable Long cartItemId,
			@RequestHeader("Authorization") String jwt,@PathVariable String entityType, @PathVariable Long entityId) throws UserException, UnauthorizedAccessException,CartItemException{
	    User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found")); 
				
		 
		 
				cartItemService.removeCartItem(user, cartItemId);
				 ApiResponse1 res=new ApiResponse1();
				 res.setMessage("Item deleted from cart successfully");
				 res.setStatus(true);
				 return new ResponseEntity<>(res,HttpStatus.OK);
				
			}
	@PutMapping("/cart/{cartItemId}/{entityType}/{entityId}")
	public ResponseEntity<CartItem> updateCartItem(@PathVariable Long cartItemId,
			@RequestBody CartItem cartItem,
			@RequestHeader("Authorization") String jwt,@PathVariable String entityType, @PathVariable Long entityId)throws UnauthorizedAccessException,CartItemException{
				
				
		 User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found")); 
			
			
				CartItem updatedCartItem=cartItemService.updateCartItem(user.getId(),cartItemId, cartItem);
				 
				 return new ResponseEntity<>(updatedCartItem,HttpStatus.OK);
				
			}
	
	

}