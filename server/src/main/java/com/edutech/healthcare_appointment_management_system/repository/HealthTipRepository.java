package com.edutech.healthcare_appointment_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.healthcare_appointment_management_system.entity.HealthTip;

import java.util.List;

@Repository
public interface HealthTipRepository extends JpaRepository<HealthTip, Long> {
    List<HealthTip> findByCategory(String category);
}
