package com.edutech.healthcare_appointment_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.healthcare_appointment_management_system.entity.Receptionist;

import java.util.Optional;

@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {
    Optional<Receptionist> findByUserId(Long userId);
    Optional<Receptionist> findByUserUsername(String username);
}
