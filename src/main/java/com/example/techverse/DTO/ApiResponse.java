package com.example.techverse.DTO;

import java.util.Map;

public class ApiResponse {
    private boolean success;
    private String message;
    private Map<String, Object> data;


    
    public ApiResponse(boolean success, String message, Map<String, Object> data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

