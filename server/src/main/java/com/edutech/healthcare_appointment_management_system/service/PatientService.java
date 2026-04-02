package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.healthcare_appointment_management_system.dto.PatientProfileUpdateDto;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired private PatientRepository patientRepository;

    public Patient updateProfile(String username, PatientProfileUpdateDto dto) {
        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        
        if (dto.getName() != null) patient.setName(dto.getName());
        if (dto.getAge() != null) patient.setAge(dto.getAge());
        if (dto.getWeight() != null) patient.setWeight(dto.getWeight());
        if (dto.getHeight() != null) patient.setHeight(dto.getHeight());
        if (dto.getPhone() != null) patient.setPhone(dto.getPhone());
        if (dto.getAddress() != null) patient.setAddress(dto.getAddress());

        return patientRepository.save(patient);
    }

    public Patient getProfileByUsername(String username) {
        return patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
    }
}
