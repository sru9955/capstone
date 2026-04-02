package com.edutech.healthcare_appointment_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    private String status; // Pending, Completed, Cancelled

    @Column(columnDefinition = "TEXT")
    private String problem;

    private String notes;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private MedicalRecord medicalRecord;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private Boolean isReminded30m = false;
    private Boolean isReminded5m = false;
    
    private Double fee = 0.0;

    public Appointment() {}

    public Appointment(Long id, Patient patient, Doctor doctor, LocalDateTime appointmentTime, String status, String problem, String notes, MedicalRecord medicalRecord, LocalDateTime createdAt, Boolean isReminded30m, Boolean isReminded5m, Double fee) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.problem = problem;
        this.notes = notes;
        this.medicalRecord = medicalRecord;
        this.createdAt = createdAt;
        this.isReminded30m = isReminded30m != null ? isReminded30m : false;
        this.isReminded5m = isReminded5m != null ? isReminded5m : false;
        this.fee = fee;
    }

    public static AppointmentBuilder builder() {
        return new AppointmentBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Boolean getIsReminded30m() {
        return isReminded30m;
    }
    
    public void setIsReminded30m(Boolean isReminded30m) {
        this.isReminded30m = isReminded30m;
    }
    
    public Boolean getIsReminded5m() {
        return isReminded5m;
    }
    
    public void setIsReminded5m(Boolean isReminded5m) {
        this.isReminded5m = isReminded5m;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "Pending";
    }

    public static class AppointmentBuilder {
        private Long id;
        private Patient patient;
        private Doctor doctor;
        private LocalDateTime appointmentTime;
        private String status;
        private String problem;
        private String notes;
        private MedicalRecord medicalRecord;
        private LocalDateTime createdAt;
        private Boolean isReminded30m;
        private Boolean isReminded5m;
        private Double fee;

        public AppointmentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AppointmentBuilder patient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public AppointmentBuilder doctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public AppointmentBuilder appointmentTime(LocalDateTime appointmentTime) {
            this.appointmentTime = appointmentTime;
            return this;
        }

        public AppointmentBuilder status(String status) {
            this.status = status;
            return this;
        }

        public AppointmentBuilder problem(String problem) {
            this.problem = problem;
            return this;
        }

        public AppointmentBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public AppointmentBuilder medicalRecord(MedicalRecord medicalRecord) {
            this.medicalRecord = medicalRecord;
            return this;
        }

        public AppointmentBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public AppointmentBuilder isReminded30m(Boolean isReminded30m) {
            this.isReminded30m = isReminded30m;
            return this;
        }

        public AppointmentBuilder isReminded5m(Boolean isReminded5m) {
            this.isReminded5m = isReminded5m;
            return this;
        }

        public AppointmentBuilder fee(Double fee) {
            this.fee = fee;
            return this;
        }

        public Appointment build() {
            return new Appointment(id, patient, doctor, appointmentTime, status, problem, notes, medicalRecord, createdAt, isReminded30m, isReminded5m, fee);
        }
    }
}
