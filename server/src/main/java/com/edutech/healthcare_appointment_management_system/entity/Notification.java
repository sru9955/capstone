package com.edutech.healthcare_appointment_management_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean isRead;

    private String type; // APPOINTMENT, AVAILABILITY, SYSTEM

    public Notification() {}

    public Notification(Long id, String message, LocalDateTime timestamp, Long userId, boolean isRead, String type) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.userId = userId;
        this.isRead = isRead;
        this.type = type;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
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

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    public static class NotificationBuilder {
        private Long id;
        private String message;
        private LocalDateTime timestamp;
        private Long userId;
        private boolean isRead;
        private String type;

        public NotificationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NotificationBuilder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public NotificationBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public NotificationBuilder isRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public NotificationBuilder type(String type) {
            this.type = type;
            return this;
        }

        public Notification build() {
            return new Notification(id, message, timestamp, userId, isRead, type);
        }
    }
}
