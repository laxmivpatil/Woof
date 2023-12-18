package com.example.techverse.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import com.example.techverse.Model.DogPost;
import com.example.techverse.Model.DogRescueNotification;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.DogPostRepository;
import com.example.techverse.Repository.DogRescueNotificationRepository;
import com.example.techverse.Repository.UserRepository;

@Service
public class DogPostService {

    @Autowired
    private DogPostRepository dogPostRepository;
    
    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository
    
    @Autowired
    private DogRescueNotificationRepository dogRescueNotificationRepository; 


    public DogPost saveDogPost(DogPost dogPost) {
        return dogPostRepository.save(dogPost);
    }
    
    
    private String uploadDir="\\Files\\";

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
           /* if (fileName.contains("..")) {
                throw new IOException("Invalid file name: " + fileName);
            }
*/
            // Copy file to the target location
            String filePath = uploadDir + File.separator + fileName;
            File targetFile = new File(filePath);
            file.transferTo(targetFile);

            return filePath;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file: " + fileName, ex);
        }
    }
    
  
    public List<User> findNearbyOrganizations(Double latitude, Double longitude, Double radius) {
        // Implement logic to find nearby organizations based on latitude, longitude, radius, and role.
        // Use userRepository to query your database for nearby Users based on their roles.
        // Return the list of nearby organizations.
        // Implement the logic for distance calculation (e.g., Haversine formula).

    	double roundedLatitude = Math.round(latitude * 1e6) / 1e6;
    	double roundedLongitude = Math.round(longitude * 1e6) / 1e6;
    	List<User> nearbyUsers=userRepository.findNearbyUsers(roundedLatitude, roundedLongitude, radius);
        /*// Filter users based on their role (NGO or Hospital)
        List<User> nearbyOrganizations = new ArrayList<>();
        for (User user : nearbyUsers) {
            if (user.getRole().equals(role)) {
                nearbyOrganizations.add(user);
            }
        }*/

        return nearbyUsers;
    } 
    public boolean notifyNearbyOrganizations( String userId,Long post_id,Double latitude,Double longitude) {
        // Implement logic to notify nearby organizations based on the provided notification details.
        // You can use the user, dogPost, latitude, and longitude fields to send notifications.
        // Return true if the notification was successful, otherwise return false.
        // Implement your notification logic here.
    	double roundedLatitude = Math.round(latitude * 1e6) / 1e6;
    	double roundedLongitude = Math.round(longitude * 1e6) / 1e6;
    	List<User> nearbyUsers=userRepository.findNearbyUsers(roundedLatitude, roundedLongitude, 5.0);
    	
    	for(User user:nearbyUsers) {
    		DogRescueNotification dogRescueNotification=new DogRescueNotification();
    		dogRescueNotification.setUser(user);
    		dogRescueNotification.setLatitude(latitude);
    		dogRescueNotification.setLongitude(longitude);
    		dogRescueNotification.setDogPost(dogPostRepository.getById(post_id));
    		dogRescueNotificationRepository.save(dogRescueNotification);
    	}
      
        return true; // Placeholder, implement the actual notification logic.
    }
}

