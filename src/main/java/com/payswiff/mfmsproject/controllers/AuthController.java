package com.payswiff.mfmsproject.controllers; // Package declaration for the controllers

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired; // Importing Autowired annotation for dependency injection
import org.springframework.http.ResponseEntity; // Importing ResponseEntity for HTTP response handling
import org.springframework.web.bind.annotation.CrossOrigin; // Importing CrossOrigin annotation for CORS support
import org.springframework.web.bind.annotation.PostMapping; // Importing PostMapping for handling POST requests
import org.springframework.web.bind.annotation.RequestBody; // Importing RequestBody for reading request bodies
import org.springframework.web.bind.annotation.RequestMapping; // Importing RequestMapping for mapping web requests
import org.springframework.web.bind.annotation.RestController; // Importing RestController to indicate a RESTful controller

import com.payswiff.mfmsproject.dtos.ForgotPasswordDto; // Importing DTO for forgot password requests
import com.payswiff.mfmsproject.dtos.LoginDto; // Importing DTO for login requests
import com.payswiff.mfmsproject.dtos.LoginResponseDto; // Importing DTO for login response
import com.payswiff.mfmsproject.exceptions.EmployeePasswordUpdationFailedException; // Importing custom exception for password update failures
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException; // Importing custom exception for resource not found scenarios
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.services.AuthService; // Importing service for authentication operations
import com.payswiff.mfmsproject.services.EmployeeService; // Importing service for employee operations

/**
 * REST controller for handling authentication-related operations.
 * <p>
 * This class provides endpoints for user authentication, including login and password recovery 
 * functionalities. It is responsible for accepting authentication requests from clients, performing 
 * the necessary operations through service layers, and responding with the appropriate results. 
 * The authentication operations are secured and perform necessary validations such as ensuring that 
 * provided credentials are valid and processing password recovery requests.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

@RestController // Indicates that this class is a REST controller
@RequestMapping("/api/authentication") // Base URL for all authentication-related endpoints
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})

public class AuthController {
    
	private static final Logger AuthControllerLogger = LogManager.getLogger(AuthController.class);
	
    @Autowired // Automatically inject the AuthService bean
    private AuthService authService; // Service for authentication operations

    /**
     * Endpoint for user login.
     *
     * @param loginDto the login data transfer object containing username and password
     * @return ResponseEntity containing the login response
     * @throws ResourceUnableToCreate 
     * @throws ResourceNotFoundException 
     */
    @PostMapping("/login") // Maps POST requests to /api/authentication/login
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) throws ResourceUnableToCreate, ResourceNotFoundException {
    	//checl for null
    	if(loginDto==null || loginDto.getEmailOrPhone().isEmpty() || loginDto.getPassword().isEmpty()) {
    		//display login failure log
    		AuthControllerLogger.error("Login Dto is Null: login failure ");
    		if(loginDto==null) {
    			throw new ResourceUnableToCreate("Null LoginDto","Login Dto",null);
    	    }
    		else {
    			throw new ResourceUnableToCreate("Null LoginDto","Login Dto",loginDto.toString());
    		}
    	}
        // Use the AuthService to perform login and retrieve the response
        LoginResponseDto response = authService.login(loginDto); // Authenticate user and get response
        //display login successful log
        AuthControllerLogger.info("Login is successful - Logged-in User Email or Phone Number is: {}", loginDto.getEmailOrPhone());
        return ResponseEntity.ok(response); // Return HTTP 200 OK with the response body
    }
    
    /**
     * Endpoint for password recovery.
     *
     * @param forgotPasswordDto the forgot password data transfer object containing email or username
     * @return boolean indicating success or failure of the operation
     * @throws ResourceNotFoundException if the user is not found
     * @throws EmployeePasswordUpdationFailedException if the password update fails
     * @throws ResourceUnableToCreate 
     */
    @PostMapping("/forgotpassword") // Maps POST requests to /api/authentication/forgotpassword
    public boolean forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) throws ResourceNotFoundException, EmployeePasswordUpdationFailedException, ResourceUnableToCreate {
        //checkl null
    	if(forgotPasswordDto==null) {
    		AuthControllerLogger.error("FogotPassword  Dto is Null: login failure ");

    		throw new ResourceUnableToCreate("null request for forgotpassowrd body", null, null);
    	}
    	// Calls the AuthService to process the password recovery request
        boolean results= authService.forgotPassword(forgotPasswordDto); // Return the result of the operation
        //checking results is true or not
        if(results) {
        	//logging success of forgot password
        	AuthControllerLogger.info("Forgot Password is successful - User Email or Phone Number is: {}", forgotPasswordDto.getEmailOrPhone());
        	//return the result
        	return results;
        }
        else {
        	//logging failure of forgot password
        	AuthControllerLogger.error("Forgot Password is Failed Due to Invalid Phoen Number of Email - User Email or Phone Number is: {}", forgotPasswordDto.getEmailOrPhone());
        	//return the result
        	return results;
        }
    }
}
