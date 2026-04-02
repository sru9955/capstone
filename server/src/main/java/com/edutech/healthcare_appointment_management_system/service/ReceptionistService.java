package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.Receptionist;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.ReceptionistRepository;

import java.util.Map;

@Service
public class ReceptionistService {

    @Autowired private ReceptionistRepository receptionistRepository;
    @Autowired private DoctorService doctorService;

    public java.util.List<Doctor> searchDoctors(String name) {
        return doctorService.searchDoctorsByName(name);
    }

    public Receptionist getProfileByUsername(String username) {
        return receptionistRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Receptionist profile not found"));
    }

    public Receptionist updateProfile(String username, Map<String, String> data) {
        Receptionist receptionist = receptionistRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Receptionist profile not found"));

        if (data.containsKey("name")) receptionist.setName(data.get("name"));
        if (data.containsKey("phone")) receptionist.setPhone(data.get("phone"));

        return receptionistRepository.save(receptionist);
    }
}
