package com.example.techverse.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.DTO.PetInfoDTO;
import com.example.techverse.Model.MonthlyDetails;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.Pet;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.PetRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.service.PetService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pet-categories")
public class PetController {


    @Autowired
    private PetService petService;
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
	private VeterinarianRepository veterinarianRepository;

	@Autowired
	private NGORepository NgoRepository;
    @GetMapping
    public Map<String, Object> getAllPetCategories() {
        Map<String, Object> response = new HashMap<>();
        List<String> petCategories = petService.getAllPetCategories();

        response.put("categories", petCategories);

        return response;
    }
    @GetMapping("/petss")
    public Map<String, Object> getPetsInfoByCategory(@RequestParam String petCategory) {
    	  Map<String, Object> response = new HashMap<>();
          
    	List<PetInfoDTO> petInfoList = petService.getPetsByCategory(petCategory);
        response.put("pet Info ", petInfoList);

        return response;
    }
    @GetMapping("/pets")
    public Map<String, Object> getPetsInfoByCategoryAndGender(@RequestParam String petCategory) {
        return petService.getPetsGroupedByNameAndGender(petCategory);
    }
    @GetMapping("/pets/byId")
    public PetInfoDTO getPetInfoById(@RequestParam Long petId) {
        return petService.getPetInfoById(petId);
    }
    @GetMapping("/pets/monthly-details")
    public Map<String, String> getMonthlyDetailsByPetIdAndMonthJson(
            @RequestParam Long petId,
            @RequestParam String month) {
        return petService.getMonthlyDetailsByPetIdAndMonth(petId, month);
    }
    @PutMapping("/savepets/{entityType}/{entityId}")
    public Map<String, Object> savepets(@RequestHeader("Authorization") String accessToken,@PathVariable String entityType, @PathVariable Long entityId,
    		@RequestParam String petCategory,@RequestParam String petName) throws UnauthorizedAccessException {
    	  Map<String, Object> response = new HashMap<>();
    	List<Pet> pets=petRepository.findAllByPetNameAndPetCategory(petName, petCategory);
    	if(pets.isEmpty())
    	{
    		response.put("message ", "no pet found");
    		response.put("status", false);
    		return response;
    	}
    	switch (entityType.toLowerCase()) {
        case "user":
            User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
            for(Pet p:pets) {
            	if(user.getSavedPets().contains(p))
            	{
            		response.put("message ", "Pet allready Saved");
            		response.put("status", true);
            		return response;
            	}
             
            	user.getSavedPets().add(p);
            	 userRepository.save(user);
            }
            break;
        case "ngo":
            NGO ngo = NgoRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
            for(Pet p:pets) {
            	if(ngo .getSavedPets().contains(p))
            	{
            		response.put("message ", "Pet allready Saved");
            		response.put("status", true);
            		return response;
            	}
             
            	ngo.getSavedPets().add(p);
            	NgoRepository.save(ngo);
            }
            break;
        case "veterinarian":
            Veterinarian veterinarian = veterinarianRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
            for(Pet p:pets) {
            	if(veterinarian.getSavedPets().contains(p))
            	{
            		response.put("message ", "Pet allready Saved");
            		response.put("status", true);
            		return response;
            	}
             
            	veterinarian.getSavedPets().add(p);
            	veterinarianRepository.save(veterinarian);
            }
            break;
            
            
         
    }
    	
    	response.put("message ", "Pet Saved done ");
		response.put("status", true);
		return response;
        
    }
    
    
    
    
    
