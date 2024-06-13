package com.example.techverse.DTO;

 

public class ApiResponse1 {
	
	
	private boolean status;
	private String message;
	private String jwt;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	 
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
	
	
	public ApiResponse1(boolean status, String message, String jwt) {
		super();
		this.status = status;
		this.message = message;
		this.jwt = jwt;
	}
	public ApiResponse1() {
	 
		// TODO Auto-generated constructor stub
	}

	
	
	

}
