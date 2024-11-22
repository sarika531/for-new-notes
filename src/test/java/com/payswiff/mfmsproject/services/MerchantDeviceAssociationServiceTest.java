package com.payswiff.mfmsproject.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
 * Test class for MerchantDeviceAssociationService.
 * This class contains test cases for methods related to assigning devices
 * to merchants, retrieving associated devices, checking association status,
 * and getting device counts for merchants.
 */
class MerchantDeviceAssociationServiceTest {

    // Mocking dependencies for the service
    @Mock
    private MerchantDeviceAssociationRepository associationRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private DeviceRepository deviceRepository;

    // Injecting mocks into the service
    @InjectMocks
    private MerchantDeviceAssociationService service;

    private AutoCloseable closeable;

    /**
     * Initializes the mock environment before each test case.
     */
    @BeforeEach
    void setUp() {
        // Initializing Mockito's annotations
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Closes the mock environment after each test case.
     */
    @AfterEach
    void tearDown() throws Exception {
        // Closing the mocks to prevent memory leaks
        closeable.close();
    }

    // **************** Test Cases for assignDeviceToMerchant **************** //

    /**
     * Tests if a device can be successfully associated with a merchant.
     * Validates successful creation and association of merchant and device.
     */
    @Test
    void testAssignDeviceToMerchant_Success() throws ResourceNotFoundException, ResourceUnableToCreate {
        Long merchantId = 1L;
        Long deviceId = 2L;

        // Creating mock merchant and device objects
        Merchant merchant = new Merchant();
        merchant.setMerchantId(merchantId);

        Device device = new Device();
        device.setDeviceId(deviceId);

        // Mocking repository responses for merchant and device retrieval
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        // Mocking save behavior of association repository
        MerchantDeviceAssociation association = new MerchantDeviceAssociation();
        association.setMerchant(merchant);
        association.setDevice(device);
        when(associationRepository.save(any(MerchantDeviceAssociation.class))).thenReturn(association);

        // Invoking the service method and verifying the association
        MerchantDeviceAssociation result = service.assignDeviceToMerchant(merchantId, deviceId);

        assertEquals(merchant, result.getMerchant());
        assertEquals(device, result.getDevice());
        
        deviceRepository.deleteById(deviceId);
        merchantRepository.deleteById(merchantId);
    }

    /**
     * Tests if assignDeviceToMerchant throws ResourceUnableToCreate when merchantId is null.
     */
    @Test
    void testAssignDeviceToMerchant_NullMerchantId() {
        // Null merchantId should throw ResourceUnableToCreate exception
        assertThrows(ResourceUnableToCreate.class, () -> {
            service.assignDeviceToMerchant(null, 2L);
        });
    }

    /**
     * Tests if assignDeviceToMerchant throws ResourceUnableToCreate when deviceId is null.
     */
    @Test
    void testAssignDeviceToMerchant_NullDeviceId() {
        // Null deviceId should throw ResourceUnableToCreate exception
        assertThrows(ResourceUnableToCreate.class, () -> {
            service.assignDeviceToMerchant(1L, null);
        });
    }

    /**
     * Tests if assignDeviceToMerchant throws ResourceNotFoundException when merchant is not found.
     */
    @Test
    void testAssignDeviceToMerchant_MerchantNotFound() {
        // Mocking empty result for merchant retrieval
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        // Should throw ResourceNotFoundException when merchant is missing
        assertThrows(ResourceNotFoundException.class, () -> {
            service.assignDeviceToMerchant(1L, 2L);
        });
    }

    /**
     * Tests if assignDeviceToMerchant throws ResourceNotFoundException when device is not found.
     */
    @Test
    void testAssignDeviceToMerchant_DeviceNotFound() {
        // Mock merchant found, device not found
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());
        merchantRepository.deleteById(1l);
        // Should throw ResourceNotFoundException when device is missing
        assertThrows(ResourceNotFoundException.class, () -> {
            service.assignDeviceToMerchant(1L, 2L);
        });
        
        
    }

    // **************** Test Cases for getDevicesByMerchantId **************** //

