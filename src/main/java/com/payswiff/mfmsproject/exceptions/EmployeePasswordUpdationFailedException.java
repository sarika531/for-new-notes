/**
 * Exception thrown when an employee's password update fails.
 */
package com.payswiff.mfmsproject.exceptions;
/**
 * EmployeePasswordUpdationFailedException is thrown when an employee's password update fails.
 * <p>
 * This exception is thrown to indicate that an error occurred while attempting to update or change
 * an employee's password, which could be due to various reasons such as invalid credentials, system issues,
 * or other failures.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

public class EmployeePasswordUpdationFailedException extends Exception {

    private static final long serialVersionUID = 1L; // Unique identifier for serialization

    private String resource;  // The resource type (e.g., "Employee")
    private String emailOrPhone;  // The email or phone number of the employee
    private String value;  // Additional information related to the exception

    /**
     * Constructs a new EmployeePasswordUpdationFailedException with the specified details.
     *
     * @param resource     The type of resource that failed to update (e.g., "Employee").
     * @param emailOrPhone The email or phone of the employee whose password update failed.
     * @param value        Additional information about the failure.
     */
    public EmployeePasswordUpdationFailedException(String resource, String emailOrPhone, String value) {
        super(String.format("%s with %s: %s is unable to change or update password at this moment.", resource, emailOrPhone, value));
        this.resource = resource;
        this.emailOrPhone = emailOrPhone;
        this.value = value;
    }

    /**
     * Gets the resource type that caused the exception.
     *
     * @return The resource type as a String.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the resource type that caused the exception.
     *
     * @param resource The resource type to set.
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * Gets the email or phone number associated with the employee.
     *
     * @return The email or phone number as a String.
     */
    public String getEmailOrPhone() {
        return emailOrPhone;
    }

    /**
     * Sets the email or phone number associated with the employee.
     *
     * @param emailOrPhone The email or phone number to set.
     */
    public void setEmailOrPhone(String emailOrPhone) {
        this.emailOrPhone = emailOrPhone;
    }

    /**
     * Gets additional information related to the exception.
     *
     * @return The value as a String.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets additional information related to the exception.
     *
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the serial version UID for serialization.
     *
     * @return The serialVersionUID as a long.
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }  
}
