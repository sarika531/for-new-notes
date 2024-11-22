package com.payswiff.mfmsproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payswiff.mfmsproject.models.Feedback;
import com.payswiff.mfmsproject.models.FeedbackQuestionsAssociation;
import com.payswiff.mfmsproject.models.Question;

/**
 * Repository interface for managing {@link FeedbackQuestionsAssociation} entities.
 * This interface extends JpaRepository to provide basic CRUD operations and additional custom query methods
 * for associating feedback with specific questions.
 * <p>Spring Data JPA will automatically implement this interface, providing methods for standard CRUD operations 
 * as well as custom queries for managing the associations between Feedback and Question entities.</p>
 * 
 * @author Chatla Sarika
 * @version MFMS_0.0.1
 */
public interface FeedbackQuestionsAssociationRepository extends JpaRepository<FeedbackQuestionsAssociation, Integer> {

    /**
     * Checks if an association exists between the specified Feedback and Question.
     *
     * @param feedback The Feedback instance to check.
     * @param question The Question instance to check.
     * @return true if the association exists, false otherwise.
     */
    boolean existsByFeedbackAndQuestion(Feedback feedback, Question question);

    /**
     * Finds all FeedbackQuestionsAssociation entities associated with the specified Feedback.
     *
     * @param feedback The Feedback instance for which to find associations.
     * @return A list of FeedbackQuestionsAssociation entities linked to the specified Feedback.
     */
    List<FeedbackQuestionsAssociation> findByFeedback(Feedback feedback);
}
