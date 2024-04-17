package com.example.techverse.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.techverse.Model.NGO;
import com.example.techverse.Model.Veterinarian;

public interface NGORepository extends JpaRepository<NGO, Long> {
	
	
	  @Query("SELECT u FROM NGO u WHERE u.phone = :value OR u.email = :value")
	    Optional<NGO> findByPhoneOrEmail(@Param("value") String phoneNumberOrEmail);
	  

	  Optional<NGO> findByToken(String accessToken);
	  boolean existsByEmailOrPhone(String email, String phone);
	  
	  

	    Optional<NGO> findByEmailOrPhone(String email,String phone);
	    
	    
	    @Query("SELECT u FROM NGO u " +
	            "WHERE FUNCTION('ACOS', FUNCTION('SIN', FUNCTION('RADIANS', u.latitude)) * FUNCTION('SIN', FUNCTION('RADIANS', :latitude)) " +
	            "+ FUNCTION('COS', FUNCTION('RADIANS', u.latitude)) * FUNCTION('COS', FUNCTION('RADIANS', :latitude)) * FUNCTION('COS', FUNCTION('RADIANS', u.longitude) - FUNCTION('RADIANS', :longitude))) * 6371 <= :radius")
	     List<NGO> findNearbyNGO(@Param("latitude") Double latitude, 
	                                @Param("longitude") Double longitude, 
	                                @Param("radius") Double radius);

}
