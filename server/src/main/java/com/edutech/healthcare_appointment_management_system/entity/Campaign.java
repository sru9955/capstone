package com.edutech.healthcare_appointment_management_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String targetAudience;

    private LocalDateTime campaignDate;

    public Campaign() {}

    public Campaign(Long id, String title, String description, String targetAudience, LocalDateTime campaignDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetAudience = targetAudience;
        this.campaignDate = campaignDate;
    }

    public static CampaignBuilder builder() {
        return new CampaignBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public LocalDateTime getCampaignDate() {
        return campaignDate;
    }

    public void setCampaignDate(LocalDateTime campaignDate) {
        this.campaignDate = campaignDate;
    }

    @PrePersist
    protected void onCreate() {
        if (this.campaignDate == null) this.campaignDate = LocalDateTime.now();
    }

    public static class CampaignBuilder {
        private Long id;
        private String title;
        private String description;
        private String targetAudience;
        private LocalDateTime campaignDate;

        public CampaignBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CampaignBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CampaignBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CampaignBuilder targetAudience(String targetAudience) {
            this.targetAudience = targetAudience;
            return this;
        }

        public CampaignBuilder campaignDate(LocalDateTime campaignDate) {
            this.campaignDate = campaignDate;
            return this;
        }

        public Campaign build() {
            return new Campaign(id, title, description, targetAudience, campaignDate);
        }
    }
}
