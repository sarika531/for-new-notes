package com.payswiff.mfmsproject.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.payswiff.mfmsproject.models.Merchant;

/**
 * Test class for the MerchantRepository.
 * This class tests the methods in MerchantRepository
 * to ensure correct functionality and behavior.
 */
@DataJpaTest // Use DataJpaTest for repository testing with an in-memory database
@ActiveProfiles("test") // Activate the 'test' profile for testing
@Transactional // Ensure that tests are run in a transaction
class MerchantRepositoryTest {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private TestEntityManager entityManager;

    /**
     * Set up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        // Any setup required before each test can be done here.
    }

    /**
     * Clean up after each test.
     */
    @AfterEach
    void tearDown() {
        // Clear the repository after each test to maintain isolation
        merchantRepository.deleteAll();
    }

    /**
     * Positive test case: Test finding a merchant by email.
     */
    @Test
    void testFindByMerchantEmail_Success() {
        // Arrange: Create and persist a Merchant entity
        Merchant merchant = new Merchant();
        merchant.setmerchantName("Mfms");
        merchant.setMerchantUuid(UUID.randomUUID().toString());
        merchant.setMerchantEmail("merchant@example.com");
        merchant.setMerchantPhone("1234567890");
        merchant.setMerchantBusinessName("merchantBusinessName");
        merchant.setMerchantBusinessType("Grocery");
        entityManager.persist(merchant);
        entityManager.flush();

        // Act: Find the merchant by email
        Merchant foundMerchant = merchantRepository.findByMerchantEmailOrMerchantPhone("merchant@example.com", null);

        // Assert: Verify that the found merchant is not null and matches the expected values
        assertNotNull(foundMerchant, "Merchant should be found by email");
        assertEquals(merchant.getMerchantEmail(), foundMerchant.getMerchantEmail());
        assertEquals(merchant.getMerchantPhone(), foundMerchant.getMerchantPhone());
    }

    /**
     * Positive test case: Test finding a merchant by phone number.
     */
    @Test
    void testFindByMerchantPhone_Success() {
        // Arrange: Create and persist a Merchant entity
        Merchant merchant = new Merchant();
        merchant.setmerchantName("Mfms");
        merchant.setMerchantUuid(UUID.randomUUID().toString());
        merchant.setMerchantEmail("merchant@example.com");
        merchant.setMerchantPhone("1234567890");
        merchant.setMerchantBusinessName("merchantBusinessName");
        merchant.setMerchantBusinessType("Grocery");
        entityManager.persist(merchant);
        entityManager.flush();

        // Act: Find the merchant by phone
        Merchant foundMerchant = merchantRepository.findByMerchantEmailOrMerchantPhone(null, "1234567890");

        // Assert: Verify that the found merchant is not null and matches the expected values
        assertNotNull(foundMerchant, "Merchant should be found by phone number");
        assertEquals(merchant.getMerchantEmail(), foundMerchant.getMerchantEmail());
        assertEquals(merchant.getMerchantPhone(), foundMerchant.getMerchantPhone());
    }

    /**
     * Positive test case: Test finding a merchant by both email and phone.
     */
    @Test
    void testFindByMerchantEmailAndPhone_Success() {
        // Arrange: Create and persist a Merchant entity
        Merchant merchant = new Merchant();
        merchant.setmerchantName("Mfms");
        merchant.setMerchantUuid(UUID.randomUUID().toString());
        merchant.setMerchantEmail("merchant@example.com");
        merchant.setMerchantPhone("1234567890");
        merchant.setMerchantBusinessName("merchantBusinessName");
        merchant.setMerchantBusinessType("Grocery");
        entityManager.persist(merchant);
        entityManager.flush();

        // Act: Find the merchant by both email and phone
        Merchant foundMerchant = merchantRepository.findByMerchantEmailOrMerchantPhone("merchant@example.com", "1234567890");

        // Assert: Verify that the found merchant is not null and matches the expected values
        assertNotNull(foundMerchant, "Merchant should be found by both email and phone number");
        assertEquals(merchant.getMerchantEmail(), foundMerchant.getMerchantEmail());
        assertEquals(merchant.getMerchantPhone(), foundMerchant.getMerchantPhone());
    }

    /**
     * Negative test case: Test finding a merchant with non-existing email and phone.
     */
    @Test
    void testFindByMerchantEmailOrMerchantPhone_NotFound() {
        // Act: Attempt to find a merchant that does not exist
        Merchant foundMerchant = merchantRepository.findByMerchantEmailOrMerchantPhone("nonexistent@example.com", "0987654321");

        // Assert: Verify that no merchant is found (result should be null)
        assertNull(foundMerchant, "Merchant should not be found with non-existing email and phone number");
    }

    /**
     * Negative test case: Test finding a merchant with only an empty email.
     */
    @Test
    void testFindByMerchantEmail_EmptyEmail() {
        // Arrange: Create and persist a Merchant entity
        Merchant merchant = new Merchant();
        merchant.setmerchantName("Mfms");
        merchant.setMerchantUuid(UUID.randomUUID().toString());
        merchant.setMerchantEmail("merchant@example.com");
        merchant.setMerchantPhone("1234567890");
        merchant.setMerchantBusinessName("merchantBusinessName");
        merchant.setMerchantBusinessType("Grocery");
        entityManager.persist(merchant);
        entityManager.flush();

        // Act: Attempt to find a merchant with an empty email
        Merchant foundMerchant = merchantRepository.findByMerchantEmailOrMerchantPhone("", "1234567890");

        // Assert: Verify that the found merchant matches the expected phone number
        assertNotNull(foundMerchant, "Merchant should be found with empty email");
        assertEquals(merchant.getMerchantPhone(), foundMerchant.getMerchantPhone());
    }

    /**
     * Negative test case: Test finding a merchant with only an empty phone number.
     */
    @Test
    void testFindByMerchantPhone_EmptyPhone() {
        // Arrange: Create and persist a Merchant entity
        Merchant merchant = new Merchant();
        merchant.setMerchantUuid(UUID.randomUUID().toString());
        merchant.setmerchantName("Mfms");
        merchant.setMerchantEmail("merchant@example.com");
        merchant.setMerchantPhone("1234567890");
        merchant.setMerchantBusinessName("merchantBusinessName");
        merchant.setMerchantBusinessType("Grocery");
        entityManager.persist(merchant);
        entityManager.flush();

        // Act: Attempt to find a merchant with an empty phone
        Merchant foundMerchant = merchantRepository.findByMerchantEmailOrMerchantPhone("merchant@example.com", "");

        // Assert: Verify that the found merchant matches the expected email
        assertNotNull(foundMerchant, "Merchant should be found with empty phone number");
        assertEquals(merchant.getMerchantEmail(), foundMerchant.getMerchantEmail());
    }

    /**
     * Negative test case: Test finding a merchant with both email and phone empty.
     */
    @Test
    void testFindByMerchantEmailAndPhone_Empty() {
        // Act: Attempt to find a merchant with both fields empty
        Merchant foundMerchant = merchantRepository.findByMerchantEmailOrMerchantPhone("", "");

        // Assert: Verify that no merchant is found (result should be null)
        assertNull(foundMerchant, "Merchant should not be found with both fields empty");
    }
}
