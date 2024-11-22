package com.payswiff.mfmsproject.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.payswiff.mfmsproject.dtos.EmailOtpResponseDto;
import com.payswiff.mfmsproject.dtos.EmailResponseDto;
import com.payswiff.mfmsproject.dtos.EmailSendDto;
import com.payswiff.mfmsproject.services.EmailService;

/**
 * REST controller for handling email-related operations such as sending emails
 * and generating One-Time Passwords (OTPs).
 * <p>
 * This controller provides endpoints for sending emails with custom subject and text,
 * and generating and sending OTPs to a specified email address. The operations 
 * leverage the `EmailService` to handle email sending and OTP generation.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */
@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})
public class EmailController {

    private static final Logger emailControllerLogger = LogManager.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    /**
     * Sends an email using the provided details.
     *
     * @param emailSendDto The data transfer object containing email details (recipient, subject, text).
     * @return ResponseEntity containing the result of the email sending operation.
     */
    @PostMapping("/send-email")
    public ResponseEntity<EmailResponseDto> sendEmail(@RequestBody EmailSendDto emailSendDto) {
        emailControllerLogger.info("Received request to send email to: {}", emailSendDto.getTo());

        try {
            // Call the EmailService to send the email and capture the success status
            boolean emailSent = emailService.sendEmail(emailSendDto.getTo(), emailSendDto.getSubject(), emailSendDto.getText());

            // Log the result of the email send operation
            if (emailSent) {
                emailControllerLogger.info("Email sent successfully to: {}", emailSendDto.getTo());
            } else {
                emailControllerLogger.error("Failed to send email to: {}", emailSendDto.getTo());
            }

            // Create response DTO with email sending status
            EmailResponseDto response = new EmailResponseDto(emailSent, emailSent ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());

            // Return response entity with appropriate HTTP status
            return new ResponseEntity<>(response, emailSent ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            emailControllerLogger.error("Exception occurred while sending email to: {}", emailSendDto.getTo(), e);
            return new ResponseEntity<>(new EmailResponseDto(false, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Generates an OTP and sends it to the specified email address.
     *
     * @param email The email address where the OTP will be sent.
     * @return ResponseEntity containing the OTP and the result of the sending operation.
     */
    @PostMapping("/generateOTP")
    public ResponseEntity<EmailOtpResponseDto> generateOtp(@RequestParam String email) {
        emailControllerLogger.info("Received request to generate OTP for email: {}", email);

        // Generate a One-Time Password (OTP)
        String otp = emailService.generateOtp();
        emailControllerLogger.info("Generated OTP for email {}: {}", email, otp);

        // Prepare the email content for sending the OTP
        EmailSendDto emailSendDto = new EmailSendDto();
        emailSendDto.setTo(email);
        emailSendDto.setSubject("Merchant Feedback Management System");
        emailSendDto.setText("Your One Time Password for login is: " + otp);

        try {
            // Send the OTP email and capture the success status
            boolean emailSent = emailService.sendEmail(emailSendDto.getTo(), emailSendDto.getSubject(), emailSendDto.getText());

            // Log the result of the OTP email send operation
            if (emailSent) {
                emailControllerLogger.info("OTP email sent successfully to: {}", email);
            } else {
                emailControllerLogger.error("Failed to send OTP email to: {}", email);
            }

            // Create response DTO with email sending status and the generated OTP
            EmailOtpResponseDto response = new EmailOtpResponseDto(emailSent, otp, emailSent ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());

            // Return response entity with appropriate HTTP status
            return new ResponseEntity<>(response, emailSent ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            emailControllerLogger.error("Exception occurred while sending OTP email to: {}", email, e);
            return new ResponseEntity<>(new EmailOtpResponseDto(false, otp, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
