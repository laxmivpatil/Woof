package com.example.techverse.Model;
import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000) // or a different length based on your requirements
    private String petName;
    @Column(length = 1000) // or a different length based on your requirements
    private String petCategory;
    @Column(length = 1000) // or a different length based on your requirements
    private String gender;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String img1;
    @Column(columnDefinition = "TEXT")
    private String img2;
    @Column(columnDefinition = "TEXT")
    private String img3;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
    private List<MonthlyDetails> monthlyDetails;
    // Constructors, getters, and setters

   
    
     

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Pet [id=" + id + ", petName=" + petName + ", petCategory=" + petCategory + ", gender=" + gender
				+ ", description=" + description + ", img1=" + img1 + ", img2=" + img2 + ", img3=" + img3
				+ ", monthlyDetails=" + monthlyDetails + "]";
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

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetCategory() {
		return petCategory;
	}

	public void setPetCategory(String petCategory) {
		this.petCategory = petCategory;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<MonthlyDetails> getMonthlyDetails() {
		return monthlyDetails;
	}

	public void setMonthlyDetails(List<MonthlyDetails> monthlyDetails) {
		this.monthlyDetails = monthlyDetails;
	}

	

    
    
    
    // Add other getters and setters
}
