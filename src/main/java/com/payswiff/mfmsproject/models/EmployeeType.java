package com.payswiff.mfmsproject.models;

/**
 * Represents the types of employees in the system.
 * This enum defines two types of employees:
 * <ul>
 *     <li><b>ADMIN</b>: Represents an administrative user with higher privileges.</li>
 *     <li><b>EMPLOYEE</b>: Represents a regular employee with standard access rights.</li>
 * </ul>
 * 
 * <p>
 * The EmployeeType enum is used to define the role of an employee in the system.
 * It helps in distinguishing between users who have elevated permissions (ADMIN)
 * and those with standard access (EMPLOYEE).
 * </p>
 * 
 * @author Revanth K
 * @version MFMS_0.0.1
 */
public enum EmployeeType {
    /**
     * Represents an administrator type employee.
     * Administrators typically have elevated permissions and access to manage the system.
     */
    admin, 

    /**
     * Represents a standard employee type.
     * Regular employees have limited access compared to administrators.
     */
    employee
}
