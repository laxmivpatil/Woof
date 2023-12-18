package com.example.techverse.Model;

import javax.persistence.*;

@Entity
public class PushNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String action;

    @ManyToOne
    @JoinColumn(name = "dog_post_id")
    private DogPost dogPost;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public DogPost getDogPost() {
		return dogPost;
	}

	public void setDogPost(DogPost dogPost) {
		this.dogPost = dogPost;
	}

	 
	 

    // Constructors, getters, setters...
    
    
    
}

