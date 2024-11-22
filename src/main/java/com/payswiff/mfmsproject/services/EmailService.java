package com.payswiff.mfmsproject.services;

import java.security.SecureRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling email-related operations, 
 * including sending emails and generating One-Time Passwords (OTPs).
 * 
 * This service interacts with Spring's JavaMailSender to dispatch emails 
 * and uses a secure random number generator to create OTPs. The class 
 * incorporates detailed logging at each significant step for easy 
 * traceability, making it easier to monitor operations and identify 
 * potential issues.
 * 
 * <p>
 * Methods:
 * <ul>
 *   <li>{@link #sendEmail(String, String, String)}: Sends a basic email 
 *       to the specified recipient with a subject and text.</li>
 *   <li>{@link #generateOtp()}: Generates a random OTP (One-Time Password) 
 *       of a specified length, using secure random generation techniques.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link JavaMailSender}: Used for sending emails.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Logging:
 * Detailed logging is implemented throughout the service to aid in tracking 
 * the flow and outcome of each operation.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * EmailService emailService = new EmailService();
 * emailService.sendEmail("recipient@example.com", "Subject", "Message body");
 * String otp = emailService.generateOtp();
 * }
 * </pre>
 * </p>
 * @author Gopi Bapanapalli
 */
@Service
public class EmailService {

    private static final Logger emailServiceLogger = LogManager.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender; // Spring's mail sender for sending emails
    
    private static final String OTP_CHARACTERS = "0123456789"; // Characters used for generating OTP
    private static final int OTP_LENGTH = 6; // Set the desired length for the OTP
    private SecureRandom random = new SecureRandom(); // SecureRandom for generating random numbers

    /**
     * Sends an email with the specified subject and text to the given recipient.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param text the body of the email
     * @return true if the email is sent successfully
     */
    public boolean sendEmail(String to, String subject, String text) {
        emailServiceLogger.info("Initiating email send operation to: {}", to);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage(); // Create a simple email message
            emailServiceLogger.debug("Setting recipient email to: {}", to);
            message.setTo(to); // Set the recipient

            emailServiceLogger.debug("Setting email subject to: {}", subject);
            message.setSubject(subject); // Set the subject line

            emailServiceLogger.debug("Setting email body text.");
            message.setText(text); // Set the body text of the email

            // Send the email using JavaMailSender
            emailSender.send(message);
            emailServiceLogger.info("Email successfully sent to: {}", to);

            return true; // Return true to indicate the email was sent
        } catch (Exception e) {
            emailServiceLogger.error("Failed to send email to: {}", to, e);
            return false;
        }
    }
    
    /**
     * Generates a random OTP (One-Time Password) of specified length.
     *
     * @return a randomly generated OTP as a string
     */
    public String generateOtp() {
        emailServiceLogger.info("Generating a new OTP of length {}", OTP_LENGTH);
        
        StringBuilder otp = new StringBuilder(OTP_LENGTH); // StringBuilder for building the OTP
        
        for (int i = 0; i < OTP_LENGTH; i++) {
            int randomIndex = random.nextInt(OTP_CHARACTERS.length());
            char otpCharacter = OTP_CHARACTERS.charAt(randomIndex);

            emailServiceLogger.debug("Generated character for OTP at position {}: {}", i, otpCharacter);

            otp.append(otpCharacter); // Append a random character from OTP_CHARACTERS to the OTP
        }
        
        String generatedOtp = otp.toString();
        emailServiceLogger.info("Generated OTP: {}", generatedOtp);

        return generatedOtp; // Return the generated OTP as a string
    }
}
