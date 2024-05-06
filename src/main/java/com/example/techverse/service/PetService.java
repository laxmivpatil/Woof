package com.example.techverse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.DTO.PetInfoDTO;
import com.example.techverse.Model.MonthlyDetails;
import com.example.techverse.Model.Pet;
import com.example.techverse.Repository.PetRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

	 @Autowired
	    private PetRepository petRepository;
	 public List<String> getAllPetCategories() {
	        List<Pet> pets = petRepository.findAll();
	        return pets.stream()
	                .map(Pet::getPetCategory)
	                .distinct()
	                .collect(Collectors.toList());
	    }
	    public List<PetInfoDTO> getPetsByCategory(String petCategory) {
	        List<Pet> pets = petRepository.findByPetCategory(petCategory);
	        return pets.stream()
	                .map(pet -> new PetInfoDTO(pet.getId(), pet.getPetName(),pet.getImg1(),pet.getImg2(),pet.getImg3()))
	                .collect(Collectors.toList());
	    }
	    public Map<String, Object> getPetsGroupedByNameAndGender(String petCategory) {
	        List<Pet> pets = petRepository.findByPetCategoryIgnoreCase(petCategory);

	        // Group pets by name and gender
	        Map<String, Map<String, Object>> groupedPetsByNameAndGender = pets.stream()
	                .collect(Collectors.groupingBy(
	                        Pet::getPetName,
	                        Collectors.toMap(
	                                Pet::getGender,
	                                pet -> {
	                                    Map<String, Object> petInfo = new HashMap<>();
	                                    petInfo.put("id", pet.getId());
	                                    petInfo.put("petName", pet.getPetName());
	                                    petInfo.put("gender", pet.getGender());
	                                    petInfo.put("description", pet.getDescription());
	                                    petInfo.put("img1", pet.getImg1());
	                                    petInfo.put("img2", pet.getImg2());
	                                    petInfo.put("img3", pet.getImg3());
	                                    return petInfo;
	                                }
	                        )
	                ));

	        Map<String, Object> response = new HashMap<>();
	        List<Map<String, Object>> listOfPets = new ArrayList<>();
	        for (Map.Entry<String, Map<String, Object>> entry : groupedPetsByNameAndGender.entrySet()) {
	            Map<String, Object> petGroup = new HashMap<>();
	            petGroup.put("pet_name", entry.getKey());
	            
	            petGroup.put("male", entry.getValue().get("Male")); 
	            
	            petGroup.put("female", entry.getValue().get("Female")); 
		            
	             listOfPets.add(petGroup);
	        }
	        response.put("list_of_pets", listOfPets);
                                                                  
	        return response;
	    }

	    
	    public PetInfoDTO getPetInfoById(Long petId) {
	        Optional<Pet> optionalPet = petRepository.findById(petId);
	        if (optionalPet.isPresent()) {
	            Pet pet = optionalPet.get();
	            return new PetInfoDTO(pet.getId(), pet.getPetName(), pet.getGender(), pet.getDescription(),pet.getImg1(),pet.getImg2(),pet.getImg3());
	        } else {
	            // Handle the case when the pet with the given ID is not found
	            return null;
	        }
	    }
	    
	    public Map<String, String> getMonthlyDetailsByPetIdAndMonth(Long petId, String month) {
	        Pet pet = petRepository.findById(petId).orElse(null);
	        String monthStr = String.valueOf(Double.parseDouble(month));
	        
	        if (pet != null) {
	            return pet.getMonthlyDetails().stream()
	                    .filter(details -> monthStr.trim().equalsIgnoreCase(details.getMonth().trim()))
	                    .findFirst()
	                    .map(this::convertToMap)
	                    .orElse(Collections.emptyMap());
	        } else {
	            // Handle the case when the pet with the given ID is not found
	            return Collections.emptyMap();
	        }
	    }

	    private Map<String, String> convertToMap(MonthlyDetails monthlyDetails) {
	        Map<String, String> detailsMap = new HashMap<>();
	        detailsMap.put("food", monthlyDetails.getFood());
	        detailsMap.put("exercise", monthlyDetails.getExercise());
	        detailsMap.put("toysToPlay", monthlyDetails.getToysToPlay());
	        detailsMap.put("color", monthlyDetails.getColor());
	        detailsMap.put("activity", monthlyDetails.getActivity());
	        detailsMap.put("grooming", monthlyDetails.getGrooming());
	        detailsMap.put("enclosure", monthlyDetails.getEnclosure());
	        detailsMap.put("clothes", monthlyDetails.getClothes());
	        detailsMap.put("vaccination", monthlyDetails.getVaccination());
	        detailsMap.put("weight", monthlyDetails.getWeight());
	        detailsMap.put("healthCare", monthlyDetails.getHealthCare());
	        detailsMap.put("precautions", monthlyDetails.getPrecautions());
	        detailsMap.put("pregnancyPrecautions", monthlyDetails.getPregnancyPrecautions());

	        return detailsMap;
	    }
}

