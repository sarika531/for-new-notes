package com.payswiff.mfmsproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JwtAuthResponseDto is a Data Transfer Object used to encapsulate 
 * the response of a JWT (JSON Web Token) authentication.
 * 
 * @version MFMS_0.0.1
 * @author Chatla Sarika
 */

@NoArgsConstructor  // No-argument constructor for deserialization
@Getter             // Lombok annotation to generate getter methods
@Setter             // Lombok annotation to generate setter methods
@Builder            // Lombok annotation to enable builder pattern for object creation
public class JwtAuthResponseDto {
	
	private String accessToken;   // The JWT token granted after successful authentication
	private String tokenType = "Bearer"; // Type of the token, default is "Bearer"

	/**
	 * Constructs a JwtAuthResponseDto with specified access token and token type.
	 *
	 * @param accessToken The JWT access token.
	 * @param tokenType The type of the token (default is "Bearer").
	 */
	public JwtAuthResponseDto(String accessToken, String tokenType) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}

	/**
	 * Gets the access token.
	 *
	 * @return The access token as a String.
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Sets the access token.
	 *
	 * @param accessToken The access token to set.
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Gets the token type.
	 *
	 * @return The token type as a String.
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * Sets the token type.
	 *
	 * @param tokenType The token type to set.
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
