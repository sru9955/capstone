package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.healthcare_appointment_management_system.dto.AppointmentDto;
import com.edutech.healthcare_appointment_management_system.dto.ChatRequest;
import com.edutech.healthcare_appointment_management_system.dto.ChatResponse;
import com.edutech.healthcare_appointment_management_system.dto.ChatSession;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired private DoctorRepository doctorRepository;
    @Autowired private AppointmentService appointmentService;

    // username -> ChatSession
    private final Map<String, ChatSession> sessions = new ConcurrentHashMap<>();

    public ChatResponse processMessageWithState(String username, ChatRequest request) {
        String msg = request.getMessage().toLowerCase().trim();
        String role = request.getRole();
        
        ChatSession session = sessions.computeIfAbsent(username, k -> new ChatSession());
        session.setUsername(username);

        // Command matching overrides current state if explicitly typing a known trigger.
        if (msg.contains("cancel") || msg.contains("reset") || msg.contains("start over")) {
            session.setState("IDLE");
            return new ChatResponse("Okay, let's start over. How can I help you?", "bot");
        }

        // Handle based on state
        switch (session.getState()) {
            case "IDLE":
                if (contains(msg, "book", "appointment", "schedule")) {
                    session.setState("WAITING_FOR_ISSUE");
                    return new ChatResponse("Sure, I can help you book an appointment. What symptoms or health problems are you experiencing?", "bot");
                }
                if (contains(msg, "record", "history", "previous")) {
                    return new ChatResponse("You can view your full medical history in the 'Medical Records' section or timeline.", "bot");
                }
                if (contains(msg, "health tip", "advice")) {
                    return new ChatResponse("Stay hydrated, exercise daily, and eat a balanced diet!", "bot");
                }
                break;

            case "WAITING_FOR_ISSUE":
                String issue = request.getMessage();
                session.setIssue(issue);
                String specialty = suggestSpecialty(issue.toLowerCase());
                session.setState("WAITING_FOR_DOCTOR");

                List<Doctor> doctors = doctorRepository.findBySpecialtyContainingIgnoreCase(specialty);
                if (doctors.isEmpty()) {
                    doctors = doctorRepository.findAll();
                }

                String docList = doctors.stream()
                        .map(d -> d.getId() + ". Dr. " + d.getName() + " (" + d.getSpecialty() + ")")
                        .collect(Collectors.joining("\n"));

                return new ChatResponse("Based on your symptoms, I suggest seeing a " + specialty + ". Here are the available doctors. Please reply with the Doctor's ID:\n" + docList, "bot");

            case "WAITING_FOR_DOCTOR":
                try {
                    Long docId = Long.parseLong(msg);
                    session.setDoctorId(docId);
                    session.setState("WAITING_FOR_TIME");
                    return new ChatResponse("Excellent. Now, please tell me the date and time you want to book (e.g., 2026-05-20 10:00:00)", "bot");
                } catch (Exception e) {
                    return new ChatResponse("Please provide a valid numeric Doctor ID.", "bot");
                }

            case "WAITING_FOR_TIME":
                session.setAppointmentTime(request.getMessage());
                // Here we book it
                try {
                    AppointmentDto dto = new AppointmentDto();
                    dto.setAppointmentTime(session.getAppointmentTime());
                    dto.setNotes("Issue: " + session.getIssue());
                    
                    if ("RECEPTIONIST".equals(role) || "ADMIN".equals(role)) {
                         session.setState("IDLE");
                         return new ChatResponse("Receptionists must book via the dashboard instead of the chat for specific patients.", "bot");
                    } else {
                        appointmentService.scheduleByPatient(username, session.getDoctorId(), dto);
                    }
                    
                    session.setState("IDLE");
                    return new ChatResponse("Great! Your appointment is confirmed for " + dto.getAppointmentTime() + ".", "bot");
                } catch (Exception e) {
                    return new ChatResponse("I had trouble booking that time. Make sure it's formatting like 'yyyy-MM-dd HH:mm:ss', or the time might be invalid.", "bot");
                }
        }

        // fallback logic for IDLE
        return new ChatResponse(getRuleBasedReply(msg, role), "bot");
    }

    private String getRuleBasedReply(String msg, String role) {
         if (contains(msg, "hello", "hi", "hey")) {
             return "Hello! I am your AI assistant. Type 'book appointment' to schedule a visit.";
         }
         return "I didn't quite catch that. You can type 'book appointment', 'health tips', or 'my records'.";
    }

    private String suggestSpecialty(String issue) {
        if (contains(issue, "heart", "chest", "breath", "cardio")) return "Cardiologist";
        if (contains(issue, "skin", "rash", "itch", "acne")) return "Dermatologist";
        if (contains(issue, "brain", "headache", "nerve", "memory")) return "Neurologist";
        if (contains(issue, "bone", "fracture", "joint", "muscle")) return "Orthopedic";
        if (contains(issue, "eye", "vision", "see")) return "Ophthalmologist";
        if (contains(issue, "kid", "child", "pediatric")) return "Pediatrician";
        if (contains(issue, "tooth", "teeth", "dent", "gum")) return "Dentist";
        if (contains(issue, "stomach", "digest", "belly", "food")) return "Gastroenterologist";
        return "General Physician";
    }

    private boolean contains(String message, String... keywords) {
        for (String keyword : keywords) {
            if (message.contains(keyword)) return true;
        }
        return false;
    }
}
