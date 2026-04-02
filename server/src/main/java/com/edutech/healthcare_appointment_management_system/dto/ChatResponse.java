package com.edutech.healthcare_appointment_management_system.dto;

public class ChatResponse {
    private String reply;
    private String type; // "bot" or "system"

    public ChatResponse() {}

    public ChatResponse(String reply, String type) {
        this.reply = reply;
        this.type = type;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
