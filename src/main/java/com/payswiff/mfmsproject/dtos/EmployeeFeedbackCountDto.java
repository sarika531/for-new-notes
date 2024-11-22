package com.payswiff.mfmsproject.dtos;

/**
 * EmployeeFeedbackCountDto is a Data Transfer Object that represents the feedback count
 * for an employee, including their ID, name, and the count of feedback received.
 * <p>
 * This class provides the necessary details regarding an employee's feedback count,
 * including their unique ID, name, and the total number of feedback entries they have received.
 * </p>
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

public class EmployeeFeedbackCountDto {

    private Long employeeId;        // Unique identifier for the employee
    private String employeeName;     // Name of the employee
    private Integer feedbackCount;   // Count of feedback received by the employee

    /**
     * Constructs a new EmployeeFeedbackCountDto with specified employee ID, name, and feedback count.
     *
     * @param employeeId The unique identifier for the employee.
     * @param employeeName The name of the employee.
     * @param feedbackCount The number of feedback entries associated with the employee.
     */
    public EmployeeFeedbackCountDto(Long employeeId, String employeeName, long feedbackCount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.feedbackCount = (int) feedbackCount; // Cast long to Integer
    }

    /**
     * Default constructor for EmployeeFeedbackCountDto.
     */
    public EmployeeFeedbackCountDto() {
    }

    /**
     * Gets the unique identifier for the employee.
     *
     * @return the employeeId as Long.
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the unique identifier for the employee.
     *
     * @param employeeId The unique identifier to set for the employee.
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the name of the employee.
     *
     * @return the employeeName as String.
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets the name of the employee.
     *
     * @param employeeName The name to set for the employee.
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Gets the count of feedback received by the employee.
     *
     * @return the feedbackCount as Integer.
     */
    public Integer getFeedbackCount() {
        return feedbackCount;
    }

    /**
     * Sets the count of feedback received by the employee.
     *
     * @param feedbackCount The feedback count to set for the employee.
     */
    public void setFeedbackCount(Integer feedbackCount) {
        this.feedbackCount = feedbackCount;
    }
}
