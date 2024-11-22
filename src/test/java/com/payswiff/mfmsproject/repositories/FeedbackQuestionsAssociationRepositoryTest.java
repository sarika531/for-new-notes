package com.payswiff.mfmsproject.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.models.EmployeeType;
import com.payswiff.mfmsproject.models.Feedback;
import com.payswiff.mfmsproject.models.FeedbackQuestionsAssociation;
import com.payswiff.mfmsproject.models.Merchant;
import com.payswiff.mfmsproject.models.Question;
import com.payswiff.mfmsproject.models.Role;

import lombok.Setter;

/**
 * Test class for the FeedbackQuestionsAssociationRepository.
 * This class tests the methods in FeedbackQuestionsAssociationRepository
 * to ensure correct functionality and behavior.
 */
@DataJpaTest // Configures an in-memory database for testing JPA repositories
@Transactional // Each test runs in a transaction and rolls back after completion
class FeedbackQuestionsAssociationRepositoryTest {

    @Autowired
    private FeedbackQuestionsAssociationRepository repository; // Inject the repository
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    QuestionRepository questionRepository;
    
    @Autowired
    DeviceRepository deviceRepository;
    
    @Autowired
    EmployeeRepository employeeRepository;
    
    @Autowired
    MerchantRepository merchantRepository;
    
    
    
    private Feedback feedback; // Feedback instance for testing
    private Question question; // Question instance for testing
    private FeedbackQuestionsAssociation association; // Association instance for testing

    /**
     * Set up a test environment before each test.
     */
    @BeforeEach
    void setUp() {
        // Create and save a Device instance
        Device posDevice = new Device();
        posDevice.setDeviceUuid(UUID.randomUUID().toString());
        posDevice.setDeviceModel("POS");
        posDevice.setDeviceManufacturer("NEWLAND");
        posDevice = deviceRepository.save(posDevice); // Save the device before using it in feedback

        // Create and save a Merchant instance
        Merchant merchant = new Merchant();
        merchant.setmerchantName("Mfms");
        merchant.setMerchantUuid(UUID.randomUUID().toString());
        merchant.setMerchantEmail("merchant@example.com");
        merchant.setMerchantPhone("1234567890");
        merchant.setMerchantBusinessName("merchantBusinessName");
        merchant.setMerchantBusinessType("Grocery");
        merchant = merchantRepository.save(merchant); // Save the merchant before using it in feedback

        // Create and save a Role instance
        Role role = new Role();
        role.setName("ROLE_admin");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // Create and save an Employee instance
        Employee employee = new Employee();
        employee.setEmployeeDesignation("dev");
        employee.setEmployeeEmail("bapanapalligopi1@gmail.com");
        employee.setEmployeeName("gopi");
        employee.setEmployeePassword("gopi1234@");
        employee.setEmployeePayswiffId("12345");
        employee.setEmployeeType(EmployeeType.admin);
        employee.setEmployeeUuid(UUID.randomUUID().toString());
        employee.setRoles(roles);
        employee.setEmployeePhoneNumber("9191956789");
        employee = employeeRepository.save(employee); // Save the employee before using it in feedback

        // Now create and save a Feedback instance
        feedback = new Feedback();
        feedback.setFeedback("Sample feedback");
        feedback.setFeedbackUuid(UUID.randomUUID().toString());
        feedback.setFeedbackImage1("https://www.google.com");
        feedback.setFeedbackRating(5.0);
        feedback.setFeedbackDevice(posDevice); // Use the saved device
        feedback.setFeedbackMerchant(merchant); // Use the saved merchant
        feedback.setFeedbackEmployee(employee); // Use the saved employee
        feedbackRepository.save(feedback); // Save the feedback after setting all related entities

        // Create and save a Question instance
        question = new Question();
        question.setQuestionDescription("Sample question");
        question.setQuestionUuid(UUID.randomUUID().toString());
        question = questionRepository.save(question); // Save the question

        // Create and save a FeedbackQuestionsAssociation instance
        association = new FeedbackQuestionsAssociation();
        association.setFeedback(feedback);
        association.setQuestion(question);
        association.setAnswer("nice");
        repository.save(association); // Save the association
    }


    /**
     * Clean up after each test.
     */
    @AfterEach
    void tearDown() {
        // Clear the repository after each test to maintain isolation
        repository.deleteAll(); // Clear associations
        feedbackRepository.deleteAll(); // Clear feedbacks
        questionRepository.deleteAll(); // Clear questions
    }

    /**
     * Positive test case: Test existence of an association with valid feedback and question.
     */
    @Test
    void testExistsByFeedbackAndQuestion_Valid() {
        // Act: Check if the association exists
        boolean exists = repository.existsByFeedbackAndQuestion(feedback, question);

        // Assert: Verify that the association exists
        assertTrue(exists, "Association should exist for the provided feedback and question.");
    }

