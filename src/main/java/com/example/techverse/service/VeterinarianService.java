package com.example.techverse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.techverse.JwtUtil;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.VeterinarianRepository;

@Service
public class VeterinarianService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
   private JwtUtil jwtUtil;
	
	private final EmailService emailService;

	private final VeterinarianRepository veterinarianRepository;

	public VeterinarianService(VeterinarianRepository veterinarianRepository, EmailService emailService ) {
		this.emailService = emailService;
		this.veterinarianRepository =veterinarianRepository;
		 
	}
	 
	 	
	public Veterinarian registerVeterinarian(String email,String phone,String password,String fullname,String role){

		String hashedPassword =passwordEncoder.encode(password);
		Veterinarian veterinarian=new Veterinarian(email,phone,hashedPassword,fullname,role);
		List<String> lastThreePasswords = veterinarian.getLastThreePasswords();
				 if (lastThreePasswords == null) {
				        lastThreePasswords = new ArrayList<>();
				    }
				    lastThreePasswords.add(hashedPassword);
				    if (lastThreePasswords.size() > 3) {
				        lastThreePasswords.remove(0); // Remove the oldest password
				    }

				    veterinarian.setLastThreePasswords(lastThreePasswords);
				  
		return veterinarianRepository.save(veterinarian);

		 
	}
 
	 public Optional<Veterinarian> generateAndSaveToken(Optional<Veterinarian>  veterinarianOptional){
		 String token = jwtUtil.generateToken(veterinarianOptional.get().getEmail());
			System.out.println("token "+token);
		
			System.out.println("token extract "+jwtUtil.extractUsername(token));
			 veterinarianOptional.get().setToken(token);
			 veterinarianRepository.save(veterinarianOptional.get());
			return  veterinarianOptional;

	 }
	 
	 public  Veterinarian generateAndSaveToken1( Veterinarian   veterinarian ){
		 String token = jwtUtil.generateToken(veterinarian.getEmail());
			System.out.println("token "+token);
		
			System.out.println("token extract "+jwtUtil.extractUsername(token));
			 veterinarian.setToken(token);
			 veterinarianRepository.save(veterinarian );
			return  veterinarian;

	 }

	 /*
	 public ResponseEntity<Map<String, Object>> loginUserByPassword(Optional<User> userOptional,String password) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean passwordMatches = encoder.matches(password,userOptional.get().getPassword());

		if( passwordMatches) {
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
*/
	 
	
	 
	
}
