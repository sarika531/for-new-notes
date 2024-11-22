package com.payswiff.mfmsproject.exceptions;

/**
 * ResourceNotFoundException is a custom exception class that is thrown when a requested resource 
 * (e.g., Device, Merchant) cannot be found in the system or database.
 * It provides detailed information about the missing resource, the field used for searching, 
 * and the value that was sought.
 * <p>
 * This exception helps in identifying scenarios where a particular resource, such as a device 
 * or merchant, does not exist or cannot be retrieved based on the search criteria.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */
public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;  // Serial version UID for serialization compatibility.

    private String resource;  // Name of the resource that is not found (e.g., "Device", "Merchant").
    private String field;     // Name of the field that was used to search for the resource (e.g., "ID", "Model").
    private String value;     // Value of the field that was searched for (e.g., "MPOS", "123").

    /**
     * Constructor for ResourceNotFoundException.
     * 
     * @param resource The resource type (e.g., "Device", "Merchant") that could not be found.
     * @param field The field (e.g., "ID", "Model") that was used to search for the resource.
     * @param value The value of the field that was searched for (e.g., "MPOS").
     */
    public ResourceNotFoundException(String resource, String field, String value) {
        // Construct a formatted error message using the provided resource, field, and value.
        super(String.format("%s with %s: %s is not found!!", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

}
