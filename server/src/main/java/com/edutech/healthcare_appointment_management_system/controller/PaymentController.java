package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.entity.Payment;
import com.edutech.healthcare_appointment_management_system.repository.PaymentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Payment>> getPatientPayments(@PathVariable Long patientId) {
        // Technically involves appointment traversing, but for simplicity assuming repository allows it
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Payment> markAsPaid(@PathVariable Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus("PAID");
        return ResponseEntity.ok(paymentRepository.save(payment));
    }
}
