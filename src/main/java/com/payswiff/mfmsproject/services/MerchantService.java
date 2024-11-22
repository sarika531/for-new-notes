package com.payswiff.mfmsproject.services;

import org.apache.logging.log4j.LogManager; // Importing LogManager for logger instantiation
import org.apache.logging.log4j.Logger; // Importing Logger for logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Merchant;
import com.payswiff.mfmsproject.repositories.MerchantRepository;

import java.util.List;
import java.util.Optional;
/**
 * Service class for handling merchant-related operations.
 * <p>This service provides methods to create, retrieve, and manage merchants within the application.
 * It includes operations such as creating a new merchant, checking if a merchant exists by email or phone,
 * retrieving all merchants, and checking for the existence of a merchant by ID. The service ensures that no 
 * merchant with duplicate email or phone can be created, and validates input data to maintain integrity.</p>
 *
 * <p>It also logs important events such as success, warnings, and errors during execution.</p>
 * 
 * @version MFMS_0.0.1
 * @author Ruchitha Guttikonda
 * */
@Service
public class MerchantService {

    private static final Logger logger = LogManager.getLogger(MerchantService.class); // Initializing logger

    @Autowired
    private MerchantRepository merchantRepository;

    /**
     * Creates a new merchant if no existing merchant with the same email or phone exists.
     *
     * @param merchant The Merchant entity to be created. Must not be null and must have 
     *                 non-empty email and phone values.
     * @return The created Merchant object.
     * @throws ResourceAlreadyExists if a merchant with the same email or phone number exists.
     * @throws ResourceUnableToCreate if the merchant is null, if either email or phone 
     *                                  is empty, or if any error occurs during the merchant 
     *                                  creation process.
     */
    public Merchant createMerchant(Merchant merchant) throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Validate merchant email and phone
        if (merchant == null || 
            (merchant.getMerchantEmail() == null || merchant.getMerchantEmail().isEmpty()) || 
            (merchant.getMerchantPhone() == null || merchant.getMerchantPhone().isEmpty())) {
            logger.error("Failed to create merchant: Email or Phone is null or empty."); // Log validation error
            throw new ResourceUnableToCreate("Merchant", "Email/Phone", 
                    "Email or Phone cannot be null or empty.");
        }

        // Check if merchant with the same email or phone already exists
        Optional<Merchant> existingMerchant = Optional.ofNullable(merchantRepository.findByMerchantEmailOrMerchantPhone(
            merchant.getMerchantEmail(), merchant.getMerchantPhone()));

        if (existingMerchant.isPresent()) {
            logger.warn("Merchant with email: {} or phone: {} already exists.", 
                        merchant.getMerchantEmail(), merchant.getMerchantPhone()); // Log existence check
            throw new ResourceAlreadyExists("Merchant", "Email/Phone", 
                    merchant.getMerchantEmail() + "/" + merchant.getMerchantPhone());
        }

        try {
            // Save the new merchant and return it
            Merchant savedMerchant = merchantRepository.save(merchant);
            logger.info("Merchant created successfully with ID: {}", savedMerchant.getMerchantId()); // Log success
            return savedMerchant;
        } catch (Exception e) {
            // Handle any exception during the save operation
            logger.error("Error occurred while saving merchant with email: {} or phone: {}. Exception: {}", 
                         merchant.getMerchantEmail(), merchant.getMerchantPhone(), e.getMessage()); // Log error details
            throw new ResourceUnableToCreate("Merchant", "Email/Phone", 
                    merchant.getMerchantEmail() + "/" + merchant.getMerchantPhone());
        }
    }

    /**
     * Retrieves a Merchant by its email or phone.
     *
     * @param email The email of the merchant to retrieve.
     * @param phone The phone number of the merchant to retrieve.
     * @return The found Merchant object.
     * @throws ResourceNotFoundException If no merchant is found with the given email or phone.
     */
    public Merchant getMerchantByEmailOrPhone(String email, String phone) throws ResourceNotFoundException {
        logger.info("Attempting to retrieve merchant by email: {} or phone: {}", email, phone); // Log retrieval attempt

        Optional<Merchant> optionalMerchant = Optional.ofNullable(
            merchantRepository.findByMerchantEmailOrMerchantPhone(email, phone));

        if (optionalMerchant.isPresent()) {
            logger.info("Merchant found with email: {} or phone: {}", email, phone); // Log success
            return optionalMerchant.get();
        } else {
            logger.warn("No merchant found with email: {} or phone: {}", email, phone); // Log failure
            throw new ResourceNotFoundException("Merchant", "Email/Phone", email != null ? email : phone);
        }
    }
    
    /**
     * Retrieves all merchants from the database.
     *
     * @return List of all Merchant entities.
     */
    public List<Merchant> getAllMerchants() {
        logger.info("Attempting to retrieve all merchants."); // Log attempt to retrieve all merchants
        List<Merchant> merchants = merchantRepository.findAll();
        logger.info("Successfully retrieved all merchants. Total count: {}", merchants.size()); // Log success with count
        return merchants;
    }

    /**
     * Checks if a merchant exists by its ID.
     *
     * @param merchantId The ID of the merchant.
     * @return true if a merchant with the given ID exists, false otherwise.
     */
    public boolean existsById(Long merchantId) {
        boolean exists = merchantRepository.existsById(merchantId); // Uses repository to check if merchant exists
        logger.info("Merchant exists check for ID {}: {}", merchantId, exists); // Log existence check result
        return exists;
    }
}
