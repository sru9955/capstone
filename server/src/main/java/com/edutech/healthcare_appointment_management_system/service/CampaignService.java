package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.healthcare_appointment_management_system.entity.Campaign;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.CampaignRepository;

import java.util.List;

@Service
public class CampaignService {

    @Autowired private CampaignRepository campaignRepository;

    public List<Campaign> getAll() {
        return campaignRepository.findAll();
    }

    public Campaign getById(Long id) {
        return campaignRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
    }

    public Campaign create(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    public void delete(Long id) {
        campaignRepository.deleteById(id);
    }
}
