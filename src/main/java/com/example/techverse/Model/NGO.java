package com.example.techverse.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Table
@Entity
public class NGO {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
     
	  @ElementCollection
	  @CollectionTable(name = "ngo_password_history", joinColumns = @JoinColumn(name = "ngo_id"))
	    @Column(name = "password")
	    @OrderColumn
	    private List<String> lastThreePasswords;

	    // Getters and setters for other fields

	private String role;
	
    private double latitude=0L;  //
    private double longitude=0L;  //
	
    private String email;
    
    @Column(nullable = false,name="password")
    private String password;
    private String fullName;
   
    private String phone;
    
    private String ngoName;
 
    private String accountStatus; 
     
	@Column(name = "otp")
    private String otp;
    
    @Column(name = "verification")
    private String verification;
    
    @Column(name="token")
    private String token;

    @Column(name="address")
    private String address;

    @Column 
    private String NGODate;
     
    @Column 
    private String NGOCertificate;
    @Column 
    private String NGOProfile;
    
    @OneToMany(mappedBy = "ngo", cascade = CascadeType.ALL)
    private List<Story> stories = new ArrayList<>();
    
    @OneToMany(mappedBy = "ngo")
    private Set<SavedRescueRequest> savedRescueRequests = new HashSet<>();


    @OneToMany(mappedBy = "ngo", cascade = CascadeType.ALL)
    private List<AnimalRescueRequest> rescueRequests;
    
    
    
	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}

	public Set<SavedRescueRequest> getSavedRescueRequests() {
		return savedRescueRequests;
	}

	public void setSavedRescueRequests(Set<SavedRescueRequest> savedRescueRequests) {
		this.savedRescueRequests = savedRescueRequests;
	}

	public List<AnimalRescueRequest> getRescueRequests() {
		return rescueRequests;
	}

	public void setRescueRequests(List<AnimalRescueRequest> rescueRequests) {
		this.rescueRequests = rescueRequests;
	}

	public NGO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNGOProfile() {
		return NGOProfile;
	}

	public void setNGOProfile(String nGOProfile) {
		NGOProfile = nGOProfile;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNgoName() {
		return ngoName;
	}

	public void setNgoName(String ngoName) {
		this.ngoName = ngoName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNGODate() {
		return NGODate;
	}

	public void setNGODate(String nGODate) {
		NGODate = nGODate;
	}

	public String getNGOCertificate() {
		return NGOCertificate;
	}

	public void setNGOCertificate(String nGOCertificate) {
		NGOCertificate = nGOCertificate;
	}

	@Override
	public String toString() {
		return "NGO [id=" + id + ", email=" + email + ", password=" + password + ", phone=" + phone + ", ngoName="
				+ ngoName + ", accountStatus=" + accountStatus + ", otp=" + otp + ", verification=" + verification
				+ ", token=" + token + "]";
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	 

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	 public List<String> getLastThreePasswords() {
	        return lastThreePasswords;
	    }

	    public void setLastThreePasswords(List<String> lastThreePasswords) {
	        this.lastThreePasswords = lastThreePasswords;
	    }
	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	 
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	public NGO(String email,String phone,String password,String fullname,String role)	{
		this.email=email;
		this.phone=phone;
		this.password=password;
		this.fullName=fullname;
		this.role=role;
	}
}
