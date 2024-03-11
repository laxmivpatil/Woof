package com.example.techverse.DTO;
 

import com.example.techverse.Model.User;

public class UserBasicInfoDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String role;

    // Constructors, getters, and setters

    public UserBasicInfoDTO() {
        // Default constructor
    }

    public UserBasicInfoDTO(Long id, String email, String fullName, String phone, String role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }
    
    
    
    public UserBasicInfoDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.role = user.getRole();
    }

    // Getters and setters

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
