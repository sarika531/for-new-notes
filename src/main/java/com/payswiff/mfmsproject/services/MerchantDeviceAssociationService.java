package com.payswiff.mfmsproject.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payswiff.mfmsproject.dtos.MerchantDeviceCountDTO;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.models.Merchant;
import com.payswiff.mfmsproject.models.MerchantDeviceAssociation;
import com.payswiff.mfmsproject.repositories.DeviceRepository;
import com.payswiff.mfmsproject.repositories.MerchantDeviceAssociationRepository;
import com.payswiff.mfmsproject.repositories.MerchantRepository;

/**
 * Service class to manage associations between merchants and devices.
 * This class provides methods to associate devices with merchants,
 * retrieve devices by merchant, check device association status, and 
 * get counts of devices for each merchant.
 * @author Ruchitha Guttikonda
 * @version MFMS_0.0.1
 */
@Service
public class MerchantDeviceAssociationService {

    private static final Logger logger = LogManager.getLogger(MerchantDeviceAssociationService.class);

    @Autowired
    private MerchantDeviceAssociationRepository associationRepository; // Repository for merchant-device associations

    @Autowired
    private MerchantRepository merchantRepository; // Repository to manage merchant data

    @Autowired
    private DeviceRepository deviceRepository; // Repository to manage device data

    /**
     * Associates a device with a merchant.
     *
     * @param merchantId The ID of the merchant.
     * @param deviceId   The ID of the device.
     * @return The created MerchantDeviceAssociation object.
     * @throws ResourceNotFoundException If the merchant or device is not found.
     * @throws ResourceUnableToCreate    If merchantId or deviceId is null or invalid.
     */
    public MerchantDeviceAssociation assignDeviceToMerchant(Long merchantId, Long deviceId) 
            throws ResourceNotFoundException, ResourceUnableToCreate {
        
        logger.info("Attempting to assign device with ID " + deviceId + " to merchant with ID " + merchantId);
        
        // Validate merchantId: cannot be null or negative
        if (merchantId == null || merchantId <= 0) {
            logger.error("Invalid Merchant ID: " + merchantId);
            throw new ResourceUnableToCreate("MerchantDeviceAssociation", "Merchant ID cannot be null or negative", "Invalid Merchant ID");
        }
        
        // Validate deviceId: cannot be null or negative
        if (deviceId == null || deviceId <= 0) {
            logger.error("Invalid Device ID: " + deviceId);
            throw new ResourceUnableToCreate("MerchantDeviceAssociation", "Device ID cannot be null or negative", "Invalid Device ID");
        }

        // Retrieve merchant from repository or throw an exception if not found
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> {
                    logger.error("Merchant not found with ID: " + merchantId);
                    return new ResourceNotFoundException("Merchant", "ID", merchantId.toString());
                });

        // Retrieve device from repository or throw an exception if not found
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> {
                    logger.error("Device not found with ID: " + deviceId);
                    return new ResourceNotFoundException("Device", "ID", deviceId.toString());
                });

        // Create a new association between merchant and device
        MerchantDeviceAssociation association = new MerchantDeviceAssociation();
        association.setMerchant(merchant);
        association.setDevice(device);

        // Save the association to the repository and return the saved object
        MerchantDeviceAssociation savedAssociation = associationRepository.save(association);
        logger.info("Device with ID " + deviceId + " successfully assigned to merchant with ID " + merchantId);
        
        return savedAssociation;
    }
    
    /**
     * Retrieves a list of devices associated with a given merchant ID.
     *
     * @param merchantId The ID of the merchant.
     * @return A list of devices associated with the merchant.
     * @throws ResourceNotFoundException If the merchant is not found.
     * @throws ResourceUnableToCreate    If merchantId is null or invalid.
     */
    public List<Device> getDevicesByMerchantId(Long merchantId) 
            throws ResourceNotFoundException, ResourceUnableToCreate {
        
        logger.info("Retrieving devices for merchant with ID " + merchantId);
        
        // Validate merchantId: cannot be null or negative
        if (merchantId == null || merchantId <= 0) {
            logger.error("Invalid Merchant ID: " + merchantId);
            throw new ResourceUnableToCreate("MerchantDeviceAssociation", "Merchant ID cannot be null or negative", "Invalid Merchant ID");
        }

        // Retrieve merchant from repository or throw an exception if not found
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> {
                    logger.error("Merchant not found with ID: " + merchantId);
                    return new ResourceNotFoundException("Merchant", "ID", merchantId.toString());
                });

        // Retrieve all device associations for the specified merchant
        List<MerchantDeviceAssociation> associations = associationRepository.findAllByMerchant(merchant);
        logger.info("Found " + associations.size() + " devices associated with merchant ID " + merchantId);

        // Extract devices from associations and return as a list
        return associations.stream()
                .map(MerchantDeviceAssociation::getDevice)
                .collect(Collectors.toList());
    }
    
    /**
     * Checks if a device is associated with a specific merchant.
     *
     * @param merchantId The ID of the merchant.
     * @param deviceId   The ID of the device.
     * @return true if the device is associated with the merchant, false otherwise.
     * @throws ResourceNotFoundException If the merchant or device is not found.
     * @throws ResourceUnableToCreate    If merchantId or deviceId is null or invalid.
     */
    public boolean isDeviceAssociatedWithMerchant(Long merchantId, Long deviceId) 
            throws ResourceNotFoundException, ResourceUnableToCreate {
        
        logger.info("Checking association between merchant ID " + merchantId + " and device ID " + deviceId);
        
        // Validate merchantId and deviceId: cannot be null or negative
        if (merchantId == null || merchantId <= 0) {
            logger.error("Invalid Merchant ID: " + merchantId);
            throw new ResourceUnableToCreate("MerchantDeviceAssociation", "Merchant ID cannot be null or negative", "Invalid Merchant ID");
        }
        if (deviceId == null || deviceId <= 0) {
            logger.error("Invalid Device ID: " + deviceId);
            throw new ResourceUnableToCreate("MerchantDeviceAssociation", "Device ID cannot be null or negative", "Invalid Device ID");
        }

        // Check association existence
        boolean exists = associationRepository.existsByMerchantAndDevice(
                merchantRepository.findById(merchantId).orElseThrow(() -> new ResourceNotFoundException("Merchant", "ID", merchantId.toString())),
                deviceRepository.findById(deviceId).orElseThrow(() -> new ResourceNotFoundException("Device", "ID", deviceId.toString()))
        );
        
        logger.info("Association exists: " + exists);
        return exists;
    }
    
    /**
     * Retrieves a count of devices associated with each merchant.
     *
     * @return A list of MerchantDeviceCountDTO containing merchant ID and device count.
     */
    public List<MerchantDeviceCountDTO> getDeviceCountByMerchant() {
        logger.info("Retrieving device counts for all merchants");
        
        // Retrieve count of devices associated with each merchant from repository
        List<Object[]> results = associationRepository.countDevicesByMerchant();

        // Map results to DTOs and collect into a list
        List<MerchantDeviceCountDTO> deviceCounts = results.stream()
                .map(result -> new MerchantDeviceCountDTO(
                        (Long) result[0],  // Merchant ID
                        (Long) result[1]   // Device count
                ))
                .collect(Collectors.toList());
        
        logger.info("Device count retrieval complete. Found " + deviceCounts.size() + " merchants with associated devices.");
        return deviceCounts;
    }
}
