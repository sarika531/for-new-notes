package com.payswiff.mfmsproject.controllers;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.reuquests.CreateDeviceRequest;
import com.payswiff.mfmsproject.services.DeviceService;

/**
 * REST controller for managing device-related operations.
 * <p>
 * This class provides REST endpoints for creating new devices and retrieving device information.
 * The operations include device creation (with validation to ensure no duplicates) and fetching 
 * details of individual devices or a list of all devices. The controller leverages the 
 * `DeviceService` for business logic and interacts with the underlying data model.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Ruchitha Guttikonda
 */
@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})
public class DeviceController {

    private static final Logger deviceControllerLogger = LogManager.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

    /**
     * Creates a new device.
     *
     * @param request The request body containing device details to be created.
     * @return The created Device object.
     * @throws ResourceAlreadyExists if a device with the same model already exists.
     * @throws ResourceUnableToCreate if there is an error while saving the device.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('admin')")
    public Device createDevice(@RequestBody CreateDeviceRequest request) throws ResourceAlreadyExists, ResourceUnableToCreate {
        deviceControllerLogger.info("Creating a new device with request: {}", request);

        try {
            // Convert CreateDeviceRequest to Device model
            Device device = request.toDevice();
            // Save the device using the service and return the created device
            Device savedDevice = deviceService.saveDevice(device);

            deviceControllerLogger.info("Device created successfully with model: {}", savedDevice.getDeviceModel());
            return savedDevice;
        } catch (ResourceAlreadyExists ex) {
            deviceControllerLogger.error("Device with the same model already exists: {}",ex);
            throw ex; // Re-throw exception after logging
        } catch (ResourceUnableToCreate ex) {
            deviceControllerLogger.error("Failed to create device with model: {}", ex);
            throw ex; // Re-throw exception after logging
        }
    }

    /**
     * Retrieves a device by its model.
     *
     * @param model The model of the device to retrieve.
     * @return ResponseEntity containing the found Device.
     * @throws ResourceNotFoundException if no device with the specified model is found.
     */
    @GetMapping("/get")
    public ResponseEntity<Device> getDeviceByModel(@RequestParam("model") String model) throws ResourceNotFoundException {
        deviceControllerLogger.info("Fetching device with model: {}", model);

        if (model == null) {
            deviceControllerLogger.error("Model parameter cannot be null");
            throw new IllegalArgumentException("Model parameter cannot be null");
        }

        try {
            ResponseEntity<Device> response = deviceService.getDeviceByModel(model);
            deviceControllerLogger.info("Device found with model: {}", model);
            return response;
        } catch (ResourceNotFoundException ex) {
            deviceControllerLogger.error("Device with model {} not found", model, ex);
            throw ex; // Re-throw exception after logging
        }
    }

    /**
     * Retrieves all devices.
     *
     * @return ResponseEntity containing a list of all Device entities.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Device>> getAllDevices() {
        deviceControllerLogger.info("Fetching all devices");

        try {
            List<Device> devices = deviceService.getAllDevices();
            deviceControllerLogger.info("Found {} devices", devices.size());
            return ResponseEntity.ok(devices);
        } catch (Exception ex) {
            deviceControllerLogger.error("Error occurred while fetching all devices", ex);
            throw new RuntimeException("Error occurred while fetching all devices", ex); // Handle any exception that may occur
        }
    }
}
