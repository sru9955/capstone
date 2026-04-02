package com.edutech.healthcare_appointment_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.healthcare_appointment_management_system.entity.Doctor;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    Optional<Doctor> findByUserUsername(String username);
    Optional<Doctor> findByName(String name);
    java.util.List<Doctor> findBySpecialtyContainingIgnoreCase(String specialty);
    java.util.List<Doctor> findByNameContainingIgnoreCase(String name);
}
