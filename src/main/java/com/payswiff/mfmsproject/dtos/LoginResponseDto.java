package com.payswiff.mfmsproject.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LoginResponseDto is a Data Transfer Object used to encapsulate the 
 * response data returned upon successful user login.
 * 
 * <p>This DTO is designed to contain the user's essential details such as email/phone, 
 * role, token, and user ID that are returned as part of the login response. It provides 
 * the necessary data to authenticate and identify the user in subsequent requests.</p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */
@NoArgsConstructor  // No-argument constructor for deserialization or default instantiation
@Getter             // Lombok annotation to generate getter methods for the fields
@Setter             // Lombok annotation to generate setter methods for the fields
public class LoginResponseDto {
	
	private String userEmailOrPhone; // The email or phone number of the user
	private String role;              // The role of the user in the system
	private String token;             // The JWT or session token for the user
	private long id;                  // Unique identifier for the user

	/**
	 * Constructs a LoginResponseDto with the specified parameters.
	 *
	 * @param userEmailOrPhone The user's email or phone number.
	 * @param role The user's role.
	 * @param id The unique identifier for the user.
	 * @param token The token associated with the user's session.
	 */
	public LoginResponseDto(String userEmailOrPhone, String role, long id, String token) {
		this.userEmailOrPhone = userEmailOrPhone;
		this.role = role;
		this.token = token;
		this.id = id;
	}

	/**
	 * Gets the user's email or phone number.
	 *
	 * @return The user's email or phone number as a String.
	 */
	public String getUserEmailOrPhone() {
		return userEmailOrPhone;
	}

	/**
	 * Sets the user's email or phone number.
	 *
	 * @param userEmailOrPhone The user's email or phone number to set.
	 */
	public void setUserEmailOrPhone(String userEmailOrPhone) {
		this.userEmailOrPhone = userEmailOrPhone;
	}

	/**
	 * Gets the user's role.
	 *
	 * @return The user's role as a String.
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the user's role.
	 *
	 * @param role The user's role to set.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the token associated with the user's session.
	 *
	 * @return The token as a String.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token associated with the user's session.
	 *
	 * @param token The token to set.
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the unique identifier for the user.
	 *
	 * @return The user's ID as a long.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for the user.
	 *
	 * @param id The ID to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
}
