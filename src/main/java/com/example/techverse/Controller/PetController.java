package com.example.techverse.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.DTO.PetInfoDTO;
import com.example.techverse.Model.MonthlyDetails;
import com.example.techverse.Model.Pet;
import com.example.techverse.service.PetService;

import java.util.List;
import java.util.Map;

@RestController
public class PetController {


    @Autowired
    private PetService petService;

    @GetMapping("/pets")
    public List<PetInfoDTO> getPetsInfoByCategory(@RequestParam String petCategory) {
        return petService.getPetsByCategory(petCategory);
    }
    @GetMapping("/petsbyId")
    public PetInfoDTO getPetInfoById(@RequestParam Long petId) {
        return petService.getPetInfoById(petId);
    }
    @GetMapping("/pets/monthly-details")
    public List<Map<String, String>> getMonthlyDetailsByPetIdAndMonth(
    		     		@RequestParam Long petId,
            @RequestParam String month) {
        return petService.getMonthlyDetailsByPetIdAndMonth(petId, month);
    }
}