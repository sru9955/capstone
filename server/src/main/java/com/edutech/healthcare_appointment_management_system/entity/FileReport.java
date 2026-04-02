package com.edutech.healthcare_appointment_management_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_reports")
public class FileReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private String fileUrl; // Path or S3 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    private LocalDateTime uploadedAt;

    public FileReport() {}

    public FileReport(Long id, String fileName, String fileType, String fileUrl, MedicalRecord medicalRecord, LocalDateTime uploadedAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.medicalRecord = medicalRecord;
        this.uploadedAt = uploadedAt;
    }

    public static FileReportBuilder builder() {
        return new FileReportBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }

    public static class FileReportBuilder {
        private Long id;
        private String fileName;
        private String fileType;
        private String fileUrl;
        private MedicalRecord medicalRecord;
        private LocalDateTime uploadedAt;

        public FileReportBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FileReportBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public FileReportBuilder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public FileReportBuilder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public FileReportBuilder medicalRecord(MedicalRecord medicalRecord) {
            this.medicalRecord = medicalRecord;
            return this;
        }

        public FileReportBuilder uploadedAt(LocalDateTime uploadedAt) {
            this.uploadedAt = uploadedAt;
            return this;
        }

        public FileReport build() {
            return new FileReport(id, fileName, fileType, fileUrl, medicalRecord, uploadedAt);
        }
    }
}
