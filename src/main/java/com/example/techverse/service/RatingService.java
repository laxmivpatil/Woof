package com.example.techverse.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.DTO.RatingRequest;
import com.example.techverse.Model.Product;
import com.example.techverse.Model.Rating;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.ProductRepository;
import com.example.techverse.Repository.RatingRepository;
import com.example.techverse.exception.ProductException;
 
 

@Service
public class RatingService  {

	@Autowired
	private RatingRepository ratingRepository;
	

    @Autowired
    private ProductRepository productRepository;
	
	@Autowired
	private ProductService productService;
	
	 
	public Rating createRating(RatingRequest req, User user) throws ProductException {
	    Product product = productService.findProductById(req.getProductId());
	    
	    // Check if the user has already rated the product
	    Optional<Rating> existingRating = ratingRepository.findByUserAndProduct(user, product);
	    
	    if (existingRating.isPresent()) {
	        // Update existing rating
	        Rating ratingToUpdate = existingRating.get();
	        ratingToUpdate.setRating(req.getRating());
	        ratingToUpdate.setCreatedAt(LocalDateTime.now());
	        ratingRepository.save(ratingToUpdate);
	    } else {
	        // Create new rating
	        Rating newRating = new Rating();
	        newRating.setProduct(product);
	        newRating.setUser(user);
	        newRating.setRating(req.getRating());
	        newRating.setCreatedAt(LocalDateTime.now());
	        ratingRepository.save(newRating);
	    }

	    // Update and save average rating for the product
	    double averageRating = ratingRepository.getAverageRatingForProduct(req.getProductId());
	    product.setAverageRating(averageRating);
	    productRepository.save(product);

	    return ratingRepository.findByUserAndProduct(user, product).orElseThrow(() -> new ProductException("Failed to create/update rating"));
	}


	 
	public List<Rating> getProductsRating(Long productId) {
	    return ratingRepository.getAllProductsRatings(productId);
	}

	public Double calculateAverageRating(Long productId) {
	    return ratingRepository.getAverageRatingForProduct(productId);
	}

	 
}