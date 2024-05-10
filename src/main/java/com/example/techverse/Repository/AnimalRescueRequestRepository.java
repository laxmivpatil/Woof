package com.example.techverse.Repository; 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;

public interface AnimalRescueRequestRepository extends JpaRepository<AnimalRescueRequest, Long> {
    // Add any custom query methods if needed
	
	   List<AnimalRescueRequest> findByUser(User user);
	   
	   List<AnimalRescueRequest> findAllByOrderByDatetimeDesc();

	   List<AnimalRescueRequest> findByNgoOrderByDatetimeDesc(NGO ngo);
	   
	   List<AnimalRescueRequest> findByUserOrderByDatetimeDesc(User user);
	   
	   
	   List<AnimalRescueRequest> findByVeterinarianOrderByDatetimeDesc(Veterinarian vet);
	   
	   
}



 
  
 
 
 