    @GetMapping("/savepets/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> getSavedPetsByUser(@RequestHeader("Authorization") String accessToken,@PathVariable String entityType, @PathVariable Long entityId)
     {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK; // Default status

        try {
            
            
            
            
            
        	List<Pet> savedPets =new ArrayList<>();
            switch (entityType.toLowerCase()) {
            case "user":
                User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
                savedPets = new ArrayList<>(user.getSavedPets());
                 response.put("message", "Saved pets retrieved successfully");
                break;
            case "ngo":
                NGO ngo = NgoRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
                
                savedPets = new ArrayList<>(ngo.getSavedPets());
                 response.put("message", "Saved pets retrieved successfully");
                break;
            case "veterinarian":
                Veterinarian veterinarian = veterinarianRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
                savedPets = new ArrayList<>(veterinarian.getSavedPets());
                 response.put("message", "Saved pets retrieved successfully");
                break;
                
                
             
        }
        	
            
            Map<String, Map<String, Object>> groupedPetsByNameAndGender = savedPets.stream()
	                .collect(Collectors.groupingBy(
	                        Pet::getPetName,
	                        Collectors.toMap(
	                                Pet::getGender,
	                                pet -> {
	                                    Map<String, Object> petInfo = new HashMap<>();
	                                    petInfo.put("id", pet.getId());
	                                    petInfo.put("petName", pet.getPetName());
	                                    petInfo.put("gender", pet.getGender().toLowerCase());
	                                    petInfo.put("description", pet.getDescription());
	                                    petInfo.put("img1", pet.getImg1());
	                                    petInfo.put("img2", pet.getImg2());
	                                    petInfo.put("img3", pet.getImg3());
	                                    return petInfo;
	                                }
	                        )
	                ));

	          List<Map<String, Object>> listOfPets = new ArrayList<>();
	        for (Map.Entry<String, Map<String, Object>> entry : groupedPetsByNameAndGender.entrySet()) {
	            Map<String, Object> petGroup = new HashMap<>();
	            petGroup.put("pet_name", entry.getKey());
	            
	          if(  entry.getValue().get("male")!=null) {
	            petGroup.put("male", entry.getValue().get("male")); 
	          }
	          else {
	        	  petGroup.put("male", entry.getValue().get("Male")); 
	          }
	          if(entry.getValue().get("female")!=null) {
		            petGroup.put("female", entry.getValue().get("female")); 
		          }
		          else {
		        	  petGroup.put("female", entry.getValue().get("Female")); 
		          }
		            
	             listOfPets.add(petGroup);
	        }
	        response.put("list_of_pets", listOfPets);
                                                                  
	         
            
            
            
        	 
            return ResponseEntity.status(HttpStatus.OK).body(response);
            
            
            
            
            
            
        } catch (UnauthorizedAccessException e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            status = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            response.put("message", "Error occurred: " + e.getMessage());
            response.put("status", false);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }
    
    
    
    @DeleteMapping("/savepets/{entityType}/{entityId}")
    public Map<String, Object> unsavepets(@RequestHeader("Authorization") String accessToken,@PathVariable String entityType, @PathVariable Long entityId,
    		@RequestParam String petCategory,@RequestParam String petName) throws UnauthorizedAccessException {
    	  Map<String, Object> response = new HashMap<>();
    	List<Pet> pets=petRepository.findAllByPetNameAndPetCategory(petName, petCategory);
    	if(pets.isEmpty())
    	{
    		response.put("message ", "no pet saved yet");
    		response.put("status", false);
    		return response;
    	}
    	switch (entityType.toLowerCase()) {
        case "user":
            User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
            for(Pet p:pets) {
            	if(user.getSavedPets().contains(p))
            	{
            		user.getSavedPets().remove(p);
            	}
             
            	
            	 userRepository.save(user);
            }
            break;
        case "ngo":
            NGO ngo = NgoRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
            for(Pet p:pets) {
            	if(ngo .getSavedPets().contains(p))
            	{
            		ngo.getSavedPets().remove(p);
            	}
             
            	
            	NgoRepository.save(ngo);
            }
            break;
        case "veterinarian":
            Veterinarian veterinarian = veterinarianRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
            for(Pet p:pets) {
            	if(veterinarian.getSavedPets().contains(p))
            	{
            		veterinarian.getSavedPets().remove(p);
            	}
             
            	
            	veterinarianRepository.save(veterinarian);
            }
            break;
            
            
         
    }
    	
    	response.put("message ", "Pet UnSaved done ");
		response.put("status", true);
		return response;
        
    }
    
    
    
    

}