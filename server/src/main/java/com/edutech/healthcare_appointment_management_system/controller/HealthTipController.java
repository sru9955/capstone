package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.entity.HealthTip;
import com.edutech.healthcare_appointment_management_system.service.HealthTipService;

import java.util.List;

@RestController
@RequestMapping("/api/healthtips")

public class HealthTipController {

    @Autowired private HealthTipService healthTipService;

    @GetMapping
    public ResponseEntity<List<HealthTip>> getAllTips(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return ResponseEntity.ok(healthTipService.getByCategory(category));
        }
        return ResponseEntity.ok(healthTipService.getAll());
    }

    @PostMapping
    public ResponseEntity<HealthTip> createTip(@RequestBody HealthTip tip) {
        return ResponseEntity.ok(healthTipService.create(tip));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTip(@PathVariable Long id) {
        healthTipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
