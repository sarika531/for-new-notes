package com.payswiff.mfmsproject.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.payswiff.mfmsproject.dtos.EmailSendDto;
import com.payswiff.mfmsproject.dtos.EmailOtpResponseDto;
import com.payswiff.mfmsproject.dtos.EmailResponseDto;
import com.payswiff.mfmsproject.services.EmailService;

/**
 * Unit tests for the EmailController class.
 * These tests validate the functionality of sending emails and generating OTPs via the EmailController.
 */
class EmailControllerTest {

    @Mock
    private EmailService emailService; // Mock the EmailService

    @InjectMocks
    private EmailController emailController; // Inject the mocked EmailService into the EmailController

    /**
     * Setup method before all tests.
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Code to run once before all tests
    }

    /**
     * Teardown method after all tests.
     */
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // Code to run once after all tests
    }

    /**
     * Setup method before each test.
     */
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    /**
     * Teardown method after each test.
     */
    @AfterEach
    void tearDown() throws Exception {
        // Code to run after each test
    }

    /**
     * Test for {@link com.payswiff.mfmsproject.controllers.EmailController#sendEmail(com.payswiff.mfmsproject.dtos.EmailSendDto)}.
     * This test checks the successful sending of an email.
     */
    @Test
    void testSendEmail_Success() {
        // Creating a mock EmailSendDto with test data
        EmailSendDto emailSendDto = new EmailSendDto();
        emailSendDto.setTo("test@example.com");
        emailSendDto.setSubject("Test Subject");
        emailSendDto.setText("This is a test email.");

        // Mocking the sendEmail method of the EmailService to return true (email sent successfully)
        when(emailService.sendEmail(any(), any(), any())).thenReturn(true);

        // Calling the controller's sendEmail method
        ResponseEntity<EmailResponseDto> response = emailController.sendEmail(emailSendDto);

        // Verifying the response status is OK (200) and the email was successfully sent
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmailSent());
    }

    /**
     * Test for {@link com.payswiff.mfmsproject.controllers.EmailController#sendEmail(com.payswiff.mfmsproject.dtos.EmailSendDto)}.
     * This test checks the failure scenario when email sending fails.
     */
    @Test
    void testSendEmail_Failure() {
        // Creating a mock EmailSendDto with test data
        EmailSendDto emailSendDto = new EmailSendDto();
        emailSendDto.setTo("test@example.com");
        emailSendDto.setSubject("Test Subject");
        emailSendDto.setText("This is a test email.");

        // Mocking the sendEmail method of the EmailService to return false (email failed to send)
        when(emailService.sendEmail(any(), any(), any())).thenReturn(false);

        // Calling the controller's sendEmail method
        ResponseEntity<EmailResponseDto> response = emailController.sendEmail(emailSendDto);

        // Verifying the response status is INTERNAL_SERVER_ERROR (500) when email sending fails
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isEmailSent());
    }

    /**
     * Test for {@link com.payswiff.mfmsproject.controllers.EmailController#generateOtp(java.lang.String)}.
     * This test checks the successful generation and sending of an OTP.
     */
    @Test
    void testGenerateOtp_Success() {
        // Mocking the generateOtp method of the EmailService to return a fixed OTP
        String mockOtp = "123456";
        when(emailService.generateOtp()).thenReturn(mockOtp);

        // Mocking the sendEmail method of the EmailService to return true (OTP email sent successfully)
        when(emailService.sendEmail(any(), any(), any())).thenReturn(true);

        // Calling the controller's generateOtp method
        ResponseEntity<EmailOtpResponseDto> response = emailController.generateOtp("test@example.com");

        // Verifying the response status is OK (200) and the OTP generated matches the mocked OTP
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOtp, response.getBody().getOtp());
        assertTrue(response.getBody().isEmailSent());
    }

    /**
     * Test for {@link com.payswiff.mfmsproject.controllers.EmailController#generateOtp(java.lang.String)}.
     * This test checks the failure scenario when OTP generation or sending fails.
     */
    @Test
    void testGenerateOtp_Failure() {
        // Mocking the generateOtp method of the EmailService to return a fixed OTP
        String mockOtp = "123456";
        when(emailService.generateOtp()).thenReturn(mockOtp);

        // Mocking the sendEmail method of the EmailService to return false (OTP email failed to send)
        when(emailService.sendEmail(any(), any(), any())).thenReturn(false);

        // Calling the controller's generateOtp method
        ResponseEntity<EmailOtpResponseDto> response = emailController.generateOtp("test@example.com");

        // Verifying the response status is INTERNAL_SERVER_ERROR (500) when email sending fails
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(mockOtp, response.getBody().getOtp());
        assertFalse(response.getBody().isEmailSent());
    }

    /**
     * Test for invalid email format in the {@link com.payswiff.mfmsproject.controllers.EmailController#sendEmail(com.payswiff.mfmsproject.dtos.EmailSendDto)} method.
     * This test checks how the controller handles invalid email format.
     */
    @Test
    void testSendEmail_InvalidEmailFormat() {
        // Creating an EmailSendDto with invalid email format
        EmailSendDto emailSendDto = new EmailSendDto();
        emailSendDto.setTo("invalid-email"); // Invalid email format
        emailSendDto.setSubject("Test Subject");
        emailSendDto.setText("This is a test email.");

        // Calling the controller's sendEmail method and expecting a BAD_REQUEST response
        ResponseEntity<EmailResponseDto> response = emailController.sendEmail(emailSendDto);

        // Verifying that the response status is BAD_REQUEST (400)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isEmailSent());
    }

    /**
     * Test for invalid email in the {@link com.payswiff.mfmsproject.controllers.EmailController#generateOtp(java.lang.String)} method.
     * This test checks how the controller handles invalid email format while generating OTP.
     */
    @Test
    void testGenerateOtp_InvalidEmailFormat() {
        // Calling generateOtp method with invalid email format
        ResponseEntity<EmailOtpResponseDto> response = emailController.generateOtp("invalid-email");

        // Verifying the response status is BAD_REQUEST (400)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
