package com.payswiff.mfmsproject.controllers;

import com.payswiff.mfmsproject.models.Feedback; // Importing model class for feedback data
import com.payswiff.mfmsproject.reuquests.CreateFeedbackRequest; // Importing request structure for feedback creation
import com.payswiff.mfmsproject.reuquests.FeedbackRequestWrapper; // Importing wrapper for feedback requests
import com.payswiff.mfmsproject.services.FeedbackService; // Service class to handle feedback business logic

import jakarta.validation.Valid; // Validation library for input validation annotations
import com.payswiff.mfmsproject.dtos.AverageRatingResponseDTO; // DTO for average rating response
import com.payswiff.mfmsproject.dtos.DeviceFeedbackCountDTO; // DTO for device feedback count
import com.payswiff.mfmsproject.dtos.EmployeeFeedbackCountDto; // DTO for employee feedback count
import com.payswiff.mfmsproject.dtos.FeedbackQuestionAnswerAssignDto; // DTO for feedback question-answer assignment
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException; // Custom exception for resources not found
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate; // Custom exception for feedback creation failure

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List; // Import List for handling collections

import org.springframework.beans.factory.annotation.Autowired; // Dependency injection
import org.springframework.http.HttpStatus; // HTTP status codes
import org.springframework.http.ResponseEntity; // Building HTTP responses
import org.springframework.web.bind.annotation.*; // RESTful web service annotations
/**
 * The FeedbackController handles all HTTP requests related to feedback management.
 * Provides endpoints for:
 * - Creating feedback records
 * - Fetching feedback based on employee, device, rating, and merchant
 * - Retrieving feedback count and average rating grouped by various criteria
 * <p>
 * Relies on {@link FeedbackService} to execute business logic, while handling request validation, error handling, and logging.
 * </p>
 *
 * @version MFMS_0.0.1
 * @author Chatla Sarika
 */

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})
public class FeedbackController {

    private static final Logger logger = LogManager.getLogger(FeedbackController.class); // Logger for this class

    @Autowired
    private FeedbackService feedbackService; // Injecting FeedbackService for business logic operations

    /**
     * Endpoint to create a new feedback entry.
     * Validates that the request is non-null and contains exactly 10 question-answer pairs.
     *
     * @param requestWrapper The wrapper containing feedback request data and question-answer pairs.
     * @return ResponseEntity with HTTP status indicating the result of the creation.
     * @throws ResourceUnableToCreate if the request is null or incomplete.
     */
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createFeedback(
            @Valid @RequestBody FeedbackRequestWrapper requestWrapper) throws Exception {

        logger.info("Initiating feedback creation process"); // Log beginning of creation process

        // Check if the requestWrapper is null
        if (requestWrapper == null) {
            logger.error("Feedback creation failed: Request wrapper is null"); // Log error for null request
            throw new ResourceUnableToCreate("Feedback", "RequestWrapper", "Feedback request cannot be null");
        }

        // Extract the feedback request and question-answer list from the wrapper
        CreateFeedbackRequest feedbackRequest = requestWrapper.getFeedbackRequest();
        List<FeedbackQuestionAnswerAssignDto> questionAnswers = requestWrapper.getQuestionAnswers();

        // Validate that the feedback contains exactly 10 question-answer pairs
        if (questionAnswers.size() != 10) {
            logger.warn("Feedback creation failed: Expected 10 questions but received {}", questionAnswers.size()); // Log warning for invalid count
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return HTTP 400 Bad Request
        }

        // Call the service layer to create feedback
        boolean isFeedbackCreated = feedbackService.createFeedback(feedbackRequest, questionAnswers);

        // Check the result of the feedback creation and log accordingly
        if (isFeedbackCreated) {
            logger.info("Feedback created successfully for request: {}", feedbackRequest); // Log success
            return new ResponseEntity<>(HttpStatus.CREATED); // Return HTTP 201 Created
        } else {
            logger.error("Feedback creation failed due to server error for request: {}", feedbackRequest); // Log failure
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return HTTP 500 Internal Server Error
        }
    }

