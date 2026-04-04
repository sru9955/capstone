package com.edutech.healthcare_appointment_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;

@DiscriminatorValue("DOCTOR")
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialty;

    private String availability;

    @Column(nullable = false)
    private String email;

    private String phone;

    private Double consultationFee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MedicalRecord> medicalRecords;

    public Doctor() {}

    public Doctor(Long id, String name, String specialty, String availability, String email, String phone, Double consultationFee, User user, List<Appointment> appointments, List<MedicalRecord> medicalRecords) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.availability = availability;
        this.email = email;
        this.phone = phone;
        this.consultationFee = consultationFee;
        this.user = user;
        this.appointments = appointments;
        this.medicalRecords = medicalRecords;
    }

    public static DoctorBuilder builder() {
        return new DoctorBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public static class DoctorBuilder {
        private Long id;
        private String name;
        private String specialty;
        private String availability;
        private String email;
        private String phone;
        private Double consultationFee;
        private User user;
        private List<Appointment> appointments;
        private List<MedicalRecord> medicalRecords;

        public DoctorBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DoctorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DoctorBuilder specialty(String specialty) {
            this.specialty = specialty;
            return this;
        }

        public DoctorBuilder availability(String availability) {
            this.availability = availability;
            return this;
        }

        public DoctorBuilder email(String email) {
            this.email = email;
            return this;
        }

        public DoctorBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public DoctorBuilder consultationFee(Double consultationFee) {
            this.consultationFee = consultationFee;
            return this;
        }

        public DoctorBuilder user(User user) {
            this.user = user;
            return this;
        }

        public DoctorBuilder appointments(List<Appointment> appointments) {
            this.appointments = appointments;
            return this;
        }

        public DoctorBuilder medicalRecords(List<MedicalRecord> medicalRecords) {
            this.medicalRecords = medicalRecords;
            return this;
        }

        public Doctor build() {
            return new Doctor(id, name, specialty, availability, email, phone, consultationFee, user, appointments, medicalRecords);
        }
    }
}
