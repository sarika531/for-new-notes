package com.payswiff.mfmsproject.controllers;

import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.services.FeedbackQuestionsAssociationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.payswiff.mfmsproject.dtos.FeedbackQuestionDTO;
import java.util.List;

/**
 * Controller for managing Feedback Questions Associations.
 * Provides an endpoint for retrieving feedback questions by feedback ID.
 * <p>
 * Relies on {@link FeedbackQuestionsAssociationService} to execute business logic, 
 * while handling error logging and exceptions for resource management.
 * </p>
 *
 * @version MFMS_0.0.1
 * @author Chatla Sarika
 */

@RestController
@RequestMapping("/api/FeedbackQuestions")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})
public class FeedbackQuestionsAssociationController {

    private static final Logger logger = LogManager.getLogger(FeedbackQuestionsAssociationController.class);

    @Autowired
    private FeedbackQuestionsAssociationService feedbackQuestionsAssociationService;

    /**
     * Endpoint to retrieve feedback questions by feedback ID.
     *
     * @param feedbackId The ID of the feedback.
     * @return A ResponseEntity containing a list of FeedbackQuestionDTOs or an error message.
     * @throws ResourceUnableToCreate if feedbackId is null.
     */
    @GetMapping("/feedback/questions/{feedbackId}")
    public ResponseEntity<List<FeedbackQuestionDTO>> getFeedbackQuestionsByFeedbackId(@PathVariable Integer feedbackId) throws ResourceUnableToCreate {
        logger.info("Received request to get feedback questions for feedback ID: " + feedbackId);
        
        // Validate feedbackId: cannot be null
        if (feedbackId == null) {
            logger.error("Feedback ID is null.");
            throw new ResourceUnableToCreate("Feedback ID cannot be null.", "", "");
        }

        try {
            logger.info("Attempting to retrieve feedback questions for feedback ID: " + feedbackId);
            List<FeedbackQuestionDTO> feedbackQuestions = feedbackQuestionsAssociationService.getFeedbackQuestionsByFeedbackId(feedbackId);
            logger.info("Successfully retrieved " + feedbackQuestions.size() + " feedback questions for feedback ID: " + feedbackId);
            return new ResponseEntity<>(feedbackQuestions, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Feedback questions not found for feedback ID: " + feedbackId, e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) { // Catch unexpected runtime exceptions
            logger.error("Unexpected error occurred while retrieving feedback questions for feedback ID: " + feedbackId, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
