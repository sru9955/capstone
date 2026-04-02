package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.MedicalRecordDto;
import com.edutech.healthcare_appointment_management_system.dto.TimeDto;
import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.MedicalRecord;
import com.edutech.healthcare_appointment_management_system.service.AppointmentService;
import com.edutech.healthcare_appointment_management_system.service.DoctorService;
import com.edutech.healthcare_appointment_management_system.service.MedicalRecordService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")

public class DoctorController {

    @Autowired private AppointmentService appointmentService;
    @Autowired private DoctorService doctorService;
    @Autowired private MedicalRecordService medicalRecordService;

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, userDetails.getUsername(), status));
    }

    @PostMapping("/appointments/{id}/medicalrecord")
    public ResponseEntity<MedicalRecord> addMedicalRecord(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MedicalRecordDto dto) {
        return ResponseEntity.ok(medicalRecordService.addRecord(id, userDetails.getUsername(), dto));
    }

    @GetMapping("/patients/{patientId}/history")
    public ResponseEntity<List<MedicalRecord>> getPatientHistory(
            @PathVariable Long patientId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(medicalRecordService.getPatientHistoryForDoctor(patientId, userDetails.getUsername()));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getMyAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(appointmentService.getByDoctor(userDetails.getUsername()));
    }

    @GetMapping({"/appointments/range", "/appointments/filter"})
    public ResponseEntity<List<Appointment>> getAppointmentsByRange(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false, name = "start") String start,
            @RequestParam(required = false, name = "end") String end,
            @RequestParam(required = false, name = "startDate") String startDate,
            @RequestParam(required = false, name = "endDate") String endDate) {
        String actualStart = startDate != null ? startDate : start;
        String actualEnd = endDate != null ? endDate : end;
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRangeForDoctor(
                userDetails.getUsername(), LocalDateTime.parse(actualStart), LocalDateTime.parse(actualEnd)));
    }

    @PostMapping("/availability")
    public ResponseEntity<Map<String, String>> updateAvailability(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TimeDto dto) {
        Doctor doctor = doctorService.updateAvailability(userDetails.getUsername(), dto);
        return ResponseEntity.ok(Map.of(
                "message", "Availability updated successfully",
                "availability", doctor.getAvailability()
        ));
    }

    @GetMapping("/profile")
    public ResponseEntity<Doctor> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(doctorService.getDoctorByUsername(userDetails.getUsername()));
    }
}
