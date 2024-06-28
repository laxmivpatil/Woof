package com.example.techverse.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.techverse.Model.NGO;
import com.example.techverse.Model.Notification;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByTimestampDesc(Long userId);
    List<Notification> findByNgoIdOrderByTimestampDesc(Long ngoId);
    List<Notification> findByVeterinarianIdOrderByTimestampDesc(Long vetId);
    
    
    List<Notification> findByUserId(Long userId); 
    
    
    List<Notification> findByUserIdAndIsReadFalseOrderByTimestampDesc(Long userId);
    List<Notification> findByNgoIdAndIsReadFalseOrderByTimestampDesc(Long ngoId);
    List<Notification> findByVeterinarianIdAndIsReadFalseOrderByTimestampDesc(Long veterinarianId);
    
    
    List<Notification> findByRescueRequestId(Long rescueId);
    
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId")
    void markAllNotificationsAsReadForUser(@Param("userId") Long userId);
    
    
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.ngo.id = :userId")
    void markAllNotificationsAsReadForNgo(@Param("userId") Long userId);
    
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.veterinarian.id = :userId")
    void markAllNotificationsAsReadForVeterinarian(@Param("userId") Long userId);
    
    
    
    
    
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = false WHERE n.user.id = :userId")
    void markAllNotificationsAsUnReadForUser(@Param("userId") Long userId);
    
    
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = false WHERE n.ngo.id = :userId")
    void markAllNotificationsAsUnReadForNgo(@Param("userId") Long userId);
    
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = false WHERE n.veterinarian.id = :userId")
    void markAllNotificationsAsUnReadForVeterinarian(@Param("userId") Long userId);
    

    
    
    void deleteByUserId(Long userId);

    void deleteByNgoId(Long ngoId);

    void deleteByVeterinarianId(Long vetId);
    
}
