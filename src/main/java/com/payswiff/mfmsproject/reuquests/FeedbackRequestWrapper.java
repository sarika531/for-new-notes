package com.payswiff.mfmsproject.reuquests;

import java.util.List;

import com.payswiff.mfmsproject.dtos.FeedbackQuestionAnswerAssignDto;

/**
 * A wrapper class that encapsulates a feedback request along with
 * a list of associated question-answer pairs.
 * @author Revanth K
 * @version MFMS_0.0.1
 */
public class FeedbackRequestWrapper {
    
    /**
     * The feedback request details.
     */
    private CreateFeedbackRequest feedbackRequest;

    /**
     * A list of question-answer assignments corresponding to the feedback request.
     */
    private List<FeedbackQuestionAnswerAssignDto> questionAnswers; // List for multiple questions

    // Getters and setters

    /**
     * Retrieves the feedback request.
     *
     * @return the feedback request details.
     */
    public CreateFeedbackRequest getFeedbackRequest() {
        return feedbackRequest;
    }

    /**
     * Sets the feedback request.
     *
     * @param feedbackRequest the feedback request to set.
     */
    public void setFeedbackRequest(CreateFeedbackRequest feedbackRequest) {
        this.feedbackRequest = feedbackRequest;
    }

    /**
     * Retrieves the list of question-answer assignments.
     *
     * @return the list of question-answer assignments.
     */
    public List<FeedbackQuestionAnswerAssignDto> getQuestionAnswers() {
        return questionAnswers;
    }

    /**
     * Sets the list of question-answer assignments.
     *
     * @param questionAnswers the list of question-answer assignments to set.
     */
    public void setQuestionAnswers(List<FeedbackQuestionAnswerAssignDto> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    /**
     * Constructs a new FeedbackRequestWrapper with the specified feedback request
     * and a list of question-answer assignments.
     *
     * @param feedbackRequest The feedback request details.
     * @param questionAnswers The list of question-answer assignments.
     */
    public FeedbackRequestWrapper(CreateFeedbackRequest feedbackRequest,
                                   List<FeedbackQuestionAnswerAssignDto> questionAnswers) {
        this.feedbackRequest = feedbackRequest;
        this.questionAnswers = questionAnswers;
    }
}
