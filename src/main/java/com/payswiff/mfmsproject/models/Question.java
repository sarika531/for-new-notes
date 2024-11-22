package com.payswiff.mfmsproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a question entity in the system.
 * This class is mapped to the 'question' table in the database, 
 * storing details about the questions used in the feedback system.
 * The class captures key information about each question, such as its unique identifier,
 * description, and UUID used for reference purposes.
 * 
 * <p>This class contains the following fields:</p>
 * <ul>
 *     <li><b>questionId</b>: The unique identifier for the question (auto-incremented).</li>
 *     <li><b>questionUuid</b>: A unique UUID for the question, used for reference.</li>
 *     <li><b>questionDescription</b>: A description of the question. This field is mandatory and must be unique.</li>
 * </ul>
 * 
 * <p>The `Question` class helps store and manage the questions used for gathering feedback from employees, 
 * allowing them to provide answers and evaluate different aspects of merchants, devices, or services.</p>
 * 
 * @author Revanth K
 * @version MFMS_0.0.1
 */
@Entity
@Table(name = "question")
@Data
@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
public class Question {

    /**
     * The unique identifier for the question.
     * This is automatically generated and used as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    
    private Long questionId;

    /**
     * A unique identifier for the question, used for reference purposes.
     * This field must be provided and must be unique.
     */
    @Column(name = "question_uuid", nullable = true, unique = true)
    private String questionUuid;

    /**
     * The description of the question.
     * This field is mandatory and must be unique.
     */
    @Column(name = "question_description", nullable = false, unique = true)
    private String questionDescription;

	/**
	 * @return the questionId
	 */
	public Long getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	/**
	 * @return the questionUuid
	 */
	public String getQuestionUuid() {
		return questionUuid;
	}

	/**
	 * @param questionUuid the questionUuid to set
	 */
	public void setQuestionUuid(String questionUuid) {
		this.questionUuid = questionUuid;
	}

	/**
	 * @return the questionDescription
	 */
	public String getQuestionDescription() {
		return questionDescription;
	}

	/**
	 * @param questionDescription the questionDescription to set
	 */
	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}

	/**
	 * @param questionId
	 * @param questionUuid
	 * @param questionDescription
	 */
	public Question(Long questionId, String questionUuid, String questionDescription) {
		this.questionId = questionId;
		this.questionUuid = questionUuid;
		this.questionDescription = questionDescription;
	}

	/**
	 * 
	 */
	public Question() {
	}
    
    
}
