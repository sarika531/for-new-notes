package com.payswiff.mfmsproject.controllers;

import java.util.List; // Importing List for handling collections of Question objects.

import org.apache.logging.log4j.LogManager; // Importing LogManager for creating logger.
import org.apache.logging.log4j.Logger; // Importing Logger.
import org.springframework.beans.factory.annotation.Autowired; // Importing annotation for dependency injection.
import org.springframework.http.HttpStatus; // Importing HTTP status codes for responses.
import org.springframework.http.ResponseEntity; // Importing ResponseEntity for building HTTP responses.
import org.springframework.web.bind.annotation.*; // Importing Spring MVC annotations for RESTful web services.

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists; // Importing exception for handling existing resource scenarios.
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException; // Importing exception for handling resource not found errors.
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate; // Importing exception for handling creation errors.
import com.payswiff.mfmsproject.models.Question; // Importing Question model for handling question data.
import com.payswiff.mfmsproject.reuquests.CreateQuestionRequest; // Importing request object for creating questions.
import com.payswiff.mfmsproject.services.QuestionService; // Importing service layer for question-related logic.

/**
 * QuestionController handles requests related to questions, including creation, retrieval, 
 * and listing of questions in the application.
 * <p>
 * Provides the following endpoints:
 * - Creating a new question
 * - Retrieving a question by its ID
 * - Retrieving a question by its description
 * - Retrieving all questions
 * </p>
 * 
 * This controller relies on {@link QuestionService} for handling the business logic related to 
 * question management.
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})
public class QuestionController {

    private static final Logger logger = LogManager.getLogger(QuestionController.class); // Initializing logger

    @Autowired
    private QuestionService questionService; // Injecting the QuestionService for business logic

    /**
     * Creates a new Question based on the provided request details.
     *
     * @param request The request containing the details of the question to be created.
     * @return ResponseEntity containing the created Question and an HTTP status code.
     * @throws ResourceAlreadyExists If a question with the same identifier already exists.
     * @throws ResourceUnableToCreate If there is an error during the creation of the question.
     */
    @PostMapping("/create")
    public ResponseEntity<Question> createQuestion(@RequestBody CreateQuestionRequest request) 
            throws ResourceAlreadyExists, ResourceUnableToCreate {

        logger.info("Attempting to create a new question with details: {}", request); // Logging info level message

        // Convert the CreateQuestionRequest to a Question entity using the service
        Question createdQuestion = questionService.saveQuestion(request.toQuestion());
        
        logger.info("Question created successfully with ID: {}", createdQuestion.getQuestionId()); // Log success message
        
        // Return a ResponseEntity with the created Question and a status of 201 Created
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    /**
     * Retrieves a Question by its ID.
     * 
     * @param id The ID of the question to retrieve.
     * @return ResponseEntity containing the found Question.
     * @throws ResourceNotFoundException if no question with the specified ID is found.
     */
    @GetMapping("/get")
    public ResponseEntity<Question> getQuestionById(@RequestParam("id") Long id) 
            throws ResourceNotFoundException {
        
        logger.info("Fetching question with ID: {}", id); // Log ID being fetched

        // Retrieve the question by ID using the service layer
        Question question = questionService.getQuestionById(id);

        logger.info("Question retrieved successfully: {}", question); // Log success message
        return new ResponseEntity<>(question, HttpStatus.OK); // Return the found question with HTTP 200 OK status
    }

    /**
     * Retrieves a Question by its description.
     * 
     * @param description The description of the question to retrieve.
     * @return ResponseEntity containing the found Question.
     * @throws ResourceNotFoundException if no question with the specified description is found.
     */
    @GetMapping("/getbydesc")
    public ResponseEntity<Question> getQuestionByDescription(@RequestParam("description") String description) 
            throws ResourceNotFoundException {
        
        logger.info("Fetching question with description: {}", description); // Log description being fetched

        // Retrieve the question by description using the service layer
        Question question = questionService.getQuestionByDescription(description);
        
        logger.info("Question retrieved successfully: {}", question); // Log success message
        return new ResponseEntity<>(question, HttpStatus.OK); // Return the found question with HTTP 200 OK status
    }

    /**
     * Retrieves all questions.
     *
     * @return ResponseEntity containing a list of all Question entities.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        logger.info("Fetching all questions"); // Log the request to fetch all questions

        // Retrieve the list of all questions from the service layer
        List<Question> questions = questionService.getAllQuestions();
        
        logger.info("Total questions retrieved: {}", questions.size()); // Log the number of questions retrieved
        return ResponseEntity.ok(questions); // Returns a 200 OK response with the list of questions
    }
}
