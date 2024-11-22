package com.payswiff.mfmsproject.dtos;
/**
 * ForgotPasswordDto is a Data Transfer Object used for handling 
 * the information required to reset a user's password.
 * 
 * @version MFMS_0.0.1
 * @author Ruchitha Guttikonda
 */
public class ForgotPasswordDto {
	
	private String emailOrPhone;   // User's email or phone number for identification
	private String resetPassword;   // The new password to be set

	/**
	 * Constructs a new ForgotPasswordDto with specified email/phone and reset password.
	 *
	 * @param emailOrPhone The user's email or phone number used for password recovery.
	 * @param resetPassword The new password that the user wants to set.
	 */
	public ForgotPasswordDto(String emailOrPhone, String resetPassword) {
		this.emailOrPhone = emailOrPhone;
		this.resetPassword = resetPassword;
	}

	/**
	 * Gets the user's email or phone number.
	 *
	 * @return the emailOrPhone as String.
	 */
	public String getEmailOrPhone() {
		return emailOrPhone;
	}

	/**
	 * Sets the user's email or phone number.
	 *
	 * @param emailOrPhone The email or phone number to set.
	 */
	public void setEmailOrPhone(String emailOrPhone) {
		this.emailOrPhone = emailOrPhone;
	}

	/**
	 * Gets the new password to be set.
	 *
	 * @return the resetPassword as String.
	 */
	public String getResetPassword() {
		return resetPassword;
	}

	/**
	 * Sets the new password to be set.
	 *
	 * @param resetPassword The new password to set.
	 */
	public void setResetPassword(String resetPassword) {
		this.resetPassword = resetPassword;
	}
}
