// package com.edutech.healthcare_appointment_management_system.service;


// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.edutech.healthcare_appointment_management_system.dto.RegisterRequest;
// import com.edutech.healthcare_appointment_management_system.entity.Doctor;
// import com.edutech.healthcare_appointment_management_system.entity.Patient;
// import com.edutech.healthcare_appointment_management_system.entity.Receptionist;
// import com.edutech.healthcare_appointment_management_system.entity.User;
// import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
// import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;
// import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;
// import com.edutech.healthcare_appointment_management_system.repository.ReceptionistRepository;
// import com.edutech.healthcare_appointment_management_system.repository.UserRepository;

// import java.util.Collections;

// import org.springframework.transaction.annotation.Transactional;

// @Service
// @Transactional
// public class UserService implements UserDetailsService {

//     private static final Logger log = LoggerFactory.getLogger(UserService.class);

//     @Autowired private UserRepository userRepository;
//     @Autowired private PatientRepository patientRepository;
//     @Autowired private DoctorRepository doctorRepository;
//     @Autowired private ReceptionistRepository receptionistRepository;
//     @Autowired @org.springframework.context.annotation.Lazy private PasswordEncoder passwordEncoder;

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         User user = userRepository.findByUsername(username)
//                 .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//         return new org.springframework.security.core.userdetails.User(
//                 user.getUsername(),
//                 user.getPassword(),
//                 Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
//         );
//     }

//     public User registerPatient(RegisterRequest req) {
//         validateUniqueUser(req);
//         User user = User.builder()
//                 .username(req.getUsername())
//                 .password(passwordEncoder.encode(req.getPassword()))
//                 .email(req.getEmail())
//                 .role("PATIENT")
//                 .build();
//         user = userRepository.save(user);

//         Patient patient = Patient.builder()
//                 .name(req.getName())
//                 .phone(req.getPhone())
//                 .address(req.getAddress())
//                 .user(user)
//                 .build();
//         patientRepository.save(patient);
//         log.info("Patient registered: {}", user.getUsername());
//         return user;
//     }

//     public User registerDoctor(RegisterRequest req) {
//         if (req.getSpecialty() == null || req.getSpecialty().isBlank()) {
//             throw new IllegalArgumentException("Specialty is required for doctors");
//         }
//         validateUniqueUser(req);
//         User user = User.builder()
//                 .username(req.getUsername())
//                 .password(passwordEncoder.encode(req.getPassword()))
//                 .email(req.getEmail())
//                 .role("DOCTOR")
//                 .build();
//         user = userRepository.save(user);

//         Doctor doctor = Doctor.builder()
//                 .name(req.getName())
//                 .specialty(req.getSpecialty())
//                 .availability(req.getAvailability() != null ? req.getAvailability() : "Not set")
//                 .email(req.getEmail())
//                 .user(user)
//                 .build();
//         doctorRepository.save(doctor);
//         log.info("Doctor registered: {}", user.getUsername());
//         return user;
//     }

//     public User registerReceptionist(RegisterRequest req) {
//         validateUniqueUser(req);
//         User user = User.builder()
//                 .username(req.getUsername())
//                 .password(passwordEncoder.encode(req.getPassword()))
//                 .email(req.getEmail())
//                 .role("RECEPTIONIST")
//                 .build();
//         user = userRepository.save(user);

//         Receptionist receptionist = Receptionist.builder()
//                 .name(req.getName())
//                 .phone(req.getPhone())
//                 .user(user)
//                 .build();
//         receptionistRepository.save(receptionist);
//         log.info("Receptionist registered: {}", user.getUsername());
//         return user;
//     }

//     public User findByUsername(String username) {
//         return userRepository.findByUsername(username)
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
//     }

//     private void validateUniqueUser(RegisterRequest req) {
//         if (userRepository.existsByUsername(req.getUsername())) {
//             throw new IllegalArgumentException("Username already taken: " + req.getUsername());
//         }
//         if (userRepository.existsByEmail(req.getEmail())) {
//             throw new IllegalArgumentException("Email already registered: " + req.getEmail());
//         }
//     }
// }

package com.edutech.healthcare_appointment_management_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edutech.healthcare_appointment_management_system.dto.RegisterRequest;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.entity.Receptionist;
import com.edutech.healthcare_appointment_management_system.entity.User;
import com.edutech.healthcare_appointment_management_system.exception.ResourceNotFoundException;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;
import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;
import com.edutech.healthcare_appointment_management_system.repository.ReceptionistRepository;
import com.edutech.healthcare_appointment_management_system.repository.UserRepository;

import java.util.Collections;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired private UserRepository userRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private ReceptionistRepository receptionistRepository;
    @Autowired @org.springframework.context.annotation.Lazy private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        // ✅ THE FIX: Append "ROLE_" to the authority so Spring Security recognizes it properly!
        String roleWithPrefix = user.getRole().startsWith("ROLE_") ? user.getRole() : "ROLE_" + user.getRole();
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix))
        );
    }

    public User registerPatient(RegisterRequest req) {
        validateUniqueUser(req);
        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .role("PATIENT")
                .build();
        user = userRepository.save(user);

        Patient patient = Patient.builder()
                .name(req.getName())
                .phone(req.getPhone())
                .address(req.getAddress())
                .user(user)
                .build();
        patientRepository.save(patient);
        log.info("Patient registered: {}", user.getUsername());
        return user;
    }

    public User registerDoctor(RegisterRequest req) {
        if (req.getSpecialty() == null || req.getSpecialty().isBlank()) {
            throw new IllegalArgumentException("Specialty is required for doctors");
        }
        validateUniqueUser(req);
        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .role("DOCTOR")
                .build();
        user = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .name(req.getName())
                .specialty(req.getSpecialty())
                .availability(req.getAvailability() != null ? req.getAvailability() : "Not set")
                .email(req.getEmail())
                .user(user)
                .build();
        doctorRepository.save(doctor);
        log.info("Doctor registered: {}", user.getUsername());
        return user;
    }

    public User registerReceptionist(RegisterRequest req) {
        validateUniqueUser(req);
        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .role("RECEPTIONIST")
                .build();
        user = userRepository.save(user);

        Receptionist receptionist = Receptionist.builder()
                .name(req.getName())
                .phone(req.getPhone())
                .user(user)
                .build();
        receptionistRepository.save(receptionist);
        log.info("Receptionist registered: {}", user.getUsername());
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private void validateUniqueUser(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken: " + req.getUsername());
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + req.getEmail());
        }
    }
}