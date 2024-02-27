package com.example.techverse.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
 

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
	
	
	  @Query("SELECT u FROM Veterinarian u WHERE u.phone = :value OR u.email = :value")
	    Optional<Veterinarian> findByPhoneOrEmail(@Param("value") String phoneNumberOrEmail);
	  boolean existsByEmailOrPhone(String email, String phone);
	  
	  Optional<Veterinarian> findByToken(String accessToken);
	   
}
