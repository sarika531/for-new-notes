package com.payswiff.mfmsproject.repositories;

import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.models.Question;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link QuestionRepository} interface.
 * This class tests both positive and negative scenarios for the repository's methods.
 */
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Set up test data before any test cases run.
     */
    @BeforeAll
    static void setUpBeforeClass() {
        // This is where setup logic would go if needed, e.g., starting external resources.
    }

    /**
     * Clean up after all test cases have run.
     */
    @AfterAll
    static void tearDownAfterClass() {
        // This is where teardown logic would go if needed.
    }

    /**
     * Test the findByDescription method for a positive scenario.
     * This tests that a question can be found by its description.
     */
    @Test
    void testFindByDescription_Positive() {
        // Arrange: Create and save a Question entity to the repository.
        Question question = new Question();
        question.setQuestionDescription("What is Java?");
        questionRepository.save(question);

        // Act: Try to find the Question by its description.
        Optional<Question> foundQuestion = questionRepository.findByDescription("What is Java?");

        // Assert: Check that the question was found and the description matches.
        assertThat(foundQuestion).isPresent();
        assertThat(foundQuestion.get().getQuestionDescription()).isEqualTo("What is Java?");
    }

    /**
     * Test the findByDescription method for a negative scenario.
     * This tests that searching for a non-existent description returns an empty result.
     */
    @Test
    void testFindByDescription_Negative() {
        // Act: Attempt to find a Question by a description that does not exist.
        Optional<Question> foundQuestion = questionRepository.findByDescription("Non-existent description");

        // Assert: Verify that the result is empty, meaning the Question was not found.
        assertThat(foundQuestion).isNotPresent();
    }

    /**
     * Test the findById method for a positive scenario.
     * This tests that a question can be found by its ID.
     */
    @Test
    void testFindById_Positive() {
        // Arrange: Create and save a Question entity to the repository.
        Question question = new Question();
        question.setQuestionDescription("What is Spring Boot?");
        question = questionRepository.save(question);

        // Act: Try to find the Question by its ID.
        Optional<Question> foundQuestion = questionRepository.findById(question.getQuestionId());

        // Assert: Check that the question was found and the ID matches.
        assertThat(foundQuestion).isPresent();
        assertThat(foundQuestion.get().getQuestionId()).isEqualTo(question.getQuestionId());
    }

    /**
     * Test the findById method for a negative scenario.
     * This tests that searching for a non-existent ID returns an empty result.
     */
    @Test
    void testFindById_Negative() {
        // Act: Attempt to find a Question by a non-existent ID.
        Optional<Question> foundQuestion = questionRepository.findById(999L);

        // Assert: Verify that the result is empty, meaning no Question was found.
        assertThat(foundQuestion).isNotPresent();
    }

    /**
     * Test for attempting to save a Question with a null description.
     * This test verifies that a DataIntegrityViolationException is thrown.
     */
    @Test
    void testSave_NullDescription() {
        // Arrange: Create a Question with a null description, violating a constraint.
        Question question = new Question();
        question.setQuestionDescription(null);

        // Act & Assert: Attempting to save should throw a DataIntegrityViolationException.
        assertThrows(DataIntegrityViolationException.class, () -> questionRepository.save(question));
    }

    /**
     * Test for saving a Question with a duplicate description.
     * This tests that saving two Questions with the same description
     * results in a DataIntegrityViolationException being thrown.
     */
    @Test
    void testSave_DuplicateDescription() {
        // Arrange: Create and save a Question entity with a unique description.
        Question question1 = new Question();
        question1.setQuestionDescription("What is a repository?");
        questionRepository.save(question1); // First save should succeed.

        // Arrange: Create a second Question with the same description.
        Question question2 = new Question();
        question2.setQuestionDescription("What is a repository?");

        // Act & Assert: Attempting to save the second Question should throw a DataIntegrityViolationException.
        assertThrows(DataIntegrityViolationException.class, () -> questionRepository.save(question2));
    }

    /**
     * Test to ensure the repository supports case sensitivity.
     * This tests that a search with a different case does not find a result if the search is case-sensitive.
     */
    @Test
    void testFindByDescription_CaseSensitivity() {
        // Arrange: Save a Question with a specific description.
        Question question = new Question();
        question.setQuestionDescription("What is Python?");
        questionRepository.save(question);

        // Act: Attempt to find the Question using a differently-cased description.
        Optional<Question> foundQuestion = questionRepository.findByDescription("WHAT IS PYTHON?");

        // Assert: Verify that the question is not found due to case sensitivity.
        assertThat(foundQuestion).isNotPresent();
    }
    /**
     * Test to verify handling of an empty description.
     * This checks if the repository allows saving a question with an empty description.
     * Adjust this test if the `description` field has `@NotEmpty` or `@NotBlank` annotations.
     */
    @Test
    void testSave_EmptyDescription() {
        // Arrange: Create a Question with an empty description
        Question question = new Question();
        question.setQuestionDescription("");

        questionRepository.save(question);
      
    }

    /**
     * Test to verify saving a Question with a description that includes special characters.
     * Ensures that the repository can store and retrieve descriptions with special characters.
     */
    @Test
    void testFindByDescription_SpecialCharacters() {
        // Arrange: Create and save a Question with special characters in the description
        Question question = new Question();
        question.setQuestionDescription("What is C++? && Why use pointers?");
        questionRepository.save(question);

        // Act: Try to retrieve the Question by its description
        Optional<Question> foundQuestion = questionRepository.findByDescription("What is C++? && Why use pointers?");

        // Assert: Verify that the question is found and the description matches exactly
        assertThat(foundQuestion).isPresent();
        assertThat(foundQuestion.get().getQuestionDescription()).isEqualTo("What is C++? && Why use pointers?");
    }

    /**
     * Test to verify that a null ID lookup returns an empty result.
     * This tests that the repository handles null ID inputs gracefully.
     */
    @Test
    void testFindById_NullId() {
        // Act & Assert: Attempting to find by a null ID should return an empty Optional.
        assertThrows(InvalidDataAccessApiUsageException.class, () -> questionRepository.findById(null));
    }

   

    /**
     * Test to ensure saving and finding multiple Questions by unique descriptions.
     * Verifies the ability to handle multiple records and confirm data integrity.
     */
    @Test
    void testSaveAndFindMultipleQuestions() {
        // Arrange: Save multiple distinct Questions
        Question question1 = new Question();
        question1.setQuestionDescription("What is polymorphism?");
        Question question2 = new Question();
        question2.setQuestionDescription("Explain inheritance.");

        questionRepository.save(question1);
        questionRepository.save(question2);

        // Act: Retrieve each Question by its description
        Optional<Question> foundQuestion1 = questionRepository.findByDescription("What is polymorphism?");
        Optional<Question> foundQuestion2 = questionRepository.findByDescription("Explain inheritance.");

        // Assert: Verify that each question is found and has the correct description
        assertThat(foundQuestion1).isPresent();
        assertThat(foundQuestion1.get().getQuestionDescription()).isEqualTo("What is polymorphism?");
        assertThat(foundQuestion2).isPresent();
        assertThat(foundQuestion2.get().getQuestionDescription()).isEqualTo("Explain inheritance.");
    }

    /**
     * Test to check the repository's handling of very long descriptions.
     * Tests the storage and retrieval of a description at the boundary of the allowed length.
     * Adjust the length based on the database column definition for questionDescription.
     */
    @Test
    void testSaveAndFindVeryLongDescription() {
        // Arrange: Create and save a Question with a very long description
        String longDescription = "A".repeat(255);  // Adjust length based on database column size
        Question question = new Question();
        question.setQuestionDescription(longDescription);

        questionRepository.save(question);

        // Act: Retrieve the Question by its long description
        Optional<Question> foundQuestion = questionRepository.findByDescription(longDescription);

        // Assert: Verify that the Question is found and the description matches
        assertThat(foundQuestion).isPresent();
        assertThat(foundQuestion.get().getQuestionDescription()).isEqualTo(longDescription);
    }

   
}
