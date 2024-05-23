package com.example.techverse.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.techverse.Model.NGO;
import com.example.techverse.Model.Notification;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByTimestampDesc(Long userId);
    List<Notification> findByNgoIdOrderByTimestampDesc(Long userId);
    List<Notification> findByVeterinarianIdOrderByTimestampDesc(Long userId);
    
    
    List<Notification> findByUserId(Long userId); 
    List<Notification> findByUserAndIsReadFalse(User user);
    List<Notification> findByNgoAndIsReadFalse(NGO ngo);
    List<Notification> findByVeterinarianAndIsReadFalse(Veterinarian veterinarian);

    
}
