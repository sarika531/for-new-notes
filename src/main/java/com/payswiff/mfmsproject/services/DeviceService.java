package com.payswiff.mfmsproject.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.repositories.DeviceRepository;
/**
 * Service class responsible for handling the business logic related to Device entities.
 * Provides methods to save, retrieve, and check the existence of devices in the system.
 * 
 * <p>
 * Version: 0.0.1
 * </p>
 * <p>
 * Author: Ruchitha Guttikonda
 * </p>
 */
@Service
public class DeviceService {

    private static final Logger deviceServiceLogger = LogManager.getLogger(DeviceService.class);

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * Saves a new device if no existing device with the same model already exists.
     *
     *
     * @param device The Device entity to be saved. Must not be null and the device model 
     *               must not be empty.
     * @return The saved Device object.
     * @throws ResourceAlreadyExists if a device with the same model already exists.
     * @throws ResourceUnableToCreate if the device is null, if the device model is empty, 
     *                                 or if any error occurs during the device saving process.
     */
    public Device saveDevice(Device device) throws ResourceAlreadyExists, ResourceUnableToCreate {
        deviceServiceLogger.info("Attempting to save device with model");
        //check null
        if(device == null ) {
            deviceServiceLogger.error("Failed to save device: Device or device model is null/empty");
            
            throw new ResourceUnableToCreate("Device", "Model", 
                    "Device cannot be null and the device model cannot be empty.");

        }
        // Validate that the device is not null and the device model is not empty
        if ((device.getDeviceModel() == null || device.getDeviceModel().isEmpty())) {
            deviceServiceLogger.error("Failed to save device: Device or device model is null/empty");
            throw new ResourceUnableToCreate("Device", "Model", 
                "Device cannot be null and the device model cannot be empty.");
        }

        // Check if a device with the same model already exists
        Optional<Device> existingDevice = Optional.ofNullable(deviceRepository.findByModel(device.getDeviceModel()));

        // If a device with the same model is found, throw a ResourceAlreadyExists exception
        if (existingDevice.isPresent()) {
            deviceServiceLogger.error("Device with model {} already exists", device.getDeviceModel());
            throw new ResourceAlreadyExists("Device", "Model", device.getDeviceModel());
        }

        try {
            // Try to save the new device and return it
            Device savedDevice = deviceRepository.save(device);
            deviceServiceLogger.info("Device with model {} saved successfully", device.getDeviceModel());
            return savedDevice;
        } catch (Exception e) {
            // If any exception occurs during the save process, throw ResourceUnableToCreate
            deviceServiceLogger.error("Error occurred while saving device with model: {}", device.getDeviceModel(), e);
            throw new ResourceUnableToCreate("Device", "Model", device.getDeviceModel());
        }
    }

    /**
     * Retrieves a Device by its model.
     * 
     * @param model The model of the device to retrieve.
     * @return The ResponseEntity containing the found Device.
     * @throws ResourceNotFoundException If no device with the specified model is found.
     */
    public ResponseEntity<Device> getDeviceByModel(String model) throws ResourceNotFoundException {
        deviceServiceLogger.info("Attempting to fetch device with model: {}", model);

        if (model == null || model.isEmpty()) {
            deviceServiceLogger.error("Model parameter is null or empty");
            throw new ResourceNotFoundException("Parameter for device ","model","null or empty");
        }

        // Use Optional to handle the possibility of the device not being found
        Optional<Device> optionalDevice = Optional.ofNullable(deviceRepository.findByModel(model));

        // If present, return the device, otherwise throw the ResourceNotFoundException
        Device device = optionalDevice
            .orElseThrow(() -> {
                deviceServiceLogger.error("Device with model {} not found", model);
                return new ResourceNotFoundException("Device", "Model", model);
            });

        deviceServiceLogger.info("Device with model {} found", model);
        return new ResponseEntity<>(device, HttpStatus.FOUND);
    }

    /**
     * Retrieves all devices from the database.
     *
     * @return List of all Device entities.
     */
    public List<Device> getAllDevices() {
        deviceServiceLogger.info("Fetching all devices from the database");

        try {
            List<Device> devices = deviceRepository.findAll(); // Get all devices from the repository
            deviceServiceLogger.info("Found {} devices in the database", devices.size());
            return devices;
        } catch (Exception e) {
            deviceServiceLogger.error("Error occurred while fetching all devices", e);
            throw new RuntimeException("Error occurred while fetching all devices", e); // Handle any exception that may occur
        }
    }

    /**
     * Checks if a device exists by its ID.
     *
     * @param deviceId The ID of the device.
     * @return true if a device with the given ID exists, false otherwise.
     */
    public boolean existsById(Long deviceId) {
        if (deviceId == null) {
            deviceServiceLogger.error("Device ID cannot be null");
            return false;
        }

        boolean exists = deviceRepository.existsById(deviceId);
        deviceServiceLogger.info("Device with ID {} exists: {}", deviceId, exists);
        return exists;
    }
}
