package com.edutech.healthcare_appointment_management_system.entity;

import javax.persistence.Embeddable;

@Embeddable
public class PrescriptionItem {

    private String medicationName;
    private String dosage;
    private String frequency;
    private String route;
    private String purpose;

    public PrescriptionItem() {}

    public PrescriptionItem(String medicationName, String dosage, String frequency, String route, String purpose) {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.route = route;
        this.purpose = purpose;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
