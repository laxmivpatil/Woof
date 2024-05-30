package com.example.techverse.Model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
public class Review {

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String review;
	
	
	
	@ManyToOne
    @JoinColumn(name = "product_id") // This links the Address to a specific User.
	@JsonIgnore
    private Product product;
	
	@ManyToOne
    @JoinColumn(name = "user_id") // This links the Address to a specific User.
    private User user;
	
	@ManyToOne
    @JoinColumn(name = "ngo_id") // This links the Address to a specific User.
    private NGO ngo;
	
	@ManyToOne
    @JoinColumn(name = "veterinarian_id") // This links the Address to a specific User.
    private Veterinarian veterinarian;
	
	 
    private LocalDateTime createdAt;

    
    
    

	public Review(Long id, String review, Product product, User user, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.review = review;
		this.product = product;
		this.user = user;
		this.createdAt = createdAt;
	}


	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getReview() {
		return review;
	}


	public void setReview(String review) {
		this.review = review;
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
    
    
}