package com.payswiff.mfmsproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.payswiff.mfmsproject.models.Question;

import java.util.Optional;
/**
 * Repository interface for managing {@link Question} entities.
 * This interface extends JpaRepository to provide basic CRUD operations 
 * and additional custom query methods for Question-related data access.
 * <p>Spring Data JPA will automatically implement this interface, providing methods for standard CRUD operations 
 * and custom queries specific to the Question entity.</p>
 * 
 * @author Revanth K
 * @version MFMS_0.0.1
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Finds a question by its description.
     *
     * @param description The description of the question to search for.
     * @return An Optional containing the found Question, or an empty Optional if no question exists with the provided description.
     */
    @Query("SELECT q FROM Question q WHERE q.questionDescription = :description")
    Optional<Question> findByDescription(@Param("description") String description);
    
    /**
     * Finds a question by its ID.
     *
     * @param id The ID of the question to search for.
     * @return An Optional containing the found Question, or an empty Optional if no question exists with the provided ID.
     */
    Optional<Question> findById(Long id);
}
