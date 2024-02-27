package com.example.techverse.Repository;

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
	   

}
