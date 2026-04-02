package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.AppointmentDto;
import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.MedicalRecord;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.entity.Receptionist;
import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;
import com.edutech.healthcare_appointment_management_system.service.AppointmentService;
import com.edutech.healthcare_appointment_management_system.service.MedicalRecordService;
import com.edutech.healthcare_appointment_management_system.service.ReceptionistService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/receptionist")
public class ReceptionistController {

    @Autowired private AppointmentService appointmentService;
    @Autowired private PatientRepository patientRepository;
    @Autowired private ReceptionistService receptionistService;
    @Autowired private MedicalRecordService medicalRecordService;

    @GetMapping("/profile")
    public ResponseEntity<Receptionist> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(receptionistService.getProfileByUsername(userDetails.getUsername()));
    }

    @PutMapping("/profile")
    public ResponseEntity<Receptionist> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> data) {
        return ResponseEntity.ok(receptionistService.updateProfile(userDetails.getUsername(), data));
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @GetMapping("/patients/search")
    public ResponseEntity<List<Patient>> searchPatientsByName(@RequestParam String name) {
        return ResponseEntity.ok(patientRepository.findByNameContainingIgnoreCase(name));
    }

    @GetMapping("/patients/{patientId}/history")
    public ResponseEntity<List<MedicalRecord>> getPatientHistory(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getByPatientId(patientId));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @GetMapping("/appointments/today")
    public ResponseEntity<List<Appointment>> getTodayAppointments() {
        return ResponseEntity.ok(appointmentService.getTodayAppointments());
    }

    @GetMapping("/appointments/yesterday")
    public ResponseEntity<List<Appointment>> getYesterdayAppointments() {
        return ResponseEntity.ok(appointmentService.getYesterdayAppointments());
    }

    @GetMapping({"/appointments/range", "/appointments/filter"})
    public ResponseEntity<List<Appointment>> getAppointmentsByRange(
            @RequestParam(required = false, name = "start") String start,
            @RequestParam(required = false, name = "end") String end,
            @RequestParam(required = false, name = "startDate") String startDate,
            @RequestParam(required = false, name = "endDate") String endDate) {
        String actualStart = startDate != null ? startDate : start;
        String actualEnd = endDate != null ? endDate : end;
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(
                LocalDateTime.parse(actualStart), LocalDateTime.parse(actualEnd)));
    }

    @GetMapping("/doctors/search")
    public ResponseEntity<List<Doctor>> searchDoctorsByName(@RequestParam String name) {
        return ResponseEntity.ok(receptionistService.searchDoctors(name));
    }

    @PostMapping("/appointment")
    public ResponseEntity<Appointment> scheduleAppointment(@RequestBody AppointmentDto dto) {
        if (dto.getPatientName() != null && dto.getDoctorName() != null) {
            return ResponseEntity.ok(appointmentService.scheduleByReceptionistByName(dto));
        }
        return ResponseEntity.ok(appointmentService.scheduleByReceptionist(dto));
    }

    @PutMapping("/appointment-reschedule/{id}")
    public ResponseEntity<Appointment> rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(appointmentService.reschedule(id, dto));
    }
}
