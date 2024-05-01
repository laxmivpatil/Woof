package com.example.techverse.Model;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AnimalRescueRequest {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ngo_id", nullable = true)
    private NGO ngo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = true)
    private Veterinarian veterinarian;
     
    private String location; ///
    private String priorityIssue;   //
    private String contactDetails;   //
    private String caption;  //caption
    
    private double latitude;  //
    private double longitude;  //
 
    private String imgorvideo="";

    private LocalDateTime datetime;
    
    

    
    
    
	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public NGO getNgo() {
		return ngo;
	}

	public void setNgo(NGO ngo) {
		this.ngo = ngo;
	}

	public Veterinarian getVeterinarian() {
		return veterinarian;
	}

	public void setVeterinarian(Veterinarian veterinarian) {
		this.veterinarian = veterinarian;
	}

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

	 

	public void setUser(User user) {
		this.user = user;
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getImgorvideo() {
		return imgorvideo;
	}

	public void setImgorvideo(String imgorvideo) {
		this.imgorvideo = imgorvideo;
	}

	 
    
    
    
}
