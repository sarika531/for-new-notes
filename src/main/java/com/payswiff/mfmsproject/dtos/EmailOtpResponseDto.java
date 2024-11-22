package com.payswiff.mfmsproject.dtos;

/**
 * EmailOtpResponseDto is a Data Transfer Object that encapsulates the response 
 * details of an email OTP (One-Time Password) request.
 * <p>
 * This class is used to transfer information related to the status of sending an OTP 
 * via email, including whether the email was sent successfully, the OTP generated, 
 * and the status code of the operation.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Revanth K
 */

public class EmailOtpResponseDto {
    
    private boolean emailSent; // Indicates if the OTP email was successfully sent
    private String otp; // The One-Time Password generated and sent to the user
    private int statusCode; // HTTP status code representing the result of the operation

    /**
     * Constructs a new EmailOtpResponseDto with specified fields.
     *
     * @param emailSent Indicates if the email was sent successfully.
     * @param otp The OTP that was generated and sent.
     * @param statusCode The status code of the email sending operation.
     */
    public EmailOtpResponseDto(boolean emailSent, String otp, int statusCode) {
        this.emailSent = emailSent;
        this.otp = otp;
        this.statusCode = statusCode;
    }

    /**
     * Default constructor for EmailOtpResponseDto.
     */
    public EmailOtpResponseDto() {
    }

    /**
     * Checks if the email was sent.
     *
     * @return true if the email was sent, false otherwise.
     */
    public boolean isEmailSent() {
        return emailSent;
    }

    /**
     * Sets the email sent status.
     *
     * @param emailSent the emailSent status to set.
     */
    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    /**
     * Gets the OTP.
     *
     * @return the OTP as a String.
     */
    public String getOtp() {
        return otp;
    }

    /**
     * Sets the OTP.
     *
     * @param otp the OTP to set.
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }

    /**
     * Gets the status code.
     *
     * @return the status code as an integer.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code.
     *
     * @param statusCode the status code to set.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
