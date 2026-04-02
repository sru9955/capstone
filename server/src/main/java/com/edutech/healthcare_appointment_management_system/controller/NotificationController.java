package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.NotificationDto;
import com.edutech.healthcare_appointment_management_system.entity.User;
import com.edutech.healthcare_appointment_management_system.service.NotificationService;
import com.edutech.healthcare_appointment_management_system.service.SseNotificationService;
import com.edutech.healthcare_appointment_management_system.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")

public class NotificationController {

    @Autowired private NotificationService notificationService;
    @Autowired private UserService userService;
    @Autowired private SseNotificationService sseNotificationService;

    @GetMapping("/stream")
    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter streamNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        return sseNotificationService.subscribe(userDetails.getUsername());
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(notificationService.getByUser(user.getId()));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        long count = notificationService.getUnreadCount(user.getId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<Map<String, String>> markAllAsRead(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }
}
