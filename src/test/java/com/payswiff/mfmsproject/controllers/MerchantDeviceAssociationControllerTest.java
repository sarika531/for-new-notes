package com.payswiff.mfmsproject.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.payswiff.mfmsproject.dtos.MerchantDeviceCountDTO;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.models.MerchantDeviceAssociation;
import com.payswiff.mfmsproject.repositories.DeviceRepository;
import com.payswiff.mfmsproject.reuquests.MerchantDeviceAssociationRequest;
import com.payswiff.mfmsproject.services.MerchantDeviceAssociationService;

/**
 * Unit test class for the MerchantDeviceAssociationController.
 * Tests include scenarios such as successfully assigning devices to merchants,
 * fetching associated devices, checking device associations, and handling errors.
 */
class MerchantDeviceAssociationControllerTest {

    @Mock
    private MerchantDeviceAssociationService associationService; // Mock the service layer

    @InjectMocks
    private MerchantDeviceAssociationController controller; // Inject the mock service into the controller

    /**
     * Setup method to initialize mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize all mocks
    }

    /**
     * Test case for assigning a device to a merchant.
     * This test ensures that a device is correctly assigned and a 201 response is returned.
     */
    @Test
    void testAssignDeviceToMerchant_Success() throws ResourceNotFoundException, ResourceUnableToCreate {
        // Prepare mock request
        MerchantDeviceAssociationRequest request = new MerchantDeviceAssociationRequest(1L,1L);

        // Mock the service method
        MerchantDeviceAssociation association = new MerchantDeviceAssociation();
        when(associationService.assignDeviceToMerchant(1L, 1L)).thenReturn(association);

        // Call the controller method
        ResponseEntity<MerchantDeviceAssociation> response = controller.assignDeviceToMerchant(request);

        // Validate the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(association, response.getBody());
        
        
    }

    /**
     * Test case for assigning a device to a merchant when the request is null.
     * This should throw a ResourceUnableToCreate exception with an appropriate message.
     */
    @Test
    void testAssignDeviceToMerchant_NullRequest() {
        assertThrows(ResourceUnableToCreate.class, () -> {
            controller.assignDeviceToMerchant(null); // Null request should throw exception
        });
    }

    /**
     * Test case for retrieving the list of devices associated with a merchant.
     * This test ensures that the devices are fetched and returned correctly.
     */
    @Test
    void testGetMerchantDevicesList_Success() throws ResourceNotFoundException, ResourceUnableToCreate {
        // Prepare mock data
        Device device1 = new Device(1L, UUID.randomUUID().toString(),"Device 1","manufacturer-1", null, null);
        Device device2 = new Device(2L,UUID.randomUUID().toString(), "Device 2","manufacturer-2", null, null);
        List<Device> devices = Arrays.asList(device1, device2);

        // Mock the service method
        when(associationService.getDevicesByMerchantId(1L)).thenReturn(devices);

        // Call the controller method
        ResponseEntity<List<Device>> response = controller.getMerchantDevicesList(1L);

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(devices, response.getBody());
        
        
    }

    /**
     * Test case for retrieving the list of devices when the merchantId is null.
     * This should throw a ResourceNotFoundException indicating that the merchant is not found.
     */
    @Test
    void testGetMerchantDevicesList_NullMerchantId() {
        assertThrows(ResourceNotFoundException.class, () -> {
            controller.getMerchantDevicesList(null); // Null merchantId should throw exception
        });
    }

    /**
     * Test case for checking if a device is associated with a merchant.
     * This test ensures that the correct association status (true/false) is returned.
     */
    @Test
    void testCheckMerchantDeviceAssociation_Success() throws ResourceNotFoundException, ResourceUnableToCreate {
        // Prepare mock data
        when(associationService.isDeviceAssociatedWithMerchant(1L, 1L)).thenReturn(true);

        // Call the controller method
        ResponseEntity<Boolean> response = controller.checkMerchantDeviceAssociation(1L, 1L);

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody()); // Device should be associated with merchant
    }

    /**
     * Test case for checking the association when either merchantId or deviceId is null.
     * This should throw a ResourceNotFoundException with an appropriate message.
     */
    @Test
    void testCheckMerchantDeviceAssociation_NullParameters() {
        assertThrows(ResourceNotFoundException.class, () -> {
            controller.checkMerchantDeviceAssociation(null, 1L); // Null merchantId should throw exception
        });

        assertThrows(ResourceNotFoundException.class, () -> {
            controller.checkMerchantDeviceAssociation(1L, null); // Null deviceId should throw exception
        });
    }

    /**
     * Test case for retrieving the count of devices associated with each merchant.
     * This test checks if the device count by merchant is correctly returned.
     */
    @Test
    void testGetDeviceCountByMerchant_Success() {
        // Prepare mock data
        MerchantDeviceCountDTO count1 = new MerchantDeviceCountDTO(1L, 5L);
        MerchantDeviceCountDTO count2 = new MerchantDeviceCountDTO(2L, 3L);
        List<MerchantDeviceCountDTO> counts = Arrays.asList(count1, count2);

        // Mock the service method
        when(associationService.getDeviceCountByMerchant()).thenReturn(counts);

        // Call the controller method
        ResponseEntity<List<MerchantDeviceCountDTO>> response = controller.getDeviceCountByMerchant();

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(counts, response.getBody());
    }
}
