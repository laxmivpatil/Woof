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
    @GetMapping("/notificationsall/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> getUserNotificationsall(@PathVariable("entityId") Long entityId,@PathVariable("entityType") String entityType) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        List<NotificationDTO> notificationList=new ArrayList<>();
        List<Notification> notifications=new ArrayList<>();
        try {
        	if(entityType.equalsIgnoreCase("user")) {
              notifications = notificationService.getUserNotifications(entityId);
        	}
        	else if(entityType.equalsIgnoreCase("ngo")) {
                 notifications = notificationService.getNgoNotifications(entityId);
            	}
        	else if(entityType.equalsIgnoreCase("veterinarian")) {
                      notifications = notificationService.getVeterinarianNotifications(entityId);
                	}
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
    @GetMapping("/notifications/unread/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> getUserNotificationsunread(@PathVariable("entityId") Long entityId,@PathVariable("entityType") String entityType) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        List<NotificationDTO> notificationList = new ArrayList<>();
        List<Notification> notifications=new ArrayList<>();
        try {
            if(entityType.equalsIgnoreCase("user")) {
                notifications = notificationService.getUserUnreadNotifications(entityId);
          	}
          	else if(entityType.equalsIgnoreCase("ngo")) {
                   notifications = notificationService.getNgoUnreadNotifications(entityId);
              	}
          	else if(entityType.equalsIgnoreCase("veterinarian")) {
                        notifications = notificationService.getVeterinarianUnreadNotifications(entityId);
                  	}
            
            for (Notification n : notifications) {
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


    
    
    
    
    
   
    //mark all notifications as read
    @PatchMapping("/notification/readall/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> markAllNotificationsAsRead(
    		@PathVariable("entityId") Long entityId,@PathVariable("entityType") String entityType) {
    	Map<String, Object> response = new HashMap<>();
        try { 
         	 if(entityType.equalsIgnoreCase("user")) {
        		 notificationRepository.markAllNotificationsAsReadForUser(entityId);
           	}
           	else if(entityType.equals("ngo")) { 
           	 notificationRepository.markAllNotificationsAsReadForNgo(entityId);
               	}
           	else if(entityType.equalsIgnoreCase("veterinarian")) {
           	 notificationRepository.markAllNotificationsAsReadForVeterinarian(entityId);
                   	}
        
        response.put("status", true);
        response.put("message", "All notifications marked as read successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
        
         }
         catch(Exception e){
        	 System.out.println(e.getMessage());
        	 response.put("status", false);
             response.put("message", "notifications Not read Successfully");
             return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    
    
    
    //mark all notifications as unread
    @PatchMapping("/notification/unreadall/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> markAllNotificationsAsUnRead(
    		@PathVariable("entityId") Long entityId,@PathVariable("entityType") String entityType) {
     	Map<String, Object> response = new HashMap<>();
        try {
         	 if(entityType.equalsIgnoreCase("user")) {
        		 notificationRepository.markAllNotificationsAsUnReadForUser(entityId);
           	}
           	else if(entityType.equalsIgnoreCase("ngo")) {
           	 notificationRepository.markAllNotificationsAsUnReadForNgo(entityId);
               	}
           	else if(entityType.equalsIgnoreCase("veterinarian")) {
           	 notificationRepository.markAllNotificationsAsUnReadForVeterinarian(entityId);
                   	}
        
        response.put("status", true);
        response.put("message", "All notifications marked as unread successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
        
         }
         catch(Exception e){
        	 response.put("status", false);
             response.put("message", "notifications Not read Successfully");
             return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    
    
    
    // Mark single/multiple Notification as Read
    @PatchMapping("/notification/read/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> markNotificationAsRead(
    		@PathVariable("entityId") Long entityId,@PathVariable("entityType") String entityType,
    		@RequestBody Map<String, List<Long>> requestBody) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        List<Long> notificationIds = requestBody.get("notificationIds");
        try {
        	boolean success=false;
        	 if(entityType.equalsIgnoreCase("user")) {
                 success = notificationService.markUserNotificationsAsRead(entityId, notificationIds);
           	}
           	else if(entityType.equalsIgnoreCase("ngo")) {
           		success = notificationService.markNgoNotificationsAsRead(entityId, notificationIds);
               	}
           	else if(entityType.equalsIgnoreCase("veterinarian")) {
           		success = notificationService.markVeterinarianNotificationsAsRead(entityId, notificationIds);
                   	}
        	
            if (success) {
                // Set success response properties
                response.put("success", true);
                response.put("message", "successfully mark notifications as read.");
            } else {
                // Set failure response properties
                response.put("success", false);
                response.put("message", "Failed to mark notifications as read.");
            }

        } catch (Exception e) {
            response.put("message", "An error occurred while processing the request.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }
    
    
 // Mark single/multiple Notification as unRead
    @PatchMapping("/notification/unread/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> markNotificationsAsUnRead(
    		@PathVariable("entityId") Long entityId,@PathVariable("entityType") String entityType,
    		@RequestBody Map<String, List<Long>> requestBody) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        List<Long> notificationIds = requestBody.get("notificationIds");
        try {
        	boolean success=false;
        	 if(entityType.equalsIgnoreCase("user")) {
                 success = notificationService.markUserNotificationsAsUnRead(entityId, notificationIds);
           	}
           	else if(entityType.equalsIgnoreCase("ngo")) {
           		success = notificationService.markNgoNotificationsAsUnRead(entityId, notificationIds);
               	}
           	else if(entityType.equalsIgnoreCase("veterinarian")) {
           		success = notificationService.markVeterinarianNotificationsAsUnRead(entityId, notificationIds);
                   	}
        	
            if (success) {
                // Set success response properties
                response.put("success", true);
                response.put("message", "successfully mark notifications as read.");
            } else {
                // Set failure response properties
                response.put("success", false);
                response.put("message", "Failed to mark notifications as read.");
            }

        } catch (Exception e) {
            response.put("message", "An error occurred while processing the request.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }
    
    
    
  
    //delete multiple notification
    @DeleteMapping("/notification/delete/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteMultipleNotifications(
    		@PathVariable("entityId") Long entityId,@PathVariable("entityType") String entityType,
            @RequestBody Map<String, List<Long>> requestBody) {
    	
    	Map<String, Object> response = new HashMap<>();
    	  List<Long> notificationIds = requestBody.get("notificationIds");
          
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
    
    
    @DeleteMapping("/notification/deleteall/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteNotifications(
            @PathVariable("entityType") String entityType,
            @PathVariable("entityId") Long entityId) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        try {
            if (entityType.equalsIgnoreCase("user")) {
                notificationService.deleteUserNotifications(entityId);
            } else if (entityType.equalsIgnoreCase("ngo")) {
                notificationService.deleteNgoNotifications(entityId);
            } else if (entityType.equalsIgnoreCase("veterinarian")) {
                notificationService.deleteVeterinarianNotifications(entityId);
            } else {
                response.put("success", false);
                response.put("message", "Invalid entity type.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            response.put("success", true);
            response.put("message", "Notifications deleted successfully.");
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            response.put("success", false);
            response.put("message", "An error occurred while deleting notifications.");
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
    
   
    
      
    public NotificationDTO toDTO(Notification n)
    {
    	NotificationDTO nDTO=new NotificationDTO(n);
    	return nDTO;
    }

}
