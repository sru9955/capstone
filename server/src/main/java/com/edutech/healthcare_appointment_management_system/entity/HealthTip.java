package com.edutech.healthcare_appointment_management_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_tips")
public class HealthTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category; // Diet, Fitness, Lifestyle

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createdAt;

    public HealthTip() {}

    public HealthTip(Long id, String category, String content, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static HealthTipBuilder builder() {
        return new HealthTipBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Simple Builder class to replace Lombok @Builder
    public static class HealthTipBuilder {
        private Long id;
        private String category;
        private String content;
        private LocalDateTime createdAt;

        public HealthTipBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public HealthTipBuilder category(String category) {
            this.category = category;
            return this;
        }

        public HealthTipBuilder content(String content) {
            this.content = content;
            return this;
        }

        public HealthTipBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public HealthTip build() {
            return new HealthTip(id, category, content, createdAt);
        }
    }
}
