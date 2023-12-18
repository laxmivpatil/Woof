package com.example.techverse.Model;


import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AnimalRescueRequest {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String animalType;
    private String location;
    private String priorityIssue;
    private String contactDetails;
    private String caption;

    @OneToMany(mappedBy = "rescueRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}
	
	

	public AnimalRescueRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnimalRescueRequest(Long id, User user, String animalType, String location, String priorityIssue,
			String contactDetails, String caption, List<Photo> photos) {
		super();
		this.id = id;
		this.user = user;
		this.animalType = animalType;
		this.location = location;
		this.priorityIssue = priorityIssue;
		this.contactDetails = contactDetails;
		this.caption = caption;
		this.photos = photos;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAnimalType() {
		return animalType;
	}

	public void setAnimalType(String animalType) {
		this.animalType = animalType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPriorityIssue() {
		return priorityIssue;
	}

	public void setPriorityIssue(String priorityIssue) {
		this.priorityIssue = priorityIssue;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
    
    
    
    
}
