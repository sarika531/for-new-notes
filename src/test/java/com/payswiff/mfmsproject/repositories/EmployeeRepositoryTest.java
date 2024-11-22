package com.payswiff.mfmsproject.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.models.EmployeeType;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Test suite for {@link EmployeeRepository} to verify data retrieval and existence checks.
 */
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;

    /**
     * Set up test data before each test case.
     * This creates an employee instance to use in each test method.
     */
    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setEmployeeEmail("test@example.com");
        testEmployee.setEmployeePhoneNumber("1234567890");
        testEmployee.setEmployeePayswiffId("PW12345");
        testEmployee.setEmployeeType(EmployeeType.admin);
        testEmployee.setEmployeeDesignation("dev");
        testEmployee.setEmployeeName("gopi");
        testEmployee.setEmployeeUuid(UUID.randomUUID().toString());
        testEmployee.setEmployeePassword("gopi12342");
        employeeRepository.save(testEmployee);
    }

    /**
     * Clean up the repository after each test case.
     */
    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    /**
     * Test method for {@link EmployeeRepository#findByEmployeeEmail(String)}.
     * Verifies that an employee can be retrieved by their email.
     */
    @Test
    void testFindByEmployeeEmail() {
        // Act: Retrieve employee by email
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeEmail("test@example.com");

        // Assert: Verify that the employee is found
        assertTrue(foundEmployee.isPresent(), "Employee should be found by email");
        assertEquals(testEmployee.getEmployeeEmail(), foundEmployee.get().getEmployeeEmail(), 
                     "The email of the found employee should match the test email");
    }

    /**
     * Test method for {@link EmployeeRepository#findByEmployeePhoneNumber(String)}.
     * Verifies that an employee can be retrieved by their phone number.
     */
    @Test
    void testFindByEmployeePhoneNumber() {
        // Act: Retrieve employee by phone number
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeePhoneNumber("1234567890");

        // Assert: Verify that the employee is found
        assertTrue(foundEmployee.isPresent(), "Employee should be found by phone number");
        assertEquals(testEmployee.getEmployeePhoneNumber(), foundEmployee.get().getEmployeePhoneNumber(), 
                     "The phone number of the found employee should match the test phone number");
    }

    /**
     * Test method for {@link EmployeeRepository#findByEmployeePayswiffId(String)}.
     * Verifies that an employee can be retrieved by their Payswiff ID.
     */
    @Test
    void testFindByEmployeePayswiffId() {
        // Act: Retrieve employee by Payswiff ID
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeePayswiffId("PW12345");

        // Assert: Verify that the employee is found
        assertTrue(foundEmployee.isPresent(), "Employee should be found by Payswiff ID");
        assertEquals(testEmployee.getEmployeePayswiffId(), foundEmployee.get().getEmployeePayswiffId(), 
                     "The Payswiff ID of the found employee should match the test Payswiff ID");
    }

    /**
     * Test method for {@link EmployeeRepository#findByEmployeeEmailOrEmployeePhoneNumber(String, String)}.
     * Verifies that an employee can be retrieved by either email or phone number.
     */
    @Test
    void testFindByEmployeeEmailOrEmployeePhoneNumber() {
        // Act: Retrieve employee by email or phone number
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeEmailOrEmployeePhoneNumber("test@example.com", "1234567890");

        // Assert: Verify that the employee is found
        assertTrue(foundEmployee.isPresent(), "Employee should be found by email or phone number");
    }

    /**
     * Test method for {@link EmployeeRepository#existsByEmployeeEmailOrEmployeePhoneNumber(String, String)}.
     * Verifies if an employee exists by either email or phone number.
     */
    @Test
    void testExistsByEmployeeEmailOrEmployeePhoneNumber() {
        // Act: Check existence by email or phone number
        boolean exists = employeeRepository.existsByEmployeeEmailOrEmployeePhoneNumber("test@example.com", "1234567890");

        // Assert: Verify that the employee exists
        assertTrue(exists, "Employee should exist with the given email or phone number");
    }

    /**
     * Test method for {@link EmployeeRepository#existsByEmployeeEmail(String)}.
     * Verifies if an employee exists by their email.
     */
    @Test
    void testExistsByEmployeeEmail() {
        // Act: Check existence by email
        boolean exists = employeeRepository.existsByEmployeeEmail("test@example.com");

        // Assert: Verify that the employee exists
        assertTrue(exists, "Employee should exist with the given email");
    }

    /**
     * Test method for {@link EmployeeRepository#existsByEmployeePhoneNumber(String)}.
     * Verifies if an employee exists by their phone number.
     */
    @Test
    void testExistsByEmployeePhoneNumber() {
        // Act: Check existence by phone number
        boolean exists = employeeRepository.existsByEmployeePhoneNumber("1234567890");

        // Assert: Verify that the employee exists
        assertTrue(exists, "Employee should exist with the given phone number");
    }

    /**
     * Negative test for {@link EmployeeRepository#findByEmployeeEmail(String)}.
     * Verifies that no employee is found with a non-existent email.
     */
    @Test
    void testFindByEmployeeEmail_NonExistent() {
        // Act: Attempt to retrieve a non-existent employee by email
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeEmail("nonexistent@example.com");

        // Assert: Verify that no employee is found
        assertFalse(foundEmployee.isPresent(), "No employee should be found with a non-existent email");
    }

    /**
     * Negative test for {@link EmployeeRepository#findByEmployeePhoneNumber(String)}.
     * Verifies that no employee is found with a non-existent phone number.
     */
    @Test
    void testFindByEmployeePhoneNumber_NonExistent() {
        // Act: Attempt to retrieve a non-existent employee by phone number
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeePhoneNumber("9999999999");

        // Assert: Verify that no employee is found
        assertFalse(foundEmployee.isPresent(), "No employee should be found with a non-existent phone number");
    }

    /**
     * Negative test for {@link EmployeeRepository#existsByEmployeeEmail(String)}.
     * Verifies that no employee exists with a non-existent email.
     */
    @Test
    void testExistsByEmployeeEmail_NonExistent() {
        // Act: Check existence with a non-existent email
        boolean exists = employeeRepository.existsByEmployeeEmail("nonexistent@example.com");

        // Assert: Verify that no employee exists
        assertFalse(exists, "No employee should exist with a non-existent email");
    }

    /**
     * Negative test for {@link EmployeeRepository#existsByEmployeePhoneNumber(String)}.
     * Verifies that no employee exists with a non-existent phone number.
     */
    @Test
    void testExistsByEmployeePhoneNumber_NonExistent() {
        // Act: Check existence with a non-existent phone number
        boolean exists = employeeRepository.existsByEmployeePhoneNumber("9999999999");

        // Assert: Verify that no employee exists
        assertFalse(exists, "No employee should exist with a non-existent phone number");
    }

   
    /**
     * Test case to handle null input for {@link EmployeeRepository#findByEmployeeEmail(String)}.
     * Verifies that passing null does not throw an unexpected exception.
     */
    @Test
    void testFindByEmployeeEmail_NullInput() {
        // Act & Assert: Verify that null input returns empty result instead of exception
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeEmail(null);
        assertFalse(foundEmployee.isPresent(), "No employee should be found when searching with null email");
    }

    /**
     * Test method for {@link EmployeeRepository#findByEmployeeEmailOrEmployeePhoneNumber(String, String)}
     * Verifies retrieval when both email and phone number are valid.
     */
    @Test
    void testFindByEmployeeEmailOrEmployeePhoneNumber_BothValid() {
        // Act: Try retrieving by both valid email and phone number
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeEmailOrEmployeePhoneNumber("test@example.com", "1234567890");

        // Assert: Verify that the employee is found
        assertTrue(foundEmployee.isPresent(), "Employee should be found when both email and phone number match");
    }

    /**
     * Test case for {@link EmployeeRepository#findByEmployeeEmailOrEmployeePhoneNumber(String, String)}
     * Verifies retrieval when only phone number is valid.
     */
    @Test
    void testFindByEmployeeEmailOrEmployeePhoneNumber_OnlyPhoneValid() {
        // Act: Try retrieving by valid phone number but non-existent email
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeEmailOrEmployeePhoneNumber("nonexistent@example.com", "1234567890");

        // Assert: Verify that the employee is found by phone number
        assertTrue(foundEmployee.isPresent(), "Employee should be found with only a valid phone number");
        assertEquals(testEmployee.getEmployeePhoneNumber(), foundEmployee.get().getEmployeePhoneNumber(),
                     "The phone number should match the test employee's phone number");
    }

    /**
     * Test case for {@link EmployeeRepository#existsByEmployeeEmailOrEmployeePhoneNumber(String, String)}
     * Verifies behavior when both email and phone number are null.
     */
    @Test
    void testExistsByEmployeeEmailOrEmployeePhoneNumber_NullValues() {
        // Act & Assert: Check existence with null email and phone number
        assertFalse(employeeRepository.existsByEmployeeEmailOrEmployeePhoneNumber(null, null),
                    "No employee should exist with null email and phone number");
    }

    /**
     * Test case for {@link EmployeeRepository#findByEmployeeEmail(String)}
     * Verifies behavior with a very long email input to test input handling limits.
     */
    @Test
    void testFindByEmployeeEmail_LongEmail() {
        // Arrange: Create a long email string
        String longEmail = "a".repeat(255) + "@example.com";

        // Act: Try retrieving by the long email
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeEmail(longEmail);

        // Assert: No employee should be found with a non-existent long email
        assertFalse(foundEmployee.isPresent(), "No employee should be found with a long, non-existent email");
    }

    

    /**
     * Test case for checking that the repository correctly identifies multiple employees.
     */
    @Test
    void testFindMultipleEmployees() {
        // Arrange: Add more employees to the repository
        Employee employee2 = new Employee();
        employee2.setEmployeeEmail("user2@example.com");
        employee2.setEmployeePhoneNumber("1111111111");
        employee2.setEmployeePayswiffId("PW12346");
        employee2.setEmployeeDesignation("dev");
        employee2.setEmployeeName("gopi");
        employee2.setEmployeeUuid(UUID.randomUUID().toString());
        employee2.setEmployeeType(EmployeeType.admin);
        employee2.setEmployeePassword("gopi1234522@");
        Employee employee3 = new Employee();
        employee3.setEmployeeEmail("user3@example.com");
        employee3.setEmployeePhoneNumber("2222222222");
        employee3.setEmployeePayswiffId("PW12347");
        employee3.setEmployeeDesignation("devops");
        employee3.setEmployeeName("gopi");
        employee3.setEmployeeUuid(UUID.randomUUID().toString());
        employee3.setEmployeeType(EmployeeType.admin);
        employee3.setEmployeePassword("gopi1234522@");

        employeeRepository.saveAll(Arrays.asList(employee2, employee3));

        // Act: Retrieve all employees
        List<Employee> allEmployees = employeeRepository.findAll();

        // Assert: Verify that all employees were added and retrieved correctly
        assertEquals(3, allEmployees.size(), "There should be exactly 3 employees in the repository");
    }

    /**
     * Test method for verifying empty result when email has leading/trailing whitespace.
     */
    @Test
    void testFindByEmployeeEmail_WithWhitespace() {
        // Act: Try retrieving by email with added whitespace
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeEmail(" test@example.com ");

        // Assert: Verify that no employee is found due to whitespace issue
        assertFalse(foundEmployee.isPresent(), "No employee should be found if email has leading or trailing whitespace");
    }

    /**
     * Test method for verifying search with phone number containing extra characters.
     */
    @Test
    void testFindByEmployeePhoneNumber_WithSpecialCharacters() {
        // Arrange: Format phone number with dashes and spaces
        String formattedPhoneNumber = "123-456-7890";

        // Act: Attempt retrieval with special characters in phone number
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeePhoneNumber(formattedPhoneNumber);

        // Assert: Verify that no employee is found if phone number has unexpected format
        assertFalse(foundEmployee.isPresent(), "No employee should be found if phone number format includes special characters");
    }

    /**
     * Test case for verifying existence check by employee's Payswiff ID.
     */
    @Test
    void testExistsByEmployeePayswiffId() {
        // Act: Check if employee exists by Payswiff ID
        boolean exists = employeeRepository.existsByEmployeePayswiffId("PW12345");

        // Assert: Verify that employee exists with the given Payswiff ID
        assertTrue(exists, "Employee should exist with the given Payswiff ID");
    }

    /**
     * Negative test for verifying non-existence with invalid Payswiff ID.
     */
    @Test
    void testExistsByEmployeePayswiffId_NonExistent() {
        // Act: Check existence with a non-existent Payswiff ID
        boolean exists = employeeRepository.existsByEmployeePayswiffId("PW99999");

        // Assert: Verify that no employee exists with a non-existent Payswiff ID
        assertFalse(exists, "No employee should exist with a non-existent Payswiff ID");
    }
}
