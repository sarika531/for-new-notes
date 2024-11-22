package com.payswiff.mfmsproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payswiff.mfmsproject.models.Employee;
import java.util.List;

/**
 * Repository interface for managing {@link Employee} entities.
 * This interface extends JpaRepository to provide basic CRUD operations 
 * on the 'employee' table in the database.
 * <p>It also includes custom query methods to search for employees based on their email, phone number, or Payswiff ID.</p>
 * <p>Spring Data JPA will automatically implement this interface, providing methods for standard CRUD operations 
 * as well as custom queries for specific requirements.</p>
 * 
 * @author Revanth K
 * @version MFMS_0.0.1
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an employee by their email.
     *
     * @param email the email of the employee
     * @return an Optional containing the employee if found, otherwise empty
     */
    Optional<Employee> findByEmployeeEmail(String email);

    /**
     * Finds an employee by their phone number.
     *
     * @param phoneNumber the phone number of the employee
     * @return an Optional containing the employee if found, otherwise empty
     */
    Optional<Employee> findByEmployeePhoneNumber(String phoneNumber);

    /**
     * Finds an employee by their Payswiff ID.
     *
     * @param payswiffId the Payswiff ID of the employee
     * @return an Optional containing the employee if found, otherwise empty
     */
    Optional<Employee> findByEmployeePayswiffId(String payswiffId);
    
    Optional<Employee> findByEmployeeEmailOrEmployeePhoneNumber(String employeeEmail,String  employeePhoneNumber);
    
    boolean existsByEmployeeEmailOrEmployeePhoneNumber(String employeeEmail,String  employeePhoneNumber);
    
    boolean existsByEmployeeEmail(String employeeEmail);
    
    boolean existsByEmployeePhoneNumber(String employeePhoneNumber);

	boolean existsByEmployeePayswiffId(String payswiffId);
    
    
    
}
