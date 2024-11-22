package com.payswiff.mfmsproject.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Represents the association between feedback and questions in the system.
 * This class is used to store the answers given by employees to specific questions
 * related to feedback. It links the feedback with the respective question and the employee's answer.
 * 
 * <p>This class contains the following fields:</p>
 * <ul>
 *     <li><b>id</b>: A unique identifier for the association entry (auto-increment).</li>
 *     <li><b>feedback</b>: A reference to the feedback entity that the question is associated with.</li>
 *     <li><b>question</b>: A reference to the question that is associated with the feedback.</li>
 *     <li><b>answer</b>: The answer given by the employee to the respective question.</li>
 * </ul>
 * 
 * <p>This class enables the system to capture detailed responses to feedback-related questions and links
 * the feedback with the corresponding questions and answers, ensuring traceability of responses.</p>
 * 
 * @author Chatla Sarika
 * @version MFMS_0.0.1
 */
@Entity
@Table(name = "feedback_questions_association")
//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeedbackQuestionsAssociation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;  // Auto-increment ID for association

    @ManyToOne
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;  // Foreign key referencing feedback

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;  // Foreign key referencing question

    @Column(name = "answer", nullable = false)
    private String answer;  // The answer associated with the feedback question

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the feedback
	 */
	public Feedback getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @param id
	 * @param feedback
	 * @param question
	 * @param answer
	 */
	public FeedbackQuestionsAssociation(Integer id, Feedback feedback, Question question, String answer) {
		this.id = id;
		this.feedback = feedback;
		this.question = question;
		this.answer = answer;
	}

	/**
	 * 
	 */
	public FeedbackQuestionsAssociation() {
	}
    
    
}
