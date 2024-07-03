package com.example.techverse.service;

 
 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.techverse.EmailSender;
import com.example.techverse.JwtUtil;
import com.example.techverse.SmsSender;
import com.example.techverse.DTO.RegistrationDTO;
import com.example.techverse.Model.Product;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.ProductRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.ProductException;
import com.example.techverse.exception.UserAlreadyExistsException;
import com.example.techverse.exception.UserException;
 

@Service
public class UserService{
	@Autowired
	private ProductRepository productRepository;
 
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
    private JwtUtil jwtUtil;
	
	private final EmailService emailService;

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository, EmailService emailService ) {
		this.emailService = emailService;
		this.userRepository = userRepository;
		 
	}
	 
 	 	
	public User registerUser(String email,String phone,String password,String fullname,String role){

		String hashedPassword =passwordEncoder.encode(password);
		User user=new User(email,phone,hashedPassword,fullname,role);
		List<String> lastThreePasswords = user.getLastThreePasswords();
				 if (lastThreePasswords == null) {
				        lastThreePasswords = new ArrayList<>();
				    }
				    lastThreePasswords.add(hashedPassword);
				    if (lastThreePasswords.size() > 3) {
				        lastThreePasswords.remove(0); // Remove the oldest password
				    }
			return userRepository.save(user);

		 
	}

	 public Optional<User> generateAndSaveToken(Optional<User> userOptional){
		 String token = jwtUtil.generateToken(userOptional.get().getEmail());
			System.out.println("token "+token);
		
			System.out.println("token extract "+jwtUtil.extractUsername(token));
			userOptional.get().setToken(token);
			userRepository.save(userOptional.get());
			return userOptional;
	 }
	 
	 public User generateAndSaveToken1(User user){
		 String token = jwtUtil.generateToken(user.getEmail());
			System.out.println("token "+token);
			System.out.println("token extract "+jwtUtil.extractUsername(token));
			user.setToken(token);
			userRepository.save(user);
			return user;
	 }

/*	  with verified check
	 public ResponseEntity<Map<String, Object>> loginUserByPassword(Optional<User> userOptional,String password) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean passwordMatches = encoder.matches(password,userOptional.get().getPassword());

		if(passwordMatches) {
			if(userOptional.get().getVerification().equals("Verified")) {
				userOptional=generateAndSaveToken(userOptional);
			responseBody.put("success", true);
			responseBody.put("message", "User Login Successfully");
			responseBody.put("Token",userOptional.get().getToken());
			responseBody.put("userId", userOptional.get().getId());
		 	return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);	
			}
			else {
			responseBody.put("success", false);
			responseBody.put("message", "User Not Verified");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
			}

		}
		responseBody.put("success", false);
		responseBody.put("message", "Invalid Password");
		 
		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);	

 		
	}*/
	 public ResponseEntity<Map<String, Object>> loginUserByPassword(Optional<User> userOptional,String password) {
			Map<String, Object> responseBody = new HashMap<String, Object>();
			RegistrationDTO  dto=new RegistrationDTO();
			boolean passwordMatches = passwordEncoder.matches(password,userOptional.get().getPassword());
			 
			 
			if(passwordMatches) {
				 	userOptional=generateAndSaveToken(userOptional);
				responseBody.put("success", true);
				responseBody.put("message", "User Login Successfully");
				responseBody.put("Token",userOptional.get().getToken());
				  responseBody.put("profile",userOptional.get().getProfile());
				responseBody.put("user", dto.toDTO(userOptional.get()));
			 	return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);	
				 
			}
			responseBody.put("success", false);
			responseBody.put("message", "Invalid Password");
			 
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);	

	 		
		}
	 
 
	    public User addFavoriteProduct(Long userId, Long productId)  throws ProductException{
	        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
	        Optional<Product> product = productRepository.findById(productId);// Implement this method to get the product from repository
	        		
	        if(product.isEmpty())	{
	        	System.out.println("hi i am not ");
	        	throw new ProductException("No product found");
	        }
	        if (user.getFavoriteProducts().contains(product.get())) {
	            // Product already exists in favorites, handle this case as per your requirements
	            // For example, throw an exception, log a message, or return the user as-is
	            return user;
	        }
	        
	        user.getFavoriteProducts().add(product.get());
	        return userRepository.save(user);
	    }
		
		 
	    public User deleteFavoriteProduct(Long userId, Long productId) throws UserException, ProductException {
	        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
	        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductException("Product not found"));

	        if (!user.getFavoriteProducts().contains(product)) {
	            throw new ProductException("Product not found in user's favorites");
	        }

	        user.getFavoriteProducts().remove(product);
	        userRepository.save(user);

	        return user;
	    }

	    
	    public List<Product> getFavoriteProducts(Long userId) throws UserException {
	        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
	        return user.getFavoriteProducts();
	    }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 public List<User> getFollowersByUserId(Long userId) {
	        // Implement the logic to retrieve followers by user ID using Spring Data JPA
	        return userRepository.findFollowersByUserId(userId);
	        //userRepository.find
	    }
	 
	 public boolean unfollowUser(Long currentUserId, Long userToUnfollowId) {
	        // Implement the logic to unfollow a user
	        // You may need to perform validations and handle errors
	        // For example:
	        User currentUser = userRepository.findById(currentUserId).orElse(null);
	        User userToUnfollow = userRepository.findById(userToUnfollowId).orElse(null);

	        if (currentUser == null || userToUnfollow == null) {
	            return false; // User not found
	        }

	        //currentUser.removeFollower(userToUnfollow);
	        
	        userToUnfollow.removeFollower(currentUser);
	        userRepository.save(userToUnfollow);

	        return true;
	    }
	 
	 public boolean followUser(Long currentUserId, Long userToFollowId) {
	        // Implement the logic to follow a user
	        // You may need to perform validations and handle errors
	        // For example:
	        User currentUser = userRepository.findById(currentUserId).orElse(null);
	        User userToFollow = userRepository.findById(userToFollowId).orElse(null);

	        if (currentUser == null || userToFollow == null) {
	            return false; // User not found
	        }

	      //  currentUser.addFollower(userToFollow);
	        userToFollow.addFollower(currentUser);
	        userRepository.save(userToFollow);

	        return true;
	    }

	 
	
	 
	
	 
}