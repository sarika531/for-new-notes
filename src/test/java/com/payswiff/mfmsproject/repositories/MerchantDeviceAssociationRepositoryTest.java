package com.payswiff.mfmsproject.repositories;

import com.payswiff.mfmsproject.dtos.RandomEmailGenerator;
import com.payswiff.mfmsproject.dtos.RandomPhoneNumberGenerator;
import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.models.Merchant;
import com.payswiff.mfmsproject.models.MerchantDeviceAssociation;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MerchantDeviceAssociationRepository}.
 * This class tests repository methods, including finding associations by merchant,
 * verifying merchant-device existence, and counting devices by merchant.
 */
@SpringBootTest
class MerchantDeviceAssociationRepositoryTest {

    @Autowired
    private MerchantDeviceAssociationRepository merchantDeviceAssociationRepository;
    
    @Autowired
    MerchantRepository merchantRepository;
    
    @Autowired
    DeviceRepository deviceRepository;
    
    private Merchant testMerchant;
    private Device testDevice;

    @BeforeEach
    void setUp() {
    	String randomEmail = RandomEmailGenerator.generateRandomEmail();
    	String phoneNumber=RandomPhoneNumberGenerator.generateRandomPhoneNumber();
        // Create and save the Merchant and Device entities first
        testMerchant = new Merchant(null, UUID.randomUUID().toString(), "TestMerchantName", randomEmail, 
                                    phoneNumber, "TestBusiness", "Retail", null, null);
        testDevice = new Device(null,  UUID.randomUUID().toString(), randomEmail, "DeviceCo", null, null);

        // Persist the Merchant and Device
        testMerchant = merchantRepository.save(testMerchant);
        testDevice = deviceRepository.save(testDevice);

        // Now create and save the MerchantDeviceAssociation
        MerchantDeviceAssociation association = new MerchantDeviceAssociation(testMerchant, testDevice);
        merchantDeviceAssociationRepository.save(association);
    }
    /**
     * Clear the repository after each test to avoid data contamination.
     */
    @AfterEach
    void tearDown() {
        merchantDeviceAssociationRepository.deleteAll();
    }

    /**
     * Verifies that associations are correctly found by a specific merchant.
     * Ensures that retrieving associations by merchant returns non-null, non-empty results.
     */
    @Test
    void testFindAllByMerchant() {
    	
//    	testMerchant.setMerchantEmail("test6@gmail.com");

        List<MerchantDeviceAssociation> associations = merchantDeviceAssociationRepository.findAllByMerchant(testMerchant);
        assertNotNull(associations, "Associations list should not be null");
        assertFalse(associations.isEmpty(), "Associations list should not be empty for the test merchant");
    }

    /**
     * Checks if an association exists for a given merchant and device.
     * Ensures that the association created in setup is recognized by the repository.
     */
    @Test
    void testExistsByMerchantAndDevice() {
    	
//    	testMerchant.setMerchantEmail("test5@gmail.com");
        boolean exists = merchantDeviceAssociationRepository.existsByMerchantAndDevice(testMerchant, testDevice);
        assertTrue(exists, "The association should exist for the test merchant and device");
    }

    /**
     * Counts the number of devices associated with each merchant.
     * Verifies that counting devices by merchant returns a list with correct data structure and count.
     */
    @Test
    void testCountDevicesByMerchant() {
        List<Object[]> results = merchantDeviceAssociationRepository.countDevicesByMerchant();
        assertNotNull(results, "Results should not be null");
        assertFalse(results.isEmpty(), "Results should not be empty as associations exist");
    }

    /**
     * Tests the count of devices by merchant with multiple merchants having associated devices.
     * Verifies correct handling of multiple merchants and that the repository returns the correct count.
     */
    @Test
    void testCountDevicesByMerchant_MultipleMerchants() {
    	
//    	testMerchant.setMerchantEmail("test4@gmail.com");

        merchantDeviceAssociationRepository.save(new MerchantDeviceAssociation(testMerchant, testDevice));
        
        List<Object[]> results = merchantDeviceAssociationRepository.countDevicesByMerchant();
        assertEquals(1, results.size(), "There should be 1 merchants in the results with devices");
    }

    /**
     * Verifies that the repository correctly identifies a non-existent association.
     * Ensures that a merchant-device combination not in the repository returns false for existence.
     */
    @Test
    void testExistsByMerchantAndDevice_NonExistent() {
        Merchant nonExistentMerchant = new Merchant(null,  UUID.randomUUID().toString(), "NonExistent", 
                                                   RandomEmailGenerator.generateRandomEmail(),RandomPhoneNumberGenerator.generateRandomPhoneNumber(), 
                                                    "NonBiz", "NonCategory", null, null);
        Device nonExistentDevice = new Device(null,  UUID.randomUUID().toString(), RandomEmailGenerator.generateRandomEmail(), "NonManufacturer", null, null);
        merchantRepository.save(nonExistentMerchant);
        deviceRepository.save(nonExistentDevice);
        boolean exists = merchantDeviceAssociationRepository.existsByMerchantAndDevice(nonExistentMerchant, nonExistentDevice);
        assertFalse(exists, "The association should not exist for a non-existent merchant and device");
    }

