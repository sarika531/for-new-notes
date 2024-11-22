package com.payswiff.mfmsproject.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.payswiff.mfmsproject.dtos.ForgotPasswordDto;
import com.payswiff.mfmsproject.dtos.LoginDto;
import com.payswiff.mfmsproject.dtos.LoginResponseDto;
import com.payswiff.mfmsproject.exceptions.EmployeePasswordUpdationFailedException;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.repositories.EmployeeRepository;
import com.payswiff.mfmsproject.security.JwtTokenProvider;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService; // AuthService to test

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeService employeeService;

    private AutoCloseable closeable;

    /**
     * Initialize mocks before running each test
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Close mocks after running each test
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Tests the login method when loginDto is null, expecting ResourceUnableToCreate exception.
     */
    @Test
    void testLogin_NullLoginDto() {
        assertThrows(ResourceUnableToCreate.class, () -> authService.login(null), "Login details are incomplete");
    }

    /**
     * Tests login with an empty email or phone, expecting ResourceUnableToCreate exception.
     */
    @Test
    void testLogin_EmptyEmailOrPhone() {
        LoginDto loginDto = new LoginDto("", "password123");
        assertThrows(ResourceUnableToCreate.class, () -> authService.login(loginDto));
    }

    /**
     * Tests login with an empty password, expecting ResourceUnableToCreate exception.
     */
    @Test
    void testLogin_EmptyPassword() {
        LoginDto loginDto = new LoginDto("user@example.com", "");
        assertThrows(ResourceUnableToCreate.class, () -> authService.login(loginDto));
    }

    /**
     * Tests successful login with valid email and password.
     * @throws ResourceUnableToCreate 
     */
    @Test
    void testLogin_SuccessfulLogin() throws ResourceUnableToCreate {
        // Set up mock data
        LoginDto loginDto = new LoginDto("user@example.com", "password123");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock token generation
        String token = "mocked-jwt-token";
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);

        // Mock user details
        UserDetails userDetails = new User("user@example.com", "password123", List.of(() -> "ROLE_USER"));
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock employee retrieval
        Employee employee = new Employee();
        employee.setEmployeeEmail("user@example.com");
        employee.setEmployeeId(1L);
        when(employeeRepository.findByEmployeeEmail("user@example.com")).thenReturn(Optional.of(employee));

        // Call the login method
        LoginResponseDto response = authService.login(loginDto);

        // Assertions
        assertEquals("user@example.com", response.getUserEmailOrPhone());
        assertEquals("ROLE_USER", response.getRole());
        assertEquals(1, response.getId());
        assertEquals(token, response.getToken());
    }

    /**
     * Tests login when employee is not found, expecting RuntimeException.
     */
    @Test
    void testLogin_EmployeeNotFound() {
        LoginDto loginDto = new LoginDto("nonexistent@example.com", "password123");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(employeeRepository.findByEmployeeEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginDto), "Employee not found");
    }

    /**
     * Tests forgotPassword method with null input, expecting ResourceUnableToCreate exception.
     */
    @Test
    void testForgotPassword_NullInput() {
        assertThrows(ResourceUnableToCreate.class, () -> authService.forgotPassword(null));
    }

    /**
     * Tests forgotPassword with empty email/phone, expecting ResourceUnableToCreate exception.
     */
    @Test
    void testForgotPassword_EmptyEmailOrPhone() {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("", "newpassword123");
        assertThrows(ResourceUnableToCreate.class, () -> authService.forgotPassword(forgotPasswordDto));
    }

    /**
     * Tests forgotPassword with empty password, expecting ResourceUnableToCreate exception.
     */
    @Test
    void testForgotPassword_EmptyPassword() {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("user@example.com", "");
        assertThrows(ResourceUnableToCreate.class, () -> authService.forgotPassword(forgotPasswordDto));
    }

    /**
     * Tests forgotPassword with non-existent email, expecting ResourceNotFoundException.
     */
    @Test
    void testForgotPassword_NonExistentEmail() {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("nonexistent@example.com", "newpassword123");
        when(employeeRepository.existsByEmployeeEmail("nonexistent@example.com")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> authService.forgotPassword(forgotPasswordDto));
    }

    /**
     * Tests forgotPassword with invalid phone number, expecting false as return value.
     * @throws ResourceUnableToCreate 
     * @throws EmployeePasswordUpdationFailedException 
     * @throws ResourceNotFoundException 
     */
    @Test
    void testForgotPassword_InvalidPhoneNumber() throws ResourceNotFoundException, EmployeePasswordUpdationFailedException, ResourceUnableToCreate {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("123", "newpassword123");
        boolean result = authService.forgotPassword(forgotPasswordDto);
        assertFalse(result);
    }

    /**
     * Tests successful password reset using email.
     */
    @Test
    void testForgotPassword_SuccessfulResetWithEmail() throws Exception {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("user@example.com", "newpassword123");
        when(employeeRepository.existsByEmployeeEmail("user@example.com")).thenReturn(true);
        when(employeeService.updateEmployeePassword("user@example.com", "newpassword123")).thenReturn(true);

        assertTrue(authService.forgotPassword(forgotPasswordDto));
    }

    /**
     * Tests forgotPassword when password update fails, expecting EmployeePasswordUpdationFailedException.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testForgotPassword_PasswordUpdateFails() throws ResourceNotFoundException {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("user@example.com", "newpassword123");
        when(employeeRepository.existsByEmployeeEmail("user@example.com")).thenReturn(true);
        when(employeeService.updateEmployeePassword("user@example.com", "newpassword123")).thenReturn(false);

        assertThrows(EmployeePasswordUpdationFailedException.class, () -> authService.forgotPassword(forgotPasswordDto));
    }

    // Add other test cases to cover additional scenarios, such as null fields in ForgotPasswordDto, successful password update with phone number, etc.
}
