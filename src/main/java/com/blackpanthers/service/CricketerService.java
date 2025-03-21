package com.blackpanthers.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blackpanthers.model.Cricketer;
import com.blackpanthers.repository.CricketerRepository;

@Service
public class CricketerService {

    private final CricketerRepository cricketerRepository;
    private final EmailService emailService;
    private final PDFGeneratorService pdfGeneratorService;

    @Autowired
    public CricketerService(CricketerRepository cricketerRepository, 
                           EmailService emailService,
                           PDFGeneratorService pdfGeneratorService) {
        this.cricketerRepository = cricketerRepository;
        this.emailService = emailService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public List<Cricketer> getAllCricketers() {
        return cricketerRepository.findAll();
    }

    public Optional<Cricketer> getCricketerById(Long id) {
        return cricketerRepository.findById(id);
    }

    public Cricketer registerCricketer(Cricketer cricketer, MultipartFile photoFile) throws IOException {
        // Check if email already exists
        if (cricketerRepository.existsByEmail(cricketer.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

//        // Check if jersey number already exists
//        if (cricketerRepository.existsByJerseyNumber(cricketer.getJerseyNumber())) {
//            throw new IllegalArgumentException("Jersey number already taken");
//        }

        // Process photo if provided
        if (photoFile != null && !photoFile.isEmpty()) {
            cricketer.setPhoto(photoFile.getBytes());
            cricketer.setPhotoContentType(photoFile.getContentType());
        }

        // Save cricketer to database
        Cricketer savedCricketer = cricketerRepository.save(cricketer);

        // Generate PDF
        byte[] pdfBytes = pdfGeneratorService.generateRegistrationPDF(savedCricketer);

        // Send confirmation email with PDF attachment
        emailService.sendRegistrationConfirmationEmail(
            savedCricketer.getEmail(),
            savedCricketer.getName(),
            pdfBytes
        );

        return savedCricketer;
    }

    public boolean isEmailRegistered(String email) {
        return cricketerRepository.existsByEmail(email);
    }

    public boolean isJerseyNumberTaken(Integer jerseyNumber) {
        return cricketerRepository.existsByJerseyNumber(jerseyNumber);
    }
}

