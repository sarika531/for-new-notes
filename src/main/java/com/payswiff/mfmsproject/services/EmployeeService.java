package com.payswiff.mfmsproject.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.payswiff.mfmsproject.dtos.EmailSendDto;
import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.exceptions.UnableSentEmail;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.models.Role;
import com.payswiff.mfmsproject.repositories.EmployeeRepository;
import com.payswiff.mfmsproject.repositories.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service layer for handling business logic related to Employee operations, including:
 * <ul>
 *   <li>Creating new employees with validation and email notification</li>
 *   <li>Retrieving employee details by various identifiers</li>
 *   <li>Checking existence of employees by ID</li>
 *   <li>Updating employee passwords securely</li>
 *   <li>Fetching all employees from the database</li>
 * </ul>
 * 
 * This service interacts with the {@link EmployeeRepository} and {@link RoleRepository}
 * to access and manage employee and role data, and leverages {@link EmailService} for
 * sending notifications. All critical operations are logged for success and error tracking.
 * @author Reavnth K
 * @version MFMS_0.0.1
 */
@Service
public class EmployeeService {

    private static final Logger logger = LogManager.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Saves a new Employee after performing checks for existing data and validating
     * required fields. Sends an email notification upon successful creation.
     *
     * @param employee The employee entity to be saved.
     * @return The saved Employee object.
     * @throws ResourceAlreadyExists If an employee with the same Payswiff ID, email, or phone number already exists.
     * @throws ResourceNotFoundException If the specified role does not exist.
     * @throws UnableSentEmail If there is an issue sending the email.
     * @throws ResourceUnableToCreate If the employee object is null or lacks required fields.
     */
    public Employee saveEmployee(Employee employee) 
            throws ResourceAlreadyExists, ResourceNotFoundException, UnableSentEmail, ResourceUnableToCreate {
        logger.info("Starting employee creation process");

        // Validate Employee object
        if (employee == null) {
            logger.error("Employee creation failed: Employee object is null");
            throw new ResourceUnableToCreate("Employee", "Object", "Employee object cannot be null");
        }

        // Validate required fields
        if (employee.getEmployeePayswiffId() == null || employee.getEmployeePayswiffId().isEmpty()) {
            logger.error("Employee creation failed: Payswiff ID is null or empty");
            throw new ResourceUnableToCreate("Employee", "Payswiff ID", "Payswiff ID cannot be null or empty");
        }

        if (employee.getEmployeeEmail() == null || employee.getEmployeeEmail().isEmpty()) {
            logger.error("Employee creation failed: Email is null or empty");
            throw new ResourceUnableToCreate("Employee", "Email", "Email cannot be null or empty");
        }

        if (employee.getEmployeePassword() == null || employee.getEmployeePassword().isEmpty()) {
            logger.error("Employee creation failed: Password is null or empty");
            throw new ResourceUnableToCreate("Employee", "Password", "Password cannot be null or empty");
        }

        if (employee.getEmployeeType() == null) {
            logger.error("Employee creation failed: Employee Type is null");
            throw new ResourceUnableToCreate("Employee", "Employee Type", "Employee type cannot be null");
        }

        // Check for existing employee with the same Payswiff ID
        if (employeeRepository.findByEmployeePayswiffId(employee.getEmployeePayswiffId()).isPresent()) {
            logger.error("Employee creation failed: Employee with Payswiff ID {} already exists", employee.getEmployeePayswiffId());
            throw new ResourceAlreadyExists("Employee", "Payswiff ID", employee.getEmployeePayswiffId());
        }

        // Check for existing employee with the same email
        if (employeeRepository.findByEmployeeEmail(employee.getEmployeeEmail()).isPresent()) {
            logger.error("Employee creation failed: Employee with Email {} already exists", employee.getEmployeeEmail());
            throw new ResourceAlreadyExists("Employee", "Email", employee.getEmployeeEmail());
        }

        // Check for existing employee with the same phone number
        if (employee.getEmployeePhoneNumber() != null
                && employeeRepository.findByEmployeePhoneNumber(employee.getEmployeePhoneNumber()).isPresent()) {
            logger.error("Employee creation failed: Employee with Phone Number {} already exists", employee.getEmployeePhoneNumber());
            throw new ResourceAlreadyExists("Employee", "Phone Number", employee.getEmployeePhoneNumber());
        }

        // Encrypt the employee password before saving
        String password = employee.getEmployeePassword();
        employee.setEmployeePassword(passwordEncoder.encode(password));

        Set<Role> roles = new HashSet<>();
        Role employeeRole;

        // Check the employee type and assign the correct role
        if ("admin".equalsIgnoreCase(employee.getEmployeeType().name())) {
            employeeRole = roleRepository.findByName("ROLE_admin")
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "Name", "ROLE_admin"));
        } else {
            employeeRole = roleRepository.findByName("ROLE_employee")
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "Name", "ROLE_employee"));
        }
        roles.add(employeeRole);
        employee.setRoles(roles);

        // Save the employee in the database
        Employee createdEmployee = employeeRepository.save(employee);
        logger.info("Employee created successfully with ID: {}", createdEmployee.getEmployeeId());

        // Prepare and send email
        EmailSendDto emailSendDto = new EmailSendDto();
        emailSendDto.setTo(employee.getEmployeeEmail());
        emailSendDto.setSubject("Merchant Feedback Management System");
        emailSendDto.setText("Your Account has been created successfully.\nYour Login Credentials are:\n"
                + "Email: " + employee.getEmployeeEmail() + "\n" + "Password: " + password);

        if (!emailService.sendEmail(emailSendDto.getTo(), emailSendDto.getSubject(), emailSendDto.getText())) {
            logger.error("Failed to send email to: {}", employee.getEmployeeEmail());
            throw new UnableSentEmail(employee.getEmployeeEmail());
        }

        logger.info("Email sent successfully to {}", employee.getEmployeeEmail());
        return createdEmployee;
    }

    /**
     * Retrieves an Employee by Payswiff ID, phone number, or email.
     *
     * @param payswiffId  The Payswiff ID of the employee to retrieve.
     * @param phoneNumber The phone number of the employee to retrieve.
     * @param email       The email of the employee to retrieve.
     * @return The Employee object if found.
     * @throws ResourceNotFoundException If no employee is found with the provided details.
     */
    public Employee getEmployee(String payswiffId, String phoneNumber, String email) throws ResourceNotFoundException {
        logger.info("Retrieving employee with Payswiff ID: {}, Phone: {}, Email: {}", payswiffId, phoneNumber, email);

        if (payswiffId != null) {
            return employeeRepository.findByEmployeePayswiffId(payswiffId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "Payswiff ID", payswiffId));
        }
        if (phoneNumber != null) {
            return employeeRepository.findByEmployeePhoneNumber(phoneNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "Phone Number", phoneNumber));
        }
        if (email != null) {
            return employeeRepository.findByEmployeeEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "Email", email));
        }

        logger.error("Employee retrieval failed: No parameters provided");
        throw new ResourceNotFoundException("Employee", "Parameters", "None provided");
    }

    /**
     * Checks if an employee exists by its ID.
     *
     * @param employeeId The ID of the employee.
     * @return true if an employee with the given ID exists, false otherwise.
     */
    public boolean existsById(Long employeeId) {
        logger.debug("Checking existence of employee with ID: {}", employeeId);
        return employeeId != null && employeeRepository.existsById(employeeId);
    }

    /**
     * Updates the password of an employee identified by their email.
     *
     * @param email       The email of the employee whose password is to be updated.
     * @param newPassword The new password to be set.
     * @return true if the password was updated successfully.
     * @throws ResourceNotFoundException If no employee is found with the given email.
     */
    public boolean updateEmployeePassword(String email, String newPassword) throws ResourceNotFoundException {
        logger.info("Updating password for employee with email: {}", email);
        Employee employee = employeeRepository.findByEmployeeEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "Email", email));

        employee.setEmployeePassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
        logger.info("Password updated successfully for employee with email: {}", email);

        return true;
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return A list of all Employee objects.
     */
    public List<Employee> getAllEmployees() {
        logger.info("Retrieving all employees from the database");
        return employeeRepository.findAll();
    }
}
