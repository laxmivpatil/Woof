package com.example.techverse.DTO;

public class UserNotificationSettingsResponse {
    private Long userId;
    private Boolean notificationsEnabled;

    public UserNotificationSettingsResponse(Long userId, Boolean notificationsEnabled) {
        this.userId = userId;
        this.notificationsEnabled = notificationsEnabled;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
