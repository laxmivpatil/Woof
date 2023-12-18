package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
 
import com.example.techverse.Model.Pet;

public interface PetRepository extends JpaRepositoryImplementation<Pet, Long> {

}
