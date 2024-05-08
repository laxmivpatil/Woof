package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.NGO;
import com.example.techverse.Model.SavedRescueRequest;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;

public interface SavedRescueRequestRepository extends JpaRepository<SavedRescueRequest, Long> {
	
	 SavedRescueRequest findByUserAndRescueRequestId(User user, Long rescueRequestId);
	 SavedRescueRequest findByNgoAndRescueRequestId(NGO ngo, Long rescueRequestId);
	 SavedRescueRequest findByVeterinarianAndRescueRequestId(Veterinarian veterinarian, Long rescueRequestId);
	 

}
