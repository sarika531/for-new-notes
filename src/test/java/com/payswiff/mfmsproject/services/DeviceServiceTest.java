package com.payswiff.mfmsproject.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.repositories.DeviceRepository;

import java.util.Collections;
import java.util.List;

/**
 * Unit tests for the DeviceService class.
 *
 * This class tests the functionality of the DeviceService, ensuring that
 * all methods behave as expected under various conditions. It uses
 * Mockito to mock the dependencies and JUnit 5 for the testing framework.
 *
 * Test Methods:
 * - testSaveDevice_Success: Tests successful saving of a device.
 * - testSaveDevice_AlreadyExists: Tests handling of an already existing device.
 * - testSaveDevice_DeviceIsNull: Tests saving a null device.
 * - testSaveDevice_DeviceModelIsEmpty: Tests saving a device with an empty model.
 * - testSaveDevice_SaveThrowsException: Tests exception handling during save.
 * - testSaveDevice_ModelIsNull: Tests saving a device with a null model.
 * - testGetDeviceByModel_Success: Tests retrieving a device by model.
 * - testGetDeviceByModel_NotFound: Tests retrieval of a non-existent device.
 * - testGetDeviceByModel_NullModel: Tests retrieval with a null model.
 * - testGetDeviceByModel_EmptyModel: Tests retrieval with an empty model.
 * - testGetAllDevices_Success: Tests successful retrieval of all devices.
 * - testGetAllDevices_EmptyList: Tests retrieval when no devices exist.
 * - testExistsById_Exists: Tests existence check for an existing device ID.
 * - testExistsById_NotExists: Tests existence check for a non-existing device ID.
 * - testExistsById_NullId: Tests existence check for a null ID.
 */
class DeviceServiceTest {

    @InjectMocks
    private DeviceService deviceService; // The class under test

    @Mock
    private DeviceRepository deviceRepository; // Mocked repository for devices

