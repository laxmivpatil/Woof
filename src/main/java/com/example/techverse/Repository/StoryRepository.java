package com.example.techverse.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.techverse.Model.Story;
import com.example.techverse.Model.User;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    // Define custom query methods to retrieve stories based on specific criteria

    // For example, to find a story by its ID
    Story findById(long id);
    List<Story> findByCreator(User user);
   List<Story> findByIdInAndCreator(List<Long> storyIds, User creator);
   
  

   // Custom query method to find stories created by a specific user within the last 24 hours
   List<Story> findByCreatorIdAndCreatedAtAfter(Long userId, LocalDateTime twentyFourHoursAgo);
   
   // Custom query method to find stories created before a specific timestamp (for deleting expired stories)
   List<Story> findByCreatedAtBefore(LocalDateTime timestamp);
   
    

   @Query("SELECT s FROM Story s JOIN s.followers u WHERE u.id = :userId")
   List<Story> findFollowedStories(@Param("userId") Long userId);
   
   @Query("SELECT s FROM Story s WHERE (s.creator.id = :userId OR s.creator IN (SELECT u FROM User u JOIN u.followers f WHERE f.id = :userId)) AND s.createdAt >= :twentyFourHoursAgo")
   List<Story> findStoriesForUserAndFollowedUsers(@Param("userId") Long userId, @Param("twentyFourHoursAgo") LocalDateTime twentyFourHoursAgo);


    // Add more custom query methods if needed
}