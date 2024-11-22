package com.payswiff.mfmsproject.dtos;

/**
 * EmailSendDto is a Data Transfer Object that encapsulates the information
 * required to send an email.
 * <p>
 * This class stores the necessary details for sending an email, such as the recipient's email 
 * address, subject line, and body content of the email.
 * </p>
 * 
 * @version MFMS_0.0.1

 * @author Gopi Bapanapalli
 */

public class EmailSendDto {
    
    private String to;       // Recipient's email address
    private String subject;  // Subject of the email
    private String text;     // Body content of the email

    /**
     * Constructs a new EmailSendDto with specified recipient, subject, and text.
     *
     * @param to The recipient's email address.
     * @param subject The subject line of the email.
     * @param text The body text of the email.
     */
    public EmailSendDto(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    /**
     * Default constructor for EmailSendDto.
     */
    public EmailSendDto() {
    }

    /**
     * Gets the recipient's email address.
     *
     * @return the recipient's email address as a String.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient's email address.
     *
     * @param to The recipient's email address to set.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the subject of the email.
     *
     * @return the subject of the email as a String.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the email.
     *
     * @param subject The subject to set for the email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the body content of the email.
     *
     * @return the body content of the email as a String.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the body content of the email.
     *
     * @param text The body content to set for the email.
     */
    public void setText(String text) {
        this.text = text;
    }
}
