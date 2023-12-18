package com.example.techverse.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.DTO.FollowerResponse;
import com.example.techverse.Model.User;
import com.example.techverse.service.UserService;

@RestController
@RequestMapping("/api/users")

public class FollowersUserController {
	
	@Autowired
	UserService userService; 
	
	 @PostMapping("/{current_user_id}/follow")
	    public ResponseEntity<Boolean> followUser(
	            @PathVariable("current_user_id") Long currentUserId,
	            @RequestParam("user_to_follow_id") Long userToFollowId) {

	        // Validate input parameters
	        if (userToFollowId == null) {
	            return ResponseEntity.badRequest().body(false);
	        }

	        // Perform the follow action and handle any errors
	        boolean success = userService.followUser(currentUserId, userToFollowId);

	        if (success) {
	            return ResponseEntity.ok(true);
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
	        }
	    }

	    @PostMapping("/{current_user_id}/unfollow")
	    public ResponseEntity<Boolean> unfollowUser(
	            @PathVariable("current_user_id") Long currentUserId,
	            @RequestParam("user_to_unfollow_id") Long userToUnfollowId) {

	        // Validate input parameters
	        if (userToUnfollowId == null) {
	            return ResponseEntity.badRequest().body(false);
	        }

	        // Perform the unfollow action and handle any errors
	        boolean success = userService.unfollowUser(currentUserId, userToUnfollowId);

	        if (success) {
	            return ResponseEntity.ok(true);
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
	        }
	    }
	    @GetMapping("/{user_id}/followers")
	    public ResponseEntity<List<FollowerResponse>> getFollowersByUserId(@PathVariable("user_id") Long userId) {
	        List<User> followers = userService.getFollowersByUserId(userId);

	        if (followers == null) {
	            return ResponseEntity.notFound().build();
	        }

	        List<FollowerResponse> followerResponses = new ArrayList<>();
	        for (User follower : followers) {
	            followerResponses.add(new FollowerResponse(follower.getId(), follower.getEmail()));
	        }

	        return ResponseEntity.ok(followerResponses);
	    }
}

