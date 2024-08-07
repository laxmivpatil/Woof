package com.example.techverse.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.DTO.ApiResponse1;
import com.example.techverse.DTO.SavedPostResponseDTO;
import com.example.techverse.Model.Product;
import com.example.techverse.Model.SavedPost;
import com.example.techverse.Model.Story;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.SavedPostRepository;
import com.example.techverse.Repository.StoryRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.ProductException;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.exception.UserException;
import com.example.techverse.service.CartService;
import com.example.techverse.service.ProductService;
import com.example.techverse.service.UserService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageOptions;
 
@RestController
@RequestMapping("")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProductService productService;
	@Autowired
	StoryRepository storyRepository;
	@Autowired
	SavedPostRepository savedPostRepository;
	
	 @Autowired
	    private Storage storage; // Inject the Google Cloud Storage client

	 @DeleteMapping("/users/delete")
		public ResponseEntity<Map<String, Object>> deleteuser(@RequestParam(required = false) Long id)
		{
		 Map<String, Object> responseBody = new HashMap<>();
	        
			Optional<User> user=userRepository.findById(id);
			if(user.isPresent())
			{
				userRepository.delete(user.get());
				responseBody.put("success", true);
	            responseBody.put("message", "User deleted");
	            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
			}
			      responseBody.put("success", false);
	            responseBody.put("message", "User not found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);

		}
	 
	 
	 
	 
	@GetMapping("/users/profile")
	public ResponseEntity<User> getUserProfile(@RequestParam(required = false) Long id)
	{
		Optional<User> user=userRepository.findById(id);
        return ResponseEntity.ok(user.get());

	}

	@PostMapping("/users/save/{story_id}")
	public ResponseEntity<String> savePost(
	        @PathVariable String story_id,
	        @RequestBody String userId) {

	    // Authenticate and authorize the user (ensure they have permissions to save a post)
        Long userId1 = Long.parseLong(userId);
        Long postId1 = Long.parseLong(story_id);

	    // Retrieve the post by ID
	    Optional<Story> optionalPost = storyRepository.findById(postId1);

	    if (optionalPost.isPresent()) {
	        Story post = optionalPost.get();

	        // Create a new SavedPost object
	        SavedPost savedPost = new SavedPost();
	        savedPost.setUser(userRepository.findById(userId1).orElse(null)); // Set the current user

	        // Set the post only if the user is valid
	        if (savedPost.getUser() != null) {
	            savedPost.setPost(post);

	            // Save the saved post
	            savedPostRepository.save(savedPost);

	            return ResponseEntity.ok("Post saved successfully.");
	        } else {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid user.");
	        }
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	/*
	@GetMapping("/saved-posts")
	public ResponseEntity<List<SavedPostResponseDTO>> getSavedPostsOfOtherUsers(@RequestParam Long userId) {
	    try {
	        // Authenticate and authorize the user (ensure they have permissions to access saved posts)

	        // Retrieve the list of saved posts for the specified user ID
	        List<SavedPost> savedPosts = savedPostRepository.findByUserId(userId);

	        if (savedPosts.isEmpty()) {
	            // If the list is empty, return an empty response
	            return ResponseEntity.ok(Collections.emptyList());
	        }

	        // Convert savedPosts to the desired response format
	        List<SavedPostResponseDTO> response = savedPosts.stream()
	            .map(savedPost -> {
	                SavedPostResponseDTO dto = new SavedPostResponseDTO();
	                dto.setPost_id(savedPost.getPost().getId());
	                dto.setUser_id(savedPost.getPost().getCreator().getId());
	                dto.setEmail(savedPost.getPost().getCreator().getEmail());
	                dto.setContent(savedPost.getPost().getMediaUrl());
	                dto.setTimestamp(savedPost.getPost().getCreatedAt().toString());
	                return dto;
	            })
	            .collect(Collectors.toList());

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        // Handle any exceptions that occur internally
	        // You can log the exception for debugging purposes
	        e.printStackTrace();
	        
	        // Return an appropriate error response
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}

	*/
	
	
	//cloud
	
	@PostMapping("/users/upload-image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) {

        try {
            // Generate a unique file name or use the user ID
            String fileName = "user_" + userId + "_" + UUID.randomUUID().toString() + ".jpg";

            // Define the storage class (e.g., STANDARD, NEARLINE, COLDLINE)
            StorageClass storageClass = StorageClass.STANDARD;

            // Upload the file to Google Cloud Storage with the specified storage class
            BlobId blobId = BlobId.of("storage-woof", fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setStorageClass(storageClass).build();

            try (InputStream content = file.getInputStream()) {
                // Upload the file content
                Blob blob = getStorage().create(blobInfo, content);

                // Save the image URL in the database
                String imageUrl = blob.getMediaLink(); // Get the public URL of the uploaded file
                // Save imageUrl in the database against the user with userId

                return ResponseEntity.ok("Image uploaded successfully. Image URL: " + imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    private Storage getStorage() throws IOException {
        return StorageOptions.getDefaultInstance().getService();
    }
    
    

	@PostMapping("/api/user/favorite-products")
    public Map<String, Object> addFavoriteProduct(@RequestHeader("Authorization") String jwt, @RequestBody Map<String, Long> request)throws UserException,ProductException {
		Map<String,Object> response = new HashMap<>();
	       
		Long productId = request.get("productId");
		Optional<User> user =userRepository.findByToken(jwt.substring(7));
		if(user.isEmpty()) {
			 response.put("status", false);
		        response.put("message", "Invalid token");
		        return response;
		}
			userService.addFavoriteProduct(user.get().getId(), productId);
			 response.put("status", true);
        response.put("message", "product added to wishlist successfully");
        return response;
	    }
	
	@DeleteMapping("/api/user/favorite-products")
    public  Map<String, Object> deleteFavoriteProduct(@RequestHeader("Authorization") String jwt,  @RequestParam Long productId) throws UserException, ProductException {
       // User user = userService.findUserProfileByJwt(jwt).get();
		  Map<String,Object> response = new HashMap<>();
	      
		
		Optional<User> user =userRepository.findByToken(jwt.substring(7));
		if(user.isEmpty()) {
			 response.put("status", false);
		        response.put("message", "Invalid token");
		        return response;
		}
		userService.deleteFavoriteProduct(user.get().getId(), productId);
        response.put("status", true);
        response.put("message", "product delete from wishlist successfully");
        return response;
    }

    @GetMapping("/api/user/favorite-products")
    public Map<String,Object> getFavoriteProducts(@RequestHeader("Authorization") String jwt) throws UserException {
      //  User user = userService.findUserProfileByJwt(jwt).get();
	   
    	
    	
    	Map<String,Object> response = new HashMap<>();
    	
    	Optional<User> user =userRepository.findByToken(jwt.substring(7));
		if(user.isEmpty()) {
			 response.put("status", false);
		        response.put("message", "Invalid token");
		        return response;
		}
	        List<Product> product=userService.getFavoriteProducts(user.get().getId());
    	response.put("product", productService.setfavouriteStatus(user.get(), product));
		response.put("status", true);
        response.put("message", "product retrived Successfully");
	        return response; 
    
    }
    
    
    

	@PostMapping("/api/user/favorite-products/addtocart")
    public Map<String, Object> addFavoriteProducttocart(@RequestHeader("Authorization") String jwt, @RequestParam Long productId)throws UserException,ProductException {
		Map<String,Object> response = new HashMap<>();
	      
		//// User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
		// System.out.println(req.getQuantity()+"hi am");
		
		Optional<User> user =userRepository.findByToken(jwt.substring(7));
		if(user.isEmpty()) {
			 response.put("status", false);
		        response.put("message", "Invalid token");
		        return response;
		}
	         cartService.addCartItemfromwishlist(user.get().getId(),productId,1);
	         userService.deleteFavoriteProduct(user.get().getId(), productId);
		  
			 response.put("message","Item added to cart successfully");
			 response.put("status", false);
			 return response;
	    }
}
