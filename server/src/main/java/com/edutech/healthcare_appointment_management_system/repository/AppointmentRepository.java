package com.edutech.healthcare_appointment_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edutech.healthcare_appointment_management_system.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByStatus(String status);
    org.springframework.data.domain.Page<Appointment> findByStatus(String status, org.springframework.data.domain.Pageable pageable);
    long countByPatientId(Long patientId);
    long countByDoctorId(Long doctorId);
    long countByStatus(String status);
    long countByDoctorIdAndStatus(Long doctorId, String status);
    List<Appointment> findByPatientIdAndAppointmentTimeAfter(Long patientId, LocalDateTime time);

    List<Appointment> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Appointment> findByStatusAndAppointmentTimeBetween(String status, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByPatientIdAndAppointmentTimeBetween(Long patientId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end ORDER BY a.appointmentTime ASC")
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(@Param("doctorId") Long doctorId,
                                                              @Param("start") LocalDateTime start,
                                                              @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM Appointment a WHERE a.doctor.id = :doctorId")
    long countDistinctPatientsByDoctorId(@Param("doctorId") Long doctorId);
}
