package com.edutech.healthcare_appointment_management_system.dto;

import java.util.List;

import com.edutech.healthcare_appointment_management_system.entity.PrescriptionItem;

public class MedicalRecordDto {
    private Integer age;
    private Double weight;
    private Double height;
    private String bp;
    private Double sugarLevel;
    private String symptoms;
    private String diagnosis;
    private List<PrescriptionItem> prescription;
    private String notes;
    private String allergies;

    public MedicalRecordDto() {}

    public MedicalRecordDto(Integer age, Double weight, Double height, String bp, Double sugarLevel, String symptoms, String diagnosis, List<PrescriptionItem> prescription, String notes, String allergies) {
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.bp = bp;
        this.sugarLevel = sugarLevel;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.allergies = allergies;
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

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public Double getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(Double sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<PrescriptionItem> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<PrescriptionItem> prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
