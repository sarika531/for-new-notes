package com.payswiff.mfmsproject.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the details of an error response in the system.
 * This class is used to encapsulate error information returned to the client
 * in case of exceptions or issues during API calls.
 * <p>
 * This class contains the following fields:
 * <ul>
 *     <li><b>timeStamp</b>: The timestamp when the error occurred.</li>
 *     <li><b>message</b>: A message describing the error.</li>
 *     <li><b>statusCode</b>: The status code of the HTTP response, indicating the type of error.</li>
 *     <li><b>details</b>: Additional details about the error, such as stack traces or error codes.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * This class provides a structure for returning detailed error information in a standardized
 * format, which helps both the client and developers to better understand and diagnose
 * issues that occur during API interactions.
 * </p>
 * 
 * @author Gopi Bapanapalli
 * @version MFMS_0.0.1
 */
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
@Data
public class ErrorDetails {
    
    /**
     * The timestamp when the error occurred.
     * This provides context regarding when the error happened.
     */
    private Date timeStamp;

    /**
     * A message describing the error.
     * This should give a brief explanation of the issue encountered.
     */
    private String message;

    /**
     * The status code of the HTTP response.
     * This indicates the type of error based on HTTP standards.
     */
    private String statusCode;

    /**
     * Additional details about the error.
     * This may include stack traces, error codes, or other relevant information
     * that can help diagnose the problem.
     */
    private String details;

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public ErrorDetails(Date timeStamp, String message, String statusCode, String details) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.statusCode = statusCode;
		this.details = details;
	}

	/**
	 * 
	 */
	public ErrorDetails() {
	}
}
