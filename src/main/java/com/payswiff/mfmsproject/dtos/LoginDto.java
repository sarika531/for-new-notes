package com.payswiff.mfmsproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LoginDto is a Data Transfer Object used to encapsulate the data 
 * required for user login.
 * 
 * <p>This DTO is used to transfer login details such as email/phone and password 
 * between layers in an application. It allows for easy handling and validation 
 * of user login information.</p>
 * 
 * @version MFMS_0.0.1
 * @author Chatla Sarika
 */
@NoArgsConstructor  // No-argument constructor for deserialization or default instantiation
@Builder            // Lombok annotation to enable the builder pattern for object creation
@Getter             // Lombok annotation to generate getter methods for the fields
@Setter             // Lombok annotation to generate setter methods for the fields
public class LoginDto {
	
	private String emailOrPhone; // Email or phone number for user identification
	private String password;      // Password for user authentication

	/**
	 * Constructs a LoginDto with the specified email or phone and password.
	 *
	 * @param emailOrPhone The email or phone number of the user.
	 * @param password The password of the user.
	 */
	public LoginDto(String emailOrPhone, String password) {
		this.emailOrPhone = emailOrPhone;
		this.password = password;
	}

	/**
	 * Gets the email or phone number.
	 *
	 * @return The email or phone number as a String.
	 */
	public String getEmailOrPhone() {
		return emailOrPhone;
	}

	/**
	 * Sets the email or phone number.
	 *
	 * @param emailOrPhone The email or phone number to set.
	 */
	public void setEmailOrPhone(String emailOrPhone) {
		this.emailOrPhone = emailOrPhone;
	}

	/**
	 * Gets the password.
	 *
	 * @return The password as a String.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
