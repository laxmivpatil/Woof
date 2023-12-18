package com.example.techverse.DTO;

import com.example.techverse.Model.User;

public class CommentRequest {
    private String text;
    private Long user_id;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

    // Constructors, getters, and setters
    
    
}
