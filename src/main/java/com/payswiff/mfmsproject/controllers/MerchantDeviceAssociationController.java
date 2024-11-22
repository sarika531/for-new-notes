package com.payswiff.mfmsproject.controllers;

import java.util.List; // Importing List for handling collections of devices.

import org.apache.logging.log4j.LogManager; // Importing LogManager for logger instantiation
import org.apache.logging.log4j.Logger; // Importing Logger for logging
import org.springframework.beans.factory.annotation.Autowired; // Importing annotation for dependency injection.
import org.springframework.http.HttpStatus; // Importing HTTP status codes for responses.
import org.springframework.http.ResponseEntity; // Importing ResponseEntity for building HTTP responses.
import org.springframework.web.bind.annotation.*; // Importing Spring MVC annotations for RESTful web services.

import com.payswiff.mfmsproject.dtos.MerchantDeviceCountDTO; // Importing DTO for device counts by merchant.
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException; // Importing exception for handling resource not found errors.
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Device; // Importing Device model for handling device data.
import com.payswiff.mfmsproject.models.MerchantDeviceAssociation; // Importing model for merchant-device associations.
import com.payswiff.mfmsproject.reuquests.MerchantDeviceAssociationRequest; // Importing request object for merchant-device associations.
import com.payswiff.mfmsproject.services.MerchantDeviceAssociationService; // Importing service layer for merchant-device association logic.

/**
 * MerchantDeviceAssociationController handles requests related to the 
 * association between merchants and devices, including assigning devices 
 * to merchants, checking existing associations, and retrieving device counts 
 * for merchants.
 * <p>
 * Provides the following endpoints:
 * - Assigning devices to merchants
 * - Retrieving the list of devices associated with a merchant
 * - Checking if a device is associated with a merchant
 * - Retrieving the count of devices associated with each merchant
 * </p>
 * 
 * This controller relies on {@link MerchantDeviceAssociationService} for handling 
 * the business logic related to merchant-device associations.
 * 
 * @version MFMS_0.0.1
 * @author Ruchitha Guttikonda
 */

@RestController
@RequestMapping("/api/MerchantDeviceAssociation")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})
public class MerchantDeviceAssociationController {

    private static final Logger logger = LogManager.getLogger(MerchantDeviceAssociationController.class); // Initializing logger

    @Autowired
    private MerchantDeviceAssociationService associationService; // Injecting the MerchantDeviceAssociationService for business logic

    /**
     * Assigns a device to a merchant.
     *
     * @param request The request containing merchant and device IDs.
     * @return ResponseEntity containing the created association.
     * @throws ResourceNotFoundException if the merchant or device is not found.
     * @throws ResourceUnableToCreate 
     */
    @PostMapping("/assign")
    public ResponseEntity<MerchantDeviceAssociation> assignDeviceToMerchant(
            @RequestBody MerchantDeviceAssociationRequest request) throws ResourceNotFoundException, ResourceUnableToCreate {
        
        if (request == null) {
            logger.error("Failed to assign device to merchant: Request is null."); // Log null request error
            throw new ResourceUnableToCreate("NULL request for devicemerchant association", null, null);
        }
        
        logger.info("Attempting to assign device ID: {} to merchant ID: {}", request.getDeviceId(), request.getMerchantId()); // Log assignment attempt

        // Create the association using the service layer
        MerchantDeviceAssociation createdAssociation = associationService
                .assignDeviceToMerchant(request.getMerchantId(), request.getDeviceId());
        
        logger.info("Device assigned successfully to merchant. Association ID: {}", createdAssociation.getId()); // Log successful association creation
        return new ResponseEntity<>(createdAssociation, HttpStatus.CREATED); // Return the created association with HTTP 201 Created status
    }

    /**
     * Retrieves the list of devices associated with a specific merchant.
     *
     * @param merchantId The ID of the merchant whose devices are to be retrieved.
     * @return ResponseEntity containing a list of associated devices.
     * @throws ResourceNotFoundException if the merchant is not found.
     * @throws ResourceUnableToCreate 
     */
    @GetMapping("/get/merchantdeviceslist")
    public ResponseEntity<List<Device>> getMerchantDevicesList(@RequestParam Long merchantId) throws ResourceNotFoundException, ResourceUnableToCreate {
        
        if (merchantId == null) {
            logger.error("Failed to retrieve devices: Merchant ID is null."); // Log null merchantId error
            throw new ResourceNotFoundException("Merchant", "ID", null);
        }

        logger.info("Retrieving devices for merchant ID: {}", merchantId); // Log retrieval attempt

        // Get the list of devices associated with the specified merchant
        List<Device> devices = associationService.getDevicesByMerchantId(merchantId);
        
        logger.info("Successfully retrieved {} devices for merchant ID: {}", devices.size(), merchantId); // Log successful retrieval with count
        return new ResponseEntity<>(devices, HttpStatus.OK); // Return the list of devices with HTTP 200 OK status
    }

    /**
     * Checks if a device is associated with a specific merchant.
     *
     * @param merchantId The ID of the merchant.
     * @param deviceId   The ID of the device.
     * @return ResponseEntity containing a boolean indicating the association status.
     * @throws ResourceNotFoundException if the merchant or device is not found.
     * @throws ResourceUnableToCreate 
     */
    @GetMapping("/check/merchant-device")
    public ResponseEntity<Boolean> checkMerchantDeviceAssociation(
            @RequestParam Long merchantId,
            @RequestParam Long deviceId) throws ResourceNotFoundException, ResourceUnableToCreate {
        
        if (merchantId == null || deviceId == null) {
            logger.error("Merchant ID or Device ID is null. Merchant ID: {}, Device ID: {}", merchantId, deviceId); // Log null ID error
            throw new ResourceNotFoundException("merchantDevice", "merchantId or DeviceId", null);
        }
        
        logger.info("Checking association between merchant ID: {} and device ID: {}", merchantId, deviceId); // Log check attempt

        // Check if the specified device is associated with the specified merchant
        boolean exists = associationService.isDeviceAssociatedWithMerchant(merchantId, deviceId);
        
        logger.info("Association check completed. Exists: {}", exists); // Log result of association check
        return new ResponseEntity<>(exists, HttpStatus.OK); // Return the association status with HTTP 200 OK status
    }

    /**
     * Retrieves the count of devices associated with each merchant.
     *
     * @return ResponseEntity containing a list of device counts by merchant.
     */
    @GetMapping("/device-count")
    public ResponseEntity<List<MerchantDeviceCountDTO>> getDeviceCountByMerchant() {
        logger.info("Retrieving device count for each merchant."); // Log attempt to retrieve device counts

        // Get the counts of devices associated with each merchant
        List<MerchantDeviceCountDTO> deviceCounts = associationService.getDeviceCountByMerchant();
        
        logger.info("Successfully retrieved device counts for merchants. Total merchants: {}", deviceCounts.size()); // Log success with merchant count
        return new ResponseEntity<>(deviceCounts, HttpStatus.OK); // Return the list of device counts with HTTP 200 OK status
    }
}
