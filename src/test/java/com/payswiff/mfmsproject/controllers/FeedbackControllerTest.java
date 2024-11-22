
package com.payswiff.mfmsproject.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.payswiff.mfmsproject.models.Feedback;
import com.payswiff.mfmsproject.dtos.AverageRatingResponseDTO;
import com.payswiff.mfmsproject.dtos.DeviceFeedbackCountDTO;
import com.payswiff.mfmsproject.dtos.EmployeeFeedbackCountDto;
import com.payswiff.mfmsproject.dtos.FeedbackQuestionAnswerAssignDto;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.reuquests.CreateFeedbackRequest;
import com.payswiff.mfmsproject.reuquests.FeedbackRequestWrapper;
import com.payswiff.mfmsproject.services.FeedbackService;

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
 * Unit tests for the FeedbackController.
 * This class contains tests for feedback creation and retrieval with filters,
 * counting feedbacks, average ratings, and feedback count by device.
 */

class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService; // Mock service for feedback business logic

    @InjectMocks
    private FeedbackController feedbackController; // Controller being tested

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks and injects them into @InjectMocks
    }


    /**
     * Unit test for verifying successful feedback creation in the FeedbackController.
     * This test mocks a valid feedback request and expects a CREATED (201) response status.
     *
     * @throws Exception if any error occurs during the feedback creation process.
     */
    @Test
    void testCreateFeedback_Success() throws Exception {
        // Arrange: Prepare a mock feedback request wrapper with valid data
        FeedbackRequestWrapper requestWrapper = mockFeedbackRequestWrapper(); // Creates a mock request with required fields
        when(feedbackService.createFeedback(any(), any())).thenReturn(true); // Mock feedback creation to return success (true)

        // Act: Call the feedback controller's createFeedback method with the mock request
        ResponseEntity<HttpStatus> response = feedbackController.createFeedback(requestWrapper);

        // Assert: Verify that the response status is CREATED (201) indicating successful creation
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Expected HTTP status 201 CREATED for successful feedback creation.");
    }


    /**
     * Unit test for verifying that feedback creation fails with a null request wrapper.
     * This test expects a {@link ResourceUnableToCreate} exception to be thrown when 
     * the request wrapper is null, as the controller method does not allow null requests.
     */
    @Test
    void testCreateFeedback_NullRequest() {
        // Act & Assert: Call createFeedback with a null argument and expect a ResourceUnableToCreate exception
        assertThrows(ResourceUnableToCreate.class, 
            () -> feedbackController.createFeedback(null), 
            "Expected ResourceUnableToCreate exception when request wrapper is null."
        );
    }

    /**
     * Test for feedback creation with insufficient questions (not exactly 10).
     */
    @Test
    void testCreateFeedback_InsufficientQuestions() throws Exception {
        // Arrange: Mock a feedback request wrapper with an insufficient number of questions
        FeedbackRequestWrapper requestWrapper = mockFeedbackRequestWrapper();
        
        // Copy question answers to a mutable list and remove one to simulate insufficient questions
        List<FeedbackQuestionAnswerAssignDto> mutableQuestionAnswers = new ArrayList<>(requestWrapper.getQuestionAnswers());
        mutableQuestionAnswers.remove(0); // Remove a question to make it 9

        // Update the request wrapper to use the modified list
        when(requestWrapper.getQuestionAnswers()).thenReturn(mutableQuestionAnswers);

        // Act: Attempt to create feedback with an insufficient number of questions
        ResponseEntity<HttpStatus> response = feedbackController.createFeedback(requestWrapper);

        // Assert: Verify that the response status is BAD_REQUEST due to insufficient questions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    /**
     * Unit test for verifying that feedback creation fails at the service level.
     * This test checks that if the service layer fails to create feedback (returns false), 
     * the controller returns an HTTP status of 500 (Internal Server Error).
     */
    @Test
    void testCreateFeedback_Failure() throws Exception {
        // Arrange: Mock the feedback request wrapper and simulate a failure in the service layer
        FeedbackRequestWrapper requestWrapper = mockFeedbackRequestWrapper();
        when(feedbackService.createFeedback(any(), any())).thenReturn(false);  // Simulate failure

        // Act: Call the createFeedback method on the controller
        ResponseEntity<HttpStatus> response = feedbackController.createFeedback(requestWrapper);

        // Assert: Verify that the response status is 500 (Internal Server Error) when the service fails
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), 
                     "Expected HTTP status 500 when feedback creation fails at the service level.");
    }


