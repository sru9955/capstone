package com.edutech.healthcare_appointment_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;


@DiscriminatorValue("PATIENT")
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String address;
    private Integer age;
    private Double weight;
    private Double height;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MedicalRecord> medicalRecords;

    public Patient() {}

    public Patient(Long id, String name, String phone, String address, Integer age, Double weight, Double height, User user, List<Appointment> appointments, List<MedicalRecord> medicalRecords) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.user = user;
        this.appointments = appointments;
        this.medicalRecords = medicalRecords;
    }

    public static PatientBuilder builder() {
        return new PatientBuilder();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public static class PatientBuilder {
        private Long id;
        private String name;
        private String phone;
        private String address;
        private Integer age;
        private Double weight;
        private Double height;
        private User user;
        private List<Appointment> appointments;
        private List<MedicalRecord> medicalRecords;

        public PatientBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PatientBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PatientBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public PatientBuilder address(String address) {
            this.address = address;
            return this;
        }

        public PatientBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public PatientBuilder weight(Double weight) {
            this.weight = weight;
            return this;
        }

        public PatientBuilder height(Double height) {
            this.height = height;
            return this;
        }

        public PatientBuilder user(User user) {
            this.user = user;
            return this;
        }

        public PatientBuilder appointments(List<Appointment> appointments) {
            this.appointments = appointments;
            return this;
        }

        public PatientBuilder medicalRecords(List<MedicalRecord> medicalRecords) {
            this.medicalRecords = medicalRecords;
            return this;
        }

        public Patient build() {
            return new Patient(id, name, phone, address, age, weight, height, user, appointments, medicalRecords);
        }
    }
}
