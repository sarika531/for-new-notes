/**
 * Unit tests for the EmailService class.
 * <p>
 * This test suite verifies the functionalities of the EmailService methods,
 * including sending emails and generating OTPs (One-Time Passwords).
 * Each test case checks a specific behavior of the methods in various scenarios
 * to ensure the service behaves as expected.
 * </p>
 */
package com.payswiff.mfmsproject.services;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the EmailService.
 * <p>
 * This class contains tests for the {@link EmailService#sendEmail(String, String, String)} 
 * and {@link EmailService#generateOtp()} methods, ensuring that they perform 
 * correctly under various conditions.
 * </p>
 */
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender; // Mocked JavaMailSender to simulate email sending

    @InjectMocks
    private EmailService emailService; // The EmailService instance under test

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link EmailService#sendEmail(String, String, String)} method
     * for successful email sending.
     * <p>
     * This test checks that the method returns true and that the 
     * JavaMailSender's send method is called exactly once with 
     * the constructed SimpleMailMessage.
     * </p>
     */
    @Test
    void testSendEmail_Success() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Email Body";

        // Act
        boolean result = emailService.sendEmail(to, subject, text);

        // Assert
        assertTrue(result);
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    /**
     * Tests the {@link EmailService#sendEmail(String, String, String)} method
     * when an invalid email address is provided.
     * <p>
     * This test verifies that the method still returns true,
     * simulating that the exception is handled internally without 
     * altering the return behavior of the method.
     * </p>
     */
    @Test
    void testSendEmail_InvalidEmail() {
        // Arrange
        String to = "invalid-email";
        String subject = "Test Subject";
        String text = "Test Email Body";

        // Act
        boolean result = emailService.sendEmail(to, subject, text);

        // Assert
        assertTrue(result); // We're testing the method's return value, not actual sending
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    /**
     * Tests the {@link EmailService#sendEmail(String, String, String)} method
     * when no subject is provided.
     * <p>
     * This test checks the method's ability to handle null or missing subject 
     * values while still returning true and sending the email.
     * </p>
     */
    @Test
    void testSendEmail_NoSubject() {
        // Arrange
        String to = "test@example.com";
        String text = "Test Email Body";

        // Act
        boolean result = emailService.sendEmail(to, null, text);

        // Assert
        assertTrue(result);
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    /**
     * Tests the {@link EmailService#sendEmail(String, String, String)} method
     * when no body text is provided.
     * <p>
     * This test ensures that the email can still be sent without a body text,
     * verifying that the method handles this scenario gracefully.
     * </p>
     */
    @Test
    void testSendEmail_NoBody() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";

        // Act
        boolean result = emailService.sendEmail(to, subject, null);

        // Assert
        assertTrue(result);
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    /**
     * Tests the construction of the message object in the 
     * {@link EmailService#sendEmail(String, String, String)} method.
     * <p>
     * This test checks that a SimpleMailMessage object is correctly constructed
     * with the provided parameters before sending the email.
     * </p>
     */
    @Test
    void testSendEmail_MessageConstruction() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Email Body";

        // Act
        emailService.sendEmail(to, subject, text);

        // Assert
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        verify(emailSender, times(1)).send(message);
    }

    /**
     * Tests the {@link EmailService#generateOtp()} method
     * to verify the length of the generated OTP.
     * <p>
     * This test checks that the generated OTP has the correct length of 6 digits,
     * ensuring compliance with expected OTP standards.
     * </p>
     */
    @Test
    void testGenerateOtp_Length() {
        // Act
        String otp = emailService.generateOtp();

        // Assert
        assertEquals(6, otp.length(), "OTP length should be 6");
    }

    /**
     * Tests the {@link EmailService#generateOtp()} method to ensure it generates
     * a numeric OTP consisting solely of digits.
     * <p>
     * This test checks that the generated OTP matches the expected numeric format.
     * </p>
     */
    @Test
    void testGenerateOtp_ContainsOnlyDigits() {
        // Act
        String otp = emailService.generateOtp();

        // Assert
        assertTrue(otp.matches("\\d{6}"), "OTP should contain only digits");
    }

    /**
     * Tests the {@link EmailService#generateOtp()} method to verify that two consecutive
     * calls produce different OTPs.
     * <p>
     * This test checks that the randomness of OTP generation functions correctly,
     * as each call to generateOtp() should ideally yield a unique OTP.
     * </p>
     */
    @Test
    void testGenerateOtp_DifferentValues() {
        // Arrange
        String otp1 = emailService.generateOtp();
        String otp2 = emailService.generateOtp();

        // Assert
        assertNotEquals(otp1, otp2, "Generated OTPs should be different");
    }

    /**
     * Tests the {@link EmailService#generateOtp()} method to check for variability
     * across multiple OTP generations.
     * <p>
     * This test runs generateOtp() multiple times and ensures all generated OTPs
     * are distinct from one another, confirming the method's randomness and variability.
     * </p>
     */
    @Test
    void testGenerateOtp_MultipleCalls() {
        // Arrange
        String[] otps = new String[10];
        
        // Act
        for (int i = 0; i < 10; i++) {
            otps[i] = emailService.generateOtp();
        }

        // Assert
        for (int i = 0; i < 10; i++) {
            for (int j = i + 1; j < 10; j++) {
                assertNotEquals(otps[i], otps[j], "Generated OTPs should be different");
            }
        }
    }
}
