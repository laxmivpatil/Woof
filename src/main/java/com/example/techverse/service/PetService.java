package com.example.techverse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.DTO.PetInfoDTO;
import com.example.techverse.Model.MonthlyDetails;
import com.example.techverse.Model.Pet;
import com.example.techverse.Repository.PetRepository;

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

	    public List<PetInfoDTO> getPetsByCategory(String petCategory) {
	        List<Pet> pets = petRepository.findByPetCategory(petCategory);
	        return pets.stream()
	                .map(pet -> new PetInfoDTO(pet.getId(), pet.getPetName()))
	                .collect(Collectors.toList());
	    }
	    
	    
	    
	    public PetInfoDTO getPetInfoById(Long petId) {
	        Optional<Pet> optionalPet = petRepository.findById(petId);
	        if (optionalPet.isPresent()) {
	            Pet pet = optionalPet.get();
	            return new PetInfoDTO(pet.getId(), pet.getPetName(), pet.getGender(), pet.getDescription());
	        } else {
	            // Handle the case when the pet with the given ID is not found
	            return null;
	        }
	    }
	    
	    public List<Map<String, String>> getMonthlyDetailsByPetIdAndMonth(Long petId, String month1) {
	        Pet pet = petRepository.findById(petId).orElse(null);
	       
	        String month=String.valueOf(Double.parseDouble(month1));
	        if (pet != null) {
	            return pet.getMonthlyDetails().stream()
	                    .filter(details -> month.trim().equalsIgnoreCase(details.getMonth().trim()))
	                    .map(this::convertToMap)
	                    .collect(Collectors.toList());
	        } else {
	            // Handle the case when the pet with the given ID is not found
	            return Collections.emptyList();
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

