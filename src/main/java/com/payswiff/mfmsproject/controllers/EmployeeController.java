package com.payswiff.mfmsproject.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.exceptions.UnableSentEmail;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.reuquests.CreateEmployeeRequest;
import com.payswiff.mfmsproject.services.EmployeeService;

import jakarta.validation.Valid;

/**
 * REST controller for managing employee-related operations including creating
 * new employees and retrieving employee details by various identifiers.
 * 
 * This controller provides endpoints for:
 * <ul>
 *   <li>Creating a new employee with unique Payswiff ID, email, and phone number.</li>
 *   <li>Retrieving employees by Payswiff ID, phone number, or email.</li>
 *   <li>Listing all employees.</li>
 * </ul>
 * 
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link EmployeeService}: Service layer responsible for employee business logic.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Exception Handling:
 * <ul>
 *   <li>{@link ResourceAlreadyExists}: Thrown when attempting to create an employee with duplicate information.</li>
 *   <li>{@link ResourceNotFoundException}: Thrown when the requested employee cannot be found.</li>
 *   <li>{@link UnableSentEmail}: Thrown if there is an issue sending email notifications.</li>
 *   <li>{@link ResourceUnableToCreate}: Thrown if a request is invalid or an error occurs during creation.</li>
 * </ul>
 * </p>
 * 
 * Logging:
 * Detailed logging is implemented for tracing the flow of requests and troubleshooting.
 * </p>
 * @version MFMS_0.0.1
 * @author Revanth K
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.2.7:5173"})
public class EmployeeController {

    private static final Logger logger = LogManager.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * Creates a new Employee after validating that no existing employee has the
     * same Payswiff ID, email, or phone number.
     *
     * @param request The request DTO containing employee details.
     * @return ResponseEntity containing the created employee.
     * @throws ResourceAlreadyExists If an employee with the same Payswiff ID, email, or phone exists.
     * @throws ResourceNotFoundException If a related resource is not found.
     * @throws UnableSentEmail If there is an issue with sending an email notification.
     * @throws ResourceUnableToCreate If the request is null or other creation errors occur.
     */
    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid CreateEmployeeRequest request) 
            throws ResourceAlreadyExists, ResourceNotFoundException, UnableSentEmail, ResourceUnableToCreate {
        
        logger.info("Received request to create a new employee");

        // Check if the request is null
        if (request == null) {
            logger.error("Received null request for employee creation");
            throw new ResourceUnableToCreate("Null Request: Employee cannot be created", null, null);
        }
        
        // Convert CreateEmployeeRequest to Employee model
        logger.debug("Converting CreateEmployeeRequest to Employee model");
        Employee employee = request.toEmployee();
        
        // Save the employee using the service and return the created employee
        logger.info("Attempting to save the new employee: {}", employee);
        Employee createdEmployee = employeeService.saveEmployee(employee);
        logger.info("Employee created successfully with ID: {}", createdEmployee.getEmployeeId());
        
        // Return response with created employee and 201 Created status
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    /**
     * Retrieves an Employee by Payswiff ID, phone number, or email.
     *
     * @param payswiffId The Payswiff ID of the employee to retrieve (optional).
     * @param phoneNumber The phone number of the employee to retrieve (optional).
     * @param email The email of the employee to retrieve (optional).
     * @return ResponseEntity containing the found employee.
     * @throws ResourceNotFoundException If no employee is found with the provided details.
     */
    @GetMapping("/get")
    public ResponseEntity<Employee> getEmployee(
            @RequestParam(required = false) String payswiffId, 
            @RequestParam(required = false) String phoneNumber, 
            @RequestParam(required = false) String email) 
            throws ResourceNotFoundException, ResourceUnableToCreate {

        logger.info("Received request to retrieve employee with Payswiff ID: {}, Phone: {}, Email: {}", 
                    payswiffId, phoneNumber, email);

        // Check if all parameters are null or empty
        if ((payswiffId == null || payswiffId.isEmpty()) &&
            (phoneNumber == null || phoneNumber.isEmpty()) &&
            (email == null || email.isEmpty())) {
            
            logger.error("Invalid request: At least one parameter must be provided to retrieve employee details.");
            throw new ResourceNotFoundException("Unable to get the resource Employee. At least one parameter must be provided.", null, null);
        }
        
        // Call the service method to retrieve the employee based on provided parameters
        logger.debug("Attempting to retrieve employee using provided identifiers");
        Employee employee = employeeService.getEmployee(payswiffId, phoneNumber, email);
        
        logger.info("Employee retrieved successfully with ID: {}", employee.getEmployeeId());
        
        // Return the found employee with 200 OK status
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    /**
     * Retrieves all Employees.
     *
     * @return ResponseEntity containing a list of all Employee entities.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Received request to retrieve all employees");

        // Call the service method to retrieve all employees
        List<Employee> employees = employeeService.getAllEmployees();

        logger.info("Retrieved all employees, total count: {}", employees.size());

        // Return the list of employees with 200 OK status
        return ResponseEntity.ok(employees);
    }
}
