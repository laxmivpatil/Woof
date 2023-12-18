package com.example.techverse.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.techverse.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);
    Optional<User> findByphone(String mobileNo);
     Optional<User> findById(Long id);
    boolean existsByEmailOrPhone(String email, String phone);
    Optional<User> findByEmailOrPhone(String email,String phone);
   

    Optional<User> findByToken(String accessToken);
    
    @Query("SELECT u.followers FROM User u WHERE u.id = :userId")
    List<User> findFollowersByUserId(Long userId);
    
    
    
    @Query("SELECT u FROM User u " +
            "WHERE (u.role = 'NGO' OR u.role = 'Hospital') " +
            "AND FUNCTION('ACOS', FUNCTION('SIN', FUNCTION('RADIANS', u.latitude)) * FUNCTION('SIN', FUNCTION('RADIANS', :latitude)) " +
            "+ FUNCTION('COS', FUNCTION('RADIANS', u.latitude)) * FUNCTION('COS', FUNCTION('RADIANS', :latitude)) * FUNCTION('COS', FUNCTION('RADIANS', u.longitude) - FUNCTION('RADIANS', :longitude))) * 6371 <= :radius")
     List<User> findNearbyUsers(@Param("latitude") Double latitude, 
                                @Param("longitude") Double longitude, 
                                @Param("radius") Double radius);
    
}
