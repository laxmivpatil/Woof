package com.example.techverse.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

 
 

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.Model.SavedPost;
import com.example.techverse.Model.Story;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.CommentRepository;
import com.example.techverse.Repository.SavedPostRepository;
import com.example.techverse.Repository.StoryRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.UnauthorizedAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;



@Service
public class StoryService {
	
	 @Autowired
	    private UserRepository userRepository;
	    @Autowired
	    private StoryRepository storyRepository;
	    
	    @Autowired
	    private CommentRepository commentRepository;
	    
	    @Autowired
	    private SavedPostRepository savedPostRepository;
	  
	    
	    
	    private String uploadDir="F:\\MyProject\\Woof\\Files\\";
	    
	    
	    @Transactional
	    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 24 hours in milliseconds
	    public void deleteExpiredStories() {
	        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
	        List<Story> storiesToDelete = storyRepository.findByCreatedAtBefore(twentyFourHoursAgo);
	        
	        for (Story story : storiesToDelete) {
	             commentRepository.deleteByStory(story);
	            List<SavedPost> savedPosts = savedPostRepository.findByPost(story);
	            savedPostRepository.deleteAll(savedPosts);
	            storyRepository.delete(story);
	            // Delete the story itself
	            storyRepository.delete(story);
	        }
	    }

	    @Transactional
	    public Story createStory(Long user_id, String caption, MultipartFile media,String visibility)
	            throws IOException, UnauthorizedAccessException {
	        // Perform user authentication and retrieve the user from the database using the access token
	        Optional<User> user = userRepository.findById(user_id);
	        if (user.isEmpty()) { 
	            throw new UnauthorizedAccessException();
	        }

	        System.out.println("hello");
	        // Perform input validation (e.g., caption length, media file type, etc.)
	        if (caption==null || caption.trim().isEmpty()) {
	        	 System.out.println("hello");
	        	throw new IllegalArgumentException("Caption cannot be empty");
	        }

	       
	        
	         
	        // Handle media file upload and storage (you can use your preferred storage mechanism)
	      String mediaUrl = storeFile(media);
	      //  String mediaUrl = media.getOriginalFilename();
	         
	          // Create a new Story instance and associate it with the user
	        Story story = new Story(user.get(), caption, mediaUrl,visibility);
	        System.out.println("not valid filencmvbmcbv"+story.toString());
	        
	        // Save the story to the database and update the user's story list
	        user.get().getStories().add(story);
	        storyRepository.save(story);
	        System.out.println("not valid file");
	        
	        userRepository.save(user.get());
	         
	        return story;
	        
	    }
	    public String storeFile(MultipartFile file) {
	        // Normalize file name
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	        try {
	            // Check if the file's name contains invalid characters
	            if (fileName.contains("..")) {
	                throw new IOException("Invalid file name: " + fileName);
	            }

	            // Copy file to the target location
	            String filePath = uploadDir + File.separator + fileName;
	            File targetFile = new File(filePath);
	            file.transferTo(targetFile);

	            return filePath;
	        } catch (IOException ex) {
	            throw new RuntimeException("Could not store file: " + fileName, ex);
	        }
	    }

	    public Story editStory(Long storyId, String caption, MultipartFile media, String visibility) throws IOException {
	        // Find the story by its ID
	        Optional<Story> optionalStory = storyRepository.findById(storyId);

	        if (optionalStory.isPresent()) {
	            Story existingStory = optionalStory.get();

	            // You may want to add additional validation, e.g., checking if the user has permission to edit this story.

	            // Update the story fields
	            existingStory.setCaption(caption);
	            existingStory.setVisibility(visibility);

	            // Handle media file update (if provided)
	            if (media != null && !media.isEmpty()) {
	                // Process and save the new media file, and update the story's media information.
	            	  String mediaUrl = storeFile(media);
	            	 
	                existingStory.setMediaUrl(mediaUrl);
 	            }

	            
	            // Set the updated timestamp
	            existingStory.setUpdatedAt(LocalDateTime.now());

	            // Save the edited story
	            return storyRepository.save(existingStory);
	        } else {
	            // Story not found
	            return null;
	        }
	    }

