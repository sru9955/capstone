package com.edutech.healthcare_appointment_management_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status; // PENDING, PAID, FAILED

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDateTime paymentDate;

    public Payment() {}

    public Payment(Long id, Double amount, String status, Appointment appointment, Doctor doctor, LocalDateTime paymentDate) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.appointment = appointment;
        this.doctor = doctor;
        this.paymentDate = paymentDate;
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @PrePersist
    protected void onCreate() {
        this.paymentDate = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
    }

    public static class PaymentBuilder {
        private Long id;
        private Double amount;
        private String status;
        private Appointment appointment;
        private Doctor doctor;
        private LocalDateTime paymentDate;

        public PaymentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PaymentBuilder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public PaymentBuilder status(String status) {
            this.status = status;
            return this;
        }

        public PaymentBuilder appointment(Appointment appointment) {
            this.appointment = appointment;
            return this;
        }

        public PaymentBuilder doctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public PaymentBuilder paymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public Payment build() {
            return new Payment(id, amount, status, appointment, doctor, paymentDate);
        }
    }
}
