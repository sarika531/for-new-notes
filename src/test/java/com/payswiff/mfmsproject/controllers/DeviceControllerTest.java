/**
 * DeviceControllerTest performs unit tests on the DeviceController class.
 * It includes positive and negative test cases for device creation, retrieval by model, and retrieval of all devices.
 */

package com.payswiff.mfmsproject.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.reuquests.CreateDeviceRequest;
import com.payswiff.mfmsproject.services.DeviceService;

class DeviceControllerTest {

    @Mock
    private DeviceService deviceService; // Mocking DeviceService for dependency injection

    @InjectMocks
    private DeviceController deviceController; // DeviceController instance to test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    /**
     * Positive test case for createDevice: verifies successful device creation.
     */
    @Test
    void testCreateDevice_Success() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Arrange
        CreateDeviceRequest request = new CreateDeviceRequest("ModelX", "Manufacturer of ModelX");
        Device device = new Device(1L,UUID.randomUUID().toString(), "ModelX", "Manufacturer of ModelX",null,null);

        // Mock saveDevice to return the created device
        when(deviceService.saveDevice(any(Device.class))).thenReturn(device);

        // Act
        Device createdDevice = deviceController.createDevice(request);

        // Assert
        assertNotNull(createdDevice, "Device should not be null");
        assertEquals("ModelX", createdDevice.getDeviceModel(), "Device model should be 'ModelX'");
    }

    /**
     * Negative test case for createDevice: verifies behavior when a duplicate device is created.
     */
    @Test
    void testCreateDevice_ResourceAlreadyExists() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Arrange
        CreateDeviceRequest request = new CreateDeviceRequest("ModelX",  "Manufacturer of ModelX");

        // Mock saveDevice to throw a ResourceAlreadyExists exception for duplicate device
        when(deviceService.saveDevice(any(Device.class))).thenThrow(new ResourceAlreadyExists("Device already exists","",""));

        // Act & Assert
        ResourceAlreadyExists exception = assertThrows(ResourceAlreadyExists.class, () -> {
            deviceController.createDevice(request);
        });
        
        assertEquals("Device already exists with :  already exists.", exception.getMessage(), "Exception message should match");
    }

    /**
     * Positive test case for getDeviceByModel: verifies successful retrieval by model.
     */
    @Test
    void testGetDeviceByModel_Success() throws ResourceNotFoundException {
        // Arrange
        String model = "ModelX";
        Device device = new Device(1L,UUID.randomUUID().toString(), "ModelX", "Manufacturer of ModelX",null,null);

        // Mock getDeviceByModel to return a device
        when(deviceService.getDeviceByModel(model)).thenReturn(new ResponseEntity<>(device, HttpStatus.FOUND));

        // Act
        ResponseEntity<Device> response = deviceController.getDeviceByModel(model);

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode(), "Status should be 302 Found");
        assertEquals(device, response.getBody(), "Response body should match the found device");
    }

    /**
     * Negative test case for getDeviceByModel: verifies behavior when the device is not found.
     */
    @Test
    void testGetDeviceByModel_NotFound() throws ResourceNotFoundException {
        // Arrange
        String model = "NonExistentModel";

        // Mock getDeviceByModel to throw a ResourceNotFoundException
        when(deviceService.getDeviceByModel(model)).thenThrow(new ResourceNotFoundException("Device not found","",""));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deviceController.getDeviceByModel(model);
        });
        
        assertEquals("Device not found with :  is not found!!", exception.getMessage(), "Exception message should match");
    }

    /**
     * Positive test case for getAllDevices: verifies successful retrieval of all devices.
     */
    @Test
    void testGetAllDevices_Success() {
        // Arrange
        Device device1 = new Device(1L, UUID.randomUUID().toString(),"ModelX" , "Manufacturer of ModelX", null, null);
        Device device2 = new Device(2L,UUID.randomUUID().toString() , "ModelY", "Manufacturer of ModelY",null,null);
        List<Device> devices = Arrays.asList(device1, device2);

        // Mock getAllDevices to return a list of devices
        when(deviceService.getAllDevices()).thenReturn(devices);

        // Act
        ResponseEntity<List<Device>> response = deviceController.getAllDevices();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK");
        assertEquals(devices, response.getBody(), "Response body should match list of devices");
    }

    /**
     * Negative test case for getAllDevices: verifies behavior when no devices are found.
     */
    @Test
    void testGetAllDevices_Empty() {
        // Arrange
        List<Device> devices = Arrays.asList(); // Empty list

        // Mock getAllDevices to return an empty list
        when(deviceService.getAllDevices()).thenReturn(devices);

        // Act
        ResponseEntity<List<Device>> response = deviceController.getAllDevices();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK");
        assertTrue(response.getBody().isEmpty(), "Response body should be an empty list");
    }

    /**
     * Negative test case for createDevice: verifies behavior when creation fails unexpectedly.
     */
    @Test
    void testCreateDevice_ResourceUnableToCreate() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Arrange
        CreateDeviceRequest request = new CreateDeviceRequest("ModelZ",  "Description of ModelZ Manufacturer");

        // Mock saveDevice to throw a ResourceUnableToCreate exception
        when(deviceService.saveDevice(any(Device.class))).thenThrow(new ResourceUnableToCreate("Unable to create device","",""));

        // Act & Assert
        ResourceUnableToCreate exception = assertThrows(ResourceUnableToCreate.class, () -> {
            deviceController.createDevice(request);
        });

        assertEquals("Unable to create device with :  is unable to create at this moment!", exception.getMessage(), "Exception message should match");
    }

    /**
     * Edge case for createDevice: verifies null input handling for createDevice method.
     */
    @Test
    void testCreateDevice_NullRequest() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            deviceController.createDevice(null);
        }, "Should throw IllegalArgumentException for null request");
    }

    /**
     * Edge case for getDeviceByModel: verifies null input handling for model parameter.
     */
    @Test
    void testGetDeviceByModel_NullModel() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            deviceController.getDeviceByModel(null);
        }, "Should throw IllegalArgumentException for null model");
    }

    /**
     * Positive test case for getDeviceByModel: verifies successful retrieval by a different model name.
     */
    @Test
    void testGetDeviceByModel_DifferentModel() throws ResourceNotFoundException {
        // Arrange
        String model = "ModelY";
        Device device = new Device(2L,UUID.randomUUID().toString(), "ModelY", "Description of ModelY", null, null);

        // Mock getDeviceByModel to return a different device model
        when(deviceService.getDeviceByModel(model)).thenReturn(new ResponseEntity<>(device, HttpStatus.FOUND));

        // Act
        ResponseEntity<Device> response = deviceController.getDeviceByModel(model);

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode(), "Status should be 302 Found");
        assertEquals(device, response.getBody(), "Response body should match the found device with model ModelY");
    }
}
