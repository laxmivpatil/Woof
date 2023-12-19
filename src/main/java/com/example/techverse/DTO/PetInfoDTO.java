package com.example.techverse.DTO;

public class PetInfoDTO {
    private Long id;
    private String petName;
    private String gender="";
    private String description="";

    // Constructors, getters, and setters

    // Constructors
    public PetInfoDTO(Long id, String petName, String gender, String description) {
        this.id = id;
        this.petName = petName;
        this.gender = gender;
        this.description = description;
    }

    // Constructors, getters, and setters

    // Constructors
    public PetInfoDTO(Long id, String petName) {
        this.id = id;
        this.petName = petName;
    }

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

    // Getters and setters
    // ...
    
    
    
    
}

