package com.example.techverse.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.techverse.DTO.CreateNotificationRequest;
import com.example.techverse.DTO.NotificationDTO;
import com.example.techverse.DTO.UpdateNotificationSettingsRequest;
import com.example.techverse.DTO.UserNotificationSettingsResponse;
import com.example.techverse.Model.Notification;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.NotificationRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.service.NotificationService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;
     
    private final UserRepository userRepository;


    @Autowired
    public NotificationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Retrieve User Notifications all include read notification(all)
    @GetMapping("/user/{user_id}/notificationsall")
    public ResponseEntity<Map<String, Object>> getUserNotificationsall(@PathVariable("user_id") Long userId) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        List<NotificationDTO> notificationList=new ArrayList<>();

        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            for(Notification n:notifications) {
            	
            	notificationList.add( toDTO(n));
            	
            }
            response.put("notifications", notificationList);
        } catch (Exception e) {
            response.put("message", "An error occurred while processing the request.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }
    
 // Retrieve User Notifications(only unread)
    @GetMapping("/user/{user_id}/notifications")
    public ResponseEntity<Map<String, Object>> getUserNotificationsunread(@PathVariable("user_id") Long userId) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        List<NotificationDTO> notificationList = new ArrayList<>();

        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            
            // Filter out read notifications from the list
            List<Notification> unreadNotifications = notifications.stream()
                    .filter(notification -> !notification.isRead())
                    .collect(Collectors.toList());
            
            for (Notification n : unreadNotifications) {
                System.out.println(n.getMessage());
                notificationList.add( toDTO(n));
                 
            }
            
            response.put("notifications", notificationList);
        } catch (Exception e) {
            response.put("message", "An error occurred while processing the request.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }


    // Mark Notification as Read
    @PatchMapping("/user/{user_id}/notifications/{notification_id}/read")
    public ResponseEntity<Map<String, Object>> markNotificationAsRead(
            @PathVariable("user_id") Long userId,
            @PathVariable("notification_id") Long notificationId) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        try {
            boolean success = notificationService.markNotificationAsRead(userId, notificationId);

            if (success) {
                // Set success response properties
                response.put("success", true);
            } else {
                // Set failure response properties
                response.put("success", false);
                response.put("message", "Failed to mark notification as read.");
            }

        } catch (Exception e) {
            response.put("message", "An error occurred while processing the request.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }

    // Create Notification
    @PostMapping("/notifications/create")
    public ResponseEntity<Map<String, Object>> createNotification(@RequestBody CreateNotificationRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        try {
            if (request.getUser_id()==0|| request.getType()==null || request.getMessage().equals(null)) {
            	System.out.println("hi");
                response.put("success", false);
                response.put("message", "Invalid request parameters or missing required fields.");
            } else {
            	System.out.println("hjfgjsdghfdsgds");
                boolean success = notificationService.createNotification(request.getUser_id(), request.getType(), request.getMessage());

                if (success) {
                    // Set success response properties
                    response.put("success", true);
                } else {
                    // Set failure response properties
                    response.put("success", false);
                    response.put("message", "Failed to create notification.");
                }
            }

        } catch (Exception e) {
            response.put("message", "An error occurred while processing the request.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }
    
     

    //get the notification settings of given user
    @GetMapping("/user/{userId}/settings")
    public ResponseEntity<?> getUserNotificationSettings(@PathVariable Long userId) {
       
    	 Map<String, Object> response = new HashMap<>();
         try {
  
    	Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserNotificationSettingsResponse response1 =
                new UserNotificationSettingsResponse(user.getId(), user.getNotificationsEnabled());
            response.put("status", true);
            response.put("resonse", response1);
            return new ResponseEntity<>(response,HttpStatus.OK);
            } else {
            	 response.put("status", false);
                 response.put("message", "User not Found");
                 return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
            }
             }
             catch(Exception e){
            	 response.put("status", false);
                 response.put("message", "get Notification settings failed");
                 return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
             }
    }
    
    //update notification setting true/false of given id
    @PatchMapping("/user/{userId}/settings")
    public ResponseEntity<?> updateUserNotificationSettings(
            @PathVariable Long userId,
            @RequestBody UpdateNotificationSettingsRequest request) {
    	 Map<String, Object> response = new HashMap<>();
         try {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setNotificationsEnabled(request.getNotificationsEnabled());
            userRepository.save(user);
           
            response.put("status", true);
            response.put("message", "Notification settings updated successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
            } else {
            	 response.put("status", false);
                 response.put("message", "User not Found");
                 return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
            }
             }
             catch(Exception e){
            	 response.put("status", false);
                 response.put("message", "Notification settings not updated successfully");
                 return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
             }
    }
    //delete given notification of given user
    @DeleteMapping("/user/{userId}/notifications/{notificationId}")
    public ResponseEntity<Map<String, Object>> deleteSingleNotification(
            @PathVariable Long userId,
            @PathVariable Long notificationId) {
    	 Map<String, Object> response = new HashMap<>();
         try {
        notificationService.deleteNotification(notificationId);
        response.put("status", true);
        response.put("message", "notification Deleted Successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
         }
         catch(Exception e){
        	 response.put("status", false);
             response.put("message", "notification Not Deleted Successfully");
             return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

    //delete notification of given userid
    @PostMapping("/user/{userId}/notifications/delete")
    public ResponseEntity<Map<String, Object>> deleteMultipleNotifications(
            @PathVariable Long userId,
            @RequestBody List<Long> notificationIds) {
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        notificationService.deleteMultipleNotifications(notificationIds);
        
        response.put("status", true);
        response.put("message", "notifications Deleted Successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
         }
         catch(Exception e){
        	 response.put("status", false);
             response.put("message", "notifications Not Deleted Successfully");
             return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    
    
    //mark read all notification to provided userid
    @PatchMapping("/user/{userId}/notifications/read-all")
    public ResponseEntity<Map<String, Object>> markAllNotificationsAsRead(
            @PathVariable Long userId) {
    	Map<String, Object> response = new HashMap<>();
        try {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
             List<Notification> userNotifications = notificationRepository.findByUserId(userId);

            for (Notification notification : userNotifications) {
                notification.setRead(true);
            }

            notificationRepository.saveAll(userNotifications);
            
        
        response.put("status", true);
        response.put("message", "All notifications marked as read successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
        } else {
        	 response.put("status", false);
             response.put("message", "User not Found");
             return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        }
         }
         catch(Exception e){
        	 response.put("status", false);
             response.put("message", "notifications Not read Successfully");
             return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    
    
    
    public NotificationDTO toDTO(Notification n)
    {
    	NotificationDTO nDTO=new NotificationDTO(n);
    	return nDTO;
    }

}
