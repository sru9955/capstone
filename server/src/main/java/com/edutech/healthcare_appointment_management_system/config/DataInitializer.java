package com.edutech.healthcare_appointment_management_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.edutech.healthcare_appointment_management_system.entity.Campaign;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.HealthTip;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.entity.User;
import com.edutech.healthcare_appointment_management_system.repository.CampaignRepository;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;
import com.edutech.healthcare_appointment_management_system.repository.HealthTipRepository;
import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;
import com.edutech.healthcare_appointment_management_system.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private HealthTipRepository healthTipRepository;
    @Autowired private CampaignRepository campaignRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        // 1. Create Admin
        User adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .email("admin@healthcare.com")
                .role("ADMIN")
                .build();
        userRepository.save(adminUser);

        // 2. Create Doctors
        createDoctor("drsmith", "Dr. John Smith", "Cardiology", "Mon-Fri 9AM-5PM", "smith@healthcare.com");
        createDoctor("drjane", "Dr. Jane Doe", "General Medicine", "Mon-Sat 10AM-4PM", "jane@healthcare.com");
        createDoctor("drwilson", "Dr. Wilson", "Pediatrics", "Tue-Thu 8AM-12PM", "wilson@healthcare.com");

        // 3. Create Patients
        createPatient("patient1", "John Doe", "1234567890", "123 Main St", "john@email.com");
        createPatient("patient2", "Alice Ross", "0987654321", "456 Oak St", "alice@email.com");

        // 4. Create Health Tips
        healthTipRepository.save(HealthTip.builder()
                .category("Hydration")
                .content("Drink at least 8 glasses of water a day.")
                .build());
        healthTipRepository.save(HealthTip.builder()
                .category("Fitness")
                .content("Walk at least 30 minutes daily.")
                .build());
        healthTipRepository.save(HealthTip.builder()
                .category("Sleep")
                .content("Get 7-8 hours of quality sleep.")
                .build());

        // 5. Create Campaigns
        campaignRepository.save(Campaign.builder()
                .title("Free Vaccination")
                .description("Get your flu shot this weekend.")
                .targetAudience("All")
                .campaignDate(LocalDateTime.now().plusDays(5))
                .build());
        campaignRepository.save(Campaign.builder()
                .title("Heart Health Seminar")
                .description("Learn how to keep your heart healthy.")
                .targetAudience("Adults")
                .campaignDate(LocalDateTime.now().plusDays(10))
                .build());

        System.out.println("Demo data initialized successfully!");
    }

    private void createDoctor(String username, String name, String specialty, String availability, String email) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode("doctor123"))
                .email(email)
                .role("DOCTOR")
                .build();
        user = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .name(name)
                .specialty(specialty)
                .availability(availability)
                .email(email)
                .user(user)
                .build();
        doctorRepository.save(doctor);
    }

    private void createPatient(String username, String name, String phone, String address, String email) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode("patient123"))
                .email(email)
                .role("PATIENT")
                .build();
        user = userRepository.save(user);

        Patient patient = Patient.builder()
                .name(name)
                .phone(phone)
                .address(address)
                .user(user)
                .build();
        patientRepository.save(patient);
    }
}
