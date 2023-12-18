package com.example.techverse.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.techverse.Model.Comment;
import com.example.techverse.Model.Story; 

public interface CommentRepository extends JpaRepository<Comment, Long>{

	@Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.story = :story")
    void deleteByStory(Story story);
}