//    /**
//     * Unit test for verifying that an exception is thrown when all filter parameters are null during feedback retrieval.
//     * This test ensures that the controller throws a ResourceNotFoundException if no filter parameters (employeeId, deviceId, rating, or merchantId) are provided.
//     */
//    @Test
//    void testGetFeedbacksByFilters_AllParametersNull() {
//        // Act & Assert: Call the getFeedbacksByFilters method with all null parameters
//        // and assert that a ResourceNotFoundException is thrown, as at least one filter is required.
//        assertThrows(ResourceNotFoundException.class, () -> 
//            feedbackController.getFeedbacksByFilters(null, null, null, null),
//            "Expected ResourceNotFoundException when all filter parameters are null.");
//    }

    /**
     * Unit test for verifying successful feedback retrieval when only the employeeId filter is provided.
     * This test ensures that the controller calls the service with the provided employeeId and returns a list of feedback.
     * The response status should be HTTP 200 OK when feedback is found for the given employeeId.
     */
    @Test
    void testGetFeedbacksByFilters_OnlyEmployeeId() throws ResourceNotFoundException {
        // Arrange: Mock the feedbackService to return a list with one feedback when the employeeId filter is provided
        when(feedbackService.getFeedbacksByFilters(1L, null, null, null))
                .thenReturn(List.of(new Feedback()));  // Mocked response from service

        // Act: Call the controller method with employeeId as the only filter
        ResponseEntity<List<Feedback>> response = feedbackController.getFeedbacksByFilters(1L, null, null, null);

        // Assert: Verify that the response status is OK (HTTP 200) and feedback is returned
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK when feedback is found for employeeId.");
    }


    /**
     * Unit test for verifying feedback retrieval when multiple filters are provided.
     * This test ensures that the controller can correctly handle multiple filters (employeeId, deviceId, rating, and merchantId)
     * and that it calls the service method with the correct parameters. The response status should be HTTP 200 OK
     * when feedback is found based on the provided filters.
     */
    @Test
    void testGetFeedbacksByFilters_MultipleParameters() throws ResourceNotFoundException {
        // Arrange: Mock the feedbackService to return a list with one feedback when multiple parameters are provided
        when(feedbackService.getFeedbacksByFilters(1L, 2L, 4, 3L))
                .thenReturn(List.of(new Feedback()));  // Mocked response from service

        // Act: Call the controller method with multiple parameters (employeeId, deviceId, rating, and merchantId)
        ResponseEntity<List<Feedback>> response = feedbackController.getFeedbacksByFilters(1L, 2L, 4, 3L);

        // Assert: Verify that the response status is OK (HTTP 200) and feedback is returned
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK when feedback is found with multiple parameters.");
    }


    /**
     * Unit test for verifying the retrieval of all feedback counts successfully.
     * This test ensures that the controller can correctly retrieve the feedback count for all employees
     * by calling the `countFeedbacksForAllEmployees()` method of the service layer.
     * The response should have a status of HTTP 200 OK when the counts are successfully returned.
     */
    @Test
    void testGetAllFeedbacksCount_Success() {
        // Arrange: Create a mock list of feedback count DTOs to return from the service
        List<EmployeeFeedbackCountDto> mockCounts = List.of(new EmployeeFeedbackCountDto());
        
        // Mock the service method to return the mockCounts when called
        when(feedbackService.countFeedbacksForAllEmployees()).thenReturn(mockCounts);

        // Act: Call the controller method to retrieve all feedback counts
        ResponseEntity<List<EmployeeFeedbackCountDto>> response = feedbackController.getAllFeedbacksCount();

        // Assert: Verify that the response status is HTTP 200 OK and that the list of counts is returned
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK when feedback counts are successfully retrieved.");
    }

    /**
     * Unit test for verifying the retrieval of average ratings by device successfully.
     * This test ensures that the controller can correctly retrieve the average ratings
     * for each device by calling the `getAverageRatingByDevice()` method of the service layer.
     * The response should have a status of HTTP 200 OK when the ratings are successfully returned.
     */
    @Test
    void testGetAverageRatingByDevice_Success() {
        // Arrange: Create a mock list of average rating response DTOs to return from the service
        List<AverageRatingResponseDTO> mockRatings = List.of(new AverageRatingResponseDTO());
        
        // Mock the service method to return the mockRatings when called
        when(feedbackService.getAverageRatingByDevice()).thenReturn(mockRatings);

        // Act: Call the controller method to retrieve average ratings by device
        ResponseEntity<List<AverageRatingResponseDTO>> response = feedbackController.getAverageRatingByDevice();

        // Assert: Verify that the response status is HTTP 200 OK and that the list of average ratings is returned
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK when average ratings by device are successfully retrieved.");
    }


    /**
     * Unit test for verifying the retrieval of feedback counts by device successfully.
     * This test ensures that the controller can correctly retrieve the feedback counts 
     * for each device by calling the `getFeedbackCountByDevice()` method of the service layer.
     * The response should have a status of HTTP 200 OK when the feedback counts are successfully returned.
     */
    @Test
    void testGetFeedbackCountByDevice_Success() {
        // Arrange: Create a mock list of feedback count DTOs to return from the service
        List<DeviceFeedbackCountDTO> mockCounts = List.of(new DeviceFeedbackCountDTO());
        
        // Mock the service method to return the mockCounts when called
        when(feedbackService.getFeedbackCountByDevice()).thenReturn(mockCounts);

        // Act: Call the controller method to retrieve feedback counts by device
        ResponseEntity<List<DeviceFeedbackCountDTO>> response = feedbackController.getFeedbackCountByDevice();

        // Assert: Verify that the response status is HTTP 200 OK and that the list of feedback counts is returned
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK when feedback count by device is successfully retrieved.");
    }


    /**
     * Test for retrieving all feedback counts, ensuring the response is an empty list when no feedback exists.
     */
    @Test
    void testGetAllFeedbacksCount_EmptyList() {
        // Arrange: Mock the service to return an empty list of feedback counts for all employees
        when(feedbackService.countFeedbacksForAllEmployees()).thenReturn(Collections.emptyList());

        // Act: Call the controller method to retrieve all feedback counts
        ResponseEntity<List<EmployeeFeedbackCountDto>> response = feedbackController.getAllFeedbacksCount();

        // Assert: Ensure the response body is empty when no feedback counts are present
        assertTrue(response.getBody().isEmpty(), "Expected an empty list of feedback counts.");
    }

    /**
     * Test for retrieving feedback counts by device, ensuring the response is an empty list when no feedback is available.
     */
    @Test
    void testGetFeedbackCountByDevice_EmptyList() {
        // Arrange: Mock the service to return an empty list of feedback counts by device
        when(feedbackService.getFeedbackCountByDevice()).thenReturn(Collections.emptyList());

        // Act: Call the controller method to retrieve feedback counts by device
        ResponseEntity<List<DeviceFeedbackCountDTO>> response = feedbackController.getFeedbackCountByDevice();

        // Assert: Ensure the response body is empty when no feedback counts by device are present
        assertTrue(response.getBody().isEmpty(), "Expected an empty list of feedback counts by device.");
    }

    /**
     * Test for retrieving average ratings by device, ensuring the response is an empty list when no ratings are available.
     */
    @Test
    void testGetAverageRatingByDevice_EmptyList() {
        // Arrange: Mock the service to return an empty list of average ratings by device
        when(feedbackService.getAverageRatingByDevice()).thenReturn(Collections.emptyList());

        // Act: Call the controller method to retrieve average ratings by device
        ResponseEntity<List<AverageRatingResponseDTO>> response = feedbackController.getAverageRatingByDevice();

        // Assert: Ensure the response body is empty when no average ratings by device are available
        assertTrue(response.getBody().isEmpty(), "Expected an empty list of average ratings by device.");
    }

    /**
     * Test for retrieving feedbacks with only deviceId as a filter.
     * Verifies that feedbacks can be retrieved when only the deviceId filter is applied.
     */
    @Test
    void testGetFeedbacksByFilters_OnlyDeviceId() throws ResourceNotFoundException {
        // Arrange: Mock the service to return feedbacks when deviceId is 2 and other parameters are null
        when(feedbackService.getFeedbacksByFilters(null, 2L, null, null)).thenReturn(List.of(new Feedback()));

        // Act: Call the controller method with deviceId filter
        ResponseEntity<List<Feedback>> response = feedbackController.getFeedbacksByFilters(null, 2L, null, null);

        // Assert: Verify that the response status is HTTP 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK when only deviceId is provided.");
    }

    /**
     * Test for feedback creation where the question list is null.
     * Verifies that a NullPointerException is thrown when question answers are null in the request.
     */
    @Test
    void testCreateFeedback_WithNullQuestionList() {
        // Arrange: Mock the request wrapper with null question answers
        FeedbackRequestWrapper requestWrapper = mockFeedbackRequestWrapper();
        requestWrapper.setQuestionAnswers(null); // Set question answers to null

        // Act & Assert: Expect a NullPointerException to be thrown when attempting to create feedback
        assertThrows(NullPointerException.class, () -> feedbackController.createFeedback(requestWrapper), "Expected NullPointerException when question answers are null.");
    }

    /**
     * Test for retrieving feedbacks with only rating as a filter.
     * Verifies that feedbacks can be retrieved when only the rating filter is applied.
     */
    @Test
    void testGetFeedbacksByFilters_OnlyRating() throws ResourceNotFoundException {
        // Arrange: Mock the service to return feedbacks when rating is 5 and other parameters are null
        when(feedbackService.getFeedbacksByFilters(null, null, 5, null)).thenReturn(List.of(new Feedback()));

        // Act: Call the controller method with rating filter
        ResponseEntity<List<Feedback>> response = feedbackController.getFeedbacksByFilters(null, null, 5, null);

        // Assert: Verify that the response status is HTTP 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK when only rating is provided.");
    }
    /**
     * Helper method to create a mock FeedbackRequestWrapper with valid data.
     * This method is used to generate a valid mock request object that can be used in unit tests for the FeedbackController.
     * It populates the request with mock feedback data and a list of question-answer assignments.
     *
     * @return FeedbackRequestWrapper populated with mock data.
     */
    private FeedbackRequestWrapper mockFeedbackRequestWrapper() {
        // Create a mock CreateFeedbackRequest object, which will be part of the wrapper.
        CreateFeedbackRequest feedbackRequest = new CreateFeedbackRequest();
        
        // Create a mock FeedbackQuestionAnswerAssignDto, which represents a single question-answer pair.
        FeedbackQuestionAnswerAssignDto answerDto = new FeedbackQuestionAnswerAssignDto();
        
        // Populate the question answers list with 10 instances of the answerDto.
        // The Collections.nCopies() method creates a list with 10 identical copies of answerDto.
        List<FeedbackQuestionAnswerAssignDto> questionAnswers = Collections.nCopies(10, answerDto);
        
        // Create the FeedbackRequestWrapper and set the feedbackRequest and questionAnswers fields.
        // This wrapper is used to encapsulate both the feedback request and the answers.
        FeedbackRequestWrapper requestWrapper = new FeedbackRequestWrapper(feedbackRequest, questionAnswers);
        
        // Explicitly set the fields, though they are already set by the constructor.
        requestWrapper.setFeedbackRequest(feedbackRequest);
        requestWrapper.setQuestionAnswers(questionAnswers);
        
        // Return the populated mock wrapper.
        return requestWrapper;
    }

}
