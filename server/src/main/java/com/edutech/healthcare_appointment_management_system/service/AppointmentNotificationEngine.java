package com.edutech.healthcare_appointment_management_system.service;

import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.repository.AppointmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@Service
public class AppointmentNotificationEngine {

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private NotificationService notificationService;

    // Run every minute
    @Scheduled(fixedRate = 60000)
    public void sendUpcomingReminders() {
        LocalDateTime now = LocalDateTime.now();

        // 30 MINUTE REMINDER
        LocalDateTime start30m = now.plusMinutes(29);
        LocalDateTime end30m = now.plusMinutes(31);
        
        List<Appointment> apps30m = appointmentRepository.findByStatusAndAppointmentTimeBetween("SCHEDULED", start30m, end30m);
        for (Appointment app : apps30m) {
            if (!Boolean.TRUE.equals(app.getIsReminded30m())) {
                String msg = "Reminder: You have an appointment with Dr. " + app.getDoctor().getName() + " in 30 minutes.";
                notificationService.createNotification(app.getPatient().getUser().getId(), msg, "REMINDER_30");
                
                String docMsg = "Reminder: Appointment with Patient " + app.getPatient().getName() + " in 30 minutes.";
                notificationService.createNotification(app.getDoctor().getUser().getId(), docMsg, "REMINDER_30");
                
                app.setIsReminded30m(true);
                appointmentRepository.save(app);
            }
        }

        // 5 MINUTE REMINDER
        LocalDateTime start5m = now.plusMinutes(4);
        LocalDateTime end5m = now.plusMinutes(6);
        
        List<Appointment> apps5m = appointmentRepository.findByStatusAndAppointmentTimeBetween("SCHEDULED", start5m, end5m);
        for (Appointment app : apps5m) {
            if (!Boolean.TRUE.equals(app.getIsReminded5m())) {
                String msg = "URGENT: Your appointment with Dr. " + app.getDoctor().getName() + " starts in 5 minutes! Please join or be ready.";
                notificationService.createNotification(app.getPatient().getUser().getId(), msg, "REMINDER_5");
                
                String docMsg = "URGENT: Appointment with Patient " + app.getPatient().getName() + " starts in 5 minutes!";
                notificationService.createNotification(app.getDoctor().getUser().getId(), docMsg, "REMINDER_5");
                
                app.setIsReminded5m(true);
                appointmentRepository.save(app);
            }
        }
    }
}
