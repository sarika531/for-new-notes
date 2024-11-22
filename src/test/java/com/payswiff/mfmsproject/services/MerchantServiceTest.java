package com.payswiff.mfmsproject.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Merchant;
import com.payswiff.mfmsproject.repositories.MerchantRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
/**
 * Unit tests for the MerchantService class.
 *
 * This test suite verifies the functionalities of the MerchantService methods, ensuring
 * that the service behaves correctly under various conditions. The following test cases are covered:
 * 
 * 1. {@link MerchantService#createMerchant(Merchant)}:
 *    - testCreateMerchant_Success: Tests successful creation of a merchant with unique email and phone.
 *    - testCreateMerchant_ResourceAlreadyExists: Tests for ResourceAlreadyExists exception when a duplicate merchant is created.
 *    - testCreateMerchant_ResourceUnableToCreate: Tests for ResourceUnableToCreate exception due to an unexpected error during merchant creation.
 * 
 * 2. {@link MerchantService#getMerchantByEmailOrPhone(String, String)}:
 *    - testGetMerchantByEmailOrPhone_Success: Tests successful retrieval of a merchant by email or phone.
 *    - testGetMerchantByEmailOrPhone_ResourceNotFoundException: Tests for ResourceNotFoundException when the merchant does not exist.
 *    - testGetMerchantByEmailOrPhone_ThrowsForNullEmailAndPhone: Tests for ResourceNotFoundException when both email and phone are null.
 * 
 * 3. {@link MerchantService#getAllMerchants()}:
 *    - testGetAllMerchants_ReturnsListOfMerchants: Tests retrieval of all merchants when multiple exist.
 *    - testGetAllMerchants_EmptyList: Tests retrieval when no merchants exist.
 *    - testGetAllMerchants_NoMerchantsFound: Tests retrieval of an empty list when no merchants are found.
 * 
 * 4. {@link MerchantService#existsById(Long)}:
 *    - testExistsById_Exists: Tests checking for existence of a merchant ID that exists.
 *    - testExistsById_DoesNotExist: Tests checking for existence of a merchant ID that does not exist.
 * 
 * 5. Additional tests for edge cases:
 *    - testCreateMerchant_NullMerchant_ThrowsResourceUnableToCreate: Tests for ResourceUnableToCreate exception when null Merchant is provided.
 *
 * This test suite ensures that the MerchantService is robust and handles various scenarios appropriately.
 */
