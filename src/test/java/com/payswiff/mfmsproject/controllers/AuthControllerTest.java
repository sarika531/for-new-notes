package com.payswiff.mfmsproject.controllers;

import com.payswiff.mfmsproject.dtos.ForgotPasswordDto;
import com.payswiff.mfmsproject.dtos.LoginDto;
import com.payswiff.mfmsproject.dtos.LoginResponseDto;
import com.payswiff.mfmsproject.exceptions.EmployeePasswordUpdationFailedException;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.services.AuthService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for AuthController.
 * This class tests the login and password recovery functionality of AuthController.
 */
class AuthControllerTest {

    @Mock // Mocking AuthService to isolate tests
    private AuthService authService;

    @InjectMocks // Injecting the mocked service into the AuthController
    private AuthController authController;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Code that runs once before all tests
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // Code that runs once after all tests
    }

    @BeforeEach
    void setUp() throws Exception {
        // Initializing mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Code that runs after each test
    }

    /**
     * Test method for successful login.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testLogin_Success() throws ResourceUnableToCreate, ResourceNotFoundException {
        // Arrange
        LoginDto loginDto = new LoginDto("user@example.com", "password123");
        LoginResponseDto expectedResponse = new LoginResponseDto("token123", "User Name", 0, null);
        
        when(authService.login(loginDto)).thenReturn(expectedResponse); // Mocking service response

        // Act
        ResponseEntity<LoginResponseDto> response = authController.login(loginDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue()); // Check for HTTP 200 OK
        assertEquals(expectedResponse, response.getBody()); // Validate response body
    }

    /**
     * Test method for failed login due to invalid credentials.
     */
    @Test
    void testLogin_Failure() throws ResourceUnableToCreate {
        // Arrange
        LoginDto loginDto = new LoginDto("user@example.com", "wrongpassword");
        when(authService.login(loginDto)).thenThrow(new ResourceUnableToCreate("Invalid credentials","",""));

        // Act & Assert
        ResourceUnableToCreate exception = assertThrows(ResourceUnableToCreate.class, () -> {
            authController.login(loginDto);
        });
        assertEquals("Invalid credentials with :  is unable to create at this moment!", exception.getMessage()); // Validate exception message
    }

    /**
     * Test method for password recovery with valid data.
     */
    @Test
    void testForgotPassword_Success() throws ResourceNotFoundException, EmployeePasswordUpdationFailedException, ResourceUnableToCreate {
        // Arrange
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("user@example.com", null);
        when(authService.forgotPassword(forgotPasswordDto)).thenReturn(true); // Mocking successful operation

        // Act
        boolean result = authController.forgotPassword(forgotPasswordDto);

        // Assert
        assertTrue(result); // Check for successful password recovery
    }

    /**
     * Test method for password recovery when user is not found.
     * @throws ResourceUnableToCreate 
     * @throws EmployeePasswordUpdationFailedException 
     * @throws ResourceNotFoundException 
     */
    @Test
    void testForgotPassword_UserNotFound() throws ResourceNotFoundException, EmployeePasswordUpdationFailedException, ResourceUnableToCreate {
        // Arrange
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("user@notfound.com", null);
        when(authService.forgotPassword(forgotPasswordDto)).thenThrow(new ResourceNotFoundException("User not found","",""));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authController.forgotPassword(forgotPasswordDto);
        });
        assertEquals("User not found with :  is not found!!", exception.getMessage()); // Validate exception message
    }

    /**
     * Test method for password recovery failure.
     * @throws ResourceUnableToCreate 
     * @throws EmployeePasswordUpdationFailedException 
     * @throws ResourceNotFoundException 
     */
    @Test
    void testForgotPassword_UpdateFailed() throws ResourceNotFoundException, EmployeePasswordUpdationFailedException, ResourceUnableToCreate {
        // Arrange
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("user@example.com", null);
        when(authService.forgotPassword(forgotPasswordDto)).thenThrow(new EmployeePasswordUpdationFailedException("Password update failed","",""));

        // Act & Assert
        EmployeePasswordUpdationFailedException exception = assertThrows(EmployeePasswordUpdationFailedException.class, () -> {
            authController.forgotPassword(forgotPasswordDto);
        });
        assertEquals("Password update failed with :  is unable to change or update password at this moment.", exception.getMessage()); // Validate exception message
    }

    /**
     * Test method for handling null login DTO.
     */
    @Test
    void testLogin_NullLoginDto() {
        // Act & Assert
        assertThrows(ResourceUnableToCreate.class, () -> {
            authController.login(null); // Expect exception when passing null
        });
    }

    /**
     * Test method for handling null forgot password DTO.
     */
    @Test
    void testForgotPassword_NullForgotPasswordDto() {
        // Act & Assert
        assertThrows(ResourceUnableToCreate.class, () -> {
            authController.forgotPassword(null); // Expect exception when passing null
        });
    }
}
