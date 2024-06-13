package com.example.techverse.Controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
	
	/*
	@Autowired
	private UserService userService;
	

	@Autowired
	private RatingService ratingService;
	
	
	@PostMapping("/create")
	public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,@RequestHeader("Authorization") String jwt)throws UserException,ProductException{
		
		User user =userService.findUserProfileByJwt(jwt).get();
		Rating rating=ratingService.createRating(req, user);
		
		 return new ResponseEntity<Rating>(rating,HttpStatus.OK);
		
		
	}
	@GetMapping("/get")
	public ResponseEntity<Map<String, Object>> getProductsRatingWithCounts(
	        @RequestParam Long productId,
	        @RequestHeader("Authorization") String jwt
	) throws UserException, ProductException {
	    User user = userService.findUserProfileByJwt(jwt).orElseThrow(() -> new UserException("User not found"));

	  //  List<Rating> ratings = ratingService.getProductsRating(productId);
	    Double averageRating = ratingService.calculateAverageRating(productId);

	    // Ensure that ratingCounts is of type Map<Integer, Long>
	   
	    Map<String, Object> response = new HashMap<>();
	  //  response.put("ratings", ratings);
	    response.put("averageRating", averageRating);
	    

	    return ResponseEntity.ok(response);
	}
*/
}
