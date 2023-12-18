package com.example.techverse.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MonthlyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String month;

    @Column(columnDefinition = "TEXT")
      private String food;

    @Column(columnDefinition = "TEXT")
    private String exercise;
    
    @Column(columnDefinition = "TEXT")
     private String toysToPlay;

    @Column(columnDefinition = "TEXT")
     private String color;

    @Column(columnDefinition = "TEXT")
     private String activity;

    @Column(columnDefinition = "TEXT")
    private String grooming;

    @Column(columnDefinition = "TEXT")
      private String enclosure;

    @Column(columnDefinition = "TEXT")
     private String clothes;

    @Column(columnDefinition = "TEXT")
    private String vaccination;

    @Column(columnDefinition = "TEXT")
     private String weight;

    @Column(columnDefinition = "TEXT")
    private String healthCare;

    @Column(columnDefinition = "TEXT")
    private String precautions;

    @Column(columnDefinition = "TEXT")
     private String pregnancyPrecautions="";



    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    // Constructors, getters, and setters

    public MonthlyDetails() {
    }

    
    @Override
	public String toString() {
		return "MonthlyDetails [id=" + id + ", month=" + month + ", food=" + food + ", exercise=" + exercise
				+ ", toysToPlay=" + toysToPlay + ", color=" + color + ", activity=" + activity + ", grooming="
				+ grooming + ", enclosure=" + enclosure + ", clothes=" + clothes + ", vaccination=" + vaccination
				+ ", weight=" + weight + ", healthCare=" + healthCare + ", precautions=" + precautions
				+ ", pregnancyPrecautions=" + pregnancyPrecautions + ", pet=" + pet + ", getId()=" + getId()
				+ ", getMonth()=" + getMonth() + ", getFood()=" + getFood() + ", getExercise()=" + getExercise()
				+ ", getToysToPlay()=" + getToysToPlay() + ", getColor()=" + getColor() + ", getActivity()="
				+ getActivity() + ", getGrooming()=" + getGrooming() + ", getEnclosure()=" + getEnclosure()
				+ ", getClothes()=" + getClothes() + ", getVaccination()=" + getVaccination() + ", getWeight()="
				+ getWeight() + ", getHealthCare()=" + getHealthCare() + ", getPrecautions()=" + getPrecautions()
				+ ", getPregnancyPrecautions()=" + getPregnancyPrecautions() + ", getPet()=" + getPet()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}


	public MonthlyDetails(String month, String food, String exercise, String toysToPlay, String color,
                          String activity, String grooming, String enclosure, String clothes,
                          String vaccination, String weight, String healthCare, String precautions,
                          String pregnancyPrecautions) {
        this.month = month;
        this.food = food;
        this.exercise = exercise;
        this.toysToPlay = toysToPlay;
        this.color = color;
        this.activity = activity;
        this.grooming = grooming;
        this.enclosure = enclosure;
        this.clothes = clothes;
        this.vaccination = vaccination;
        this.weight = weight;
        this.healthCare = healthCare;
        this.precautions = precautions;
        this.pregnancyPrecautions = pregnancyPrecautions;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public  String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public String getExercise() {
		return exercise;
	}

	public void setExercise(String exercise) {
		this.exercise = exercise;
	}

	public String getToysToPlay() {
		return toysToPlay;
	}

	public void setToysToPlay(String toysToPlay) {
		this.toysToPlay = toysToPlay;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getGrooming() {
		return grooming;
	}

	public void setGrooming(String grooming) {
		this.grooming = grooming;
	}

	public String getEnclosure() {
		return enclosure;
	}

	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}

	public String getClothes() {
		return clothes;
	}

	public void setClothes(String clothes) {
		this.clothes = clothes;
	}

	public String getVaccination() {
		return vaccination;
	}

	public void setVaccination(String vaccination) {
		this.vaccination = vaccination;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHealthCare() {
		return healthCare;
	}

	public void setHealthCare(String healthCare) {
		this.healthCare = healthCare;
	}

	public String getPrecautions() {
		return precautions;
	}

	public void setPrecautions(String precautions) {
		this.precautions = precautions;
	}

	public String getPregnancyPrecautions() {
		return pregnancyPrecautions;
	}

	public void setPregnancyPrecautions(String pregnancyPrecautions) {
		this.pregnancyPrecautions = pregnancyPrecautions;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

    // Add other getters and setters
    
    
    
}

