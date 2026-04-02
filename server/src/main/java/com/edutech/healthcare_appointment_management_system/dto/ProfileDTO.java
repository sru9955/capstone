package com.edutech.healthcare_appointment_management_system.dto;

public class ProfileDTO {
    private String name;
    private String email;
    private String phone;
    private String role;
    private String profileImageUrl;
    private Double consultationFee; // Only for Doctor
    private String specialty; // Only for Doctor
    private String availability; // Only for Doctor
    private String address; // Only for Patient
    private Integer age; // Only for Patient
    
    public ProfileDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    
    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
    
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
