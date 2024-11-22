package com.payswiff.mfmsproject.exceptions;

/**
 * ResourceAlreadyExists is an exception thrown when an attempt is made to create a resource 
 * that already exists. It captures detailed information about the conflicting resource, 
 * the specific field causing the conflict, and the value that already exists in the system.
 * <p>
 * This exception is used to indicate that a particular resource, such as a user or device, 
 * cannot be created because a record with the same identifier or field value already exists.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

public class ResourceAlreadyExists extends Exception {
    
    // Serial version UID for serialization compatibility
    private static final long serialVersionUID = 1L;

    private String resource;  // The name of the resource that already exists
    private String field;     // The specific field of the resource that is causing the conflict
    private String value;     // The value of the field that is causing the conflict

    /**
     * Constructs a ResourceAlreadyExists exception with detailed information.
     *
     * @param resource The name of the resource (e.g., "User").
     * @param field    The field of the resource that is causing the conflict (e.g., "email").
     * @param value    The value of the field that already exists (e.g., "user@example.com").
     */
    public ResourceAlreadyExists(String resource, String field, String value) {
        super(String.format("%s with %s: %s already exists.", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    // Optionally, you can add getters to access the private fields
    /**
     * Gets the name of the resource that already exists.
     *
     * @return The name of the resource.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Gets the field of the resource that is causing the conflict.
     *
     * @return The field of the resource.
     */
    public String getField() {
        return field;
    }

    /**
     * Gets the value of the field that already exists.
     *
     * @return The value of the field.
     */
    public String getValue() {
        return value;
    }
}
