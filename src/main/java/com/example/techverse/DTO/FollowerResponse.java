package com.example.techverse.DTO;

public class FollowerResponse {
    private Long user_id;
    private String email;

    public FollowerResponse(Long user_id, String email) {
        this.user_id = user_id;
        this.email = email;
    }

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    
    

    // Getters and setters...
}