class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository; // Mock the MerchantRepository dependency

    @InjectMocks
    private MerchantService merchantService; // Inject mocks into MerchantService

    /**
     * Initializes the Mockito annotations before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the successful creation of a merchant.
     * 
     * This test verifies that a merchant can be created successfully when the provided
     * email and phone number are unique. It checks that the returned merchant is not null
     * and has the expected properties.
     * 
     * @throws ResourceAlreadyExists if a merchant with the same email or phone already exists.
     * @throws ResourceUnableToCreate if an unexpected error occurs during merchant creation.
     */
    @Test
    void testCreateMerchant_Success() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Arrange: Create a Merchant object with unique email and phone
        Merchant merchant = new Merchant();
        merchant.setMerchantEmail("test@example.com");
        merchant.setMerchantPhone("1234567890");

        // Mock repository behavior: no existing merchant with the same email or phone
        when(merchantRepository.findByMerchantEmailOrMerchantPhone("test@example.com", "1234567890")).thenReturn(null);
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        // Act: Call createMerchant
        Merchant createdMerchant = merchantService.createMerchant(merchant);

        // Assert: Verify the result
        assertNotNull(createdMerchant);
        assertEquals("test@example.com", createdMerchant.getMerchantEmail());
        verify(merchantRepository, times(1)).save(merchant);
    }

    /**
     * Tests the ResourceAlreadyExists exception when attempting to create a merchant
     * that already exists.
     * 
     * This test verifies that if a merchant with the same email or phone already exists,
     * a ResourceAlreadyExists exception is thrown.
     */
    @Test
    void testCreateMerchant_ResourceAlreadyExists() {
        // Arrange: Create a Merchant object with duplicate email and phone
        Merchant merchant = new Merchant();
        merchant.setMerchantEmail("test@example.com");
        merchant.setMerchantPhone("1234567890");

        // Mock repository behavior: existing merchant found
        when(merchantRepository.findByMerchantEmailOrMerchantPhone("test@example.com", "1234567890"))
                .thenReturn(merchant);

        // Act & Assert: Verify that ResourceAlreadyExists exception is thrown
        assertThrows(ResourceAlreadyExists.class, () -> merchantService.createMerchant(merchant));
    }

    /**
     * Tests the ResourceUnableToCreate exception due to an unexpected error
     * during the merchant creation process.
     * 
     * This test verifies that if an exception occurs when saving the merchant,
     * a ResourceUnableToCreate exception is thrown.
     */
    @Test
    void testCreateMerchant_ResourceUnableToCreate() {
        // Arrange: Create a Merchant object with unique email and phone
        Merchant merchant = new Merchant();
        merchant.setMerchantEmail("test@example.com");
        merchant.setMerchantPhone("1234567890");

        // Mock repository behavior: throw exception during save
        when(merchantRepository.findByMerchantEmailOrMerchantPhone("test@example.com", "1234567890")).thenReturn(null);
        when(merchantRepository.save(any(Merchant.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert: Verify that ResourceUnableToCreate exception is thrown
        assertThrows(ResourceUnableToCreate.class, () -> merchantService.createMerchant(merchant));
    }

    /**
     * Tests the successful retrieval of a merchant by email or phone.
     * 
     * This test verifies that when a merchant is found using the provided email
     * or phone number, the correct merchant object is returned.
     * 
     * @throws ResourceNotFoundException if the merchant is not found.
     */
    @Test
    void testGetMerchantByEmailOrPhone_Success() throws ResourceNotFoundException {
        // Arrange: Create a Merchant object
        Merchant merchant = new Merchant();
        merchant.setMerchantEmail("test@example.com");
        merchant.setMerchantPhone("1234567890");

        // Mock repository behavior: return the merchant when searching by email or phone
        when(merchantRepository.findByMerchantEmailOrMerchantPhone("test@example.com", "1234567890"))
                .thenReturn(merchant);

        // Act: Call getMerchantByEmailOrPhone
        Merchant foundMerchant = merchantService.getMerchantByEmailOrPhone("test@example.com", "1234567890");

        // Assert: Verify the result
        assertNotNull(foundMerchant);
        assertEquals("test@example.com", foundMerchant.getMerchantEmail());
        verify(merchantRepository, times(1)).findByMerchantEmailOrMerchantPhone("test@example.com", "1234567890");
    }

    /**
     * Tests the ResourceNotFoundException when the merchant does not exist.
     * 
     * This test verifies that if no merchant is found using the provided email or phone
     * number, a ResourceNotFoundException is thrown.
     */
    @Test
    void testGetMerchantByEmailOrPhone_ResourceNotFoundException() {
        // Arrange: Mock repository behavior: no merchant found
        when(merchantRepository.findByMerchantEmailOrMerchantPhone("nonexistent@example.com", "0987654321"))
                .thenReturn(null);

        // Act & Assert: Verify that ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> 
            merchantService.getMerchantByEmailOrPhone("nonexistent@example.com", "0987654321"));
    }

    /**
     * Tests the retrieval of all merchants.
     * 
     * This test verifies that when there are multiple merchants in the repository,
     * the getAllMerchants method returns a list of all merchants correctly.
     */
    @Test
    void testGetAllMerchants_ReturnsListOfMerchants() {
        // Arrange: Create multiple Merchant objects
        Merchant merchant1 = new Merchant();
        merchant1.setMerchantEmail("test1@example.com");
        
        Merchant merchant2 = new Merchant();
        merchant2.setMerchantEmail("test2@example.com");

        // Mock repository behavior: return a list of merchants
        when(merchantRepository.findAll()).thenReturn(List.of(merchant1, merchant2));

        // Act: Call getAllMerchants
        List<Merchant> merchants = merchantService.getAllMerchants();

        // Assert: Verify the result
        assertNotNull(merchants);
        assertEquals(2, merchants.size());
        verify(merchantRepository, times(1)).findAll();
    }

    /**
     * Tests retrieval when there are no merchants.
     * 
     * This test verifies that when there are no merchants in the repository,
     * the getAllMerchants method returns an empty list.
     */
    @Test
    void testGetAllMerchants_EmptyList() {
        // Arrange: Mock repository behavior: return an empty list
        when(merchantRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: Call getAllMerchants
        List<Merchant> merchants = merchantService.getAllMerchants();

        // Assert: Verify the result is an empty list
        assertNotNull(merchants);
        assertTrue(merchants.isEmpty());
        verify(merchantRepository, times(1)).findAll();
    }

    /**
     * Tests the existence check for an existing merchant ID.
     * 
     * This test verifies that when checking for a merchant ID that exists,
     * the existsById method returns true.
     */
    @Test
    void testExistsById_Exists() {
        // Arrange: Mock repository behavior: merchant exists
        Long merchantId = 1L;
        when(merchantRepository.existsById(merchantId)).thenReturn(true);

        // Act: Call existsById
        boolean exists = merchantService.existsById(merchantId);

        // Assert: Verify the result
        assertTrue(exists);
        verify(merchantRepository, times(1)).existsById(merchantId);
    }

    /**
     * Tests the existence check for a non-existing merchant ID.
     * 
     * This test verifies that when checking for a merchant ID that does not exist,
     * the existsById method returns false.
     */
    @Test
    void testExistsById_DoesNotExist() {
        // Arrange: Mock repository behavior: merchant does not exist
        Long merchantId = 1L;
        when(merchantRepository.existsById(merchantId)).thenReturn(false);

        // Act: Call existsById
        boolean exists = merchantService.existsById(merchantId);

        // Assert: Verify the result
        assertFalse(exists);
        verify(merchantRepository, times(1)).existsById(merchantId);
    }

    /**
     * Tests the retrieval of a merchant by email or phone when both are null.
     * 
     * This test verifies that if both email and phone are null,
     * a ResourceNotFoundException is thrown.
     */
    @Test
    void testGetMerchantByEmailOrPhone_ThrowsForNullEmailAndPhone() {
        // Arrange: Null values for both email and phone
        String email = null;
        String phone = null;

        // Act & Assert: Verify that ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> 
            merchantService.getMerchantByEmailOrPhone(email, phone));
    }

    /**
     * Tests the creation of a merchant with a null Merchant object.
     * 
     * This test verifies that if a null Merchant object is passed to the createMerchant method,
     * a ResourceUnableToCreate exception is thrown.
     */
    @Test
    void testCreateMerchant_NullMerchant_ThrowsResourceUnableToCreate() {
        // Arrange: Null Merchant object
        Merchant merchant = null;

        // Act & Assert: Verify that ResourceUnableToCreate exception is thrown
        assertThrows(ResourceUnableToCreate.class, () -> merchantService.createMerchant(merchant));
    }

    /**
     * Tests the retrieval of all merchants when none are found.
     * 
     * This test verifies that if the repository returns an empty list,
     * the getAllMerchants method correctly returns an empty list.
     */
    @Test
    void testGetAllMerchants_NoMerchantsFound() {
        // Arrange: Set repository to return empty list
        when(merchantRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: Call getAllMerchants
        List<Merchant> merchants = merchantService.getAllMerchants();

        // Assert: Verify the result
        assertNotNull(merchants);
        assertTrue(merchants.isEmpty());
        verify(merchantRepository, times(1)).findAll();
    }
}
