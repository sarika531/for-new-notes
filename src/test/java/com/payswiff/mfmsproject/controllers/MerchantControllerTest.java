/**
 * MerchantControllerTest performs unit tests on the MerchantController class, 
 * covering both positive and negative test cases for all endpoints.
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
import com.payswiff.mfmsproject.models.Merchant;
import com.payswiff.mfmsproject.reuquests.CreateMerchantRequest;
import com.payswiff.mfmsproject.services.MerchantService;

class MerchantControllerTest {

    @Mock
    private MerchantService merchantService; // Mocking MerchantService

    @InjectMocks
    private MerchantController merchantController; // Controller under test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    /**
     * Positive test case for createMerchant: verifies successful creation of a new Merchant.
     */
    @Test
    void testCreateMerchant_Success() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Arrange
        CreateMerchantRequest request = new CreateMerchantRequest("John Doe", "john@example.com", "1234567890","merchnatshop","retail");
        Merchant createdMerchant = new Merchant(1L, UUID.randomUUID().toString(), "John Doe", "john@example.com", "1234567890","merchnatshop","retail", null, null);
        
        // Mock behavior for createMerchant
        when(merchantService.createMerchant(any(Merchant.class))).thenReturn(createdMerchant);

        // Act
        ResponseEntity<Merchant> response = merchantController.createMerchant(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status should be 201 Created");
        assertEquals(createdMerchant, response.getBody(), "Response body should match created merchant");
    }

    /**
     * Negative test case for createMerchant: verifies behavior when a Merchant already exists.
     */
    @Test
    void testCreateMerchant_ResourceAlreadyExists() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Arrange
        CreateMerchantRequest request = new CreateMerchantRequest("John Doe", "john@example.com", "1234567890","merchnatshop","retail");

        // Mock behavior for duplicate merchant scenario
        when(merchantService.createMerchant(any(Merchant.class))).thenThrow(new ResourceAlreadyExists("Merchant exists","",""));

        // Act & Assert
        ResourceAlreadyExists exception = assertThrows(ResourceAlreadyExists.class, () -> {
            merchantController.createMerchant(request);
        });
        
        assertEquals("Merchant exists with :  already exists.", exception.getMessage(), "Exception message should match");
    }

    /**
     * Positive test case for getMerchantByEmailOrPhone: verifies successful retrieval by email.
     */
    @Test
    void testGetMerchantByEmailOrPhone_ByEmail() throws ResourceNotFoundException {
        // Arrange
        String email = "john@example.com";
        Merchant merchant = new Merchant(1L, UUID.randomUUID().toString(), "John Doe", "john@example.com", "1234567890","merchnatshop","retail", null, null);

        // Mock behavior for getMerchantByEmailOrPhone
        when(merchantService.getMerchantByEmailOrPhone(email, null)).thenReturn(merchant);

        // Act
        ResponseEntity<Merchant> response = merchantController.getMerchantByEmailOrPhone(email, null);

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode(), "Status should be 302 Found");
        assertEquals(merchant, response.getBody(), "Response body should match found merchant");
    }

    /**
     * Negative test case for getMerchantByEmailOrPhone: verifies behavior when Merchant is not found.
     */
    @Test
    void testGetMerchantByEmailOrPhone_NotFound() throws ResourceNotFoundException {
        // Arrange
        String email = "nonexistent@example.com";

        // Mock behavior for not found scenario
        when(merchantService.getMerchantByEmailOrPhone(email, null)).thenThrow(new ResourceNotFoundException("Merchant not found","",""));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantController.getMerchantByEmailOrPhone(email, null);
        });
        
        assertEquals("Merchant not found with :  is not found!!", exception.getMessage(), "Exception message should match");
    }

    /**
     * Positive test case for getAllMerchants: verifies retrieval of all Merchants.
     */
    @Test
    void testGetAllMerchants_Success() {
        // Arrange
        Merchant merchant1 = new Merchant(1L, UUID.randomUUID().toString(), "John Doe", "john@example.com", "1234567890","merchnatshop","retail", null, null);
        Merchant merchant2 = new Merchant(2L,UUID.randomUUID().toString(), "Jane Smith", "jane@example.com", "0987654321","merchnatshop","retail", null, null);
        List<Merchant> merchants = Arrays.asList(merchant1, merchant2);

        // Mock behavior for getAllMerchants
        when(merchantService.getAllMerchants()).thenReturn(merchants);

        // Act
        ResponseEntity<List<Merchant>> response = merchantController.getAllMerchants();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK");
        assertEquals(merchants, response.getBody(), "Response body should match list of merchants");
    }

    /**
     * Negative test case for getAllMerchants: verifies empty list response when no merchants exist.
     */
    @Test
    void testGetAllMerchants_Empty() {
        // Arrange
        List<Merchant> merchants = Arrays.asList(); // Empty list

        // Mock behavior for empty list scenario
        when(merchantService.getAllMerchants()).thenReturn(merchants);

        // Act
        ResponseEntity<List<Merchant>> response = merchantController.getAllMerchants();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK");
        assertTrue(response.getBody().isEmpty(), "Response body should be an empty list");
    }
}
