package com.payswiff.mfmsproject.dtos;
/**
 * Data Transfer Object (DTO) for Feedback Question Association.
 * <p>
 * This DTO is used to transfer only the necessary fields related to feedback questions
 * in the API response, minimizing the data exposure and improving performance.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Ruchitha Guttikonda
 */
public class FeedbackQuestionDTO {

    /**
     * The unique identifier for the question.
     */
    private long questionId;

    /**
     * A description of the question being asked.
     */
    private String questionDescription;

    /**
     * The answer provided for the question.
     */
    private String answer;

    /**
     * Constructs a new FeedbackQuestionDTO with the specified properties.
     *
     * @param questionId        The unique identifier for the question.
     * @param questionDescription The description of the question.
     * @param answer            The answer provided for the question.
     */
    public FeedbackQuestionDTO(Long questionId, String questionDescription, String answer) {
        this.questionId = questionId;
        this.questionDescription = questionDescription;
        this.answer = answer;
    }

    /**
     * Returns the unique identifier for the question.
     *
     * @return the questionId
     */
    public long getQuestionId() {
        return questionId;
    }

    /**
     * Sets the unique identifier for the question.
     *
     * @param questionId The unique identifier to set for the question.
     */
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    /**
     * Returns the description of the question.
     *
     * @return the questionDescription
     */
    public String getQuestionDescription() {
        return questionDescription;
    }

    /**
     * Sets the description of the question.
     *
     * @param questionDescription The description to set for the question.
     */
    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    /**
     * Returns the answer provided for the question.
     *
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the answer for the question.
     *
     * @param answer The answer to set for the question.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
