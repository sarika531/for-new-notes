package com.payswiff.mfmsproject.reuquests;

import java.util.UUID;

import com.payswiff.mfmsproject.models.Question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

/**
 * Represents a request to create a new Question.
 * This class holds the necessary information to create a Question entity.
 * @author Revanth K
 * @version MFMS_0.0.1
 */
@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateQuestionRequest {
    
    /**
     * The description of the question.
     */
    private String questionDescription;

    /**
     * Converts this CreateQuestionRequest to a Question entity.
     *
     * @return A new Question entity populated with the data from this request,
     *         including a randomly generated UUID for the question.
     */
    public Question toQuestion() {
//        return Question.builder()
//                .questionUuid(UUID.randomUUID().toString())
//                .questionDescription(questionDescription)
//                .build();
    	 ModelMapper modelMapper = new ModelMapper();
    	    Question question = modelMapper.map(this, Question.class);
    	    question.setQuestionUuid(UUID.randomUUID().toString()); // Set UUID separately
    	    return question;
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
	 * @param questionDescription
	 */
	public CreateQuestionRequest(String questionDescription) {
		this.questionDescription = questionDescription;
	}

	/**
	 * 
	 */
	public CreateQuestionRequest() {
	}
    
}
