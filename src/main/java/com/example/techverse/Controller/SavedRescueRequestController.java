package com.example.techverse.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.DistanceCalculator;
import com.example.techverse.DTO.AnimalRescueRequestDTO;
import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.SavedRescueRequest;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.AnimalRescueRequestRepository;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.SavedRescueRequestRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.service.AnimalRescueRequestService;

import java.util.Optional;
import java.util.Set;
@RestController

@RequestMapping("/api/animal_rescue")
public class SavedRescueRequestController {
	
	@Autowired
    private SavedRescueRequestRepository savedRescueRequestRepository;
	

	@Autowired
    private AnimalRescueRequestService  animalRescueRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NGORepository ngoRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private AnimalRescueRequestRepository rescueRequestRepository;
    
    @PostMapping("/save_rescue_requests/{entityType}/{entityId}")
	public ResponseEntity<Map<String,Object>> getUserRescueRequests(
			@RequestHeader("Authorization") String accessToken,@PathVariable String entityType, @PathVariable Long entityId,
			@RequestParam("rescueId") String rescueId ) throws IOException, UnauthorizedAccessException {
		// Find the user based on the access token
		 Map<String, Object> response = new HashMap<String, Object>();
		 Optional<AnimalRescueRequest>  animal=  rescueRequestRepository.findById(Long.parseLong(rescueId));
		 if(!animal.isPresent()) {
			 response.put("success", false);
		     response.put("message", "Animal rescue request not found");
			 return ResponseEntity.ok(response);
		 }
		 
		 SavedRescueRequest s=new SavedRescueRequest();
		
		 
		 switch (entityType.toLowerCase()) {
        case "user":
            User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
            SavedRescueRequest existingSavedRequest = savedRescueRequestRepository.findByUserAndRescueRequestId(user, Long.parseLong(rescueId));

		    if (existingSavedRequest != null) {
		        response.put("success", false);
		        response.put("message", "Rescue request already saved by the user");
		        return ResponseEntity.ok(response);
		    }
		 
           s.setUser(user);
           s.setRescueRequest(animal.get());
            user.getSavedRescueRequests().add(s);
            userRepository.save(user);
            savedRescueRequestRepository.save(s);
            break;
        case "ngo":
            NGO ngo = ngoRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
            SavedRescueRequest ngoSavedRequest = savedRescueRequestRepository.findByNgoAndRescueRequestId(ngo, Long.parseLong(rescueId));

		    if (ngoSavedRequest != null) {
		        response.put("success", false);
		        response.put("message", "Rescue request already saved by the user");
		        return ResponseEntity.ok(response);
		    }
		 
            s.setNgo(ngo);
            s.setRescueRequest(animal.get());
             ngo.getSavedRescueRequests().add(s);
             ngoRepository.save(ngo);
             savedRescueRequestRepository.save(s);
            break;
        case "veterinarian":
            Veterinarian veterinarian = veterinarianRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
            SavedRescueRequest veterinarianSavedRequest = savedRescueRequestRepository.findByVeterinarianAndRescueRequestId(veterinarian, Long.parseLong(rescueId));

		    if (veterinarianSavedRequest != null) {
		        response.put("success", false);
		        response.put("message", "Rescue request already saved by the user");
		        return ResponseEntity.ok(response);
		    }
            s.setVeterinarian(veterinarian);
            s.setRescueRequest(animal.get());
            veterinarian.getSavedRescueRequests().add(s);
            veterinarianRepository.save(veterinarian);
             savedRescueRequestRepository.save(s);
            break;
         
    }
		 
		 

		// Retrieve rescue requests posted by the user
		 
			
		 AnimalRescueRequestDTO a=animalRescueRequestService.mapToDTO(s.getRescueRequest());
		response.put("success", true);
		response.put("message", "Post saved successfully");
		response.put("savedpost", a);
		return ResponseEntity.ok(response);
	}
	
	
    @GetMapping("/save_rescue_requests/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> getSavedRescueRequests(
            @PathVariable String entityType,
            @PathVariable Long entityId
    )throws  UnauthorizedAccessException {
        Map<String, Object> response = new HashMap<>();
        Set<SavedRescueRequest> savedRequests=new HashSet<>();
        switch (entityType.toLowerCase()) {
            case "user":
                User user = userRepository.findById(entityId)
                        .orElseThrow(() -> new UnauthorizedAccessException("User not found"));
                savedRequests = user.getSavedRescueRequests();
                
                break;
            case "ngo":
                NGO ngo = ngoRepository.findById(entityId)
                        .orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
              savedRequests = ngo.getSavedRescueRequests();
                
                break;
            case "veterinarian":
                Veterinarian veterinarian = veterinarianRepository.findById(entityId)
                        .orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
               savedRequests = veterinarian.getSavedRescueRequests();
                 
                break;
            default:
                response.put("success", false);
                response.put("message", "Invalid entity type");
                return ResponseEntity.badRequest().body(response);
        }

        
        List<AnimalRescueRequestDTO> dto1=new ArrayList<AnimalRescueRequestDTO>();
		 
        
        
        for(SavedRescueRequest s:savedRequests) {
        	AnimalRescueRequestDTO a=animalRescueRequestService.mapToDTO(s.getRescueRequest());
        	dto1.add(a);
        }
        response.put("success", true);
        response.put("message", "Saved rescue requests retrieved successfully");
        response.put("saved_post", dto1);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/unsave_rescue_requests/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> unsaveRescueRequest(
            @RequestHeader("Authorization") String accessToken, @PathVariable String entityType, @PathVariable Long entityId,
            @RequestParam("rescueId") String rescueId)throws IOException, UnauthorizedAccessException {
        // Find the user based on the access token
        Map<String, Object> response = new HashMap<>();

        switch (entityType.toLowerCase()) {
            case "user":
                User user = userRepository.findById(entityId)
                        .orElseThrow(() -> new UnauthorizedAccessException("User not found"));
                SavedRescueRequest existingSavedRequestByUser = savedRescueRequestRepository
                        .findByUserAndRescueRequestId(user, Long.parseLong(rescueId));

                if (existingSavedRequestByUser == null) {
                    response.put("success", false);
                    response.put("message", "Rescue request is not saved by the user");
                    return ResponseEntity.ok(response);
                }

                user.getSavedRescueRequests().remove(existingSavedRequestByUser);
                userRepository.save(user);
                savedRescueRequestRepository.delete(existingSavedRequestByUser);
                break;
            case "ngo":
                NGO ngo = ngoRepository.findById(entityId)
                        .orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
                SavedRescueRequest existingSavedRequestByNGO = savedRescueRequestRepository
                        .findByNgoAndRescueRequestId(ngo, Long.parseLong(rescueId));

                if (existingSavedRequestByNGO == null) {
                    response.put("success", false);
                    response.put("message", "Rescue request is not saved by the NGO");
                    return ResponseEntity.ok(response);
                }

                ngo.getSavedRescueRequests().remove(existingSavedRequestByNGO);
                ngoRepository.save(ngo);
                savedRescueRequestRepository.delete(existingSavedRequestByNGO);
                break;
            case "veterinarian":
                Veterinarian veterinarian = veterinarianRepository.findById(entityId)
                        .orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
                SavedRescueRequest existingSavedRequestByVet = savedRescueRequestRepository
                        .findByVeterinarianAndRescueRequestId(veterinarian, Long.parseLong(rescueId));

                if (existingSavedRequestByVet == null) {
                    response.put("success", false);
                    response.put("message", "Rescue request is not saved by the veterinarian");
                    return ResponseEntity.ok(response);
                }

                veterinarian.getSavedRescueRequests().remove(existingSavedRequestByVet);
                veterinarianRepository.save(veterinarian);
                savedRescueRequestRepository.delete(existingSavedRequestByVet);
                break;
            default:
                response.put("success", false);
                response.put("message", "Invalid entity type");
                return ResponseEntity.ok(response);
        }

        response.put("success", true);
        response.put("message", "Rescue request unsaved successfully");
        return ResponseEntity.ok(response);
    }

    
    

}
