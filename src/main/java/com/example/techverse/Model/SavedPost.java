package com.example.techverse.Model;
 

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SavedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    private Story post; // Assuming you have a Post entity

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public SavedPost() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructors, getters, setters, and other methods...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Story getPost() {
        return post;
    }

    public void setPost(Story post) {
        this.post = post;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

	public SavedPost(Long id, User user, Story post, LocalDateTime timestamp) {
		super();
		this.id = id;
		this.user = user;
		this.post = post;
		this.timestamp = timestamp;
	}
    
    
    
}

