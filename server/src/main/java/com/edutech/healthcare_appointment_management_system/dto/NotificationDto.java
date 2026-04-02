package com.edutech.healthcare_appointment_management_system.dto;

import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private Long userId;
    private boolean isRead;
    private String type;

    public NotificationDto() {}

    public NotificationDto(Long id, String message, LocalDateTime timestamp, Long userId, boolean isRead, String type) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.userId = userId;
        this.isRead = isRead;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