    private Device device; // Device object used for testing

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        device = new Device(); // Create a new device instance for each test
        device.setDeviceModel("TestModel"); // Set a model for the device
    }

    // Test cases for the saveDevice method
    /**
     * Tests successful saving of a device.
     * 
     * This test verifies that when a device does not already exist,
     * the save operation succeeds and returns the saved device.
     * 
     * @throws ResourceAlreadyExists if a device with the same model already exists.
     * @throws ResourceUnableToCreate if an unexpected error occurs during save.
     */
    @Test
    void testSaveDevice_Success() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Mock the behavior for device repository
        when(deviceRepository.findByModel(device.getDeviceModel())).thenReturn(null);
        when(deviceRepository.save(device)).thenReturn(device);

        // Act: Save the device
        Device savedDevice = deviceService.saveDevice(device);
        
        // Assert: Check that the saved device is the same as the input device
        assertEquals(device, savedDevice);
        verify(deviceRepository).save(device); // Verify that save was called
    }
    /**
     * Tests handling of an already existing device.
     * 
     * This test verifies that when attempting to save a device that
     * already exists, a ResourceAlreadyExists exception is thrown.
     */
    @Test
    void testSaveDevice_AlreadyExists() {
        // Mock the behavior to return an existing device
        when(deviceRepository.findByModel(device.getDeviceModel())).thenReturn(device);

        // Assert: Expect ResourceAlreadyExists to be thrown
        ResourceAlreadyExists exception = assertThrows(ResourceAlreadyExists.class, () -> {
            deviceService.saveDevice(device);
        });
        assertEquals("Device with Model: TestModel already exists.", exception.getMessage());
    }
    /**
     * Tests saving a null device.
     * 
     * This test verifies that passing a null device to the save
     * method throws a ResourceUnableToCreate exception.
     */
    @Test
    void testSaveDevice_DeviceIsNull() {
        // Assert: Expect ResourceUnableToCreate to be thrown when null is passed
        ResourceUnableToCreate exception = assertThrows(ResourceUnableToCreate.class, () -> {
            deviceService.saveDevice(null);
        });
        assertEquals("Device with Model: Device cannot be null and the device model cannot be empty. is unable to create at this moment!", exception.getMessage());
    }
    /**
     * Tests saving a device with an empty model.
     * 
     * This test verifies that if the device model is empty, the
     * save method throws a ResourceUnableToCreate exception.
     */
    @Test
    void testSaveDevice_DeviceModelIsEmpty() {
        // Set device model to empty string
        device.setDeviceModel("");
        
        // Assert: Expect ResourceUnableToCreate to be thrown
        ResourceUnableToCreate exception = assertThrows(ResourceUnableToCreate.class, () -> {
            deviceService.saveDevice(device);
        });
        assertEquals("Device with Model: Device cannot be null and the device model cannot be empty. is unable to create at this moment!", exception.getMessage());
    }
    /**
     * Tests exception handling during save.
     * 
     * This test verifies that if an exception occurs while trying to save
     * the device, a ResourceUnableToCreate exception is thrown.
     */
    @Test
    void testSaveDevice_SaveThrowsException() {
        // Mock the behavior to simulate an error during save
        when(deviceRepository.findByModel(device.getDeviceModel())).thenReturn(null);
        when(deviceRepository.save(device)).thenThrow(new RuntimeException("Database error"));

        // Assert: Expect ResourceUnableToCreate to be thrown
        ResourceUnableToCreate exception = assertThrows(ResourceUnableToCreate.class, () -> {
            deviceService.saveDevice(device);
        });
        assertEquals("Device with Model: TestModel is unable to create at this moment!", exception.getMessage());
    }
    
    /**
     * Tests saving a device with a null model.
     * 
     * This test verifies that if the device model is null, the
     * save method throws a ResourceUnableToCreate exception.
     */
    @Test
    void testSaveDevice_ModelIsNull() {
        // Set the device model to null
        device.setDeviceModel(null);
        
        // Assert: Expect ResourceUnableToCreate to be thrown
        ResourceUnableToCreate exception = assertThrows(ResourceUnableToCreate.class, () -> {
            deviceService.saveDevice(device);
        });
        assertEquals("Device with Model: Device cannot be null and the device model cannot be empty. is unable to create at this moment!", exception.getMessage());
    }

    // Test cases for the getDeviceByModel 

    /**
     * Tests retrieving a device by model successfully.
     * 
     * This test verifies that when a device with the specified model
     * exists, the getDeviceByModel method returns the correct device.
     * 
     * @throws ResourceNotFoundException if the device is not found.
     */
    @Test
    void testGetDeviceByModel_Success() throws ResourceNotFoundException {
        // Mock the behavior to return the device
        when(deviceRepository.findByModel(device.getDeviceModel())).thenReturn(device);

        // Act: Retrieve the device by model
        ResponseEntity<Device> response = deviceService.getDeviceByModel(device.getDeviceModel());
        
        // Assert: Check that the response status and body are correct
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(device, response.getBody());
    }
    /**
     * Tests retrieval of a non-existent device.
     * 
     * This test verifies that when attempting to retrieve a device
     * by a model that does not exist, a ResourceNotFoundException is thrown.
     */
    @Test
    void testGetDeviceByModel_NotFound() {
        // Mock the behavior to return null for a nonexistent device
        when(deviceRepository.findByModel(device.getDeviceModel())).thenReturn(null);

        // Assert: Expect ResourceNotFoundException to be thrown
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deviceService.getDeviceByModel(device.getDeviceModel());
        });
        assertEquals("Device with Model: TestModel is not found!!", exception.getMessage());
    }
    /**
     * Tests retrieval with a null model.
     * 
     * This test verifies that if the model is null, a
     * ResourceNotFoundException is thrown when attempting to retrieve the device.
     */
    @Test
    void testGetDeviceByModel_NullModel() {
        // Assert: Expect ResourceNotFoundException to be thrown when model is null
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deviceService.getDeviceByModel(null);
        });
        assertEquals("Parameter for device  with model: null or empty is not found!!", exception.getMessage());
    }
    /**
     * Tests retrieval with an empty model.
     * 
     * This test verifies that when attempting to retrieve a device
     * with an empty model, a ResourceNotFoundException is thrown.
     */
    @Test
    void testGetDeviceByModel_EmptyModel() {
        // Assert: Expect ResourceNotFoundException to be thrown when model is empty
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deviceService.getDeviceByModel("");
        });
        assertEquals("Parameter for device  with model: null or empty is not found!!", exception.getMessage());
    }

    // Test cases for the getAllDevices method
    /**
     * Tests successful retrieval of all devices.
     * 
     * This test verifies that when there are devices in the repository,
     * the getAllDevices method returns them correctly.
     */
    @Test
    void testGetAllDevices_Success() {
        // Mock the behavior to return a list containing the device
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        // Act: Retrieve all devices
        List<Device> devices = deviceService.getAllDevices();
        
        // Assert: Check that the returned list contains the correct device
        assertFalse(devices.isEmpty());
        assertEquals(1, devices.size());
        assertEquals(device, devices.get(0));
    }
    
    /**
     * Tests retrieval when no devices exist.
     * 
     * This test verifies that when there are no devices in the repository,
     * the getAllDevices method returns an empty list.
     */
    @Test
    void testGetAllDevices_EmptyList() {
        // Mock the behavior to return an empty list
        when(deviceRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: Retrieve all devices
        List<Device> devices = deviceService.getAllDevices();
        
        // Assert: Check that the list is empty
        assertTrue(devices.isEmpty());
    }

    // Test cases for the existsById method
    
    /**
     * Tests existence check for an existing device ID.
     * 
     * This test verifies that when checking for a device ID that exists,
     * the existsById method returns true.
     */
    
    @Test
    void testExistsById_Exists() {
        Long deviceId = 1L; // Example device ID
        when(deviceRepository.existsById(deviceId)).thenReturn(true); // Mock behavior for existing ID

        // Act: Check if the device exists
        boolean exists = deviceService.existsById(deviceId);
        
        // Assert: Verify that the device exists
        assertTrue(exists);
    }
    /**
     * Tests existence check for a non-existing device ID.
     * 
     * This test verifies that when checking for a device ID that does not exist,
     * the existsById method returns false.
     */
    @Test
    void testExistsById_NotExists() {
        Long deviceId = 1L; // Example device ID
        when(deviceRepository.existsById(deviceId)).thenReturn(false); // Mock behavior for non-existing ID

        // Act: Check if the device exists
        boolean exists = deviceService.existsById(deviceId);
        
        // Assert: Verify that the device does not exist
        assertFalse(exists);
    }
    /**
     * Tests existence check for a null ID.
     * 
     * This test verifies that when checking for a null ID,
     * the existsById method returns false.
     */
    @Test
    void testExistsById_NullId() {
        // Act: Check if the device exists with null ID
        boolean exists = deviceService.existsById(null);
        
        // Assert: Verify that the method returns false for null ID
        assertFalse(exists);
    }
}
