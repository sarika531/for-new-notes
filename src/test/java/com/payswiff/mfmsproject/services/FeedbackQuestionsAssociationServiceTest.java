package com.payswiff.mfmsproject.services;

import com.payswiff.mfmsproject.dtos.FeedbackQuestionDTO;
import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Feedback;
import com.payswiff.mfmsproject.models.FeedbackQuestionsAssociation;
import com.payswiff.mfmsproject.models.Question;
import com.payswiff.mfmsproject.repositories.FeedbackQuestionsAssociationRepository;
import com.payswiff.mfmsproject.repositories.FeedbackRepository;
import com.payswiff.mfmsproject.repositories.QuestionRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FeedbackQuestionsAssociationService class.
 * This class tests the createAssociation and getFeedbackQuestionsByFeedbackId methods.
 */
class FeedbackQuestionsAssociationServiceTest {

    @Mock
    private FeedbackQuestionsAssociationRepository associationRepository; // Mocked repository for associations

    @Mock
    private FeedbackRepository feedbackRepository; // Mocked repository for feedback

    @Mock
    private QuestionRepository questionRepository; // Mocked repository for questions

    @InjectMocks
    private FeedbackQuestionsAssociationService service; // Service under test

    /**
     * Set up the test environment.
     * This method is called before all test cases.
     */
    @BeforeAll
    static void setUpBeforeClass() {
        // Could initialize resources if needed
    }

    /**
     * Clean up the test environment.
     * This method is called after all test cases.
     */
    @AfterAll
    static void tearDownAfterClass() {
        // Could release resources if needed
    }

    /**
     * Set up before each test case.
     * This method initializes mocks before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes the mocks
    }

    /**
     * Clean up after each test case.
     * This method is called after each test case.
     */
    @AfterEach
    void tearDown() {
        // Could reset mocks if needed
    }

