package com.example.techverse.DTO;

import com.example.techverse.Model.NGO;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;

public class RegistrationDTO {

	private Long id;
    private String email;
    private String phone;
    private String password;
    private String fullname; 
    private String role;

    // Default constructor
    public  RegistrationDTO() {
    }

    
    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	// Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
 
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

     public RegistrationDTO toDTO(User user) {
    	 
    	 
    	 RegistrationDTO registrationDTO =new RegistrationDTO();
    	 registrationDTO.id=user.getId();
    	 registrationDTO.email=user.getEmail();
    	 registrationDTO.phone=user.getPhone();
    	 registrationDTO.password=user.getPassword();
    	 registrationDTO.fullname=user.getFullName(); 
    	 registrationDTO.role=user.getRole();
    	 return registrationDTO;
    	 
     }
public RegistrationDTO toDTO(Veterinarian user) {
    	 
    	 RegistrationDTO registrationDTO =new RegistrationDTO();
    	 registrationDTO.id=user.getId();
    	 registrationDTO.email=user.getEmail();
    	 registrationDTO.phone=user.getPhone();
    	 registrationDTO.password=user.getPassword();
    	 registrationDTO.fullname=user.getFullName(); 
    	 registrationDTO.role=user.getRole();
    	 return registrationDTO;
    	 
     }

public RegistrationDTO toDTO(NGO user) {
	 
	 RegistrationDTO registrationDTO =new RegistrationDTO();
	 registrationDTO.id=user.getId();
	 registrationDTO.email=user.getEmail();
	 registrationDTO.phone=user.getPhone();
	 registrationDTO.password=user.getPassword();
	 registrationDTO.fullname=user.getFullName(); 
	 registrationDTO.role=user.getRole();
	 return registrationDTO;
	 
}

}
