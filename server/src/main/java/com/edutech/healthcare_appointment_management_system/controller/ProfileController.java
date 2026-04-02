package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.edutech.healthcare_appointment_management_system.dto.ProfileDTO;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.entity.Patient;
import com.edutech.healthcare_appointment_management_system.entity.Receptionist;
import com.edutech.healthcare_appointment_management_system.entity.User;
import com.edutech.healthcare_appointment_management_system.repository.DoctorRepository;
import com.edutech.healthcare_appointment_management_system.repository.PatientRepository;
import com.edutech.healthcare_appointment_management_system.repository.ReceptionistRepository;
import com.edutech.healthcare_appointment_management_system.repository.UserRepository;
import com.edutech.healthcare_appointment_management_system.service.FileStorageService;
import com.edutech.healthcare_appointment_management_system.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired private UserService userService;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private ReceptionistRepository receptionistRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private FileStorageService fileStorageService;

    @GetMapping("/my-profile")
    public ResponseEntity<ProfileDTO> getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        ProfileDTO dto = new ProfileDTO();
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setProfileImageUrl(user.getProfileImageUrl() != null ? 
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/").path(user.getProfileImageUrl()).toUriString() : null);

        if (user.getRole().equals("PATIENT")) {
            Patient p = patientRepository.findByUserUsername(username).orElseThrow();
            dto.setName(p.getName());
            dto.setPhone(p.getPhone());
            dto.setAge(p.getAge());
            dto.setAddress(p.getAddress());
        } else if (user.getRole().equals("DOCTOR")) {
            Doctor d = doctorRepository.findByUserUsername(username).orElseThrow();
            dto.setName(d.getName());
            dto.setPhone(d.getPhone());
            dto.setSpecialty(d.getSpecialty());
            dto.setAvailability(d.getAvailability());
            dto.setConsultationFee(d.getConsultationFee());
        } else if (user.getRole().equals("RECEPTIONIST")) {
            Receptionist r = receptionistRepository.findByUserUsername(username).orElseThrow();
            dto.setName(r.getName());
            dto.setPhone(r.getPhone());
        } else {
            dto.setName(user.getUsername());
        }

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/my-profile")
    public ResponseEntity<Map<String, String>> updateMyProfile(@RequestBody ProfileDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        userRepository.save(user);

        if (user.getRole().equals("PATIENT")) {
            Patient p = patientRepository.findByUserUsername(username).orElseThrow();
            if (dto.getName() != null) p.setName(dto.getName());
            if (dto.getPhone() != null) p.setPhone(dto.getPhone());
            if (dto.getAge() != null) p.setAge(dto.getAge());
            if (dto.getAddress() != null) p.setAddress(dto.getAddress());
            patientRepository.save(p);
        } else if (user.getRole().equals("DOCTOR")) {
            Doctor d = doctorRepository.findByUserUsername(username).orElseThrow();
            if (dto.getName() != null) d.setName(dto.getName());
            if (dto.getPhone() != null) d.setPhone(dto.getPhone());
            if (dto.getSpecialty() != null) d.setSpecialty(dto.getSpecialty());
            if (dto.getAvailability() != null) d.setAvailability(dto.getAvailability());
            if (dto.getConsultationFee() != null) d.setConsultationFee(dto.getConsultationFee());
            doctorRepository.save(d);
        } else if (user.getRole().equals("RECEPTIONIST")) {
            Receptionist r = receptionistRepository.findByUserUsername(username).orElseThrow();
            if (dto.getName() != null) r.setName(dto.getName());
            if (dto.getPhone() != null) r.setPhone(dto.getPhone());
            receptionistRepository.save(r);
        }

        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        
        String filename = fileStorageService.storeProfileImage(file);
        user.setProfileImageUrl(filename);
        userRepository.save(user);
        
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(filename)
                .toUriString();

        return ResponseEntity.ok(Map.of("message", "Profile image uploaded", "url", fileDownloadUri));
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String filename, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsNativeResource(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) { }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
