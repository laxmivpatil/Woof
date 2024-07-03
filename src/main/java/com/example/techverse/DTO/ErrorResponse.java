package com.example.techverse.DTO;
public class ErrorResponse {
    private boolean status;
    private String message;

    public ErrorResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean statusCode) {
		this.status = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
    

    // Getters and setters
}