package com.edutech.healthcare_appointment_management_system.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutech.healthcare_appointment_management_system.dto.AppointmentDto;
import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.DoctorAvailability;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.AppointmentRepository;
import com.edutech.healthcare_appointment_management_system.repository.DoctorAvailabilityRepository;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;
import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;

@Service
@Transactional
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private NotificationService notificationService;
    @Autowired private DoctorAvailabilityRepository availabilityRepository;
    @Autowired private SseNotificationService sseNotificationService;

    public Appointment scheduleByPatient(String username, Long doctorId, AppointmentDto dto) {
        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        String aptStr = dto.getAppointmentTime().replace("T", " ");
        if (aptStr.length() == 16) {
            aptStr += ":00";
        }
        LocalDateTime apptTime = LocalDateTime.parse(aptStr, FORMATTER);
        LocalDate apptDate = apptTime.toLocalDate();
        LocalTime apptClockTime = apptTime.toLocalTime();
        
        List<DoctorAvailability> slots = availabilityRepository.findByDoctorIdAndDateAndIsBookedFalse(doctor.getId(), apptDate);
        boolean slotFound = false;
        for (DoctorAvailability slot : slots) {
            if (!apptClockTime.isBefore(slot.getStartTime()) && apptClockTime.isBefore(slot.getEndTime())) {
                slot.setIsBooked(true);
                availabilityRepository.save(slot);
                slotFound = true;
                break;
            }
        }
        
        if (!slotFound) {
            // Check if it's already booked
            List<DoctorAvailability> allSlots = availabilityRepository.findByDoctorIdAndDate(doctor.getId(), apptDate);
            for (DoctorAvailability slot : allSlots) {
                if (!apptClockTime.isBefore(slot.getStartTime()) && apptClockTime.isBefore(slot.getEndTime()) && slot.getIsBooked()) {
                    throw new IllegalStateException("Doctor is already booked at this time");
                }
            }
            // Auto-create slot to enforce locks
            DoctorAvailability newSlot = new DoctorAvailability();
            newSlot.setDoctor(doctor);
            newSlot.setDate(apptDate);
            newSlot.setStartTime(apptClockTime);
            newSlot.setEndTime(apptClockTime.plusMinutes(30));
            newSlot.setIsBooked(true);
            availabilityRepository.save(newSlot);
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(apptTime)
                .notes(dto.getNotes())
                .status("SCHEDULED")
                .fee(doctor.getConsultationFee() != null ? doctor.getConsultationFee() : 0.0)
                .build();
        appointment = appointmentRepository.save(appointment);

        notificationService.createNotification(patient.getUser().getId(),
                "Appointment scheduled with Dr. " + doctor.getName() + " on " + dto.getAppointmentTime(), "APPOINTMENT");
        notificationService.createNotification(doctor.getUser().getId(),
                "New appointment from " + patient.getName() + " on " + dto.getAppointmentTime(), "APPOINTMENT");
        log.info("Appointment scheduled: patient={}, doctor={}", patient.getName(), doctor.getName());

        sseNotificationService.sendEventToAll("APPOINTMENT", java.util.Map.of("action", "appointmentCreated", "id", appointment.getId()));
        
        return appointment;
    }

    public Appointment scheduleByReceptionist(AppointmentDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + dto.getDoctorId()));

        return saveAppointmentAndSyncPatient(patient, doctor, dto);
    }

    public Appointment scheduleByReceptionistByName(AppointmentDto dto) {
        Patient patient = patientRepository.findByName(dto.getPatientName())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with name: " + dto.getPatientName()));
        Doctor doctor = doctorRepository.findByName(dto.getDoctorName())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with name: " + dto.getDoctorName()));

        return saveAppointmentAndSyncPatient(patient, doctor, dto);
    }

    private Appointment saveAppointmentAndSyncPatient(Patient patient, Doctor doctor, AppointmentDto dto) {
        // Sync age and weight
        if (dto.getAge() != null) patient.setAge(dto.getAge());
        if (dto.getWeight() != null) patient.setWeight(dto.getWeight());
        patientRepository.save(patient);
        
        String aptStr = dto.getAppointmentTime().replace("T", " ");
        if (aptStr.length() == 16) {
            aptStr += ":00";
        }
        LocalDateTime apptTime = LocalDateTime.parse(aptStr, FORMATTER);
        LocalDate apptDate = apptTime.toLocalDate();
        LocalTime apptClockTime = apptTime.toLocalTime();
        
        List<DoctorAvailability> slots = availabilityRepository.findByDoctorIdAndDateAndIsBookedFalse(doctor.getId(), apptDate);
        boolean slotFound = false;
        for (DoctorAvailability slot : slots) {
            if (!apptClockTime.isBefore(slot.getStartTime()) && apptClockTime.isBefore(slot.getEndTime())) {
                slot.setIsBooked(true);
                availabilityRepository.save(slot);
                slotFound = true;
                break;
            }
        }
        
        if (!slotFound) {
            // Check if it's already booked
            List<DoctorAvailability> allSlots = availabilityRepository.findByDoctorIdAndDate(doctor.getId(), apptDate);
            for (DoctorAvailability slot : allSlots) {
                if (!apptClockTime.isBefore(slot.getStartTime()) && apptClockTime.isBefore(slot.getEndTime()) && slot.getIsBooked()) {
                    throw new IllegalStateException("Doctor is already booked at this time");
                }
            }
            // Auto-create slot to enforce locks
            DoctorAvailability newSlot = new DoctorAvailability();
            newSlot.setDoctor(doctor);
            newSlot.setDate(apptDate);
            newSlot.setStartTime(apptClockTime);
            newSlot.setEndTime(apptClockTime.plusMinutes(30));
            newSlot.setIsBooked(true);
            availabilityRepository.save(newSlot);
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(apptTime)
                .notes(dto.getNotes())
                .problem(dto.getProblem())
                .status("Pending")
                .fee(doctor.getConsultationFee() != null ? doctor.getConsultationFee() : 0.0)
                .build();
        appointment = appointmentRepository.save(appointment);

        notificationService.createNotification(patient.getUser().getId(),
                "Appointment scheduled by reception with Dr. " + doctor.getName(), "APPOINTMENT");
        notificationService.createNotification(doctor.getUser().getId(),
                "Receptionist scheduled an appointment for you with " + patient.getName(), "APPOINTMENT");
                
        sseNotificationService.sendEventToAll("APPOINTMENT", java.util.Map.of("action", "appointmentCreated", "id", appointment.getId()));
        
        return appointment;
    }

    public Appointment reschedule(Long appointmentId, AppointmentDto dto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        String aptStr = dto.getAppointmentTime().replace("T", " ");
        if (aptStr.length() == 16) {
            aptStr += ":00";
        }
        appointment.setAppointmentTime(LocalDateTime.parse(aptStr, FORMATTER));
        appointment.setStatus("Pending");
        if (dto.getNotes() != null) appointment.setNotes(dto.getNotes());
        appointment = appointmentRepository.save(appointment);

        notificationService.createNotification(appointment.getPatient().getUser().getId(),
                "Your appointment has been rescheduled to " + dto.getAppointmentTime(), "APPOINTMENT");
        log.info("Appointment {} rescheduled", appointmentId);
        
        sseNotificationService.sendEventToAll("APPOINTMENT", java.util.Map.of("action", "appointmentRescheduled", "id", appointment.getId()));
        
        return appointment;
    }

    public List<Appointment> getByPatient(String username) {
        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return appointmentRepository.findByPatientId(patient.getId());
    }

    public List<Appointment> getByDoctor(String username) {
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return appointmentRepository.findByDoctorId(doctor.getId());
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getTodayAppointments() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return appointmentRepository.findByAppointmentTimeBetween(start, end);
    }

    public List<Appointment> getYesterdayAppointments() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);
        return appointmentRepository.findByAppointmentTimeBetween(start, end);
    }

    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentTimeBetween(start, end);
    }

    public List<Appointment> getTodayAppointmentsForDoctor(String username) {
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), start, end);
    }

    public List<Appointment> getAppointmentsByDateRangeForDoctor(String username, LocalDateTime start, LocalDateTime end) {
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), start, end);
    }

    public org.springframework.data.domain.Page<Appointment> getPaginatedAppointments(int page, int size, String status) {
        if (status != null && !status.trim().isEmpty()) {
            return appointmentRepository.findByStatus(status, org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by("appointmentTime").descending()));
        }
        return appointmentRepository.findAll(org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by("appointmentTime").descending()));
    }

    public Appointment updateStatus(Long appointmentId, String doctorUsername, String status) {
        Doctor doctor = doctorRepository.findByUserUsername(doctorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
             throw new IllegalArgumentException("Not authorized to update this appointment");
        }
        
        if ("CANCELLED".equalsIgnoreCase(status) && !"CANCELLED".equalsIgnoreCase(appointment.getStatus())) {
            List<DoctorAvailability> slots = availabilityRepository.findByDoctorIdAndDate(
                doctor.getId(), appointment.getAppointmentTime().toLocalDate()
            );
            LocalTime apptTime = appointment.getAppointmentTime().toLocalTime();
            for (DoctorAvailability slot : slots) {
                if (!apptTime.isBefore(slot.getStartTime()) && apptTime.isBefore(slot.getEndTime()) && slot.getIsBooked()) {
                    slot.setIsBooked(false);
                    availabilityRepository.save(slot);
                    break;
                }
            }
        }
        
        appointment.setStatus(status);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        String action = "CANCELLED".equalsIgnoreCase(status) ? "appointmentCancelled" : "appointmentUpdated";
        sseNotificationService.sendEventToAll("APPOINTMENT", java.util.Map.of("action", action, "id", savedAppointment.getId()));
        
        return savedAppointment;
    }
}
