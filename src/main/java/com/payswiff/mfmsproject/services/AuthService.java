package com.payswiff.mfmsproject.services;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.payswiff.mfmsproject.controllers.AuthController;
import com.payswiff.mfmsproject.dtos.ForgotPasswordDto;
import com.payswiff.mfmsproject.dtos.LoginDto;
import com.payswiff.mfmsproject.dtos.LoginResponseDto;
import com.payswiff.mfmsproject.exceptions.EmployeePasswordUpdationFailedException;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.repositories.EmployeeRepository;
import com.payswiff.mfmsproject.security.CustomeUserDetailsService;
import com.payswiff.mfmsproject.security.JwtAuthenticationEntryPoint;
import com.payswiff.mfmsproject.security.JwtAuthenticationFilter;
import com.payswiff.mfmsproject.security.JwtTokenProvider;
/**
 * Service class responsible for handling authentication-related operations like login, 
 * and password reset. It includes logic for authenticating users, generating JWT tokens, 
 * and handling password reset requests.
 * 
 * <p>
 * Version: 0.0.1
 * </p>
 * <p>
 * Author: Gopi Bapanapalli
 * </p>
 */
@Service
public class AuthService {
	
	private static final Logger AuthServiceLogger = LogManager.getLogger(AuthService.class);

    @Autowired
    
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomeUserDetailsService userDetailsService;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeService employeeService;

