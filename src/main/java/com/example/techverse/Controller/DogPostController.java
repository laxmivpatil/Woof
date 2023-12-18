package com.example.techverse.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.Model.DogPost;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.service.DogPostService;
import com.example.techverse.service.StorageService;

@RestController
@RequestMapping("/api/dog-posts")
public class DogPostController {

    @Autowired
    private DogPostService dogPostService;
 /*   @Autowired
    private StorageService storageService;
*/
    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository for managing users

    @PostMapping("/post")
    public ResponseEntity<?> postDogPost(
    		@RequestParam("user_id") String user_id,
            @RequestParam("media") MultipartFile media,
            @RequestParam("caption") String caption,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude
            ) {
    	 System.out.println("hi i am here ");
    	 User currentUser = userRepository.findById(Long.parseLong(user_id)).orElse(null);
    	    if (currentUser == null) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    	    }

    	    try {
    	      //  String mediaUrl = storageService.uploadFile(media);
    	    	String mediaUrl = dogPostService.storeFile(media);
    	        DogPost dogPost = new DogPost();
    	        dogPost.setUser(currentUser);
    	        dogPost.setMediaUrl(mediaUrl);
    	        dogPost.setCaption(caption);
    	        dogPost.setLatitude(latitude);
    	        dogPost.setLongitude(longitude);

    	        DogPost savedPost = dogPostService.saveDogPost(dogPost);

    	        // Notify nearby NGOs and veterinary hospitals (implement this logic)

    	        // Create a map to represent the response JSON
    	        Map<String, Object> response = new HashMap<>();
    	        response.put("post_id", savedPost.getId().toString());
    	        response.put("success", true);

    	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    	    } catch (Exception e) {
    	        System.out.print(e);
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    	    }
    }
    
    
    @GetMapping("/fetch-nearby")
    public ResponseEntity<?> fetchNearbyOrganizations(
            @RequestParam(value = "user_id", required = false) String userId,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {

        // Validate input parameters (latitude, longitude, and radius can also be validated)
        if (latitude == null || longitude == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid latitude or longitude");
        }

        // Call the service to fetch nearby organizations (users with roles NGO and Hospital)
        List<User> nearbyOrganizations = dogPostService.findNearbyOrganizations(latitude, longitude, 5.0); // Example radius: 5.0 kilometers

        if (nearbyOrganizations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No nearby organizations found");
        }

        // Prepare the response format
        List<Map<String, Object>> responseOrganizations = new ArrayList<>();
        for (User user : nearbyOrganizations) {
            Map<String, Object> orgMap = new HashMap<>();
            orgMap.put("organization_id", user.getId());
            orgMap.put("name", user.getFullName()); // Assuming full name is the name of the organization
            orgMap.put("type", user.getRole()); // Role determines the type (NGO or Hospital)
            orgMap.put("distance", calculateDistance(latitude, longitude, user.getLatitude(), user.getLongitude())); // Implement this method to calculate distance
            responseOrganizations.add(orgMap);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("nearby_organizations", responseOrganizations);

        return ResponseEntity.ok(response);
    }

    // Implement the calculateDistance method to calculate the distance between two sets of latitude and longitude coordinates.
    // You can use the Haversine formula or other distance calculation algorithms.
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance using the Haversine formula
        double distance = R * c;

        return distance; // Distance in kilometers
    }

    
    
    @PostMapping("/notify-nearby")
    public ResponseEntity<?> notifyNearbyOrganizations(  @RequestParam(value = "user_id", required = false) String userId,
    		@RequestParam("post_id") String post_id,
    		@RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude){
    		
    		
        boolean notificationResult = dogPostService.notifyNearbyOrganizations(userId,Long.parseLong(post_id), latitude,longitude);
        if (notificationResult) {
            // Notification successful
            return ResponseEntity.ok(Map.of("success", true));
        } else {
            // Notification failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}