    /**
     * Retrieves feedback based on filter criteria, such as employee ID, device ID, rating, and merchant ID.
     *
     * @param employeeId The ID of the employee (optional).
     * @param deviceId   The ID of the device (optional).
     * @param rating     The feedback rating (optional).
     * @param merchantId The ID of the merchant (optional).
     * @return ResponseEntity containing a list of feedback entries matching the filters.
     * @throws ResourceNotFoundException if no feedbacks match the provided criteria.
     */
    @GetMapping("/getallfeedbacks")
    public ResponseEntity<List<Feedback>> getFeedbacksByFilters(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Long merchantId) throws ResourceNotFoundException {
        
        // Log input filter parameters
        logger.info("Retrieving feedbacks with filters - Employee ID: {}, Device ID: {}, Rating: {}, Merchant ID: {}", 
                    employeeId, deviceId, rating, merchantId);

        // Fetch feedback based on filters
        List<Feedback> feedbacks = feedbackService.getFeedbacksByFilters(employeeId, deviceId, rating, merchantId);

        // Log result based on feedback data availability
        if (feedbacks.isEmpty()) {
            logger.warn("No feedbacks found matching the provided filters"); // Warn if no results
        } else {
            logger.info("Successfully retrieved {} feedback(s) matching the filters", feedbacks.size()); // Log success with count
        }

        // Return feedback list with HTTP 200 OK status
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    /**
     * Retrieves the count of feedbacks submitted for each employee.
     *
     * @return ResponseEntity containing a list of feedback counts per employee.
     */
    @GetMapping("/allfeedbackscount")
    public ResponseEntity<List<EmployeeFeedbackCountDto>> getAllFeedbacksCount() {
        logger.info("Retrieving feedback counts for all employees"); // Log request start

        // Get feedback counts from the service
        List<EmployeeFeedbackCountDto> feedbackCounts = feedbackService.countFeedbacksForAllEmployees();

        // Log result based on feedback count availability
        if (feedbackCounts.isEmpty()) {
            logger.warn("No feedback counts available for employees"); // Warn if no data
        } else {
            logger.info("Successfully retrieved feedback counts for {} employee(s)", feedbackCounts.size()); // Log success
        }

        // Return list of feedback counts with HTTP 200 OK
        return ResponseEntity.ok(feedbackCounts);
    }

    /**
     * Retrieves the average feedback rating for each device.
     *
     * @return ResponseEntity containing a list of average ratings grouped by device.
     */
    @GetMapping("/average-rating-by-device")
    public ResponseEntity<List<AverageRatingResponseDTO>> getAverageRatingByDevice() {
        logger.info("Retrieving average feedback ratings grouped by device"); // Log request start

        // Get average ratings by device from the service
        List<AverageRatingResponseDTO> averageRatings = feedbackService.getAverageRatingByDevice();

        // Log result based on average ratings data availability
        if (averageRatings.isEmpty()) {
            logger.warn("No average ratings available for devices"); // Warn if no data
        } else {
            logger.info("Successfully retrieved average ratings for {} device(s)", averageRatings.size()); // Log success
        }

        // Return average ratings list with HTTP 200 OK
        return ResponseEntity.ok(averageRatings);
    }

    /**
     * Retrieves the count of feedbacks grouped by device.
     *
     * @return ResponseEntity containing a list of feedback counts per device.
     */
    @GetMapping("/device-count")
    public ResponseEntity<List<DeviceFeedbackCountDTO>> getFeedbackCountByDevice() {
        logger.info("Retrieving feedback counts grouped by device"); // Log request start

        // Get feedback counts by device from the service
        List<DeviceFeedbackCountDTO> feedbackCounts = feedbackService.getFeedbackCountByDevice();

        // Log result based on feedback count data availability
        if (feedbackCounts.isEmpty()) {
            logger.warn("No feedback counts available for devices"); // Warn if no data
        } else {
            logger.info("Successfully retrieved feedback counts for {} device(s)", feedbackCounts.size()); // Log success
        }

        // Return device feedback counts list with HTTP 200 OK
        return ResponseEntity.ok(feedbackCounts);
    }
}
