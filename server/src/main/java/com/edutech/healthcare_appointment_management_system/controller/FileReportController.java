package com.edutech.healthcare_appointment_management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.edutech.healthcare_appointment_management_system.entity.FileReport;
import com.edutech.healthcare_appointment_management_system.service.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")

public class FileReportController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload/{medicalRecordId}")
    public ResponseEntity<FileReport> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long medicalRecordId) {
        FileReport savedFile = fileStorageService.storeFile(file, medicalRecordId);
        return ResponseEntity.ok(savedFile);
    }

    @GetMapping("/record/{medicalRecordId}")
    public ResponseEntity<List<FileReport>> getFilesForRecord(@PathVariable Long medicalRecordId) {
        return ResponseEntity.ok(fileStorageService.getFilesByMedicalRecord(medicalRecordId));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileId);
        FileReport fileReport = fileStorageService.getFileReport(fileId);
        
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Default to octet-stream
        }
        
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileReport.getFileName() + "\"")
                .body(resource);
    }
}
