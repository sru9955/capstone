package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.LoginRequest;
import com.edutech.healthcare_appointment_management_system.dto.LoginResponse;
import com.edutech.healthcare_appointment_management_system.dto.RegisterRequest;
import com.edutech.healthcare_appointment_management_system.entity.User;
import com.edutech.healthcare_appointment_management_system.jwt.JwtUtil;
import com.edutech.healthcare_appointment_management_system.service.UserService;

import java.util.Map;

@RestController

public class RegisterAndLoginController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserService userService;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/api/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        User user = userService.findByUsername(request.getUsername());
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new LoginResponse(token, user.getRole(),
                user.getUsername(), user.getId(), "Login successful"));
    }

    @PostMapping("/api/patient/register")
    public ResponseEntity<Map<String, String>> registerPatient(@RequestBody RegisterRequest request) {
        userService.registerPatient(request);
        return ResponseEntity.ok(Map.of("message", "Patient registered successfully"));
    }

    @PostMapping("/api/doctor/register")
    public ResponseEntity<Map<String, String>> registerDoctor(@RequestBody RegisterRequest request) {
        userService.registerDoctor(request);
        return ResponseEntity.ok(Map.of("message", "Doctor registered successfully"));
    }

    @PostMapping("/api/receptionist/register")
    public ResponseEntity<Map<String, String>> registerReceptionist(@RequestBody RegisterRequest request) {
        userService.registerReceptionist(request);
        return ResponseEntity.ok(Map.of("message", "Receptionist registered successfully"));
    }
}
