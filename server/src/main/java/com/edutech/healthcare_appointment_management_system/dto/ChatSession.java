package com.edutech.healthcare_appointment_management_system.dto;

public class ChatSession {
    private String username;
    private String state = "IDLE"; // IDLE, WAITING_FOR_ISSUE, WAITING_FOR_DOCTOR, WAITING_FOR_TIME
    private String issue;
    private Long doctorId;
    private String appointmentTime;

    public ChatSession() {}

    public ChatSession(String username, String state, String issue, Long doctorId, String appointmentTime) {
        this.username = username;
        this.state = state;
        this.issue = issue;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
