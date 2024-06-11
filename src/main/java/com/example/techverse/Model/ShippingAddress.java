package com.example.techverse.Model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street1;
    private String street2;
    private String city;
    private String pincode;
    private String mobile;
  //  private String alternateMobile;
   // private String landmark;
    private String state;
    private String country;
   // private String addressType;
    private boolean setDefaultAddress;
    
    @JsonIgnore
    @ManyToOne
    private User user;
    
    
    @JsonIgnore
    @ManyToOne
    private NGO ngo;
    
    @JsonIgnore
    @ManyToOne
    private Veterinarian veterinarian;
    
    
    
    
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStreet1() {
		return street1;
	}
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	 
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	 
	public boolean isSetDefaultAddress() {
		return setDefaultAddress;
	}
	public void setSetDefaultAddress(boolean setDefaultAddress) {
		this.setDefaultAddress = setDefaultAddress;
	}
    
    
    
    
    
    
    
    
    
    
    

    // Getters and setters
    // Constructors
}

