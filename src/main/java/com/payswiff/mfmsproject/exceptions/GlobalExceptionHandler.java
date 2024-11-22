package com.payswiff.mfmsproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.payswiff.mfmsproject.models.ErrorDetails;

import java.util.Date;

/**
 * GlobalExceptionHandler is a central exception handler that handles various types of exceptions thrown
 * during the execution of the application and provides structured error responses.
 * <p>
 * This class uses the @ControllerAdvice annotation to globally handle exceptions and map them to appropriate
 * HTTP responses. The exception handlers return detailed error information, including timestamps, error messages,
 * and HTTP status codes.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException.
     * 
     * @param ex      The thrown ResourceNotFoundException.
     * @param request The current web request.
     * @return A ResponseEntity containing the error details and an HTTP status code.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(), 
            ex.getMessage(),
            String.valueOf(HttpStatus.NOT_FOUND),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ResourceAlreadyExists exception.
     * 
     * @param ex      The thrown ResourceAlreadyExists exception.
     * @param request The current web request.
     * @return A ResponseEntity containing the error details and an HTTP status code.
     */
    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ErrorDetails> handleResourceAlreadyExists(
            ResourceAlreadyExists ex, WebRequest request) {
        
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(), 
            ex.getMessage(),
            String.valueOf(HttpStatus.CONFLICT),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * Handles ResourceUnableToCreate exception.
     * 
     * @param ex      The thrown ResourceUnableToCreate exception.
     * @param request The current web request.
     * @return A ResponseEntity containing the error details and an HTTP status code.
     */
    @ExceptionHandler(ResourceUnableToCreate.class)
    public ResponseEntity<ErrorDetails> handleResourceUnableToCreateException(
            ResourceUnableToCreate ex, WebRequest request) {
        
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(), 
            ex.getMessage(),
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles any unhandled exceptions in the application.
     * 
     * @param ex      The thrown generic exception.
     * @param request The current web request.
     * @return A ResponseEntity containing the error details and an HTTP status code.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(), 
            ex.getMessage(),
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Handles MerchantDeviceNotAssignedException.
     * 
     * @param e      The thrown MerchantDeviceNotAssignedException.
     * @param request The current web request.
     * @return A ResponseEntity containing the error details and an HTTP status code.
     */
    @ExceptionHandler(MerchantDeviceNotAssignedException.class)
    public ResponseEntity<ErrorDetails> handleMerchnatDeviceNotAssignedException(MerchantDeviceNotAssignedException e,
            WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(), 
            e.getMessage(),
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles EmployeePasswordUpdationFailedException.
     * 
     * @param e      The thrown EmployeePasswordUpdationFailedException.
     * @param request The current web request.
     * @return A ResponseEntity containing the error details and an HTTP status code.
     */
    @ExceptionHandler(EmployeePasswordUpdationFailedException.class)
    public ResponseEntity<ErrorDetails> handleEmployeePasswordUpdationFailedException(EmployeePasswordUpdationFailedException e,
            WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(), 
            e.getMessage(),
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles UnableSentEmail exceptions.
     * 
     * @param e      The thrown UnableSentEmail exception.
     * @param request The current web request.
     * @return A ResponseEntity containing the error details and an HTTP status code.
     */
    @ExceptionHandler(UnableSentEmail.class)
    public ResponseEntity<ErrorDetails> handleUnableSentEmail(UnableSentEmail e,
            WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(), 
            e.getMessage(),
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
