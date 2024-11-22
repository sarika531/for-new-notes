package com.payswiff.mfmsproject.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.models.EmployeeType;
import com.payswiff.mfmsproject.models.Feedback;
import com.payswiff.mfmsproject.models.Merchant;

/**
 * Test class for {@link FeedbackRepository}.
 * This class tests all methods provided in the FeedbackRepository interface
 * using an in-memory database for testing purposes.
 */
@DataJpaTest
class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private MerchantRepository merchantRepository;

    private Employee testEmployee;
    private Device testDevice;
    private Feedback testFeedback;
    private Merchant testMerchant;

    /**
     * Sets up the test environment by creating and saving test instances of
     * Employee, Device, and Feedback. This method is called before each test
     * is executed to ensure a clean state.
     */
    @BeforeEach
    void setUp() {
        // Create and save a test Employee with all necessary fields
        testEmployee = new Employee();
        testEmployee.setEmployeeId(1L); // Setting ID for testing
        testEmployee.setEmployeeName("John Doe");
        testEmployee.setEmployeeEmail("john.doe@example.com");
        testEmployee.setEmployeePhoneNumber("123-456-7890");
        testEmployee.setEmployeeType(EmployeeType.employee);
        testEmployee.setEmployeeUuid(UUID.randomUUID().toString());
        testEmployee.setEmployeePayswiffId("12345");
        testEmployee.setEmployeeDesignation("dev");
        testEmployee.setEmployeePassword("1234pgduytuw222@");
        testEmployee = employeeRepository.save(testEmployee);
        
        // Create and save a test Device
        testDevice = new Device();
        testDevice.setDeviceUuid("device-uuid-123");
        testDevice.setDeviceModel("Model X");
        testDevice.setDeviceManufacturer("Manufacturer Y");
        testDevice = deviceRepository.save(testDevice);
        //create new merchnat
        testMerchant = new Merchant();
        testMerchant.setMerchantBusinessName("gopi shopi");
        testMerchant.setMerchantBusinessType("retail");
        testMerchant.setMerchantEmail("merchnat123@gmail.com");
        testMerchant.setmerchantName("gopi");
        testMerchant.setMerchantUuid(UUID.randomUUID().toString());
        testMerchant.setMerchantPhone("1234567890");
        testMerchant =merchantRepository.save(testMerchant);
        // Create and save a test Feedback
        testFeedback = new Feedback();
        testFeedback.setFeedbackId(1);
        testFeedback.setFeedback("Great service!");
        testFeedback.setFeedbackEmployee(testEmployee);
        testFeedback.setFeedbackDevice(testDevice);
        testFeedback.setFeedbackMerchant(testMerchant);
        testFeedback.setFeedbackImage1("url");
        testFeedback.setFeedbackRating(2.5);
        testFeedback.setFeedbackUuid(UUID.randomUUID().toString());
        feedbackRepository.save(testFeedback);
    }

    /**
     * Cleans up the test environment by deleting all records after each test.
     */
    @AfterEach
    void tearDown() {
        feedbackRepository.deleteAll();
        deviceRepository.deleteAll();
        employeeRepository.deleteAll();
        if (!feedbackRepository.findAll().isEmpty()) {
            feedbackRepository.deleteAll();
        }
    }

    /**
     * Test case to save a Feedback entity.
     * Verifies that the saved feedback can be retrieved.
     */
    @Test
    void testSaveFeedback() {
        Feedback feedback = new Feedback();
        feedback.setFeedback("Excellent product!");
        feedback.setFeedbackEmployee(testEmployee);
        feedback.setFeedbackDevice(testDevice);
        feedback.setFeedbackMerchant(testMerchant);
        feedback.setFeedbackImage1("url");
       feedback.setFeedbackRating(2.5);
        feedback.setFeedbackUuid(UUID.randomUUID().toString());
        Feedback savedFeedback = feedbackRepository.save(feedback);
        assertNotNull(savedFeedback.getFeedbackId(), "Feedback ID should not be null after saving");
    }

    /**
     * Test case to find Feedback by ID.
     * Verifies that the feedback is correctly retrieved based on the given ID.
     */
    @Test
    void testFindFeedbackById() {
        Feedback feedback = new Feedback();
        feedback.setFeedback("Good service");
        feedback.setFeedbackEmployee(testEmployee);
        feedback.setFeedbackDevice(testDevice);
        feedback.setFeedbackMerchant(testMerchant);
        feedback.setFeedbackUuid(UUID.randomUUID().toString());
        feedback.setFeedbackImage1("url");
        feedback.setFeedbackRating(2.5);
        // Save the feedback and capture the ID
         feedbackRepository.save(feedback);
        

        // Retrieve by ID and assert it is found
        Optional<Feedback> retrievedFeedback = feedbackRepository.findById(feedback.getFeedbackId());
        assertTrue(retrievedFeedback.isPresent(), "Feedback should be found by ID");
    }

    /**
     * Test case to find all Feedback entries.
     * Verifies that all saved feedback entries can be retrieved.
     */
    @Test
    void testFindAllFeedback() {
        Feedback feedback1 = new Feedback();
        feedback1.setFeedback("Excellent product!");
        feedback1.setFeedbackEmployee(testEmployee);
        feedback1.setFeedbackDevice(testDevice);
        feedback1.setFeedbackMerchant(testMerchant);
       feedback1.setFeedbackImage1("url");
       feedback1.setFeedbackRating(2.5);
       feedback1.setFeedbackUuid(UUID.randomUUID().toString());
        feedbackRepository.save(feedback1);

        Feedback feedback2 = new Feedback();
        feedback2.setFeedback("Needs improvement.");
        feedback2.setFeedbackEmployee(testEmployee);
        feedback2.setFeedbackDevice(testDevice);
        feedback2.setFeedbackMerchant(testMerchant);
        feedback2.setFeedbackImage1("url");
        feedback2.setFeedbackRating(2.5);
        feedback2.setFeedbackUuid(UUID.randomUUID().toString());
        feedbackRepository.save(feedback2);

        assertEquals(3, feedbackRepository.findAll().size(), "Should return all saved feedback entries");
    }

   

    /**
     * Test case to delete a Feedback entry by ID.
     * Verifies that the feedback is removed from the repository after deletion.
     */
    @Test
    void testDeleteFeedbackById() {
        feedbackRepository.deleteById(testFeedback.getFeedbackId());
        Optional<Feedback> foundFeedback = feedbackRepository.findById(testFeedback.getFeedbackId());
        assertFalse(foundFeedback.isPresent(), "Feedback should be deleted and not found");
    }


    /**
     * Test case to verify that two feedback entries with the same comment 
     * for the same employee and device can be saved separately.
     */
    @Test
    void testDuplicateFeedbackAllowed() {
        Feedback feedback1 = new Feedback();
        feedback1.setFeedback("Feedback Comment");
        feedback1.setFeedbackEmployee(testEmployee);
        feedback1.setFeedbackDevice(testDevice);
        feedback1.setFeedbackMerchant(testMerchant);
        feedback1.setFeedbackImage1("url");
        feedback1.setFeedbackRating(2.5);
        feedback1.setFeedbackUuid(UUID.randomUUID().toString());
        feedbackRepository.save(feedback1);

        Feedback feedback2 = new Feedback();
        feedback2.setFeedback("Feedback Comment"); // Same comment
        feedback2.setFeedbackEmployee(testEmployee);
        feedback2.setFeedbackDevice(testDevice);
        feedback2.setFeedbackMerchant(testMerchant);
        feedback2.setFeedbackImage1("url");
       feedback2.setFeedbackRating(2.5);
        feedback2.setFeedbackUuid(UUID.randomUUID().toString());
        Feedback savedFeedback = feedbackRepository.save(feedback2);

        assertNotNull(savedFeedback.getFeedbackId(), "Should allow saving duplicate comments for the same employee and device");
    }

    /**
     * Test case to find a Feedback by a non-existing ID.
     * Verifies that the result is empty when searching for a non-existent feedback ID.
     */
    @Test
    void testFindNonExistentFeedbackById() {
        Optional<Feedback> foundFeedback = feedbackRepository.findById(999); // ID 999 does not exist
        assertFalse(foundFeedback.isPresent(), "Should not find feedback with a non-existent ID");
    }

    /**
     * Test case to check if feedback can be saved with different devices.
     * Verifies that feedback can be related to different devices.
     */
    @Test
    void testFeedbackForDifferentDevices() {
        Device anotherDevice = new Device();
        anotherDevice.setDeviceUuid(UUID.randomUUID().toString());
        anotherDevice.setDeviceModel("Model Y");
        anotherDevice.setDeviceManufacturer("Manufacturer Z");
        deviceRepository.save(anotherDevice);

        Feedback feedbackForAnotherDevice = new Feedback();
        feedbackForAnotherDevice.setFeedback("Great device performance!");
        feedbackForAnotherDevice.setFeedbackEmployee(testEmployee);
        feedbackForAnotherDevice.setFeedbackDevice(anotherDevice);
        feedbackForAnotherDevice.setFeedbackMerchant(testMerchant);
        feedbackForAnotherDevice.setFeedbackImage1("url");
       feedbackForAnotherDevice.setFeedbackRating(2.5);
        feedbackForAnotherDevice.setFeedbackUuid(UUID.randomUUID().toString());
        Feedback savedFeedback = feedbackRepository.save(feedbackForAnotherDevice);

        assertNotNull(savedFeedback.getFeedbackId(), "Should save feedback related to different devices");
    }
}
