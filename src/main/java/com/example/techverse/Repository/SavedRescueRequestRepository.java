package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.SavedRescueRequest;

public interface SavedRescueRequestRepository extends JpaRepository<SavedRescueRequest, Long> {
	
	

}
