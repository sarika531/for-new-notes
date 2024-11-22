package com.payswiff.mfmsproject.reuquests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import com.payswiff.mfmsproject.models.Employee;


/**
 * Represents a request to create feedback.
 * <p>This class captures all the necessary information required to create feedback, 
 * including the employee's ID, merchant's ID, device's ID, feedback image, rating, and the feedback text.</p>
 * 
 * @author Chatla Sarika
 * @version MFMS_0.0.1
 */
@Data
@Builder
//@AllArgsConstructor
//@NoArgsConstructor

@Getter
@Setter
public class CreateFeedbackRequest {

  

    /**
     * The ID of the employee providing the feedback.
     */
    private Long feedbackEmployeeId; // ID of the employee providing the feedback

    /**
     * The ID of the merchant associated with the feedback.
     */
    private Long feedbackMerchantId; // ID of the merchant related to the feedback

    /**
     * The ID of the device associated with the feedback.
     */
    private Long feedbackDeviceId; // ID of the device related to the feedback

    /**
     * The URL or path for the first feedback image.
     */
    private String feedbackImage1; // URL or path for image 1

	/**
     * The rating given in the feedback.
     */
    private Double feedbackRating; // Rating for the feedback

    /**
     * The actual feedback text provided by the user.
     */
    private String feedback; // Additional feedback text

    public CreateFeedbackRequest(Long feedbackEmployeeId, Long feedbackMerchantId, Long feedbackDeviceId,
			String feedbackImage1, Double feedbackRating, String feedback) {
		
		this.feedbackEmployeeId = feedbackEmployeeId;
		this.feedbackMerchantId = feedbackMerchantId;
		this.feedbackDeviceId = feedbackDeviceId;
		this.feedbackImage1 = feedbackImage1;
		this.feedbackRating = feedbackRating;
		this.feedback = feedback;
	}

	/**
	 * @return the feedbackEmployeeId
	 */
	public Long getFeedbackEmployeeId() {
		return feedbackEmployeeId;
	}

	/**
	 * @param feedbackEmployeeId the feedbackEmployeeId to set
	 */
	public void setFeedbackEmployeeId(Long feedbackEmployeeId) {
		this.feedbackEmployeeId = feedbackEmployeeId;
	}

	/**
	 * @return the feedbackMerchantId
	 */
	public Long getFeedbackMerchantId() {
		return feedbackMerchantId;
	}

	/**
	 * @param feedbackMerchantId the feedbackMerchantId to set
	 */
	public void setFeedbackMerchantId(Long feedbackMerchantId) {
		this.feedbackMerchantId = feedbackMerchantId;
	}

	/**
	 * @return the feedbackDeviceId
	 */
	public Long getFeedbackDeviceId() {
		return feedbackDeviceId;
	}

	/**
	 * @param feedbackDeviceId the feedbackDeviceId to set
	 */
	public void setFeedbackDeviceId(Long feedbackDeviceId) {
		this.feedbackDeviceId = feedbackDeviceId;
	}

	/**
	 * @return the feedbackImage1
	 */
	public String getFeedbackImage1() {
		return feedbackImage1;
	}

	/**
	 * @param feedbackImage1 the feedbackImage1 to set
	 */
	public void setFeedbackImage1(String feedbackImage1) {
		this.feedbackImage1 = feedbackImage1;
	}

	/**
	 * @return the feedbackRating
	 */
	public Double getFeedbackRating() {
		return feedbackRating;
	}

	/**
	 * @param feedbackRating the feedbackRating to set
	 */
	public void setFeedbackRating(Double feedbackRating) {
		this.feedbackRating = feedbackRating;
	}

	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	/**
	 * 
	 */
	public CreateFeedbackRequest() {
	}

	

    
}
