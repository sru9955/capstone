package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.MedicalRecordDto;
import com.edutech.healthcare_appointment_management_system.entity.MedicalRecord;
import com.edutech.healthcare_appointment_management_system.repository.MedicalRecordRepository;
import com.edutech.healthcare_appointment_management_system.service.MedicalRecordService;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@CrossOrigin(origins = "*")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    // Doctor creates a medical record
    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(
            @RequestParam Long appointmentId,
            @RequestBody MedicalRecordDto dto,
            Authentication authentication) {
        String username = authentication.getName();
        MedicalRecord record = medicalRecordService.addRecord(appointmentId, username, dto);
        return ResponseEntity.ok(record);
    }

    // Receptionist/Admin viewing all
    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAll());
    }

    // Role-agnostic request to view a specific patient's history based on their DB ID.
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecord>> getRecordsByPatientId(
            @PathVariable Long patientId,
            Authentication authentication) {
        // Because a patient should only see their own, Receptionists can see all, Doctors can only see patients they've treated.
        // We'll trust SecurityConfig to handle base route authorities and the service to handle deep checks if needed.
        return ResponseEntity.ok(medicalRecordService.getByPatientId(patientId));
    }

    // Fetch single record by ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getRecordById(@PathVariable Long id) {
        return medicalRecordRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
}
