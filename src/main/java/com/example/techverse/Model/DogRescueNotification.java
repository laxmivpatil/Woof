package com.example.techverse.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dog_rescue_notifications")
public class DogRescueNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private DogPost dogPost;

    
    @Column 
    private Double latitude;

    @Column 
    private Double longitude;

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

	public DogPost getDogPost() {
		return dogPost;
	}

	public void setDogPost(DogPost dogPost) {
		this.dogPost = dogPost;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public DogRescueNotification(Long id, User user, DogPost dogPost, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.user = user;
		this.dogPost = dogPost;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public DogRescueNotification() {
		super();
		// TODO Auto-generated constructor stub
	}

    
    
    
    // Other fields

    // Constructors, getters, and setters
}
