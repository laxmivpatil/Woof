package com.example.techverse.Controller;
 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.DTO.RegistrationDTO;
import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.Photo;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.AnimalRescueRequestRepository;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.PhotoRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;
import com.example.techverse.service.StorageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private  VeterinarianRepository veterinarianRepository;

    @Autowired
    private NGORepository NgoRepository;

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
    public ResponseEntity<Map<String, Object>> postRescueRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("role") String role,
            @RequestPart("latitude") String latitude,
            @RequestPart("longitude") String longitude,
            @RequestPart("location") String location,
            @RequestPart("priority_issue") String priorityIssue,
            @RequestPart("contact_details") String contactDetails,
            @RequestPart("caption") String caption,
            @RequestPart(value = "imgorvideo", required = false)  MultipartFile  imgorvideo
    ) throws IOException {
        // Find the user based on the access token or create a new user if it's their first request
    	 
        Optional<User> user1 = userRepository.findByToken(authorization.substring(7));
        Map<String, Object> response = new HashMap<String, Object>();
        if (user1.isEmpty()) {
        	 response.put("success",false);
	    	 response.put("message","Unauthorized user");
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = user1.get();
        double lan=Double.parseDouble(latitude);
        double lon=Double.parseDouble(longitude);
        // Initialize the rescueRequest object
        AnimalRescueRequest rescueRequest = new AnimalRescueRequest();
        rescueRequest.setUser(user);
        rescueRequest.setLatitude(lan);
        rescueRequest.setLongitude(lon);
        rescueRequest.setLocation(location);
        rescueRequest.setPriorityIssue(priorityIssue);
        rescueRequest.setContactDetails(contactDetails);
        rescueRequest.setCaption(caption);

        // Save the rescue request in the database

        // Process photos if available
        if (imgorvideo != null && !imgorvideo.isEmpty()) {
                String path= storageService.uploadFileOnAzure(imgorvideo);
                rescueRequest.setImgorvideo(path);
            }
            
      
        double roundedLatitude = Math.round(lan * 1e6) / 1e6;
    	double roundedLongitude = Math.round(lon * 1e6) / 1e6;
    	List<NGO> nearbyNGO=NgoRepository.findNearbyNGO(roundedLatitude, roundedLongitude, 5.0);
    	List<Veterinarian> nearbyVeterinarian=veterinarianRepository.findNearbyVeterinarian(roundedLatitude, roundedLongitude, 5.0);
    	List<RegistrationDTO> registrationDTOs=new ArrayList<>();
    	for(NGO ngo: nearbyNGO) {
    		RegistrationDTO  dto=new RegistrationDTO();
    		registrationDTOs.add(dto.toDTO(ngo));
    		
    	}
    	
    	for(Veterinarian v: nearbyVeterinarian) {
    		RegistrationDTO  dto=new RegistrationDTO();
    		registrationDTOs.add(dto.toDTO(v));
    		
    	}
    	response.put("success",true);
        response.put("message","Rescue request posted successfully");
        response.put("nearbyngoandvet", registrationDTOs);
        
      

    rescueRequest = rescueRequestRepository.save(rescueRequest);
    
//  response.setData(token);
    return ResponseEntity.ok(response);
     
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
