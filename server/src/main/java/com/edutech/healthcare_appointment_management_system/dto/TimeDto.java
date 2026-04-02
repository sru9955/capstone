package com.edutech.healthcare_appointment_management_system.dto;

public class TimeDto {

    private String availability;

    public TimeDto() {}

    public TimeDto(String availability) {
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
