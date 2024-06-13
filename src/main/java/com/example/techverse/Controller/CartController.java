package com.example.techverse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.DTO.ApiResponse1;
import com.example.techverse.Model.AddItemRequest;
import com.example.techverse.Model.Cart;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.ProductException;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.service.CartService;
import com.example.techverse.service.UserService;

 

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/{entityType}/{entityId}")
	public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt,@PathVariable String entityType, @PathVariable Long entityId) throws UnauthorizedAccessException{
	    User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
        
		Cart cart= cartService.findUserCart(user.getId());
		 return new ResponseEntity<Cart>(cart,HttpStatus.OK);
	}
	
	@PutMapping("/add/{entityType}/{entityId}")
	public ResponseEntity<ApiResponse1> addItemToCart(@RequestBody AddItemRequest req,
			@RequestHeader("Authorization") String jwt,@PathVariable String entityType, @PathVariable Long entityId) throws UnauthorizedAccessException,ProductException{
		 User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
	         cartService.addCartItem(user.getId(), req);
		  ApiResponse1 res=new ApiResponse1();
			 res.setMessage("Item added to cart successfully");
			 res.setStatus(true);
			 return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	
	
	
}