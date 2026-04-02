package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.DashboardStatsDto;
import com.edutech.healthcare_appointment_management_system.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")

public class DashboardController {

    @Autowired private DashboardService dashboardService;

    @GetMapping("/patient")
    public ResponseEntity<DashboardStatsDto> getPatientDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(dashboardService.getPatientStats(userDetails.getUsername()));
    }

    @GetMapping("/doctor")
    public ResponseEntity<DashboardStatsDto> getDoctorDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(dashboardService.getDoctorStats(userDetails.getUsername()));
    }

    @GetMapping("/receptionist")
    public ResponseEntity<DashboardStatsDto> getReceptionistDashboard() {
        return ResponseEntity.ok(dashboardService.getReceptionistStats());
    }

    @GetMapping("/admin")
    public ResponseEntity<DashboardStatsDto> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminStats());
    }
}