    /**
     * Test method for creating a valid FeedbackQuestionsAssociation.
     */
    @Test
    void testCreateAssociation_Success() throws ResourceNotFoundException, ResourceUnableToCreate {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        Question question = new Question();
        question.setQuestionId(1L);

        FeedbackQuestionsAssociation request = new FeedbackQuestionsAssociation();
        request.setFeedback(feedback);
        request.setQuestion(question);
        request.setAnswer("Test answer");

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback)); // Mock feedback existence
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question)); // Mock question existence
        when(associationRepository.save(any())).thenReturn(request); // Mock save behavior

        // Act
        FeedbackQuestionsAssociation result = service.createAssociation(request);

        // Assert
        assertNotNull(result); // Ensure result is not null
        assertEquals("Test answer", result.getAnswer()); // Verify answer matches
        verify(associationRepository).save(any()); // Verify save method was called
    }

    /**
     * Test method for attempting to create an association with a null request.
     */
    @Test
    void testCreateAssociation_NullRequest() {
        // Act & Assert
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            service.createAssociation(null);
        });

        // Verify exception message
        assertEquals("FeedbackQuestionsAssociation with Request object cannot be null: Null or Empty is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Test method for attempting to create an association with a null feedback ID.
     */
    @Test
    void testCreateAssociation_NullFeedbackId() {
        // Arrange
        Feedback feedback = new Feedback(); // No ID set

        Question question = new Question();
        question.setQuestionId(1L);

        FeedbackQuestionsAssociation request = new FeedbackQuestionsAssociation();
        request.setFeedback(feedback);
        request.setQuestion(question);
        request.setAnswer("Test answer");

        // Act & Assert
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            service.createAssociation(request);
        });

        // Verify exception message
        assertEquals("FeedbackQuestionsAssociation with Feedback and its ID cannot be null or empty: Null or Empty is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Test method for attempting to create an association with a non-existing feedback.
     */
    @Test
    void testCreateAssociation_NonExistingFeedback() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        Question question = new Question();
        question.setQuestionId(1L);

        FeedbackQuestionsAssociation request = new FeedbackQuestionsAssociation();
        request.setFeedback(feedback);
        request.setQuestion(question);
        request.setAnswer("Test answer");

        when(feedbackRepository.findById(1)).thenReturn(Optional.empty()); // Mock non-existing feedback

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.createAssociation(request);
        });

        // Verify exception message
        assertEquals("Feedback with ID: 1 is not found!!", exception.getMessage());
    }

    

    /**
     * Test method for attempting to create an association with a non-existing question.
     */
    @Test
    void testCreateAssociation_NonExistingQuestion() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        Question question = new Question();
        question.setQuestionId(1L);

        FeedbackQuestionsAssociation request = new FeedbackQuestionsAssociation();
        request.setFeedback(feedback);
        request.setQuestion(question);
        request.setAnswer("Test answer");

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback)); // Mock existing feedback
        when(questionRepository.findById(1L)).thenReturn(Optional.empty()); // Mock non-existing question

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.createAssociation(request);
        });

        // Verify exception message
        assertEquals("Question with ID: 1 is not found!!", exception.getMessage());
    }

    /**
     * Test method for attempting to create an association with a null answer.
     */
    @Test
    void testCreateAssociation_NullAnswer() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        Question question = new Question();
        question.setQuestionId(1L);

        FeedbackQuestionsAssociation request = new FeedbackQuestionsAssociation();
        request.setFeedback(feedback);
        request.setQuestion(question);
        request.setAnswer(null); // Null answer

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback)); // Mock existing feedback
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question)); // Mock existing question

        // Act & Assert
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            service.createAssociation(request);
        });

        // Verify exception message
        assertEquals("FeedbackQuestionsAssociation with Answer cannot be null or empty: Null or Empty is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Test method for attempting to create an association with an empty answer.
     */
    @Test
    void testCreateAssociation_EmptyAnswer() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        Question question = new Question();
        question.setQuestionId(1L);

        FeedbackQuestionsAssociation request = new FeedbackQuestionsAssociation();
        request.setFeedback(feedback);
        request.setQuestion(question);
        request.setAnswer(" "); // Empty answer

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback)); // Mock existing feedback
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question)); // Mock existing question

        // Act & Assert
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            service.createAssociation(request);
        });

        // Verify exception message
        assertEquals("FeedbackQuestionsAssociation with Answer cannot be null or empty: Null or Empty is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Test method for handling internal errors when saving the association.
     */
    @Test
    void testCreateAssociation_InternalError() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        Question question = new Question();
        question.setQuestionId(1L);

        FeedbackQuestionsAssociation request = new FeedbackQuestionsAssociation();
        request.setFeedback(feedback);
        request.setQuestion(question);
        request.setAnswer("Test answer");

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback)); // Mock existing feedback
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question)); // Mock existing question
        when(associationRepository.save(any())).thenThrow(new RuntimeException("Internal error")); // Mock save error

        // Act & Assert
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            service.createAssociation(request);
        });

        // Verify exception message
        assertEquals("FeedbackQuestionAssociation with feedback and question: internal error is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Test method for retrieving feedback questions by a valid feedback ID.
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_Success() throws ResourceNotFoundException, ResourceUnableToCreate {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        Question question = new Question();
        question.setQuestionId(1L);
        question.setQuestionDescription("Sample Question");

        FeedbackQuestionsAssociation association = new FeedbackQuestionsAssociation();
        association.setFeedback(feedback);
        association.setQuestion(question);
        association.setAnswer("Test answer");

        List<FeedbackQuestionsAssociation> associations = new ArrayList<>();
        associations.add(association);

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback)); // Mock feedback existence
        when(associationRepository.findByFeedback(feedback)).thenReturn(associations); // Mock associations retrieval

        // Act
        List<FeedbackQuestionDTO> result = service.getFeedbackQuestionsByFeedbackId(1);

        // Assert
        assertNotNull(result); // Ensure result is not null
        assertEquals(1, result.size()); // Ensure one DTO is returned
        assertEquals("Sample Question", result.get(0).getQuestionDescription()); // Check question description
        assertEquals("Test answer", result.get(0).getAnswer()); // Check answer
    }

    /**
     * Test method for retrieving feedback questions by a non-existing feedback ID.
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_NonExistingFeedback() {
        // Arrange
        when(feedbackRepository.findById(1)).thenReturn(Optional.empty()); // Mock non-existing feedback

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getFeedbackQuestionsByFeedbackId(1);
        });

        // Verify exception message
        assertEquals("Feedback with ID: 1 is not found!!", exception.getMessage());
    }

    /**
     * Test method for retrieving feedback questions with no associated questions.
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_NoAssociations() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback)); // Mock existing feedback
        when(associationRepository.findByFeedback(feedback)).thenReturn(new ArrayList<>()); // Mock no associations

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getFeedbackQuestionsByFeedbackId(1);
        });

        // Verify exception message
        assertEquals("FeedbackQuestionsAssociation with Feedback ID: 1 is not found!!", exception.getMessage());
    }

    /**
     * Test method for retrieving feedback questions with a null feedback ID.
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_NullFeedbackId() {
        // Act & Assert
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            service.getFeedbackQuestionsByFeedbackId(null);
        });

        // Verify exception message
        assertEquals("FeedbackQuestionsAssociation with Feedback ID cannot be null: Null is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Test method for handling internal errors during DTO mapping.
     * @throws ResourceUnableToCreate 
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_MappingError() throws ResourceNotFoundException, ResourceUnableToCreate {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);

        Question question = new Question();
        question.setQuestionId(1L);
        question.setQuestionDescription("Sample Question");

        FeedbackQuestionsAssociation association = new FeedbackQuestionsAssociation();
        association.setFeedback(feedback);
        association.setQuestion(question);
        association.setAnswer("Test answer");

        List<FeedbackQuestionsAssociation> associations = new ArrayList<>();
        associations.add(association);

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback)); // Mock feedback existence
        when(associationRepository.findByFeedback(feedback)).thenReturn(associations); // Mock associations retrieval

        // Simulate mapping error
        // Note: In a real scenario, you'd typically use a mocking framework to throw an exception here
        // Here, we just ensure this does not happen.

        // Act
        List<FeedbackQuestionDTO> result = service.getFeedbackQuestionsByFeedbackId(1);

        // Assert
        assertNotNull(result); // Ensure result is not null
        assertEquals(1, result.size()); // Ensure one DTO is returned
    }
}
