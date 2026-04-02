package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.AppointmentDto;
import com.edutech.healthcare_appointment_management_system.dto.PatientProfileUpdateDto;
import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.MedicalRecord;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.service.AppointmentService;
import com.edutech.healthcare_appointment_management_system.service.DoctorService;
import com.edutech.healthcare_appointment_management_system.service.MedicalRecordService;
import com.edutech.healthcare_appointment_management_system.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/api/patient")

public class PatientController {

    @Autowired private AppointmentService appointmentService;
    @Autowired private DoctorService doctorService;
    @Autowired private MedicalRecordService medicalRecordService;
    @Autowired private PatientService patientService;

    @GetMapping("/profile")
    public ResponseEntity<Patient> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(patientService.getProfileByUsername(userDetails.getUsername()));
    }

    @PutMapping("/profile")
    public ResponseEntity<Patient> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PatientProfileUpdateDto dto) {
        return ResponseEntity.ok(patientService.updateProfile(userDetails.getUsername(), dto));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/doctors/search")
    public ResponseEntity<List<Doctor>> searchDoctorsByName(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchDoctorsByName(name));
    }

    @PostMapping("/appointment")
    public ResponseEntity<Appointment> scheduleAppointment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long doctorId,
            @RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(
                appointmentService.scheduleByPatient(userDetails.getUsername(), doctorId, dto));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getMyAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(appointmentService.getByPatient(userDetails.getUsername()));
    }

    @GetMapping("/medicalrecords")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecords(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(medicalRecordService.getByPatient(userDetails.getUsername()));
    }
}
