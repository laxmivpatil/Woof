package com.example.techverse.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.SavedPost;
import com.example.techverse.Model.Story;

public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
	
	 List<SavedPost> findByUserId(Long userId);
	 
	 List<SavedPost> findByPost(Story story);
	 

}
