package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.healthcare_appointment_management_system.entity.Payment;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public List<Payment> getByDoctorId(Long doctorId) {
        return paymentRepository.findByDoctorId(doctorId);
    }
    
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updateStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }
}
