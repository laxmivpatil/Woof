package com.example.techverse.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
 
import com.example.techverse.Model.Pet;

public interface PetRepository extends JpaRepositoryImplementation<Pet, Long> {

	List<Pet> findByPetCategory(String petCategory);
	 List<Pet> findByPetCategoryIgnoreCase(String petCategory);
}
