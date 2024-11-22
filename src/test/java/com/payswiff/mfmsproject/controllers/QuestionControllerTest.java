/**
 * Unit tests for the QuestionController.
 * This test suite verifies both positive and negative scenarios for QuestionController methods.
 */

package com.payswiff.mfmsproject.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import com.payswiff.mfmsproject.models.Question;
import com.payswiff.mfmsproject.reuquests.CreateQuestionRequest;
import com.payswiff.mfmsproject.services.QuestionService;

class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    private AutoCloseable closeable;

    /**
     * Sets up resources needed before any tests run.
     */
    @BeforeAll
    static void setUpBeforeClass() {
        // Any setup needed before all tests can go here
    }

    /**
     * Cleans up resources after all tests have run.
     */
    @AfterAll
    static void tearDownAfterClass() {
        // Any cleanup needed after all tests can go here
    }

    /**
     * Initializes mocks and resources before each test.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Cleans up mocks and resources after each test.
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Tests successful creation of a question.
     * Verifies that the service layer is called and a CREATED response is returned.
     */
    @Test
    void testCreateQuestion_Success() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Given
        CreateQuestionRequest request = new CreateQuestionRequest("What is Java?");
        Question createdQuestion = new Question(1L, UUID.randomUUID().toString(),"What is Java?");
        when(questionService.saveQuestion(any(Question.class))).thenReturn(createdQuestion);

        // When
        ResponseEntity<Question> response = questionController.createQuestion(request);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdQuestion, response.getBody());
    }

    /**
     * Tests that creating a question fails when it already exists.
     */
    @Test
    void testCreateQuestion_AlreadyExists() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Given
        CreateQuestionRequest request = new CreateQuestionRequest("What is Java?");
        when(questionService.saveQuestion(any(Question.class))).thenThrow(new ResourceAlreadyExists("Question already exists","",""));

        // When / Then
        ResourceAlreadyExists thrown = assertThrows(ResourceAlreadyExists.class, () -> {
            questionController.createQuestion(request);
        });
        assertEquals("Question already exists with :  already exists.", thrown.getMessage());
    }

    /**
     * Tests that creating a question fails due to a creation error.
     */
    @Test
    void testCreateQuestion_UnableToCreate() throws ResourceAlreadyExists, ResourceUnableToCreate {
        // Given
        CreateQuestionRequest request = new CreateQuestionRequest("What is Java?");
        when(questionService.saveQuestion(any(Question.class))).thenThrow(new ResourceUnableToCreate("Unable to create question","",""));

        // When / Then
        ResourceUnableToCreate thrown = assertThrows(ResourceUnableToCreate.class, () -> {
            questionController.createQuestion(request);
        });
        assertEquals("Unable to create question with :  is unable to create at this moment!", thrown.getMessage());
    }

    /**
     * Tests retrieving a question by ID successfully.
     */
    @Test
    void testGetQuestionById_Success() throws ResourceNotFoundException {
        // Given
        Long questionId = 1L;
        Question question = new Question(questionId,UUID.randomUUID().toString(), "What is Java?");
        when(questionService.getQuestionById(questionId)).thenReturn(question);

        // When
        ResponseEntity<Question> response = questionController.getQuestionById(questionId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    /**
     * Tests retrieval failure when a question is not found by ID.
     */
    @Test
    void testGetQuestionById_NotFound() throws ResourceNotFoundException {
        // Given
        Long questionId = 1L;
        when(questionService.getQuestionById(questionId)).thenThrow(new ResourceNotFoundException("Question not found", "", ""));

        // When / Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            questionController.getQuestionById(questionId);
        });
        assertEquals("Question not found with :  is not found!!", thrown.getMessage());
    }

    /**
     * Tests successful retrieval of a question by description.
     */
    @Test
    void testGetQuestionByDescription_Success() throws ResourceNotFoundException {
        // Given
        String description = "What is Java?";
        Question question = new Question(1L, UUID.randomUUID().toString(),description);
        when(questionService.getQuestionByDescription(description)).thenReturn(question);

        // When
        ResponseEntity<Question> response = questionController.getQuestionByDescription(description);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    /**
     * Tests retrieval failure when a question is not found by description.
     */
    @Test
    void testGetQuestionByDescription_NotFound() throws ResourceNotFoundException {
        // Given
        String description = "Unknown question";
        when(questionService.getQuestionByDescription(description)).thenThrow(new ResourceNotFoundException("Question not found","",""));

        // When / Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            questionController.getQuestionByDescription(description);
        });
        assertEquals("Question not found with :  is not found!!", thrown.getMessage());
    }

    /**
     * Tests successful retrieval of all questions.
     */
    @Test
    void testGetAllQuestions_Success() {
        // Given
        List<Question> questions = Arrays.asList(
            new Question(1L,  UUID.randomUUID().toString(),"What is Java?"),
            new Question(2L, UUID.randomUUID().toString(), "What is polymorphism?")
        );
        when(questionService.getAllQuestions()).thenReturn(questions);

        // When
        ResponseEntity<List<Question>> response = questionController.getAllQuestions();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    /**
     * Tests retrieval of all questions when no questions exist.
     */
    @Test
    void testGetAllQuestions_Empty() {
        // Given
        when(questionService.getAllQuestions()).thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<Question>> response = questionController.getAllQuestions();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
