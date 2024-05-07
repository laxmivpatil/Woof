package com.example.techverse.Model;

 

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SavedRescueRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ngo_id")
    private NGO ngo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private Veterinarian veterinarian;

    @ManyToOne
    @JoinColumn(name = "rescue_request_id")
    private AnimalRescueRequest rescueRequest;

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

	public AnimalRescueRequest getRescueRequest() {
		return rescueRequest;
	}

	public void setRescueRequest(AnimalRescueRequest rescueRequest) {
		this.rescueRequest = rescueRequest;
	}
    
    
    
    
    

    // Additional fields if needed, such as timestamp, status, etc.

    // Getters and setters
}
