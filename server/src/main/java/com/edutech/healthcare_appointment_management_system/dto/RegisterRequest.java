package com.edutech.healthcare_appointment_management_system.dto;

public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String specialty;
    private String availability;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String email, String name, String phone, String address, String specialty, String availability) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.specialty = specialty;
        this.availability = availability;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
