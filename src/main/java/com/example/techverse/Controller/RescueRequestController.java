package com.example.techverse.Controller;
 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Model.Photo;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.AnimalRescueRequestRepository;
import com.example.techverse.Repository.PhotoRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.service.StorageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;



 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animal_rescue")
public class RescueRequestController {

    private final UserRepository userRepository;
    private final AnimalRescueRequestRepository rescueRequestRepository;
    private final PhotoRepository photoRepository;
    
    @Autowired
    private StorageService storageService;

    @Autowired
    public RescueRequestController(UserRepository userRepository,
                                   AnimalRescueRequestRepository rescueRequestRepository,
                                   PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.rescueRequestRepository = rescueRequestRepository;
        this.photoRepository = photoRepository;
    }

    @PostMapping("/post_rescue_request")
    public ResponseEntity<String> postRescueRequest(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("animal_type") String animalType,
            @RequestParam("location") String location,
            @RequestParam("priority_issue") String priorityIssue,
            @RequestParam("contact_details") String contactDetails,
            @RequestParam("caption") String caption,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos
    ) throws IOException {
        // Find the user based on the access token or create a new user if it's their first request
        Optional<User> user1 = userRepository.findByToken(accessToken);

        if (user1.isEmpty()) {
            return new ResponseEntity<>("unauthorized user", HttpStatus.UNAUTHORIZED);
        }
        User user = user1.get();

        // Initialize the rescueRequest object
        AnimalRescueRequest rescueRequest = new AnimalRescueRequest();
        rescueRequest.setUser(user);
        rescueRequest.setAnimalType(animalType);
        rescueRequest.setLocation(location);
        rescueRequest.setPriorityIssue(priorityIssue);
        rescueRequest.setContactDetails(contactDetails);
        rescueRequest.setCaption(caption);

        // Save the rescue request in the database
        rescueRequest = rescueRequestRepository.save(rescueRequest);

        // Process photos if available
        if (photos != null && !photos.isEmpty()) {
            List<Photo> savedPhotos = new ArrayList<>();
            for (MultipartFile photoFile : photos) {
                Photo photo = new Photo();
                String mediaUrl = storageService.uploadFile(photoFile);
                photo.setFilename(mediaUrl);
               // photo.setFilename(photoFile.getOriginalFilename());
                photo.setData(photoFile.getBytes());
                photo.setRescueRequest(rescueRequest);
                savedPhotos.add(photo);
            }
            photoRepository.saveAll(savedPhotos);
        }

        return new ResponseEntity<>("Rescue request posted successfully", HttpStatus.OK);
    }
    
    
    
    
    @GetMapping("/user_rescue_requests")
    public ResponseEntity<List<AnimalRescueRequest>> getUserRescueRequests(@RequestHeader("Authorization") String accessToken) {
        // Find the user based on the access token
    	
        Optional<User> userOptional = userRepository.findByToken(accessToken);
        if (!userOptional.isPresent()) {
        	
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        System.out.println("hi how are you");
        User user = userOptional.get();

        // Retrieve rescue requests posted by the user
        List<AnimalRescueRequest> userRescueRequests = rescueRequestRepository.findByUser(user);
        for(AnimalRescueRequest request:userRescueRequests) {
        	System.out.println(request.getId());
        }
        return new ResponseEntity<>(userRescueRequests, HttpStatus.OK);
    }
}
