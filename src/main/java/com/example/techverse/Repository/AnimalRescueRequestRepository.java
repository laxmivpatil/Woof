package com.example.techverse.Repository; 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Model.User;

public interface AnimalRescueRequestRepository extends JpaRepository<AnimalRescueRequest, Long> {
    // Add any custom query methods if needed
	
	   List<AnimalRescueRequest> findByUser(User user);
	   
	   List<AnimalRescueRequest> findAllByOrderByDatetimeDesc();

}

