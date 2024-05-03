package com.example.techverse.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.DTO.PetInfoDTO;
import com.example.techverse.Model.MonthlyDetails;
import com.example.techverse.Model.Pet;
import com.example.techverse.service.PetService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pet-categories")
public class PetController {


    @Autowired
    private PetService petService;
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

}