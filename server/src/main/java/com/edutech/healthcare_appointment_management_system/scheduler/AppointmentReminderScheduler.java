package com.edutech.healthcare_appointment_management_system.scheduler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.repository.AppointmentRepository;
import com.edutech.healthcare_appointment_management_system.service.NotificationService;
import com.edutech.healthcare_appointment_management_system.service.SseNotificationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class AppointmentReminderScheduler {

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private NotificationService notificationService;
    @Autowired private SseNotificationService sseNotificationService;

    // Run every minute
    @Scheduled(fixedRate = 60000)
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Appointment> allScheduled = appointmentRepository.findAll().stream()
                .filter(a -> "Pending".equalsIgnoreCase(a.getStatus()) || "Scheduled".equalsIgnoreCase(a.getStatus()))
                .filter(a -> a.getAppointmentTime() != null)
                .collect(java.util.stream.Collectors.toList());

        for (Appointment appt : allScheduled) {
            long minutesUntil = java.time.Duration.between(now, appt.getAppointmentTime()).toMinutes();
            
            // 30-minute reminder
            if (minutesUntil > 15 && minutesUntil <= 30 && (appt.getIsReminded30m() == null || !appt.getIsReminded30m())) {
                sendReminder(appt, "Your appointment with Dr. " + appt.getDoctor().getName() + " is in 30 minutes.");
                appt.setIsReminded30m(true);
                appointmentRepository.save(appt);
            }
            
            // 5-minute reminder
            if (minutesUntil >= 0 && minutesUntil <= 5 && (appt.getIsReminded5m() == null || !appt.getIsReminded5m())) {
                sendReminder(appt, "Your appointment with Dr. " + appt.getDoctor().getName() + " starts in 5 minutes!");
                appt.setIsReminded5m(true);
                appointmentRepository.save(appt);
            }
        }
    }

    private void sendReminder(Appointment appt, String message) {
        // Persist DB notification
        notificationService.createNotification(appt.getPatient().getUser().getId(), message, "REMINDER");
        
        // Push realtime via SSE
        sseNotificationService.sendEventToUser(
                appt.getPatient().getUser().getUsername(),
                "NOTIFICATION",
                Map.of(
                        "type", "REMINDER",
                        "message", message,
                        "appointmentId", appt.getId()
                )
        );
    }
}