    /**
     * Authenticates a user based on the provided login credentials and generates a JWT token.
     *
     * @param loginDto contains login credentials (email or phone and password)
     * @return LoginResponseDto containing user details and JWT token
     * @throws ResourceUnableToCreate if the email/phone or password is null or empty
     */
    public LoginResponseDto login(LoginDto loginDto) throws ResourceUnableToCreate {
    	//initilate log message
    	AuthServiceLogger.info("<=== Login process started ===>");
    	
        // Validate that login credentials are not null or empty
        if (loginDto == null || loginDto.getEmailOrPhone() == null || loginDto.getEmailOrPhone().isEmpty()
                || loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
            throw new ResourceUnableToCreate("Email/Phone or Password", "Login", "Login details are incomplete");
        }

        // Authenticate the user using the provided credentials
    	AuthServiceLogger.info("<=== Authentication Process ===>");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmailOrPhone(), loginDto.getPassword()));

        // Store the authentication in the security context
    	AuthServiceLogger.info("<=== Store the authentication in the security context ===>");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate a JWT token for the authenticated user
        AuthServiceLogger.info("<=== Generate JWT Token===>");
        String token = jwtTokenProvider.generateToken(authentication);

        // Retrieve user details (email and roles) from the authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        // Get the first role assigned to the user
        AuthServiceLogger.info("<=== Get The User Roles ===>");
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No roles assigned"))
                .getAuthority();

        // Retrieve employee by email
        AuthServiceLogger.info("<=== Retrieve employee by email ===>");
        Optional<Employee> employeeOptional = employeeRepository.findByEmployeeEmail(userEmail);
        
        // Ensure the employee exists; throw exception if not found
        Employee employee = employeeOptional.orElseThrow(() -> new RuntimeException("Employee not found"));

        // Return the response containing the user's email, role, employee ID, and token
        return new LoginResponseDto(userEmail, role, employee.getEmployeeId(), token);
    }

    /**
     * Handles the password reset process for users who forgot their password.
     *
     * @param forgotPasswordDto contains the email or phone number and the new password
     * @return true if the password update was successful, false if the input is invalid
     * @throws ResourceNotFoundException if the employee with the given email or phone does not exist
     * @throws EmployeePasswordUpdationFailedException if updating the password fails
     * @throws ResourceUnableToCreate if input fields are null or empty
     */
    public boolean forgotPassword(ForgotPasswordDto forgotPasswordDto)
            throws ResourceNotFoundException, EmployeePasswordUpdationFailedException, ResourceUnableToCreate {
        AuthServiceLogger.error("ForgotPassword - Process started");

        // Validate that forgotPasswordDto and required fields are not null or empty
        if (forgotPasswordDto == null || forgotPasswordDto.getEmailOrPhone() == null 
                || forgotPasswordDto.getEmailOrPhone().isEmpty()
                || forgotPasswordDto.getResetPassword() == null || forgotPasswordDto.getResetPassword().isEmpty()) {
            AuthServiceLogger.error("ForgotPassword - Details are incomplete. Email/Phone or Password is missing.");
            throw new ResourceUnableToCreate("Email/Phone or Password", "ForgotPassword", "Details are incomplete");
        }

        String emailOrPhone = forgotPasswordDto.getEmailOrPhone();

        // Regular expression for validating email
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        // Regular expression for validating phone numbers (exactly 10 digits)
        String phoneRegex = "^[0-9]{10}$";

        Pattern emailPattern = Pattern.compile(emailRegex);
        Pattern phonePattern = Pattern.compile(phoneRegex);

        Matcher emailMatcher = emailPattern.matcher(emailOrPhone);
        Matcher phoneMatcher = phonePattern.matcher(emailOrPhone);

        // Log the validation process
        AuthServiceLogger.info("ForgotPassword - Validating input (Email/Phone): {}", emailOrPhone);

        // Check if the input is a valid email or phone number
        boolean isEmail = emailMatcher.matches();
        boolean isPhone = phoneMatcher.matches();

        // If the input is an email
        if (isEmail) {
            // Log the process of checking if the email exists
            AuthServiceLogger.info("ForgotPassword - Checking if employee exists by email: {}", emailOrPhone);
            
            if (!employeeRepository.existsByEmployeeEmail(forgotPasswordDto.getEmailOrPhone())) {
                AuthServiceLogger.error("ForgotPassword - Employee with email {} not found", emailOrPhone);
                throw new ResourceNotFoundException("Employee", "Email", forgotPasswordDto.getEmailOrPhone());
            }

            // Log the password update attempt
            AuthServiceLogger.info("ForgotPassword - Updating password for email: {}", emailOrPhone);
            if (!employeeService.updateEmployeePassword(forgotPasswordDto.getEmailOrPhone(),
                    forgotPasswordDto.getResetPassword())) {
                AuthServiceLogger.error("ForgotPassword - Failed to update password for email: {}", emailOrPhone);
                throw new EmployeePasswordUpdationFailedException("Employee", "Email", emailOrPhone);
            }
            // Log success
            AuthServiceLogger.info("ForgotPassword - Password update successful for email: {}", emailOrPhone);
            return true; // Password update successful
        } 
        // If the input is a phone number
        else if (isPhone) {
            // Log the process of checking if the phone exists
            AuthServiceLogger.info("ForgotPassword - Checking if employee exists by phone: {}", emailOrPhone);
            
            if (!employeeRepository.existsByEmployeePhoneNumber(forgotPasswordDto.getEmailOrPhone())) {
                AuthServiceLogger.error("ForgotPassword - Employee with phone number {} not found", emailOrPhone);
                throw new ResourceNotFoundException("Employee", "Phone", forgotPasswordDto.getEmailOrPhone());
            }

            // Log the password update attempt
            AuthServiceLogger.info("ForgotPassword - Updating password for phone number: {}", emailOrPhone);
            if (!employeeService.updateEmployeePassword(forgotPasswordDto.getEmailOrPhone(),
                    forgotPasswordDto.getResetPassword())) {
                AuthServiceLogger.error("ForgotPassword - Failed to update password for phone number: {}", emailOrPhone);
                throw new EmployeePasswordUpdationFailedException("Employee", "Phone", emailOrPhone);
            }
            // Log success
            AuthServiceLogger.info("ForgotPassword - Password update successful for phone number: {}", emailOrPhone);
            return true; // Password update successful
        } else {
            // Log invalid input scenario
            AuthServiceLogger.error("ForgotPassword - Invalid input. Neither a valid email nor phone number provided: {}", emailOrPhone);
            return false; // Invalid input
        }
    }

}
