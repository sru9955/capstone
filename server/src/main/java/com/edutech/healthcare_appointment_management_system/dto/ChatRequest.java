package com.edutech.healthcare_appointment_management_system.dto;

public class ChatRequest {

    private String message;
    private String role;

    public ChatRequest() {}

    public ChatRequest(String message, String role) {
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
