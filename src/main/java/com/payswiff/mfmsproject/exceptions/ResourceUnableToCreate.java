package com.payswiff.mfmsproject.exceptions;

/**
 * ResourceUnableToCreate is a custom exception class that is thrown when an attempt to create a resource 
 * (e.g., User, Product) fails. The exception captures details about the resource, the field involved, 
 * and the value that was attempted to be created, providing a structured way to handle resource creation failures.
 * <p>
 * This exception helps in identifying and managing errors that occur when trying to create a resource that 
 * cannot be successfully created at a given moment.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

public class ResourceUnableToCreate extends Exception {
    
    // Serial version UID for serialization compatibility
    private static final long serialVersionUID = 1L;
    
    private String resource;  // The name of the resource that could not be created
    private String field;     // The specific field of the resource related to the failure
    private String value;     // The value of the field that was attempted to be created

    /**
     * Constructs a ResourceUnableToCreate exception with detailed information.
     *
     * @param resource The name of the resource (e.g., "User").
     * @param field    The field of the resource that caused the creation failure (e.g., "email").
     * @param value    The value of the field that could not be created (e.g., "user@example.com").
     */
    public ResourceUnableToCreate(String resource, String field, String value) {
        // Construct a formatted error message using the provided resource, field, and value.
        super(String.format("%s with %s: %s is unable to create at this moment!", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    // Optionally, you can add getters to access the private fields
    /**
     * Gets the name of the resource that could not be created.
     *
     * @return The name of the resource.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Gets the field of the resource that is related to the failure.
     *
     * @return The field of the resource.
     */
    public String getField() {
        return field;
    }

    /**
     * Gets the value of the field that was attempted to be created.
     *
     * @return The value of the field.
     */
    public String getValue() {
        return value;
    }
}