	    private Optional<User> getUserFromAccessToken(String accessToken) {
	        // ... Your implementation to retrieve the user based on the access token ...
	    	
	    	return userRepository.findByToken(accessToken);
	    }

	 
	    public List<Story> getStoriesByUser(User user) {
	        // Assuming you have a Story entity and StoryRepository for database access

	        // Replace "Story" with your actual entity class name
	        List<Story> stories = storyRepository.findByCreator(user);

	        // You should have a method like "findByUserId" in your StoryRepository
	        // that queries the database for stories by user ID.

	        return stories;
	    }
	    
	  
	 	    @Transactional
	    public ResponseEntity<String> addFollowersToStory(String accessToken, Long storyId, List<Long> followers) {
	        // Here, you can perform user authentication using the access token.
	        // For simplicity, I'm skipping authentication and directly adding followers to the story.

	        Optional<User> userOptional = userRepository.findByToken(accessToken); // Implement this method in UserRepository
	        if (userOptional.isEmpty()) {
	            return ResponseEntity.status(401).body("Invalid access token. User authentication failed.");
	        }
	        User user = userOptional.get();

	        Story story = storyRepository.findById(storyId).orElse(null);

	        if (story == null) {
	            return ResponseEntity.notFound().build();
	        }

	        if (!story.getCreator().equals(user)) {
	            return ResponseEntity.badRequest().body("You can only add followers to your own story.");
	        }

	        for (Long followerId : followers) {
	            if (followerId.equals(user.getId())) {
	                // Skip adding the creator's ID as a follower
	                continue;
	            }

	            User follower = userRepository.findById(followerId).orElse(null);
	            if (follower == null) {
	                return ResponseEntity.badRequest().body("Follower with ID " + followerId + " not found.");
	            }

	            // Check if the follower is already following the story
	            if (!story.getFollowers().contains(follower)) {
	            	System.out.println("user->"+follower.getId());
	               // user.addStoryFollowed(story);
	            	follower.addStoryFollowed(story);
	            }
	            userRepository.save(follower);
	        }

	       
	        return ResponseEntity.ok("Followers added to the story successfully.");
	    }

	    
	    
	    
	    
	    public List<Long> getFollowersOfStory(Long storyId) {
	        Story story = storyRepository.findById(storyId).orElse(null);
	        if (story == null) {
	            // Handle the case when the story is not found
	            return Collections.emptyList(); // Return an empty list or handle the error as appropriate
	        }
	        List<User> users=story.getFollowers();
	        
	        List<Long> userID=new ArrayList<>();
	        for(User user:users) {
	        userID.add(user.getId());	
	        }

	        return userID;
	    }
	    
	    
	    public List<Story> getStoriesFollowedByUser(Long userId) {
	        Optional<User> userOptional = userRepository.findById(userId);
	        if (userOptional.isEmpty()) {
	            return Collections.emptyList();
	        }

	        User user = userOptional.get();
	        return user.getStoriesFollowed();
	    }
	    
	    
	    public boolean deleteStory(Long storyId, Long userId) {
	        // Check if the story exists
	        Story story = storyRepository.findById(storyId).orElse(null);

	        if (story == null) {
	            return false; // Story not found
	        }

	        // Check if the story belongs to the user
	        if (!story.getCreator().getId().equals(userId)) {
	            return false; // Story doesn't belong to the user
	        }

	        // Remove followers from the story
	        story.removeAllFollowers();

	        // Delete the story
	        storyRepository.delete(story);

	        return true; // Story successfully deleted
	    }

	    
	    public boolean deleteMultipleStories(Long userId, List<Long> storyIds) {
	        // Fetch stories to be deleted
	        List<Story> storiesToDelete = storyRepository.findByIdInAndCreator(storyIds, userRepository.findById(userId).get());

	        if (storiesToDelete.isEmpty()) {
	            return false; // No matching stories found
	        }

	        // Remove followers from the stories
	        for (Story story : storiesToDelete) {
	            story.removeAllFollowers();
	        }

	        // Delete the stories
	        storyRepository.deleteAll(storiesToDelete);

	        return true; // Stories successfully deleted
	    }
	    
	    
	    public List<Story> fetchUserStories(String userId) {
	        // Fetch stories created by the user
	    //    List<Story> userCreatedStories = storyRepository.findByCreator(userRepository.findById(Long.parseLong(userId)).get());

	        // Fetch stories followed by the user (implement this method in your repository)
	    	LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
	    	List<Story> stories = storyRepository.findStoriesForUserAndFollowedUsers(Long.parseLong(userId), twentyFourHoursAgo);
	        // Combine user created stories and followed stories
	        List<Story> userStories = new ArrayList<>();
	       // userStories.addAll(userCreatedStories);
	        userStories.addAll(stories);

	        return userStories;
	    }
	    	

}
