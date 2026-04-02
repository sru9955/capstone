package com.edutech.healthcare_appointment_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.entity.Campaign;
import com.edutech.healthcare_appointment_management_system.service.CampaignService;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")

public class CampaignController {

    @Autowired private CampaignService campaignService;

    @GetMapping
    public ResponseEntity<List<Campaign>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAll());
    }

    @PostMapping
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        return ResponseEntity.ok(campaignService.create(campaign));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
