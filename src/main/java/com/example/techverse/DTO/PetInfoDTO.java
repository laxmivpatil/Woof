package com.example.techverse.DTO;

public class PetInfoDTO {
    private Long id;
    private String petName;
    private String gender="";
    private String description="";
    private String img1="";
    private String img2="";
    private String img3="";
    

    // Constructors, getters, and setters

    // Constructors
    public PetInfoDTO(Long id, String petName, String gender, String description,String img1,String img2,String img3) {
        this.id = id;
        this.petName = petName;
        this.gender = gender;
        this.description = description;
        this.img1=img1;
        this.img2=img2;
        this.img3=img3;
    }
    public PetInfoDTO(Long id, String petName,String img1,String img2,String img3) {
        this.id = id;
        this.petName = petName;
        this.img1=img1;
        this.img2=img2;
        this.img3=img3;
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

	public String getImg1() {
		return img1;
	}

	public void setImg1(String img1) {
		this.img1 = img1;
	}

	public String getImg2() {
		return img2;
	}

	public void setImg2(String img2) {
		this.img2 = img2;
	}

	public String getImg3() {
		return img3;
	}

	public void setImg3(String img3) {
		this.img3 = img3;
	}

    // Getters and setters
    // ...
    
    
    
    
}

