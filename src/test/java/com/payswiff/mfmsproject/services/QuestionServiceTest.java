package com.payswiff.mfmsproject.services;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Question;
import com.payswiff.mfmsproject.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the QuestionService class.
 *
 * This test suite verifies various functionalities of the QuestionService methods, ensuring that 
 * the service behaves correctly under different scenarios. The following test cases are covered:
 *
 * 1. {@link QuestionService#saveQuestion(Question)}:
 *    - testSaveQuestion_Success: Tests successful saving of a new question.
 *    - testSaveQuestion_EmptyDescription_ThrowsResourceUnableToCreate: Tests exception thrown for empty question description.
 *    - testSaveQuestion_NullDescription_ThrowsResourceUnableToCreate: Tests exception thrown for null question description.
 *    - testSaveQuestion_ThrowsResourceAlreadyExists: Tests exception thrown for duplicate question description.
 *    - testSaveQuestion_ThrowsResourceUnableToCreate: Tests exception thrown for unexpected save failures.
 *
 * 2. {@link QuestionService#getQuestionById(Long)}:
 *    - testGetQuestionById_Success: Tests successful retrieval of a question by its ID.
 *    - testGetQuestionById_ThrowsResourceNotFoundException: Tests exception thrown for non-existent question ID.
 *    - testGetQuestionById_NegativeId_ThrowsResourceNotFoundException: Tests exception thrown for negative question ID.
 *
 * 3. {@link QuestionService#getQuestionByDescription(String)}:
 *    - testGetQuestionByDescription_Success: Tests successful retrieval of a question by its description.
 *    - testGetQuestionByDescription_ThrowsResourceNotFoundException: Tests exception thrown for non-existent question description.
 *
 * 4. {@link QuestionService#getAllQuestions()}:
 *    - testGetAllQuestions_ReturnsListOfQuestions: Tests retrieval of all questions when multiple exist.
 *    - testGetAllQuestions_ReturnsEmptyList: Tests retrieval of all questions when none exist.
 *    - testGetAllQuestions_MultipleEntries: Tests retrieval of multiple questions.
 *
 * This test suite ensures that the QuestionService is robust and handles various scenarios appropriately.
 */
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository; // Mock the QuestionRepository dependency

    @InjectMocks
    private QuestionService questionService; // Inject mocks into QuestionService

    /**
     * Initializes the Mockito annotations before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests saving a question successfully.
     * Verifies that saveQuestion returns the saved question when it doesn't already exist.
     *
     * @throws ResourceAlreadyExists if a question with the same description already exists.
     * @throws ResourceUnableToCreate if an unexpected error occurs during saving the question.
     */
    @Test
    void testSaveQuestion_Success() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Arrange: Create a new Question object
        Question question = new Question();
        question.setQuestionDescription("Sample question");

        // Mock repository behavior: no existing question with the same description
        when(questionRepository.findByDescription("Sample question")).thenReturn(Optional.empty());
        
        // Mock repository to return the saved question when save is called
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        // Act: Call saveQuestion on the service
        Question savedQuestion = questionService.saveQuestion(question);

        // Assert: Verify the result and that save was called once
        assertEquals("Sample question", savedQuestion.getQuestionDescription());
        verify(questionRepository, times(1)).save(question);
    }

    /**
     * Tests saving a question with an empty description.
     * 
     * @throws ResourceUnableToCreate if the question description is empty.
     */
    @Test
    void testSaveQuestion_EmptyDescription_ThrowsResourceUnableToCreate() {
        // Arrange: Create a Question object with an empty description
        Question question = new Question();
        question.setQuestionDescription("");

        // Act & Assert: Verify that the exception is thrown for empty description
        assertThrows(ResourceUnableToCreate.class, () -> questionService.saveQuestion(question));
    }

    /**
     * Tests saving a question with a null description.
     * 
     * @throws ResourceUnableToCreate if the question description is null.
     */
    @Test
    void testSaveQuestion_NullDescription_ThrowsResourceUnableToCreate() {
        // Arrange: Create a Question object with a null description
        Question question = new Question();
        question.setQuestionDescription(null);

        // Act & Assert: Verify that the exception is thrown due to null description
        assertThrows(ResourceUnableToCreate.class, () -> questionService.saveQuestion(question));
    }

    /**
     * Tests for ResourceAlreadyExists exception when trying to save a duplicate question.
     * 
     * @throws ResourceAlreadyExists if a question with the same description already exists.
     */
    @Test
    void testSaveQuestion_ThrowsResourceAlreadyExists() {
        // Arrange: Create a Question object with a description that already exists
        Question question = new Question();
        question.setQuestionDescription("Sample question");

        // Mock repository behavior: an existing question with the same description
        when(questionRepository.findByDescription("Sample question")).thenReturn(Optional.of(question));

        // Act & Assert: Verify that the exception is thrown
        assertThrows(ResourceAlreadyExists.class, () -> questionService.saveQuestion(question));
    }

    /**
     * Tests for ResourceUnableToCreate exception when a save operation fails unexpectedly.
     * 
     * @throws ResourceUnableToCreate if there is a failure in saving the question.
     */
    @Test
    void testSaveQuestion_ThrowsResourceUnableToCreate() {
        // Arrange: Create a Question object with a unique description
        Question question = new Question();
        question.setQuestionDescription("Sample question");

        // Mock repository behavior: no existing question but throw a RuntimeException on save
        when(questionRepository.findByDescription("Sample question")).thenReturn(Optional.empty());
        when(questionRepository.save(any(Question.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert: Verify that the exception is thrown
        assertThrows(ResourceUnableToCreate.class, () -> questionService.saveQuestion(question));
    }

    /**
     * Tests retrieving a question by ID successfully.
     * 
     * @throws ResourceNotFoundException if no question is found with the provided ID.
     */
    @Test
    void testGetQuestionById_Success() throws ResourceNotFoundException {
        // Arrange: Create a Question object with a specific ID
        Question question = new Question();
        question.setQuestionId(1L);
        question.setQuestionDescription("Sample question");

        // Mock repository behavior: return the question when findById is called
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // Act: Retrieve the question by ID
        Question foundQuestion = questionService.getQuestionById(1L);

        // Assert: Check that the question is returned correctly and findById was called once
        assertNotNull(foundQuestion);
        assertEquals(1L, foundQuestion.getQuestionId());
        verify(questionRepository, times(1)).findById(1L);
    }

    /**
     * Tests for ResourceNotFoundException when retrieving a question by an invalid ID.
     * 
     * @throws ResourceNotFoundException if no question is found with the provided ID.
     */
    @Test
    void testGetQuestionById_ThrowsResourceNotFoundException() {
        // Arrange: Set repository to return an empty result when finding by ID
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Verify that the exception is thrown
        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuestionById(1L));
    }

    /**
     * Tests retrieving a question by ID with a negative ID.
     * 
     * @throws ResourceNotFoundException if a negative ID is provided.
     */
    @Test
    void testGetQuestionById_NegativeId_ThrowsResourceNotFoundException() {
        // Arrange: Use an invalid (negative) ID
        long invalidId = -1L;

        // Act & Assert: Verify that the exception is thrown for invalid ID
        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuestionById(invalidId));
    }

    /**
     * Tests retrieving a question by description successfully.
     * 
     * @throws ResourceNotFoundException if no question is found with the provided description.
     */
    @Test
    void testGetQuestionByDescription_Success() throws ResourceNotFoundException {
        // Arrange: Create a Question object with a specific description
        Question question = new Question();
        question.setQuestionDescription("Sample question");

        // Mock repository behavior: return the question when findByDescription is called
        when(questionRepository.findByDescription("Sample question")).thenReturn(Optional.of(question));

        // Act: Retrieve the question by description
        Question foundQuestion = questionService.getQuestionByDescription("Sample question");

        // Assert: Verify the result and that findByDescription was called once
        assertNotNull(foundQuestion);
        assertEquals("Sample question", foundQuestion.getQuestionDescription());
        verify(questionRepository, times(1)).findByDescription("Sample question");
    }

    /**
     * Tests for ResourceNotFoundException when retrieving a question by an invalid description.
     * 
     * @throws ResourceNotFoundException if no question is found with the provided description.
     */
    @Test
    void testGetQuestionByDescription_ThrowsResourceNotFoundException() {
        // Arrange: Set repository to return an empty result for the description
        when(questionRepository.findByDescription("Non-existing question")).thenReturn(Optional.empty());

        // Act & Assert: Verify that the exception is thrown
        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuestionByDescription("Non-existing question"));
    }

    /**
     * Tests retrieving all questions, verifying it returns a list of questions.
     */
    @Test
    void testGetAllQuestions_ReturnsListOfQuestions() {
        // Arrange: Create multiple Question objects
        Question question1 = new Question();
        question1.setQuestionDescription("Question 1");

        Question question2 = new Question();
        question2.setQuestionDescription("Question 2");

        // Mock repository behavior: return a list of questions
        when(questionRepository.findAll()).thenReturn(List.of(question1, question2));

        // Act: Retrieve all questions
        List<Question> questions = questionService.getAllQuestions();

        // Assert: Verify that the list contains the expected questions
        assertEquals(2, questions.size());
        assertEquals("Question 1", questions.get(0).getQuestionDescription());
        assertEquals("Question 2", questions.get(1).getQuestionDescription());
        verify(questionRepository, times(1)).findAll();
    }

    /**
     * Tests retrieving all questions when there are none, expecting an empty list.
     */
    @Test
    void testGetAllQuestions_ReturnsEmptyList() {
        // Arrange: Set repository to return an empty list of questions
        when(questionRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: Retrieve all questions
        List<Question> questions = questionService.getAllQuestions();

        // Assert: Verify that the result is an empty list and findAll was called once
        assertTrue(questions.isEmpty());
        verify(questionRepository, times(1)).findAll();
    }

    /**
     * Tests for a case where multiple questions are saved and retrieved.
     */
    @Test
    void testGetAllQuestions_MultipleEntries() {
        // Arrange: Create multiple Question objects
        Question question1 = new Question();
        question1.setQuestionDescription("Question 1");

        Question question2 = new Question();
        question2.setQuestionDescription("Question 2");

        // Mock repository behavior to return the created questions
        when(questionRepository.findAll()).thenReturn(List.of(question1, question2));

        // Act: Retrieve all questions
        List<Question> questions = questionService.getAllQuestions();

        // Assert: Verify that the correct number of questions are retrieved
        assertEquals(2, questions.size());
        assertEquals("Question 1", questions.get(0).getQuestionDescription());
        assertEquals("Question 2", questions.get(1).getQuestionDescription());
        verify(questionRepository, times(1)).findAll();
    }
}
