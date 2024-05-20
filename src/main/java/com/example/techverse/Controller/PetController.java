package com.example.techverse.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}