package com.payswiff.mfmsproject.dtos;

/**
 * FeedbackQuestionAnswerAssignDto is a Data Transfer Object that represents
 * the association between a feedback question and its corresponding answer.
 * <p>
 * This class stores the relationship between a feedback question (identified by 
 * its unique ID) and the answer provided to that question.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Ruchitha Guttikonda
 */

public class FeedbackQuestionAnswerAssignDto {
	
	private long questionId;        // Unique identifier for the feedback question
	private String questionAnswer;   // The answer corresponding to the feedback question

	/**
	 * Constructs a new FeedbackQuestionAnswerAssignDto with specified question ID and answer.
	 *
	 * @param questionId The unique identifier for the feedback question.
	 * @param questionAnswer The answer to the feedback question.
	 */
	public FeedbackQuestionAnswerAssignDto(long questionId, String questionAnswer) {
		this.questionId = questionId;
		this.questionAnswer = questionAnswer;
	}

	/**
	 * Default constructor for FeedbackQuestionAnswerAssignDto.
	 */
	public FeedbackQuestionAnswerAssignDto() {
	}

	/**
	 * Gets the unique identifier for the feedback question.
	 *
	 * @return the questionId as long.
	 */
	public long getQuestionId() {
		return questionId;
	}

	/**
	 * Sets the unique identifier for the feedback question.
	 *
	 * @param questionId The unique identifier to set for the feedback question.
	 */
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	/**
	 * Gets the answer corresponding to the feedback question.
	 *
	 * @return the questionAnswer as String.
	 */
	public String getQuestionAnswer() {
		return questionAnswer;
	}

	/**
	 * Sets the answer corresponding to the feedback question.
	 *
	 * @param questionAnswer The answer to set for the feedback question.
	 */
	public void setQuestionAnswer(String questionAnswer) {
		this.questionAnswer = questionAnswer;
	}
}
