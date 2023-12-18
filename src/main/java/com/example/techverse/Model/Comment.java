package com.example.techverse.Model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User commenter;

    @ManyToOne // Establish a many-to-one relationship with Story
    @JoinColumn(name = "story_id", nullable = false)
    private Story story; // Add a reference to the Story entity

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    // Constructors, getters, and setters for Comment
	
	public void updateCommentCount() {
        if (story != null) {
            story.incrementCommentCount();
        }
    }

    // Add a method to remove the comment and update the comment count in the associated Story
    public void removeAndUpdateCommentCount() {
        if (story != null) {
            story.decrementCommentCount();
        }
        // Set the reference to Story to null when the comment is removed
        story = null;
    }
    
    
}


