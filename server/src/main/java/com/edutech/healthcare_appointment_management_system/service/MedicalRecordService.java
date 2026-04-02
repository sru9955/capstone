package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutech.healthcare_appointment_management_system.dto.MedicalRecordDto;
import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.MedicalRecord;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.AppointmentRepository;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;
import com.edutech.healthcare_appointment_management_system.repository.MedicalRecordRepository;
import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;

import java.util.List;

@Service
@Transactional
public class MedicalRecordService {

    @Autowired private MedicalRecordRepository medicalRecordRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private AppointmentRepository appointmentRepository;

    public List<MedicalRecord> getByPatient(String username) {
        var patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return medicalRecordRepository.findByPatientId(patient.getId());
    }

    public List<MedicalRecord> getByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    public List<MedicalRecord> getPatientHistoryForDoctor(Long patientId, String doctorUsername) {
        Doctor doctor = doctorRepository.findByUserUsername(doctorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
        // Check if doctor has ever had an appointment with this patient
        boolean hasAppointment = appointmentRepository.findByDoctorId(doctor.getId())
                .stream()
                .anyMatch(a -> a.getPatient().getId().equals(patientId));
        
        if (!hasAppointment) {
            throw new IllegalArgumentException("Not authorized to view this patient's history");
        }
        
        return medicalRecordRepository.findByPatientId(patientId);
    }

    public List<MedicalRecord> getAll() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord save(MedicalRecord record) {
        return medicalRecordRepository.save(record);
    }

    public MedicalRecord addRecord(Long appointmentId, String doctorUsername, MedicalRecordDto dto) {
        Doctor doctor = doctorRepository.findByUserUsername(doctorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
       Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
             throw new IllegalArgumentException("Not authorized to add record for this appointment");
        }

        Patient patient = appointment.getPatient();
        if (dto.getAge() != null) patient.setAge(dto.getAge());
        if (dto.getWeight() != null) patient.setWeight(dto.getWeight());
        if (dto.getHeight() != null) patient.setHeight(dto.getHeight());
        patientRepository.save(patient);

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .appointment(appointment)
                .age(dto.getAge() != null ? dto.getAge() : patient.getAge())
                .weight(dto.getWeight() != null ? dto.getWeight() : patient.getWeight())
                .height(dto.getHeight() != null ? dto.getHeight() : patient.getHeight())
                .bp(dto.getBp())
                .sugarLevel(dto.getSugarLevel())
                .symptoms(dto.getSymptoms())
                .diagnosis(dto.getDiagnosis())
                .prescription(dto.getPrescription())
                .notes(dto.getNotes())
                .allergies(dto.getAllergies())
                .build();
        
        return medicalRecordRepository.save(record);
    }
}
