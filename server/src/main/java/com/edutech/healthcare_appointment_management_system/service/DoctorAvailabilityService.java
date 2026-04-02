package com.edutech.healthcare_appointment_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutech.healthcare_appointment_management_system.dto.DoctorAvailabilityDto;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.DoctorAvailability;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.DoctorAvailabilityRepository;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DoctorAvailabilityService {

    @Autowired private DoctorAvailabilityRepository availabilityRepository;
    @Autowired private DoctorRepository doctorRepository;

    public DoctorAvailability addAvailability(String username, DoctorAvailabilityDto dto) {
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        DoctorAvailability availability = new DoctorAvailability(
                doctor, dto.getDate(), dto.getStartTime(), dto.getEndTime()
        );
        return availabilityRepository.save(availability);
    }

    public List<DoctorAvailability> getAvailableSlots(Long doctorId, LocalDate date) {
        return availabilityRepository.findByDoctorIdAndDateAndIsBookedFalse(doctorId, date);
    }

    public void deleteAvailability(Long availabilityId, String username) {
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
        DoctorAvailability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));
                
        if (!availability.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Not authorized to delete this availability");
        }
        
        if (availability.getIsBooked()) {
            throw new IllegalStateException("Cannot delete a booked availability slot");
        }
        
        availabilityRepository.delete(availability);
    }
}
