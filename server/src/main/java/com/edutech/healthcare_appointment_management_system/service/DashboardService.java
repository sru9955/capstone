package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.healthcare_appointment_management_system.dto.DashboardStatsDto;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.AppointmentRepository;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;
import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;

import java.time.LocalDateTime;

@Service
public class DashboardService {

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;


    public DashboardStatsDto getPatientStats(String username) {
        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        long total = appointmentRepository.countByPatientId(patient.getId());
        long upcoming = appointmentRepository
                .findByPatientIdAndAppointmentTimeAfter(patient.getId(), LocalDateTime.now()).size();
        long cancelled = appointmentRepository.findByPatientId(patient.getId())
                .stream().filter(a -> "CANCELLED".equals(a.getStatus())).count();
        long completed = appointmentRepository.findByPatientId(patient.getId())
                .stream().filter(a -> "COMPLETED".equals(a.getStatus())).count();

        return DashboardStatsDto.builder()
                .totalAppointments(total)
                .upcomingAppointments((long) upcoming)
                .cancelledAppointments(cancelled)
                .completedAppointments(completed)
                .totalDoctors((long) doctorRepository.findAll().size())
                .build();
    }

    public DashboardStatsDto getDoctorStats(String username) {
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        long todayCount = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), startOfDay, endOfDay).size();
        long totalPatients = appointmentRepository.countDistinctPatientsByDoctorId(doctor.getId());
        long totalAppts = appointmentRepository.countByDoctorId(doctor.getId());

        return DashboardStatsDto.builder()
                .todayAppointments((long) todayCount)
                .totalPatients(totalPatients)
                .totalAppointments(totalAppts)
                .build();
    }

    public DashboardStatsDto getReceptionistStats() {
        long total = appointmentRepository.count();
        long pending = appointmentRepository.countByStatus("SCHEDULED");
        long cancelled = appointmentRepository.countByStatus("CANCELLED");
        long totalDoctors = doctorRepository.count();
        long totalPatients = patientRepository.count();

        return DashboardStatsDto.builder()
                .totalAppointments(total)
                .pendingAppointments(pending)
                .cancelledAppointments(cancelled)
                .totalDoctors(totalDoctors)
                .totalPatients(totalPatients)
                .build();
    }

    public DashboardStatsDto getAdminStats() {
        long totalAppointments = appointmentRepository.count();
        long totalDoctors = doctorRepository.count();
        long totalPatients = patientRepository.count();

        var appointments = appointmentRepository.findAll();
        
        double totalRevenue = appointments.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .mapToDouble(a -> a.getFee() == null ? 0 : a.getFee())
                .sum();
                
        var revenuePerDoctor = appointments.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .collect(java.util.stream.Collectors.groupingBy(
                        a -> a.getDoctor().getName(),
                        java.util.stream.Collectors.summingDouble(a -> a.getFee() == null ? 0 : a.getFee())
                ));

        var appointmentsPerDoctor = appointments.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        a -> a.getDoctor().getName(),
                        java.util.stream.Collectors.counting()
                ));

        return DashboardStatsDto.builder()
                .totalAppointments(totalAppointments)
                .totalDoctors(totalDoctors)
                .totalPatients(totalPatients)
                .totalRevenue(totalRevenue)
                .revenuePerDoctor(revenuePerDoctor)
                .appointmentsPerDoctor(appointmentsPerDoctor)
                .build();
    }
}
