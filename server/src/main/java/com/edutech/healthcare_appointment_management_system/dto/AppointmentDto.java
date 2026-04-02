package com.edutech.healthcare_appointment_management_system.dto;

public class AppointmentDto {

    private String appointmentTime;
    private String notes;
    private String status;
    private Long doctorId;
    private Long patientId;
    private String patientName;
    private String doctorName;
    private Integer age;
    private Double weight;
    private String problem;

    public AppointmentDto() {}

    public AppointmentDto(String appointmentTime, String notes, String status, Long doctorId, Long patientId, String patientName, String doctorName, Integer age, Double weight, String problem) {
        this.appointmentTime = appointmentTime;
        this.notes = notes;
        this.status = status;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.age = age;
        this.weight = weight;
        this.problem = problem;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
