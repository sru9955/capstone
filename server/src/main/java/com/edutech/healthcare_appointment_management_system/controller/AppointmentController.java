package com.edutech.healthcare_appointment_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")

public class AppointmentController {

    @Autowired private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<Page<Appointment>> getAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(appointmentService.getPaginatedAppointments(page, size, status));
    }
}
