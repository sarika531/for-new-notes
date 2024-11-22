package com.payswiff.mfmsproject.services;

import org.apache.logging.log4j.LogManager; // Importing LogManager for creating logger
import org.apache.logging.log4j.Logger; // Importing Logger for logging

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;  // Assuming you have this exception
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Question;
import com.payswiff.mfmsproject.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;
/**
 * Service class for handling operations related to Questions.
 * <p>This service provides methods for saving, retrieving, and managing question data within the application. 
 * It includes operations for adding new questions, fetching questions by ID or description, 
 * and retrieving all questions from the repository. The service performs validations 
 * to ensure data integrity, such as checking for existing questions with the same description.</p>
 * 
 * <p>It also logs important events and errors during the execution of these operations.</p>
 * 
 * @version MFMS_0.0.1
 * @author Revanth K
 */
@Service
public class QuestionService {

    private static final Logger logger = LogManager.getLogger(QuestionService.class); // Initializing logger

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Saves a question to the repository.
     *
     * @param question The question object to be saved.
     * @return The saved question object.
     * @throws ResourceAlreadyExists If a question with the same description already exists.
     * @throws ResourceUnableToCreate If the question description is empty or if an error occurs during saving.
     */
    public Question saveQuestion(Question question) throws ResourceAlreadyExists, ResourceUnableToCreate {
        logger.info("Attempting to save a new question: {}", question); // Log the attempt to save

        // Validate the question description
        if (question.getQuestionDescription() == null || question.getQuestionDescription().trim().isEmpty()) {
            logger.error("Failed to save question - Description is empty."); // Log failure for empty description
            throw new ResourceUnableToCreate("Question", "Description cannot be empty", "Empty Description");
        }

        if (question.getQuestionDescription().length() == 0) {
            logger.error("Failed to save question - Description is empty."); // Log failure for empty description
            throw new ResourceUnableToCreate("Question", "Description cannot be empty", "Empty Description");
        }

        // Check for existing question with the same description
        Optional<Question> existingQuestion = questionRepository.findByDescription(question.getQuestionDescription());

        if (existingQuestion.isPresent()) {
            logger.error("Failed to save question - Question with description '{}' already exists.", 
                         question.getQuestionDescription()); // Log failure for duplicate description
            throw new ResourceAlreadyExists("Question", "Description", question.getQuestionDescription());
        }

        try {
            // Attempt to save the question and return the saved question
            Question savedQuestion = questionRepository.save(question);
            logger.info("Question saved successfully with ID: {}", savedQuestion.getQuestionId()); // Log success
            return savedQuestion;
        } catch (Exception e) {
            // If any error occurs during saving, throw ResourceUnableToCreate
            logger.error("Failed to save question due to an error: {}", e.getMessage()); // Log save failure
            throw new ResourceUnableToCreate("Question", "An error occurred while saving the question: ", e.getMessage());
        }
    }

    /**
     * Retrieves a Question by its ID.
     * 
     * @param id The ID of the question to retrieve.
     * @return The found Question object.
     * @throws ResourceNotFoundException If no question with the specified ID is found.
     */
    public Question getQuestionById(Long id) throws ResourceNotFoundException {
        logger.info("Attempting to retrieve question by ID: {}", id); // Log retrieval attempt by ID
        return questionRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("No question found with ID: {}", id); // Log failure to find question
                return new ResourceNotFoundException("Question", "ID", String.valueOf(id));
            });
    }

    /**
     * Retrieves a Question by its description.
     * 
     * @param description The description of the question to retrieve.
     * @return The found Question object.
     * @throws ResourceNotFoundException If no question with the specified description is found.
     */
    public Question getQuestionByDescription(String description) throws ResourceNotFoundException {
        logger.info("Attempting to retrieve question by description: {}", description); // Log retrieval attempt by description
        return questionRepository.findByDescription(description)
            .orElseThrow(() -> {
                logger.error("No question found with description: {}", description); // Log failure to find question
                return new ResourceNotFoundException("Question", "Description", description);
            });
    }

    /**
     * Retrieves all questions from the database.
     *
     * @return List of all Question entities.
     */
    public List<Question> getAllQuestions() {
        logger.info("Attempting to retrieve all questions."); // Log the attempt to fetch all questions
        List<Question> questions = questionRepository.findAll();
        logger.info("Successfully retrieved all questions. Total count: {}", questions.size()); // Log success with count
        return questions; // Returns all questions from the repository
    }
}
