package com.example.techverse.service;

 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.DTO.UpdateNotificationSettingsRequest;
import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.Notification;
import com.example.techverse.Model.NotificationType;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.NotificationRepository;
import com.example.techverse.Repository.UserRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    
    
    
    public Notification sendNotificationToUser(User user, String message,AnimalRescueRequest rescueRequest) {
        Notification notification = new Notification(user, message, LocalDateTime.now(), false,rescueRequest);
        return notificationRepository.save(notification);
    }

    public Notification sendNotificationToNGO(NGO ngo, String message,AnimalRescueRequest rescueRequest) {
        Notification notification = new Notification(ngo, message, LocalDateTime.now(), false,rescueRequest);
        return notificationRepository.save(notification);
    }

    public Notification sendNotificationToVeterinarian(Veterinarian veterinarian, String message,AnimalRescueRequest rescueRequest) {
        Notification notification = new Notification(veterinarian, message, LocalDateTime.now(), false,rescueRequest);
       return  notificationRepository.save(notification);
    }
    
    
    //get all notification
    
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }
    public List<Notification> getNgoNotifications(Long userId) {
        return notificationRepository.findByNgoIdOrderByTimestampDesc(userId);
    }
    public List<Notification> getVeterinarianNotifications(Long userId) {
        return notificationRepository.findByVeterinarianIdOrderByTimestampDesc(userId);
    }
    
    
    
    
    
    //get unread notification
    public List<Notification> getUserUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByTimestampDesc(userId);
    }
    public List<Notification> getNgoUnreadNotifications(Long userId) {
        return notificationRepository.findByNgoIdAndIsReadFalseOrderByTimestampDesc(userId);
    }
    public List<Notification> getVeterinarianUnreadNotifications(Long userId) {
        return notificationRepository.findByVeterinarianIdAndIsReadFalseOrderByTimestampDesc(userId);
    }
    
    
    
    

    public boolean markUserNotificationsAsRead(Long userId, List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        // Filter notifications that belong to the user and mark them as read
        List<Notification> userNotifications = notifications.stream()
                .filter(notification -> notification.getUser() != null && notification.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        if (!userNotifications.isEmpty()) {
            userNotifications.forEach(notification -> notification.setRead(true));
            notificationRepository.saveAll(userNotifications);
            return true;
        }

        return false;
    }
    
    public boolean markNgoNotificationsAsRead(Long ngoId, List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        // Filter notifications that belong to the NGO and mark them as read
        List<Notification> ngoNotifications = notifications.stream()
                .filter(notification -> notification.getNgo() != null && notification.getNgo().getId().equals(ngoId))
                .collect(Collectors.toList());

        if (!ngoNotifications.isEmpty()) {
            ngoNotifications.forEach(notification -> notification.setRead(true));
            notificationRepository.saveAll(ngoNotifications);
            return true;
        }

        return false;
    }
    public boolean markVeterinarianNotificationsAsRead(Long veterinarianId, List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        // Filter notifications that belong to the Veterinarian and mark them as read
        List<Notification> veterinarianNotifications = notifications.stream()
                .filter(notification -> notification.getVeterinarian() != null && notification.getVeterinarian().getId().equals(veterinarianId))
                .collect(Collectors.toList());

        if (!veterinarianNotifications.isEmpty()) {
            veterinarianNotifications.forEach(notification -> notification.setRead(true));
            notificationRepository.saveAll(veterinarianNotifications);
            return true;
        }

        return false;
    }

    
    public boolean markUserNotificationsAsUnRead(Long userId, List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        // Filter notifications that belong to the user and mark them as read
        List<Notification> userNotifications = notifications.stream()
                .filter(notification -> notification.getUser() != null && notification.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        if (!userNotifications.isEmpty()) {
            userNotifications.forEach(notification -> notification.setRead(false));
            notificationRepository.saveAll(userNotifications);
            return true;
        }

        return false;
    }
    
    public boolean markNgoNotificationsAsUnRead(Long ngoId, List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        // Filter notifications that belong to the NGO and mark them as read
        List<Notification> ngoNotifications = notifications.stream()
                .filter(notification -> notification.getNgo() != null && notification.getNgo().getId().equals(ngoId))
                .collect(Collectors.toList());

        if (!ngoNotifications.isEmpty()) {
            ngoNotifications.forEach(notification -> notification.setRead(false));
            notificationRepository.saveAll(ngoNotifications);
            return true;
        }

        return false;
    }
    public boolean markVeterinarianNotificationsAsUnRead(Long veterinarianId, List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        // Filter notifications that belong to the Veterinarian and mark them as read
        List<Notification> veterinarianNotifications = notifications.stream()
                .filter(notification -> notification.getVeterinarian() != null && notification.getVeterinarian().getId().equals(veterinarianId))
                .collect(Collectors.toList());

        if (!veterinarianNotifications.isEmpty()) {
            veterinarianNotifications.forEach(notification -> notification.setRead(false));
            notificationRepository.saveAll(veterinarianNotifications);
            return true;
        }

        return false;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  //mark single notification as read
    public boolean markUserNotificationAsRead(Long userId, Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);

        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();

            if (notification.getUser().getId().equals(userId)) {
                notification.setRead(true);
                notificationRepository.save(notification);
                return true;
            }
        }

        return false;
    }

    
    public boolean markNgoNotificationAsRead(Long ngoId, Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);

        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();

            if (notification.getNgo().getId().equals(ngoId)) {
                notification.setRead(true);
                notificationRepository.save(notification);
                return true;
            }
        }

        return false;
    }

    
    public boolean markVeterinarianNotificationAsRead(Long vetId, Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);

        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();

            if (notification.getVeterinarian().getId().equals(vetId)) {
                notification.setRead(true);
                notificationRepository.save(notification);
                return true;
            }
        }

        return false;
    }

    
    
    
    
    
    
    
    
    
    
    public boolean createNotification(Long userId, NotificationType type, String message) {
        // Retrieve the user from the database
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false; // User not found
        }

        // Create a new notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);
        return true;
    }
    
    public boolean updateUserNotificationSettings(Long userId, UpdateNotificationSettingsRequest requestDTO) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return false;
        }

        Boolean notificationsEnabled = requestDTO.getNotificationsEnabled();

        if (notificationsEnabled != null) {
            user.setNotificationsEnabled(notificationsEnabled);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
    
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    @Transactional
    public void deleteUserNotifications(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }

    @Transactional
    public void deleteNgoNotifications(Long ngoId) {
        notificationRepository.deleteByNgoId(ngoId);
    }

    @Transactional
    public void deleteVeterinarianNotifications(Long vetId) {
        notificationRepository.deleteByVeterinarianId(vetId);
    }
    public void deleteMultipleNotifications(List<Long> notificationIds) {
        notificationRepository.deleteAllById(notificationIds);
         
    }
}

