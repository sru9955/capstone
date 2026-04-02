package com.edutech.healthcare_appointment_management_system.dto;

public class LoginResponse {
    private String token;
    private String role;
    private String username;
    private Long userId;
    private String message;

    public LoginResponse() {}

    public LoginResponse(String token, String role, String username, Long userId, String message) {
        this.token = token;
        this.role = role;
        this.username = username;
        this.userId = userId;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
