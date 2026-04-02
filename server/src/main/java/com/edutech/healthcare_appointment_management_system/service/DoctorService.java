package com.edutech.healthcare_appointment_management_system.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutech.healthcare_appointment_management_system.dto.TimeDto;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;

import java.util.List;

@Service
@Transactional
public class DoctorService {

    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);

    @Autowired private DoctorRepository doctorRepository;
    @Autowired private NotificationService notificationService;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }

    public Doctor getDoctorByUsername(String username) {
        return doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + username));
    }

    public Doctor updateAvailability(String username, TimeDto dto) {
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + username));
        doctor.setAvailability(dto.getAvailability());
        doctor = doctorRepository.save(doctor);

        notificationService.createNotification(doctor.getUser().getId(),
                "Your availability has been updated to: " + dto.getAvailability(), "AVAILABILITY");
        log.info("Doctor {} updated availability: {}", username, dto.getAvailability());
        return doctor;
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }
}
