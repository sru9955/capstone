package com.edutech.healthcare_appointment_management_system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseNotificationService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String username) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Infinite timeout
        
        emitters.put(username, emitter);
        
        emitter.onCompletion(() -> emitters.remove(username));
        emitter.onTimeout(() -> emitters.remove(username));
        emitter.onError((e) -> emitters.remove(username));
        
        // Push initial dummy event to establish connection successfully
        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connected successfully"));
        } catch (IOException e) {
            emitters.remove(username);
        }
        
        return emitter;
    }

    public void sendNotification(String username, Object payload) {
        SseEmitter emitter = emitters.get(username);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("NOTIFICATION").data(payload));
            } catch (IOException e) {
                emitters.remove(username);
            }
        }
    }

    public void sendEventToUser(String username, String eventName, Object payload) {
        SseEmitter emitter = emitters.get(username);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(payload));
            } catch (IOException e) {
                emitters.remove(username);
            }
        }
    }

    public void sendEventToAll(String eventName, Object payload) {
        emitters.forEach((username, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(payload));
            } catch (IOException e) {
                emitters.remove(username);
            }
        });
    }
}
