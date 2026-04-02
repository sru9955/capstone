package com.edutech.healthcare_appointment_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    @JsonIgnore
    private Appointment appointment;

    private Integer age;
    private Double weight;
    private Double height;
    private String bp;
    private Double sugarLevel;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @ElementCollection
    @CollectionTable(name = "medical_record_prescriptions", joinColumns = @JoinColumn(name = "medical_record_id"))
    private List<PrescriptionItem> prescription;

    private String notes;
    private String allergies;

    private LocalDateTime recordDate;

    public MedicalRecord() {}

    public MedicalRecord(Long id, Patient patient, Doctor doctor, Appointment appointment, Integer age, Double weight, Double height, String bp, Double sugarLevel, String symptoms, String diagnosis, List<PrescriptionItem> prescription, String notes, String allergies, LocalDateTime recordDate) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.appointment = appointment;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.bp = bp;
        this.sugarLevel = sugarLevel;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.allergies = allergies;
        this.recordDate = recordDate;
    }

    public static MedicalRecordBuilder builder() {
        return new MedicalRecordBuilder();
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

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public Double getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(Double sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<PrescriptionItem> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<PrescriptionItem> prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }

    @PrePersist
    protected void onCreate() {
        this.recordDate = LocalDateTime.now();
    }

    public static class MedicalRecordBuilder {
        private Long id;
        private Patient patient;
        private Doctor doctor;
        private Appointment appointment;
        private Integer age;
        private Double weight;
        private Double height;
        private String bp;
        private Double sugarLevel;
        private String symptoms;
        private String diagnosis;
        private List<PrescriptionItem> prescription;
        private String notes;
        private String allergies;
        private LocalDateTime recordDate;

        public MedicalRecordBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MedicalRecordBuilder patient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public MedicalRecordBuilder doctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public MedicalRecordBuilder appointment(Appointment appointment) {
            this.appointment = appointment;
            return this;
        }

        public MedicalRecordBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public MedicalRecordBuilder weight(Double weight) {
            this.weight = weight;
            return this;
        }

        public MedicalRecordBuilder height(Double height) {
            this.height = height;
            return this;
        }

        public MedicalRecordBuilder bp(String bp) {
            this.bp = bp;
            return this;
        }

        public MedicalRecordBuilder sugarLevel(Double sugarLevel) {
            this.sugarLevel = sugarLevel;
            return this;
        }

        public MedicalRecordBuilder symptoms(String symptoms) {
            this.symptoms = symptoms;
            return this;
        }

        public MedicalRecordBuilder diagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
            return this;
        }

        public MedicalRecordBuilder prescription(List<PrescriptionItem> prescription) {
            this.prescription = prescription;
            return this;
        }

        public MedicalRecordBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public MedicalRecordBuilder allergies(String allergies) {
            this.allergies = allergies;
            return this;
        }

        public MedicalRecordBuilder recordDate(LocalDateTime recordDate) {
            this.recordDate = recordDate;
            return this;
        }

        public MedicalRecord build() {
            return new MedicalRecord(id, patient, doctor, appointment, age, weight, height, bp, sugarLevel, symptoms, diagnosis, prescription, notes, allergies, recordDate);
        }
    }
}
