package com.example.techverse.Repository;

import java.util.List;
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
	  

	    Optional<Veterinarian> findByEmailOrPhone(String email,String phone);
	    
	    
	    
	    
	    @Query("SELECT u FROM Veterinarian u " +
	            "WHERE FUNCTION('ACOS', FUNCTION('SIN', FUNCTION('RADIANS', u.latitude)) * FUNCTION('SIN', FUNCTION('RADIANS', :latitude)) " +
	            "+ FUNCTION('COS', FUNCTION('RADIANS', u.latitude)) * FUNCTION('COS', FUNCTION('RADIANS', :latitude)) * FUNCTION('COS', FUNCTION('RADIANS', u.longitude) - FUNCTION('RADIANS', :longitude))) * 6371 <= :radius")
	     List<Veterinarian> findNearbyVeterinarian(@Param("latitude") Double latitude, 
	                                @Param("longitude") Double longitude, 
	                                @Param("radius") Double radius);
	   
}
