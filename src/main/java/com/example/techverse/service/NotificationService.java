package com.example.techverse.service;

 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.DTO.UpdateNotificationSettingsRequest;
import com.example.techverse.Model.Notification;
import com.example.techverse.Model.NotificationType;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.NotificationRepository;
import com.example.techverse.Repository.UserRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public boolean markNotificationAsRead(Long userId, Long notificationId) {
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

    public void deleteMultipleNotifications(List<Long> notificationIds) {
        notificationRepository.deleteAllById(notificationIds);
    }
}

