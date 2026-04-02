package com.edutech.healthcare_appointment_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.DoctorAvailabilityDto;
import com.edutech.healthcare_appointment_management_system.entity.DoctorAvailability;
import com.edutech.healthcare_appointment_management_system.service.DoctorAvailabilityService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @Autowired private DoctorAvailabilityService availabilityService;

    @PostMapping("/doctor")
    public ResponseEntity<DoctorAvailability> addAvailability(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DoctorAvailabilityDto dto) {
        return ResponseEntity.ok(availabilityService.addAvailability(userDetails.getUsername(), dto));
    }

    @DeleteMapping("/doctor/{id}")
    public ResponseEntity<Map<String, String>> deleteAvailability(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        availabilityService.deleteAvailability(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Availability deleted successfully"));
    }

    @GetMapping("/slots/{doctorId}")
    public ResponseEntity<List<DoctorAvailability>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        return ResponseEntity.ok(availabilityService.getAvailableSlots(doctorId, parsedDate));
    }
}
