package com.example.techverse.Controller;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.example.techverse.DTO.PushNotificationRequest;
 
import com.example.techverse.Model.User;
import com.example.techverse.Repository.DogPostRepository;
import com.example.techverse.Repository.StoryRepository;
import com.example.techverse.Repository.UserRepository; 
import com.example.techverse.service.PushNotificationService;
import com.example.techverse.service.UserService;

@RestController
@RequestMapping("/api")
public class PushNotificationController {
	
	@Autowired
	UserService userService; 
	
	@Autowired
	PushNotificationService notificationService;
	
	@Autowired
	StoryRepository storyRepository;
	
	@Autowired
	DogPostRepository dogPostRepository;
	
	@Autowired
	UserRepository userRepository;
	
	 
	@PostMapping("/notify")
	public ResponseEntity<Map<String, Boolean>> pushNotification(@RequestBody PushNotificationRequest notificationRequest) {
	    // Extract parameters from notificationRequest
	     Long userId = Long.parseLong(notificationRequest.getUser_id());
	    String action = notificationRequest.getAction();
	    Long dogPostId = Long.parseLong(notificationRequest.getDog_post_id());

	    Map<String, Boolean> response = new HashMap<>();

	    // Notify followers
	    List<User> followers = userService.getFollowersByUserId(userId);
	    System.out.println(followers.size());

	    if (followers.isEmpty()) {
	        response.put("success", false);
	        return ResponseEntity.badRequest().body(response);
	    }

	    /*	    User follower = userRepository.findById(followerId).orElse(null);

	    if (follower != null && followers.contains(follower)) {
	        System.out.println(followers.size() + "hdsjfhjdsgjfg");
	        notificationService.createPushNotification(follower, userRepository.findById(userId).orElse(null), action, dogPostRepository.findById(dogPostId).orElse(null));
	    }*/
	    for(User user:followers) {
	        System.out.println(followers.size() + "hdsjfhjdsgjfg");
	        notificationService.createPushNotification(user, userRepository.findById(userId).orElse(null), action, dogPostRepository.findById(dogPostId).orElse(null));
	
	    }

	    response.put("success", true);
	    return ResponseEntity.ok(response);
	}
/*
	@PostMapping("/notify")
	public ResponseEntity<Map<String, Boolean>> pushNotification(@RequestBody PushNotificationRequest notificationRequest) {
	    // Extract parameters from notificationRequest
	    Long followerId = Long.parseLong(notificationRequest.getFollower_id());
	    Long userId = Long.parseLong(notificationRequest.getUser_id());
	    String action = notificationRequest.getAction();
	    Long storyId = Long.parseLong(notificationRequest.getStory_id());

	    Map<String, Boolean> response = new HashMap<>();

	    // Notify followers
	    List<User> followers = userService.getFollowersByUserId(userId);
	    System.out.println(followers.size());

	    if (followers.isEmpty()) {
	        response.put("success", false);
	        return ResponseEntity.badRequest().body(response);
	    }

	    User follower = userRepository.findById(followerId).orElse(null);

	    if (follower != null && followers.contains(follower)) {
	        System.out.println(followers.size() + "hdsjfhjdsgjfg");
	        notificationService.createPushNotification(follower, userRepository.findById(userId).orElse(null), action, storyRepository.findById(storyId).orElse(null));
	    }

	    response.put("success", true);
	    return ResponseEntity.ok(response);
	}*/

}
