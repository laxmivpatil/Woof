package com.example.techverse.DTO;

public class AnimalRescueRequestDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userProfile;
    private String userAddress;
    
    private String datetimeFormatted;
    private String location;
    private String caption;
    private String imgorvideo;
    private String role;
    private double distance;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}
	public String getDatetimeFormatted() {
		return datetimeFormatted;
	}
	public void setDatetimeFormatted(String datetimeFormatted) {
		this.datetimeFormatted = datetimeFormatted;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getImgorvideo() {
		return imgorvideo;
	}
	public void setImgorvideo(String imgorvideo) {
		this.imgorvideo = imgorvideo;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
    
    
    
    
    
    
    
    
    // Getters and setters
}
