package com.example.techverse.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.DTO.ApiResponse;
import com.example.techverse.DTO.CommentRequest;
import com.example.techverse.DTO.StoryResponse;
import com.example.techverse.DTO.UserStoriesResponse;
import com.example.techverse.Model.Comment;
import com.example.techverse.Model.Story;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.CommentRepository;
import com.example.techverse.Repository.StoryRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.service.StoryService;


@RestController
@RequestMapping("/api/stories")
public class StoryController {
	
	@Autowired
    private StoryService storyService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/create/{entityType}/{entityId}")
    public ResponseEntity<ApiResponse> createStory(
    		 @RequestHeader("Authorization") String authorization,
    		 
    		 @PathVariable String entityType,
             @PathVariable Long entityId,
                                                   @RequestParam("caption") String caption,
                                                   @RequestParam("media") MultipartFile media,
                                                   @RequestParam("visibility") String visibility) {
        try {
        	  Map<String, Object> response = new HashMap<String, Object>();
             
            Story createdStory = storyService.createStory(entityType,entityId, caption, media,visibility);

            if (createdStory != null) {
                // Construct the response with the desired fields
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("story_id", createdStory.getId());
                responseData.put("entity_id", entityId);
                responseData.put("entity_type", entityType);
                responseData.put("content", caption);
                responseData.put("visibility", visibility);
                responseData.put("timestamp", createdStory.getCreatedAt().toString()); // Assuming getTimestamp returns a Date

                return ResponseEntity.ok(new ApiResponse(true, "Story created successfully", responseData));
            } else {
                return ResponseEntity.ok(new ApiResponse(false, "Failed to create the story"));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error while processing media file"));
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Unauthorized access"));
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false,"All Fields mandatory"));
        }         
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred"));
        }
}
    @GetMapping("/")
    public ResponseEntity<List<Story>> getAllStories() {
        List<Story> stories = storyService.getAllStories();
        return ResponseEntity.ok(stories);
    }
   /* @GetMapping("/{user_id}/stories")
    public ResponseEntity<List<StoryResponse>> getStoriesByUser(@PathVariable("user_id") Long userId) {
         	List<Story> allStories = storyService.getStoriesByUser(userRepository.findById(userId).orElse(null));

        if (allStories == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
        }

        // Filter stories based on visibility
        List<StoryResponse> response = allStories.stream()
                .map(this::convertToStoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{user_id}/stories1")
    public ResponseEntity<UserStoriesResponse> getStoriesByUserPublicFollower(@PathVariable("user_id") Long userId) {
        // Get the user's stories
        List<Story> allStories = storyService.getStoriesByUser(userRepository.findById(userId).orElse(null));

        if (allStories == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
        }

        // Filter stories based on visibility
        List<StoryResponse> publicStories = allStories.stream()
                .filter(story -> "public".equalsIgnoreCase(story.getVisibility()))
                .map(this::convertToStoryResponse)
                .collect(Collectors.toList());

        // Assuming you have a way to get the current user ID, replace 1L with the actual ID
        List<StoryResponse> followersStories = allStories.stream()
                .filter(story -> "followers".equalsIgnoreCase(story.getVisibility()))
                .map(this::convertToStoryResponse)
                .collect(Collectors.toList());

        UserStoriesResponse response = new UserStoriesResponse(publicStories, followersStories);
        return ResponseEntity.ok(response);
    }

    private StoryResponse convertToStoryResponse(Story story) {
        StoryResponse response = new StoryResponse();
        response.setStory_id(story.getId());
        response.setContent(story.getCaption());
        response.setTimestamp(story.getCreatedAt().toString()); // Convert to ISO 8601 format
        response.setLikes(story.getLikes());
      response.setComments(story.getCommentCount());
        return response;
    }
    
    @DeleteMapping("/delete/{story_id}")
    public ResponseEntity<Map<String, Boolean>> deleteStory(
            @PathVariable("story_id") Long storyId,
            @RequestParam("user_id") Long userId
    ) {
    	Map<String, Boolean> response = new HashMap<>();
        try {
            // Call the StoryService to delete the story
            boolean success = storyService.deleteStory(storyId, userId);
            
            if (success) {
            	 response.put("success", true);
                 return ResponseEntity.ok(response);
            } else {
            	response.put("success", false);
            	return ResponseEntity.badRequest().body(response);                 
            }
        } catch (Exception e) {
        	response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Server error
        }
    }
    
    
    @PostMapping("/delete-multiple")
    public ResponseEntity<Map<String, Boolean>> deleteMultipleStories(@RequestBody Map<String, Object> requestBody) {
    	Long userId = ((Number) requestBody.get("user_id")).longValue(); // Convert to Long
    	 // Extract storyIds as a List and convert them to Long
        List<Long> storyIds = ((List<?>) requestBody.get("story_ids")).stream()
                .filter(item -> item instanceof Number)
                .map(Number.class::cast)
                .map(Number::longValue)
                .collect(Collectors.toList());

      Map<String, Boolean> response = new HashMap<>();

        boolean success = storyService.deleteMultipleStories(userId, storyIds);

        if (success) {
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    
    // Endpoint to like a story
    @PostMapping("/like/{story_id}")
    public ResponseEntity<String> likeStory(@PathVariable Long story_id , @RequestParam Long userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Story> storyOptional = storyRepository.findById(story_id);

            if (userOptional.isPresent() && storyOptional.isPresent()) {
                User user = userOptional.get();
                Story story = storyOptional.get();
                story.likeStory(user); // Invoke the likeStory method in the Story entity
                storyRepository.save(story); // Save the updated story
                return ResponseEntity.ok("Story liked successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or story not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/comment/{story_id}")
    public ResponseEntity<String> addCommentToStory(
            @PathVariable Long story_id,
            @RequestBody CommentRequest commentRequest) {
        
        // Find the story by ID
        Optional<Story> optionalStory = storyRepository.findById(story_id);

        if (optionalStory.isPresent()) {
            Story story = optionalStory.get();

            // Create a new Comment object
            Comment comment = new Comment();
            comment.setText(commentRequest.getText());
            comment.setCommenter(userRepository.findById(commentRequest.getUser_id()).get());

            // Set the comment's story to the found story
            comment.setStory(story);
            
         // Set the created_at timestamp to the current time
            comment.setCreatedAt(LocalDateTime.now());


            // Save the comment
            commentRepository.save(comment);

            // Update the comment count in the associated story
            story.incrementCommentCount();
            storyRepository.save(story);

            return ResponseEntity.ok("Comment added successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/edit/{story_id}")
    public ResponseEntity<ApiResponse> editStory(
            @PathVariable("story_id") Long storyId,
            @RequestParam("user_id") Long userId,
            @RequestParam("caption") String caption,
            @RequestParam("media") MultipartFile media,
            @RequestParam("visibility") String visibility) {

        try {
            // Check if the story with the given ID exists and belongs to the user
            Story existingStory = storyRepository.findById(storyId).get();
            if (existingStory == null || !existingStory.getCreator().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "Unauthorized access or story not found"));
            }

            // Perform the story editing logic here, updating caption, media, and visibility as needed
            Story editedStory = storyService.editStory(storyId, caption, media, visibility);

            if (editedStory != null) {
                // Construct the response with the desired fields
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("story_id", editedStory.getId());
                responseData.put("user_id", userId);
                responseData.put("content", caption);
                responseData.put("visibility", visibility);
                responseData.put("timestamp", editedStory.getUpdatedAt().toString()); // Assuming getUpdatedAt returns a Date

                return ResponseEntity.ok(new ApiResponse(true, "Story edited successfully", responseData));
            } else {
                return ResponseEntity.ok(new ApiResponse(false, "Failed to edit the story"));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error while processing media file"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "All Fields mandatory"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred"));
        }
    }

    
    @GetMapping("/fetch")
    public ResponseEntity<?> fetchCurrentDayStories(@RequestParam("user_id") String userId) {
        // Validate input parameters
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user_id");
        }

        // Call the service to fetch current day's stories for the given user
        List<Story> userStories = storyService.fetchUserStories(userId);

        if (userStories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stories found for the user");
        }

        // Prepare the custom response format
        List<Map<String, Object>> responseStories = new ArrayList<>();
        for (Story story : userStories) {
            Map<String, Object> storyMap = new HashMap<>();
            storyMap.put("story_id", story.getId().toString());
            storyMap.put("user_id", story.getCreator().getId().toString());
            storyMap.put("media", story.getMediaUrl());
            storyMap.put("caption", story.getCaption());
            responseStories.add(storyMap);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("stories", responseStories);

        return ResponseEntity.ok(response);
    }
    
    */
}
