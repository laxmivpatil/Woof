package com.example.techverse.DTO;



import com.example.techverse.Model.NotificationType;

 

public class CreateNotificationRequest {
    private Long user_id;
    private NotificationType type;
    private String message;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