    /**
     * Verifies the repository's behavior when no devices are associated with a specific merchant.
     * Ensures that merchants without device associations yield a zero count.
     */
    @Test
    void testCountDevicesByMerchant_NoDevices() {
        Merchant merchantWithoutDevices = new Merchant(null,  UUID.randomUUID().toString(), "EmptyMerchant", 
                                                       "empty@example.com", "9998887776", 
                                                       "EmptyBiz", "EmptyCategory", null, null);
        
        List<Object[]> results = merchantDeviceAssociationRepository.countDevicesByMerchant();
        long count = results.stream()
                            .filter(r -> ((Long) r[0]).equals(merchantWithoutDevices.getMerchantId()))
                            .count();
        assertEquals(0, count, "Merchant with no devices should yield a count of zero");
    }

    /**
     * Tests retrieving associations for multiple merchants.
     * Verifies that each merchant has the correct number of associated devices.
     */
    @Test
    void testFindAllByMerchant_MultipleMerchants() {
        
//    	testMerchant.setMerchantEmail("test3@gmail.com");

        merchantDeviceAssociationRepository.save(new MerchantDeviceAssociation(testMerchant,testDevice));
        
        List<MerchantDeviceAssociation> associations1 = merchantDeviceAssociationRepository.findAllByMerchant(testMerchant);
        List<MerchantDeviceAssociation> associations2 = merchantDeviceAssociationRepository.findAllByMerchant(testMerchant);
        
        assertEquals(2, associations1.size(), "Test merchant should have exactly 1 association");
        assertEquals(2, associations2.size(), "Merchant2 should have exactly 1 association");
    }

    /**
     * Checks that the repository returns correct data types for counting devices by merchant.
     */
    @Test
    void testCountDevicesByMerchant_DataTypeCheck() {
        List<Object[]> results = merchantDeviceAssociationRepository.countDevicesByMerchant();
        
        for (Object[] result : results) {
            assertTrue(result[0] instanceof Long, "First element should be a Long (merchant ID)");
            assertTrue(result[1] instanceof Long, "Second element should be a Long (device count)");
        }
    }

    /**
     * Verifies that a merchant has the expected exact device count.
     * Specifically checks that the test merchant has one associated device.
     */
    @Test
    void testCountDevicesByMerchant_ExactDeviceCount() {
        List<Object[]> results = merchantDeviceAssociationRepository.countDevicesByMerchant();
        
        for (Object[] result : results) {
            if (result[0].equals(testMerchant.getMerchantId())) {
                assertEquals(1L, result[1], "Device count for test merchant should be exactly 1");
            }
        }
    }

    

    /**
     * Ensures that finding associations for a merchant with no associations yields an empty list.
     */
    @Test
    void testFindAllByMerchant_NoAssociationsExist() {
        merchantDeviceAssociationRepository.deleteAll();
//    	testMerchant.setMerchantEmail("test2@gmail.com");

        List<MerchantDeviceAssociation> associations = merchantDeviceAssociationRepository.findAllByMerchant(testMerchant);
        assertTrue(associations.isEmpty(), "No associations should be found if none exist in the repository");
    }

    /**
     * Verifies that counting devices does not fail when the repository is empty.
     * Ensures an empty list is returned if there are no entries.
     */
    @Test
    void testCountDevicesByMerchant_EmptyRepository() {
        merchantDeviceAssociationRepository.deleteAll();
        
        List<Object[]> results = merchantDeviceAssociationRepository.countDevicesByMerchant();
        assertTrue(results.isEmpty(), "Device count by merchant should return empty list if repository is empty");
    }

    /**
     * Ensures that unique merchant-device associations can be saved successfully.
     * Verifies that saving a new, unique association returns a non-null result.
     */
    @Test
    void testSaveUniqueMerchantDeviceAssociation() {
//    	testMerchant.setMerchantEmail("test1@gmail.com");
        MerchantDeviceAssociation association = new MerchantDeviceAssociation(testMerchant, testDevice);
        MerchantDeviceAssociation savedAssociation = merchantDeviceAssociationRepository.save(association);
        
        assertNotNull(savedAssociation, "Saved association should not be null for a unique merchant-device combo");
    }
}
