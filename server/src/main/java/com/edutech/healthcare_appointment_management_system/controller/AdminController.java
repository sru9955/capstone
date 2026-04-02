package com.edutech.healthcare_appointment_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.Payment;
import com.edutech.healthcare_appointment_management_system.repository.AppointmentRepository;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;
import com.edutech.healthcare_appointment_management_system.repository.PaymentRepository;
import com.edutech.healthcare_appointment_management_system.service.AppointmentService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private AppointmentService appointmentService;

    @PutMapping("/doctors/{id}/fee")
    public ResponseEntity<Doctor> assignConsultationFee(
            @PathVariable Long id, 
            @RequestParam Double fee) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        doctor.setConsultationFee(fee);
        return ResponseEntity.ok(doctorRepository.save(doctor));
    }

    @PostMapping("/appointments/{appointmentId}/bill")
    public ResponseEntity<Payment> createBill(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, Object> req) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        Doctor doctor = appointment.getDoctor();
        
        Double extraAmount = req.containsKey("amount") ? Double.valueOf(req.get("amount").toString()) : 0.0;
        Double totalAmount = (doctor.getConsultationFee() != null ? doctor.getConsultationFee() : 0.0) + extraAmount;

        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setDoctor(doctor);
        payment.setAmount(totalAmount);
        payment.setStatus("PAID");
        payment.setPaymentDate(LocalDateTime.now());
        
        return ResponseEntity.ok(paymentRepository.save(payment));
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    @GetMapping({"/appointments/range", "/appointments/filter"})
    public ResponseEntity<List<Appointment>> getAppointmentsByRange(
            @RequestParam(required = false, name = "start") String start,
            @RequestParam(required = false, name = "end") String end,
            @RequestParam(required = false, name = "startDate") String startDate,
            @RequestParam(required = false, name = "endDate") String endDate) {
        String actualStart = startDate != null ? startDate : start;
        String actualEnd = endDate != null ? endDate : end;
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(
                LocalDateTime.parse(actualStart), LocalDateTime.parse(actualEnd)));
    }
}
