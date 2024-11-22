package com.payswiff.mfmsproject.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.payswiff.mfmsproject.dtos.EmailSendDto;
import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.exceptions.UnableSentEmail;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.models.Role;
import com.payswiff.mfmsproject.repositories.EmployeeRepository;
import com.payswiff.mfmsproject.repositories.RoleRepository;

import java.util.*;

/**
 * Unit tests for the EmployeeService class.
 * This class contains test cases that verify the behavior of methods in the EmployeeService,
 * ensuring correct functionality when saving, retrieving, updating, and checking employees.
 * Mocking is used for dependencies to isolate the service layer from the persistence layer.
 */
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository; // Mock for employee repository dependency.

    @Mock
    private RoleRepository roleRepository; // Mock for role repository dependency.

    @Mock
    private EmailService emailService; // Mock for email service dependency.

    @Mock
    private PasswordEncoder passwordEncoder; // Mock for password encoder dependency.

    @InjectMocks
    private EmployeeService employeeService; // The service under test, with mocked dependencies injected.

    private AutoCloseable closeable; // Used for cleaning up mocks after each test.

    /**
     * Sets up the test environment by initializing mocks.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this); // Initialize all mocks before each test.
    }

    /**
     * Cleans up the mocks after each test to prevent interference between tests.
     */
    @AfterEach
    void close() throws Exception {
        closeable.close(); // Close all mocks after each test.
    }

    /**
     * Tests the successful creation of an employee.
     * Verifies that the employee is saved correctly and an email is sent.
     */
    @Test
    void testSaveEmployee_Success() throws Exception {
        // Create a sample employee object for testing
        Employee employee = new Employee("12345", "test@example.com", "password", "admin", null);
        
        // Set up mock behavior for repository calls
        when(employeeRepository.findByEmployeePayswiffId(employee.getEmployeePayswiffId())).thenReturn(Optional.empty());
        when(employeeRepository.findByEmployeeEmail(employee.getEmployeeEmail())).thenReturn(Optional.empty());
        when(employeeRepository.findByEmployeePhoneNumber(employee.getEmployeePhoneNumber())).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_admin")).thenReturn(Optional.of(new Role(null, "ROLE_admin")));
        
        // Set up password encoding and saving behavior
        when(passwordEncoder.encode(employee.getEmployeePassword())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee); // Mock save behavior
        when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true); // Mock email sending
        
        // Call the method under test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        
        // Assert the saved employee is not null and has the expected email
        assertNotNull(savedEmployee); 
        assertEquals(employee.getEmployeeEmail(), savedEmployee.getEmployeeEmail());
        
        // Verify that the save and email send methods were called
        verify(employeeRepository).save(any(Employee.class)); 
        verify(emailService).sendEmail(anyString(), anyString(), anyString()); 
    }

    /**
     * Tests the behavior when trying to save a null employee.
     * Asserts that a ResourceUnableToCreate exception is thrown.
     */
    @Test
    void testSaveEmployee_ThrowsResourceUnableToCreate_WhenEmployeeIsNull() {
        // Assert that saving a null employee throws the expected exception
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            employeeService.saveEmployee(null); // Attempt to save null employee
        });
        // Check the exception message
        assertEquals("Employee with Object: Employee object cannot be null is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Tests the behavior when trying to save an employee with an empty Payswiff ID.
     * Asserts that a ResourceUnableToCreate exception is thrown.
     */
    @Test
    void testSaveEmployee_ThrowsResourceUnableToCreate_WhenPayswiffIdIsEmpty() {
        // Create an employee with an empty Payswiff ID
        Employee employee = new Employee(null, "", "test@example.com", "password", "admin", null, null, null, null, null, null, null);
        
        // Assert that the expected exception is thrown
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            employeeService.saveEmployee(employee); // Attempt to save the employee
        });
        // Check the exception message
        assertEquals("Employee with Password: Password cannot be null or empty is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Tests the behavior when trying to save an employee with an empty email.
     * Asserts that a ResourceUnableToCreate exception is thrown.
     */
    @Test
    void testSaveEmployee_ThrowsResourceUnableToCreate_WhenEmailIsEmpty() {
        // Create an employee with an empty email
        Employee employee = new Employee(null, "12345", "", "password", "admin", null, null, null, null, null, null, null);
        
        // Assert that the expected exception is thrown
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            employeeService.saveEmployee(employee); // Attempt to save the employee
        });
        // Check the exception message
        assertEquals("Employee with Payswiff ID: Payswiff ID cannot be null or empty is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Tests the behavior when trying to save an employee with an empty password.
     * Asserts that a ResourceUnableToCreate exception is thrown.
     */
    @Test
    void testSaveEmployee_ThrowsResourceUnableToCreate_WhenPasswordIsEmpty() {
        // Create an employee with an empty password
        Employee employee = new Employee(null, "12345", "test@example.com", "", "admin", null, null, null, null, null, null, null);
        
        // Assert that the expected exception is thrown
        Exception exception = assertThrows(ResourceUnableToCreate.class, () -> {
            employeeService.saveEmployee(employee); // Attempt to save the employee
        });
        // Check the exception message
        assertEquals("Employee with Password: Password cannot be null or empty is unable to create at this moment!", exception.getMessage());
    }

    /**
     * Tests the behavior when trying to save an employee with an existing Payswiff ID.
     * Asserts that a ResourceAlreadyExists exception is thrown.
     */
    @Test
    void testSaveEmployee_ThrowsResourceAlreadyExists_WhenPayswiffIdExists() {
        // Create an employee object for testing
        Employee employee = new Employee("12345", "test@example.com", "password", "admin", null);
        
        // Set up mock behavior to simulate existing Payswiff ID
        when(employeeRepository.findByEmployeePayswiffId(employee.getEmployeePayswiffId())).thenReturn(Optional.of(employee));

        // Assert that the expected exception is thrown
        Exception exception = assertThrows(ResourceAlreadyExists.class, () -> {
            employeeService.saveEmployee(employee); // Attempt to save the employee
        });
        // Check the exception message
        assertEquals("Employee with Payswiff ID: 12345 already exists.", exception.getMessage());
    }

    /**
     * Tests the behavior when trying to save an employee with an existing email.
     * Asserts that a ResourceAlreadyExists exception is thrown.
     */
    @Test
    void testSaveEmployee_ThrowsResourceAlreadyExists_WhenEmailExists() {
        // Create an employee object for testing
        Employee employee = new Employee("12345", "test@example.com", "password", "admin",null);
        
        // Set up mock behavior to simulate existing email
        when(employeeRepository.findByEmployeeEmail(employee.getEmployeeEmail())).thenReturn(Optional.of(employee));

        // Assert that the expected exception is thrown
        Exception exception = assertThrows(ResourceAlreadyExists.class, () -> {
            employeeService.saveEmployee(employee); // Attempt to save the employee
        });
        // Check the exception message
        assertEquals("Employee with Email: test@example.com already exists.", exception.getMessage());
    }

    /**
     * Tests the behavior when trying to save an employee with an existing phone number.
     * Asserts that a ResourceAlreadyExists exception is thrown.
     */
    @Test
    void testSaveEmployee_ThrowsResourceAlreadyExists_WhenPhoneNumberExists() {
        // Create an employee object for testing
        Employee employee = new Employee("12345", "test@example.com", "password", "admin", "1234567890");
        
        // Set up mock behavior to simulate existing phone number
        when(employeeRepository.findByEmployeePhoneNumber(employee.getEmployeePhoneNumber())).thenReturn(Optional.of(employee));

        // Assert that the expected exception is thrown
        Exception exception = assertThrows(ResourceAlreadyExists.class, () -> {
            employeeService.saveEmployee(employee); // Attempt to save the employee
        });
        // Check the exception message
        assertEquals("Employee with Phone Number: 1234567890 already exists.", exception.getMessage());
    }

    /**
     * Tests the successful retrieval of an employee by Payswiff ID.
     * Asserts that the retrieved employee matches the expected employee.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetEmployeeByPayswiffId_Success() throws ResourceNotFoundException {
        // Create a sample employee object for testing
        Employee employee = new Employee(null, "12345", "test@example.com", "password", "admin", null, null, null, null, null, null, null);
        
        // Set up mock behavior to simulate finding the employee by Payswiff ID
        when(employeeRepository.findByEmployeePayswiffId(employee.getEmployeePayswiffId())).thenReturn(Optional.of(employee));

        // Call the method under test
        Employee retrievedEmployee = employeeService.getEmployee(employee.getEmployeePayswiffId(), null, null);
        
        // Assert the retrieved employee matches the expected employee
        assertEquals(employee.getEmployeeEmail(), retrievedEmployee.getEmployeeEmail());
    }

    /**
     * Tests the successful retrieval of an employee by phone number.
     * Asserts that the retrieved employee matches the expected employee.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetEmployeeByPhoneNumber_Success() throws ResourceNotFoundException {
        // Create a sample employee object for testing
        Employee employee = new Employee("12345", "test@example.com", "password", "admin", "1234567890");
        
        // Set up mock behavior to simulate finding the employee by phone number
        when(employeeRepository.findByEmployeePhoneNumber(employee.getEmployeePhoneNumber())).thenReturn(Optional.of(employee));

        // Call the method under test
        Employee retrievedEmployee = employeeService.getEmployee(null, employee.getEmployeePhoneNumber(), null);
        
        // Assert the retrieved employee matches the expected employee
        assertEquals(employee.getEmployeeEmail(), retrievedEmployee.getEmployeeEmail());
    }

    /**
     * Tests the successful retrieval of an employee by email.
     * Asserts that the retrieved employee matches the expected employee.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testGetEmployeeByEmail_Success() throws ResourceNotFoundException {
        // Create a sample employee object for testing
        Employee employee = new Employee(null, "12345", "test@example.com", "password", "admin", null, null, null, null, null, null, null);
        
        // Set up mock behavior to simulate finding the employee by email
        when(employeeRepository.findByEmployeeEmail(employee.getEmployeeEmail())).thenReturn(Optional.of(employee));

        // Call the method under test
        Employee retrievedEmployee = employeeService.getEmployee(null, null, employee.getEmployeeEmail());
        
        // Assert the retrieved employee matches the expected employee
        assertEquals(employee.getEmployeeEmail(), retrievedEmployee.getEmployeeEmail());
    }

    /**
     * Tests the behavior when trying to retrieve an employee with no parameters.
     * Asserts that a ResourceNotFoundException is thrown.
     */
    @Test
    void testGetEmployee_ThrowsResourceNotFound_WhenNoParametersProvided() {
        // Assert that retrieving an employee with no parameters throws the expected exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployee(null, null, null); // Attempt to get employee without any parameters
        });
        // Check the exception message
        assertEquals("Employee with Parameters: None provided is not found!!", exception.getMessage());
    }

    /**
     * Tests the behavior when trying to retrieve a non-existent employee.
     * Asserts that a ResourceNotFoundException is thrown.
     */
    @Test
    void testGetEmployee_ThrowsResourceNotFound_WhenEmployeeNotFound() {
        // Set up mock behavior to simulate not finding the employee by email
        when(employeeRepository.findByEmployeeEmail(anyString())).thenReturn(Optional.empty());

        // Assert that retrieving a non-existent employee throws the expected exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployee(null, null, "nonexistent@example.com"); // Attempt to get a non-existent employee
        });
        // Check the exception message
        assertEquals("Employee with Email: nonexistent@example.com is not found!!", exception.getMessage());
    }

    /**
     * Tests the existence check for an existing employee ID.
     * Asserts that the method returns true.
     */
    @Test
    void testExistsById_ReturnsTrue() {
        String employeeId = "12345"; // Sample employee ID for testing
        
        // Set up mock behavior to simulate existing employee ID
        when(employeeRepository.existsById(Long.parseLong(employeeId))).thenReturn(true);

        // Call the method under test and check existence
        boolean exists = employeeService.existsById(Long.parseLong(employeeId));
        
        // Assert that the existence check returns true
        assertTrue(exists);
    }

    /**
     * Tests the behavior when checking existence for a non-existent employee ID.
     * Asserts that the method returns false.
     */
    @Test
    void testExistsById_ReturnsFalse() {
        String employeeId = "12345"; // Sample employee ID for testing
        
        // Set up mock behavior to simulate non-existing employee ID
        when(employeeRepository.existsById(Long.parseLong(employeeId))).thenReturn(false);

        // Call the method under test and check existence
        boolean exists = employeeService.existsById(Long.parseLong(employeeId));
        
        // Assert that the existence check returns false
        assertFalse(exists);
    }

    /**
     * Tests the successful password update for an existing employee.
     * Verifies that the password is encoded and saved correctly.
     * @throws ResourceNotFoundException 
     */
    @Test
    void testUpdateEmployeePassword_Success() throws ResourceNotFoundException {
        String email = "test@example.com"; // Sample email for testing
        Employee employee = new Employee(null, "12345", email, "oldPassword", "admin", null, email, email, null, null, null, null);
        
        // Set up mock behavior to simulate finding the employee by email
        when(employeeRepository.findByEmployeeEmail(email)).thenReturn(Optional.of(employee));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword"); // Mock password encoding

        // Call the method under test
        employeeService.updateEmployeePassword(email, "newPassword");
        
        // Assert that the employee's password was updated correctly
        assertEquals("encodedNewPassword", employee.getEmployeePassword());
        verify(employeeRepository).save(employee); // Verify that the employee was saved
    }

    /**
     * Tests the behavior when trying to update the password for a non-existent email.
     * Asserts that a ResourceNotFoundException is thrown.
     */
    @Test
    void testUpdateEmployeePassword_ThrowsResourceNotFound_WhenEmailNotFound() {
        String email = "nonexistent@example.com"; // Sample email for testing
        
        // Set up mock behavior to simulate not finding the employee by email
        when(employeeRepository.findByEmployeeEmail(email)).thenReturn(Optional.empty());

        // Assert that updating password for a non-existent email throws the expected exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployeePassword(email, "newPassword"); // Attempt to update password
        });
        // Check the exception message
        assertEquals("Employee with Email: nonexistent@example.com is not found!!", exception.getMessage());
    }

    /**
     * Tests the successful retrieval of all employees.
     * Asserts that the returned list contains the correct number of employees.
     */
    @Test
    void testGetAllEmployees_Success() {
        // Create a sample list of employees for testing
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("12345", "test1@example.com", "password1", "admin", null));
        employees.add(new Employee("12346", "test2@example.com", "password2", "employee", null));
        
        // Set up mock behavior to simulate retrieving all employees
        when(employeeRepository.findAll()).thenReturn(employees);

        // Call the method under test
        List<Employee> result = employeeService.getAllEmployees();
        
        // Assert the returned list contains the correct number of employees
        assertEquals(2, result.size());
        assertEquals("test1@example.com", result.get(0).getEmployeeEmail()); // Check first employee's email
        assertEquals("test2@example.com", result.get(1).getEmployeeEmail()); // Check second employee's email
    }
}
