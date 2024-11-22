package com.payswiff.mfmsproject.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class to handle operations related to Feedback-Question Associations.
 * @author Chatla Sarika
 * @version MFMS_0.0.1
 */
@Service
public class FeedbackQuestionsAssociationService {

    // Logger initialization for the class
    private static final Logger logger = LogManager.getLogger(FeedbackQuestionsAssociationService.class);

    private final FeedbackQuestionsAssociationRepository associationRepository;
    private final FeedbackRepository feedbackRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public FeedbackQuestionsAssociationService(
            FeedbackQuestionsAssociationRepository associationRepository,
            FeedbackRepository feedbackRepository,
            QuestionRepository questionRepository) {
        this.associationRepository = associationRepository;
        this.feedbackRepository = feedbackRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Creates a new FeedbackQuestionsAssociation.
     *
     * @param request The request object containing necessary data.
     * @return The created FeedbackQuestionsAssociation.
     * @throws ResourceNotFoundException if the feedback or question does not exist.
     * @throws ResourceUnableToCreate if the request object or its fields are invalid.
     */
    public FeedbackQuestionsAssociation createAssociation(FeedbackQuestionsAssociation request) 
            throws ResourceNotFoundException, ResourceUnableToCreate {

        // Validate that the request is not null
        if (request == null) {
            logger.error("Request object is null.");
            throw new ResourceUnableToCreate("FeedbackQuestionsAssociation", "Request object cannot be null", "Null or Empty");
        }

        // Validate feedback existence
        if (request.getFeedback() == null || request.getFeedback().getFeedbackId() == null) {
            logger.error("Feedback or Feedback ID is null.");
            throw new ResourceUnableToCreate("FeedbackQuestionsAssociation", "Feedback and its ID cannot be null or empty", "Null or Empty");
        }

        Feedback feedback = feedbackRepository.findById(request.getFeedback().getFeedbackId())
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", "ID", String.valueOf(request.getFeedback().getFeedbackId())));
        
        logger.info("Found feedback with ID: " + feedback.getFeedbackId());

        // Validate question existence
        if (request.getQuestion() == null || request.getQuestion().getQuestionId() == null) {
            logger.error("Question or Question ID is null.");
            throw new ResourceUnableToCreate("FeedbackQuestionsAssociation", "Question and its ID cannot be null or empty", "Null or Empty");
        }

        Question question = questionRepository.findById(request.getQuestion().getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question", "ID", String.valueOf(request.getQuestion().getQuestionId())));
        
        logger.info("Found question with ID: " + question.getQuestionId());

        // Validate answer
        if (request.getAnswer() == null || request.getAnswer().trim().isEmpty()) {
            logger.error("Answer is null or empty.");
            throw new ResourceUnableToCreate("FeedbackQuestionsAssociation", "Answer cannot be null or empty", "Null or Empty");
        }

        // Create the association
        ModelMapper modelMapper = new ModelMapper();
        FeedbackQuestionsAssociation association = modelMapper.map(request, FeedbackQuestionsAssociation.class);
        association.setFeedback(feedback);  // Set the feedback object
        association.setQuestion(question);    // Set the question object
        association.setAnswer(request.getAnswer()); // Set the answer from the request
        
        try {
            logger.info("Saving the association for feedback ID: " + feedback.getFeedbackId() + " and question ID: " + question.getQuestionId());
            return associationRepository.save(association); // Save the association
        } catch (Exception e) {
            logger.error("Error occurred while saving FeedbackQuestionsAssociation", e);
            throw new ResourceUnableToCreate("FeedbackQuestionAssociation", "feedback and question", "internal error");
        }
    }

    /**
     * Retrieves a list of FeedbackQuestionDTOs by feedback ID.
     *
     * @param feedbackId The ID of the feedback for which to retrieve associations.
     * @return A list of FeedbackQuestionDTOs associated with the given feedback ID.
     * @throws ResourceNotFoundException if the feedback or its associations are not found.
     * @throws ResourceUnableToCreate if the feedback ID is invalid.
     */
    public List<FeedbackQuestionDTO> getFeedbackQuestionsByFeedbackId(Integer feedbackId) 
            throws ResourceNotFoundException, ResourceUnableToCreate {

        // Validate feedback ID
        if (feedbackId == null) {
            logger.error("Feedback ID is null.");
            throw new ResourceUnableToCreate("FeedbackQuestionsAssociation", "Feedback ID cannot be null", "Null");
        }

        // Retrieve feedback by ID
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", "ID", feedbackId.toString()));
        
        logger.info("Found feedback with ID: " + feedbackId);

        // Fetch associations using the feedback object
        List<FeedbackQuestionsAssociation> associations = associationRepository.findByFeedback(feedback);

        if (associations.isEmpty()) {
            logger.error("No associations found for feedback ID: " + feedbackId);
            throw new ResourceNotFoundException("FeedbackQuestionsAssociation", "Feedback ID", feedbackId.toString());
        }

        // Map to DTOs
        logger.info("Successfully retrieved " + associations.size() + " associations for feedback ID: " + feedbackId);
        return associations.stream()
                .map(association -> new FeedbackQuestionDTO(
                        association.getQuestion().getQuestionId(),
                        association.getQuestion().getQuestionDescription(),
                        association.getAnswer()))
                .collect(Collectors.toList());
    }
}
