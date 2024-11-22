package com.payswiff.mfmsproject.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.payswiff.mfmsproject.dtos.FeedbackQuestionDTO;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.services.FeedbackQuestionsAssociationService;
import org.junit.jupiter.api.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for FeedbackQuestionsAssociationController.
 * This class contains unit tests for the FeedbackQuestionsAssociationController
 * to ensure the correct behavior of the getFeedbackQuestionsByFeedbackId method.
 */
class FeedbackQuestionsAssociationControllerTest {

    @Mock
    private FeedbackQuestionsAssociationService feedbackQuestionsAssociationService; // Mocked service

    @InjectMocks
    private FeedbackQuestionsAssociationController feedbackQuestionsAssociationController; // Controller under test

    /**
     * Setup method to initialize mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    /**
     * Test method for a successful retrieval of feedback questions.
     * It verifies that the correct response is returned when valid feedback ID is provided.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_Success() throws ResourceUnableToCreate, ResourceNotFoundException {
        // Arrange
        Integer feedbackId = 1; // Valid feedback ID
        List<FeedbackQuestionDTO> expectedQuestions = new ArrayList<>();
        expectedQuestions.add(new FeedbackQuestionDTO(1L,"is our product is not working fine","yeah nice working")); // Add mock data
        
        when(feedbackQuestionsAssociationService.getFeedbackQuestionsByFeedbackId(feedbackId)).thenReturn(expectedQuestions);

        // Act
        ResponseEntity<List<FeedbackQuestionDTO>> response = feedbackQuestionsAssociationController.getFeedbackQuestionsByFeedbackId(feedbackId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verify status code
        assertEquals(expectedQuestions, response.getBody()); // Verify response body
    }

    /**
     * Test method for handling ResourceNotFoundException when an invalid feedback ID is provided.
     * It checks that a NOT_FOUND response is returned.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_NotFound() throws ResourceUnableToCreate, ResourceNotFoundException {
        // Arrange
        Integer feedbackId = 999; // Invalid feedback ID
        when(feedbackQuestionsAssociationService.getFeedbackQuestionsByFeedbackId(feedbackId))
                .thenThrow(new ResourceNotFoundException("Feedback not found","",""));

        // Act
        ResponseEntity<List<FeedbackQuestionDTO>> response = feedbackQuestionsAssociationController.getFeedbackQuestionsByFeedbackId(feedbackId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // Verify status code
        assertNull(response.getBody()); // Verify response body is null
    }


    /**
     * Test method for handling an empty list of feedback questions.
     * It verifies that an empty list is returned when there are no questions associated with the feedback ID.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_EmptyList() throws ResourceUnableToCreate, ResourceNotFoundException {
        // Arrange
        Integer feedbackId = 2; // Valid feedback ID
        when(feedbackQuestionsAssociationService.getFeedbackQuestionsByFeedbackId(feedbackId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<FeedbackQuestionDTO>> response = feedbackQuestionsAssociationController.getFeedbackQuestionsByFeedbackId(feedbackId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verify status code
        assertEquals(Collections.emptyList(), response.getBody()); // Verify response body is empty
    }

    /**
     * Test method for handling unexpected exceptions.
     * It verifies that the controller handles unexpected exceptions gracefully.
     * @throws ResourceNotFoundException 
     */
    /**
     * Test method for handling unexpected exceptions.
     * It verifies that the controller handles unexpected exceptions gracefully.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_UnexpectedException() throws ResourceUnableToCreate, ResourceNotFoundException {
        // Arrange
        Integer feedbackId = 3; // Valid feedback ID
        when(feedbackQuestionsAssociationService.getFeedbackQuestionsByFeedbackId(feedbackId))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<List<FeedbackQuestionDTO>> response = feedbackQuestionsAssociationController.getFeedbackQuestionsByFeedbackId(feedbackId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()); // Check for internal server error
        assertNull(response.getBody()); // Check for null response body
    }

    /**
     * Test method for verifying that a feedback ID that does not exist results in no questions being returned.
     * It ensures the method handles this case without throwing an exception.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_NoQuestions() throws ResourceUnableToCreate, ResourceNotFoundException {
        // Arrange
        Integer feedbackId = 4; // Feedback ID that exists but has no associated questions
        when(feedbackQuestionsAssociationService.getFeedbackQuestionsByFeedbackId(feedbackId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<FeedbackQuestionDTO>> response = feedbackQuestionsAssociationController.getFeedbackQuestionsByFeedbackId(feedbackId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verify status code is OK
        assertNotNull(response.getBody()); // Verify response body is not null
        assertTrue(response.getBody().isEmpty()); // Verify that the response body is an empty list
    }

    /**
     * Test method for verifying the service call is made once for a valid feedback ID.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetFeedbackQuestionsByFeedbackId_CallsServiceOnce() throws ResourceUnableToCreate, ResourceNotFoundException {
        // Arrange
        Integer feedbackId = 5; // Valid feedback ID
        List<FeedbackQuestionDTO> expectedQuestions = new ArrayList<>();
        expectedQuestions.add(new FeedbackQuestionDTO(1L,"is our product is not working fine","yeah nice working")); // Add mock data
        
        when(feedbackQuestionsAssociationService.getFeedbackQuestionsByFeedbackId(feedbackId)).thenReturn(expectedQuestions);

        // Act
        feedbackQuestionsAssociationController.getFeedbackQuestionsByFeedbackId(feedbackId);

        // Assert
        verify(feedbackQuestionsAssociationService, times(1)).getFeedbackQuestionsByFeedbackId(feedbackId); // Verify service method was called once
    }

    /**
     * Cleanup method after all tests have been run.
     */
    @AfterAll
    static void tearDownAfterClass() {
        // Clean up resources if needed
    }
}
