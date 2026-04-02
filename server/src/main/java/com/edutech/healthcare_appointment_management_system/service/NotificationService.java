package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutech.healthcare_appointment_management_system.dto.NotificationDto;
import com.edutech.healthcare_appointment_management_system.entity.Notification;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.NotificationRepository;
import com.edutech.healthcare_appointment_management_system.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SseNotificationService sseNotificationService;

    public Notification createNotification(Long userId, String message, String type) {
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .type(type)
                .isRead(false)
                .build();
        Notification saved = notificationRepository.save(notification);
        
        userRepository.findById(userId).ifPresent(user -> {
            sseNotificationService.sendNotification(user.getUsername(), toDto(saved));
        });
        
        return saved;
    }

    public List<NotificationDto> getByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public NotificationDto markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        notification.setRead(true);
        return toDto(notificationRepository.save(notification));
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndIsReadFalse(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    private NotificationDto toDto(Notification n) {
        return new NotificationDto(n.getId(), n.getMessage(), n.getTimestamp(),
                n.getUserId(), n.isRead(), n.getType());
    }
}
