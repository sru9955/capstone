package com.edutech.healthcare_appointment_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.edutech.healthcare_appointment_management_system.entity.FileReport;
import com.edutech.healthcare_appointment_management_system.entity.MedicalRecord;
import com.edutech.healthcare_appointment_management_system.repository.FileReportRepository;
import com.edutech.healthcare_appointment_management_system.repository.MedicalRecordRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    private FileReportRepository fileReportRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create directory where uploaded files will be stored.", ex);
        }
    }

    public FileReport storeFile(MultipartFile file, Long medicalRecordId) {
        MedicalRecord record = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new RuntimeException("Medical Record not found for id: " + medicalRecordId));

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String fileExtension = "";
        
        if (originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            if (newFileName.contains("..")) {
                throw new RuntimeException("Filename contains invalid path sequence " + newFileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileReport fileReport = new FileReport();
            fileReport.setFileName(originalFileName);
            fileReport.setFileType(file.getContentType());
            fileReport.setFileUrl(targetLocation.toString());
            fileReport.setMedicalRecord(record);

            return fileReportRepository.save(fileReport);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    public String storeProfileImage(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg");
        String fileExtension = "";
        if (originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String newFileName = "profile_" + UUID.randomUUID().toString() + fileExtension;
        try {
            if (newFileName.contains("..")) {
                throw new RuntimeException("Filename contains invalid path sequence " + newFileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store image " + originalFileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(Long fileId) {
        try {
            FileReport fileReport = fileReportRepository.findById(fileId)
                    .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));

            Path filePath = Paths.get(fileReport.getFileUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileId);
            }
        } catch (Exception ex) {
            throw new RuntimeException("File not found " + fileId, ex);
        }
    }
    
    public Resource loadFileAsNativeResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            if(!filePath.startsWith(this.fileStorageLocation)) {
                throw new RuntimeException("Cannot load files outside storage directory");
            }
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + filename);
            }
        } catch(Exception ex) {
            throw new RuntimeException("File not found " + filename, ex);
        }
    }

    public FileReport getFileReport(Long fileId) {
        return fileReportRepository.findById(fileId).orElse(null);
    }

    public List<FileReport> getFilesByMedicalRecord(Long medicalRecordId) {
        return fileReportRepository.findByMedicalRecordId(medicalRecordId);
    }
}