    /**
     * Tests retrieval of devices by merchant ID.
     * Confirms that devices associated with a merchant are correctly retrieved.
     */
    @Test
    void testGetDevicesByMerchantId_Success() throws ResourceNotFoundException, ResourceUnableToCreate {
        Long merchantId = 1L;
        Merchant merchant = new Merchant();
        merchant.setMerchantId(merchantId);

        // Creating two mock devices
        Device device1 = new Device();
        Device device2 = new Device();

        // Creating mock associations between merchant and devices
        List<MerchantDeviceAssociation> associations = Arrays.asList(
            new MerchantDeviceAssociation(merchant, device1),
            new MerchantDeviceAssociation(merchant, device2)
        );

        // Mock repository responses for merchant retrieval and associations
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));
        when(associationRepository.findAllByMerchant(merchant)).thenReturn(associations);

        // Invoking the service method and verifying the devices
        List<Device> result = service.getDevicesByMerchantId(merchantId);

        assertEquals(2, result.size());
        assertTrue(result.contains(device1));
        assertTrue(result.contains(device2));
        
        merchantRepository.deleteById(merchantId);
       
    }

    /**
     * Tests if getDevicesByMerchantId throws ResourceUnableToCreate when merchantId is null.
     */
    @Test
    void testGetDevicesByMerchantId_NullMerchantId() {
        // Null merchantId should throw ResourceUnableToCreate exception
        assertThrows(ResourceUnableToCreate.class, () -> {
            service.getDevicesByMerchantId(null);
        });
    }

    /**
     * Tests if getDevicesByMerchantId throws ResourceNotFoundException when merchant is not found.
     */
    @Test
    void testGetDevicesByMerchantId_MerchantNotFound() {
        // Mocking empty result for merchant retrieval
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        // Should throw ResourceNotFoundException when merchant is missing
        assertThrows(ResourceNotFoundException.class, () -> {
            service.getDevicesByMerchantId(1L);
        });
    }

    // **************** Test Cases for isDeviceAssociatedWithMerchant **************** //

    /**
     * Tests if isDeviceAssociatedWithMerchant returns true when association exists.
     */
    @Test
    void testIsDeviceAssociatedWithMerchant_Success() throws ResourceNotFoundException, ResourceUnableToCreate {
        Long merchantId = 1L;
        Long deviceId = 2L;

        // Creating mock merchant and device objects
        Merchant merchant = new Merchant();
        merchant.setMerchantId(merchantId);

        Device device = new Device();
        device.setDeviceId(deviceId);

        // Mocking repository responses and association check
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        when(associationRepository.existsByMerchantAndDevice(merchant, device)).thenReturn(true);

        // Invoking the service method and verifying association
        assertTrue(service.isDeviceAssociatedWithMerchant(merchantId, deviceId));
    }

    /**
     * Tests if isDeviceAssociatedWithMerchant returns false when association does not exist.
     */
    @Test
    void testIsDeviceAssociatedWithMerchant_NoAssociation() throws ResourceNotFoundException, ResourceUnableToCreate {
        Long merchantId = 1L;
        Long deviceId = 2L;

        // Creating mock merchant and device objects
        Merchant merchant = new Merchant();
        merchant.setMerchantId(merchantId);

        Device device = new Device();
        device.setDeviceId(deviceId);

        // Mocking repository responses and no association found
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        when(associationRepository.existsByMerchantAndDevice(merchant, device)).thenReturn(false);

        // Invoking the service method and verifying no association
        assertFalse(service.isDeviceAssociatedWithMerchant(merchantId, deviceId));
    }

    /**
     * Tests if isDeviceAssociatedWithMerchant throws ResourceUnableToCreate when merchantId is null.
     */
    @Test
    void testIsDeviceAssociatedWithMerchant_NullMerchantId() {
        // Null merchantId should throw ResourceUnableToCreate exception
        assertThrows(ResourceUnableToCreate.class, () -> {
            service.isDeviceAssociatedWithMerchant(null, 2L);
        });
    }

    /**
     * Tests if isDeviceAssociatedWithMerchant throws ResourceUnableToCreate when deviceId is null.
     */
    @Test
    void testIsDeviceAssociatedWithMerchant_NullDeviceId() {
        // Null deviceId should throw ResourceUnableToCreate exception
        assertThrows(ResourceUnableToCreate.class, () -> {
            service.isDeviceAssociatedWithMerchant(1L, null);
        });
    }

    // **************** Test Cases for getDeviceCountByMerchant **************** //

    /**
     * Tests retrieval of device counts by merchant.
     * Confirms that correct device count per merchant is returned.
     */
    @Test
    void testGetDeviceCountByMerchant_Success() {
        List<Object[]> results = Arrays.asList(new Object[] {1L, 3L}, new Object[] {2L, 5L});

        // Mocking repository response for count query
        when(associationRepository.countDevicesByMerchant()).thenReturn(results);

        // Invoking the service method and verifying counts
        List<MerchantDeviceCountDTO> result = service.getDeviceCountByMerchant();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getMerchantId());
        assertEquals(3L, result.get(0).getDeviceCount());
        assertEquals(2L, result.get(1).getMerchantId());
        assertEquals(5L, result.get(1).getDeviceCount());
    }
}
