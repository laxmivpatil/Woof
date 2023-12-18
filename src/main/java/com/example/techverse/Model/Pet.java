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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
    private List<MonthlyDetails> monthlyDetails;
    // Constructors, getters, and setters

   
    
     

	public Long getId() {
		return id;
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
