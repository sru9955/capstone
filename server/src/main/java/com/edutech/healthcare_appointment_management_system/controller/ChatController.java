package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.healthcare_appointment_management_system.dto.ChatRequest;
import com.edutech.healthcare_appointment_management_system.dto.ChatResponse;
import com.edutech.healthcare_appointment_management_system.service.ChatService;

@RestController
@RequestMapping("/api/chat")

public class ChatController {

    @Autowired private ChatService chatService;

    @PostMapping("/process")
    public ResponseEntity<ChatResponse> processChat(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChatRequest request) {
        String username = userDetails != null ? userDetails.getUsername() : "GUEST";
        return ResponseEntity.ok(chatService.processMessageWithState(username, request));
    }
}
