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
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
 

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
@Table
@Entity
 public class User  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
     
	@JsonIgnore
	  @ElementCollection
	  @CollectionTable(name = "user_password_history", joinColumns = @JoinColumn(name = "user_id"))
	    @Column(name = "password")
	    @OrderColumn
	    private List<String> lastThreePasswords;

	    // Getters and setters for other fields

	  private String profile;
	private String role;
	
    private String email;
    
    @Column(nullable = false,name="password")
    private String password;
  
    private Long age;
    private String phone;
    
    private String fullName;
 
    private String accountStatus; 
    private String gender; 
     
	@Column(name = "otp")
    private String otp;
    
    @Column(name = "verification")
    private String verification;
    
    @Column(name="token")
    private String token;
    
    
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<SavedRescueRequest> savedRescueRequests = new HashSet<>();

     
    
   
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_saved_pets",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id")
    )
    private Set<Pet> savedPets = new HashSet<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShippingAddress> shippingAddresses = new ArrayList<>();

    public List<ShippingAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public void setShippingAddresses(List<ShippingAddress> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
    }

	public Set<SavedRescueRequest> getSavedRescueRequests() {
		return savedRescueRequests;
	}

	public void setSavedRescueRequests(Set<SavedRescueRequest> savedRescueRequests) {
		this.savedRescueRequests = savedRescueRequests;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", phone=" + phone + ", fullName="
				+ fullName + ", accountStatus=" + accountStatus + ", otp=" + otp + ", verification=" + verification
				+ ", token=" + token + "]";
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

 

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@JsonIgnore
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	    private List<AnimalRescueRequest> rescueRequests;

	public List<AnimalRescueRequest> getRescueRequests() {
		return rescueRequests;
	}

	public void setRescueRequests(List<AnimalRescueRequest> rescueRequests) {
		this.rescueRequests = rescueRequests;
	}

	
	@JsonIgnore
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Notification> notifications = new ArrayList<>();

	public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    // Add a notification to the user's notifications
    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setUser(this);
    }
    
    
    @Column(name = "notifications_enabled",columnDefinition = "BOOLEAN")
    private Boolean notificationsEnabled;

    // ... (other getters and setters)
    
    
    public Boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public Boolean getNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
	
	@ManyToMany
    @JoinTable(
            name = "user_favorite_products",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> favoriteProducts = new ArrayList<>();
	
	
	
	
	
	public List<Product> getFavoriteProducts() {
		return favoriteProducts;
	}

	public void setFavoriteProducts(List<Product> favoriteProducts) {
		this.favoriteProducts = favoriteProducts;
	}

	@JsonIgnore
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	    private List<Story> stories;
	 
	 public List<Story> getStories() {
			return stories;
		}

		public void setStories(List<Story> stories) {
			this.stories = stories;
		}
		
		@JsonIgnore	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "user_story_followers",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "story_id")
    )
    private List<Story> storiesFollowed;

	    // Constructors, getters, setters, and other methods...

	    // Add a story to the user's created stories
	     

	    // Add a story to the user's followed stories
	    public void addStoryFollowed(Story story) {
	    	System.out.println("Story=>"+story.getId()+story.getCaption());
	        storiesFollowed.add(story);
	        story.getFollowers().add(this);
	    }
	    
	    
	    
	    public List<Story> getStoriesFollowed() {
			return storiesFollowed;
		}

		public void setStoriesFollowed(List<Story> storiesFollowed) {
			this.storiesFollowed = storiesFollowed;
		}

		
		// Define a many-to-many relationship for followers
		
		@JsonIgnore
	    @ManyToMany
	    @JoinTable(
	        name = "follower_relationship",
	        joinColumns = @JoinColumn(name = "following_id"),
	        inverseJoinColumns = @JoinColumn(name = "follower_id")
	    )
	    private Set<User> followers = new HashSet<>();

	    // Constructors, getters, setters, and other fields

	    // Method to add a follower
	    public void addFollower(User follower) {
	        followers.add(follower);
	    }

	    // Method to remove a follower
	    public void removeFollower(User follower) {
	        followers.remove(follower);
	    }

	    // Getter for followers
	    public Set<User> getFollowers() {
	        return followers;
	    }

		public void setFollowers(Set<User> followers) {
			this.followers = followers;
		}

	 
		
	    // Define a one-to-many relationship with comments
		@JsonIgnore
	    @OneToMany(mappedBy = "commenter")
	    private List<Comment> comments;

		public List<Comment> getComments() {
			return comments;
		}

		public void setComments(List<Comment> comments) {
			this.comments = comments;
		}

	    // Constructors, getters, and setters for User
		
		@Column
		 private String fingerprint_template;

		public String getFingerprint_template() {
			return fingerprint_template;
		}

		public void setFingerprint_template(String fingerprint_template) {
			this.fingerprint_template = fingerprint_template;
		}
		 
		@Column
		 private String face_template;

		public String getFace_template() {
			return face_template;
		}

		public void setFace_template(String face_template) {
			this.face_template = face_template;
		}
		
		@Column
	    private Double latitude;

	    @Column
	    private Double longitude;

		public Double getLatitude() {
			return latitude;
		}

		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}

		public Double getLongitude() {
			return longitude;
		}

		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		} 
	    
public User(String email,String phone,String password,String fullname,String role)	{
	this.email=email;
	this.phone=phone;
	this.password=password;
	this.fullName=fullname;
	this.role=role;
}

public Set<Pet> getSavedPets() {
	return savedPets;
}

public void setSavedPets(Set<Pet> savedPets) {
	this.savedPets = savedPets;
}
	    
	    

}