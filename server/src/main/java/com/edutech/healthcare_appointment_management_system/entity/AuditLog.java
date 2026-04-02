package com.edutech.healthcare_appointment_management_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Assuming ID reference

    @Column(nullable = false)
    private String action; // e.g., UPDATE_PATIENT, BOOK_APPOINTMENT

    @Column(columnDefinition = "TEXT")
    private String details;

    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(Long id, Long userId, String action, String details, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    public static class AuditLogBuilder {
        private Long id;
        private Long userId;
        private String action;
        private String details;
        private LocalDateTime timestamp;

        public AuditLogBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AuditLogBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AuditLogBuilder action(String action) {
            this.action = action;
            return this;
        }

        public AuditLogBuilder details(String details) {
            this.details = details;
            return this;
        }

        public AuditLogBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public AuditLog build() {
            return new AuditLog(id, userId, action, details, timestamp);
        }
    }
}
