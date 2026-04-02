package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.healthcare_appointment_management_system.entity.HealthTip;
import com.edutech.healthcare_appointment_management_system.repository.HealthTipRepository;

import java.util.List;

@Service
public class HealthTipService {

    @Autowired private HealthTipRepository healthTipRepository;

    public List<HealthTip> getAll() {
        return healthTipRepository.findAll();
    }

    public List<HealthTip> getByCategory(String category) {
        return healthTipRepository.findByCategory(category);
    }

    public HealthTip create(HealthTip tip) {
        return healthTipRepository.save(tip);
    }
    
    public void delete(Long id) {
        healthTipRepository.deleteById(id);
    }
}
