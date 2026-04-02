package com.edutech.healthcare_appointment_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.edutech.healthcare_appointment_management_system.entity.DoctorAvailability;

import javax.persistence.LockModeType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<DoctorAvailability> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DoctorAvailability> findByDoctorIdAndDateAndStartTimeAndEndTime(
            Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);
            
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<DoctorAvailability> findByDoctorIdAndDateAndIsBookedFalse(Long doctorId, LocalDate date);
}
