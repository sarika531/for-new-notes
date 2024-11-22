package com.payswiff.mfmsproject.controllers;

import java.util.List; // Importing List for handling collections of Merchant entities.

import org.apache.logging.log4j.LogManager; // Importing LogManager for creating logger
import org.apache.logging.log4j.Logger; // Importing Logger for logging

import org.springframework.beans.factory.annotation.Autowired; // Importing annotation for dependency injection.
import org.springframework.http.HttpStatus; // Importing HTTP status codes for responses.
import org.springframework.http.ResponseEntity; // Importing ResponseEntity for building HTTP responses.
import org.springframework.web.bind.annotation.*; // Importing Spring MVC annotations for RESTful web services.

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists; // Importing exception for handling already existing resources.
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException; // Importing exception for handling resource not found errors.
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate; // Importing exception for handling creation errors.
import com.payswiff.mfmsproject.models.Merchant; // Importing the Merchant model for handling merchant data.
import com.payswiff.mfmsproject.reuquests.CreateMerchantRequest; // Importing the request object for creating merchants.
import com.payswiff.mfmsproject.services.MerchantService; // Importing the service layer for business logic related to merchants.

/**
 * MerchantController handles requests related to merchant management,
 * including creating new merchants and retrieving existing merchants.
 * Provides endpoints for:
 * - Creating merchants
 * - Retrieving merchants by email or phone number
 * - Retrieving all merchants
 * <p>
 * Relies on {@link MerchantService} for executing business logic, handling exception scenarios,
 * and managing the retrieval and creation of merchant data.
 * </p>
 *
 * @version MFMS_0.0.1
 * @author Ruchitha Guttikonda
 */

@RestController
@RequestMapping("/api/merchants")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})
public class MerchantController {

    private static final Logger logger = LogManager.getLogger(MerchantController.class); // Initializing logger

    @Autowired
    private MerchantService merchantService; // Injecting the MerchantService for business logic

    /**
     * Creates a new Merchant after checking for existing merchants with the same email or phone number.
     *
     * @param request The request containing merchant details.
     * @return ResponseEntity containing the created merchant.
     * @throws ResourceAlreadyExists if a merchant with the same email or phone already exists.
     * @throws ResourceUnableToCreate if there's an error while creating the merchant.
     */
    @PostMapping("/create")
    public ResponseEntity<Merchant> createMerchant(@RequestBody CreateMerchantRequest request) 
            throws ResourceAlreadyExists, ResourceUnableToCreate {
        
        logger.info("Attempting to create a new merchant with details: {}", request); // Log the creation attempt

        // Attempt to create a new Merchant using the service and return the created Merchant
        try {
            Merchant createdMerchant = merchantService.createMerchant(request.toMerchant());
            logger.info("Merchant created successfully with ID: {}", createdMerchant.getMerchantId()); // Log success
            return new ResponseEntity<>(createdMerchant, HttpStatus.CREATED); // Return the created Merchant with HTTP 201 Created status
        } catch (ResourceAlreadyExists | ResourceUnableToCreate e) {
            logger.error("Failed to create merchant: {}", e.getMessage()); // Log failure details
            throw e; // Re-throwing the exception to be handled as per application’s exception handling
        }
    }

    /**
     * Retrieves a Merchant by email or phone number.
     *
     * @param email The email of the merchant (optional).
     * @param phone The phone number of the merchant (optional).
     * @return ResponseEntity containing the found Merchant.
     * @throws ResourceNotFoundException if no merchant is found with the provided email or phone.
     */
    @GetMapping("/get")
    public ResponseEntity<Merchant> getMerchantByEmailOrPhone(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone) throws ResourceNotFoundException {
        
        logger.info("Attempting to retrieve merchant by email: {} or phone: {}", email, phone); // Log retrieval attempt

        // Fetch the Merchant based on email or phone number from the service
        try {
            Merchant merchant = merchantService.getMerchantByEmailOrPhone(email, phone);
            logger.info("Merchant retrieved successfully: {}", merchant); // Log success
            return new ResponseEntity<>(merchant, HttpStatus.FOUND); // Return the found Merchant with HTTP 302 Found status
        } catch (ResourceNotFoundException e) {
            logger.error("No merchant found with email: {} or phone: {}", email, phone); // Log failure details
            throw e; // Re-throwing the exception to be handled appropriately
        }
    }

    /**
     * Retrieves all merchants.
     *
     * @return ResponseEntity containing a list of all Merchant entities.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        logger.info("Attempting to retrieve all merchants."); // Log attempt to fetch all merchants

        // Get all merchants from the service
        List<Merchant> merchants = merchantService.getAllMerchants();
        logger.info("Successfully retrieved all merchants. Total count: {}", merchants.size()); // Log success with count
        return ResponseEntity.ok(merchants); // Returns a 200 OK response with the list of merchants
    }
}