    /**
     * Negative test case: Test non-existence of an association with invalid feedback.
     */
    @Test
    void testExistsByFeedbackAndQuestion_InvalidFeedback() {
        // Create a new Feedback that doesn't exist in the repository
        Feedback invalidFeedback = new Feedback();
        invalidFeedback.setFeedbackId(Integer.MAX_VALUE);
        invalidFeedback.setFeedback("Invalid feedback");
        
        invalidFeedback.setFeedbackDevice(feedback.getFeedbackDevice());
        invalidFeedback.setFeedbackEmployee(feedback.getFeedbackEmployee());
        invalidFeedback.setFeedbackMerchant(feedback.getFeedbackMerchant());
        invalidFeedback.setFeedbackImage1("https://www.google.com");
        invalidFeedback.setFeedbackRating(4.6);
        invalidFeedback.setFeedbackUuid(UUID.randomUUID().toString());
        
        // Act: Check if the association exists with invalid feedback
        boolean exists = repository.existsByFeedbackAndQuestion(invalidFeedback, question);

        // Assert: Verify that the association does not exist
        assertFalse(exists, "Association should not exist for an invalid feedback.");
    }

    /**
     * Negative test case: Test non-existence of an association with invalid question.
     */
    @Test
    void testExistsByFeedbackAndQuestion_InvalidQuestion() {
        // Create a Question object with a non-existent ID
        Question invalidQuestion = new Question();
        invalidQuestion.setQuestionId(Long.MAX_VALUE);  // Use a high ID that doesn’t exist in the database
        invalidQuestion.setQuestionDescription("Invalid question");
        invalidQuestion.setQuestionUuid(UUID.randomUUID().toString());
        
        // Act: Check if the association exists with invalid question
        boolean exists = repository.existsByFeedbackAndQuestion(feedback, invalidQuestion);

        // Assert: Verify that the association does not exist
        assertFalse(exists, "Association should not exist for an invalid question.");
    }


    /**
     * Negative test case: Test non-existence of an association with both invalid feedback and question.
     */
    @Test
    void testExistsByFeedbackAndQuestion_InvalidFeedbackAndQuestion() {
        // Create new instances of Feedback and Question that don't exist
        Feedback invalidFeedback = new Feedback();
        invalidFeedback.setFeedbackId(Integer.MAX_VALUE);
        invalidFeedback.setFeedback("Invalid feedback");
        
        Question invalidQuestion = new Question();
        invalidQuestion.setQuestionId(Long.MAX_VALUE);
        invalidQuestion.setQuestionDescription("Invalid question");
        invalidQuestion.setQuestionUuid(UUID.randomUUID().toString());
        // Act: Check if the association exists with invalid feedback and question
        boolean exists = repository.existsByFeedbackAndQuestion(invalidFeedback, invalidQuestion);

        // Assert: Verify that the association does not exist
        assertFalse(exists, "Association should not exist for both invalid feedback and question.");
    }

    /**
     * Positive test case: Test finding associations by valid feedback.
     */
    @Test
    void testFindByFeedback_Valid() {
        // Act: Find associations by valid feedback
        List<FeedbackQuestionsAssociation> associations = repository.findByFeedback(feedback);

        // Assert: Verify that the association is returned
        assertNotNull(associations, "The list of associations should not be null.");
        assertEquals(1, associations.size(), "There should be one association for the provided feedback.");
        assertEquals(association, associations.get(0), "The association returned should match the saved association.");
    }

    /**
     * Negative test case: Test finding associations by feedback that does not exist.
     */
    @Test
    void testFindByFeedback_NonExistentFeedback() {
        // Create a Feedback object with a non-existent ID
        Feedback nonExistentFeedback = new Feedback();
        nonExistentFeedback.setFeedbackId(Integer.MAX_VALUE);  // Assuming this ID doesn’t exist in the database
        nonExistentFeedback.setFeedback("Non-existent feedback");
        nonExistentFeedback.setFeedbackDevice(feedback.getFeedbackDevice());
        nonExistentFeedback.setFeedbackEmployee(feedback.getFeedbackEmployee());
        nonExistentFeedback.setFeedbackMerchant(feedback.getFeedbackMerchant());
        nonExistentFeedback.setFeedbackImage1("https://www.google.com");
        nonExistentFeedback.setFeedbackRating(4.6);

        // Act: Find associations by non-existent feedback
        List<FeedbackQuestionsAssociation> associations = repository.findByFeedback(nonExistentFeedback);

        // Assert: Verify that no associations are returned
        assertNotNull(associations, "The list of associations should not be null.");
        assertTrue(associations.isEmpty(), "The list of associations should be empty for non-existent feedback.");
    }

}
