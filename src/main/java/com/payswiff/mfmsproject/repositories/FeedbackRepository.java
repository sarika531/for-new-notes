package com.payswiff.mfmsproject.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.models.Feedback;
import com.payswiff.mfmsproject.models.Merchant;

/**
 * Repository interface for managing {@link Feedback} entities.
 * This interface extends JpaRepository to provide basic CRUD operations and additional custom query methods
 * for handling feedback data.
 * <p>Spring Data JPA will automatically implement this interface, providing methods for standard CRUD operations 
 * and custom queries specific to the Feedback entity.</p>
 * 
 * @author Chatla Sarika
 * @version MFMS_0.0.1
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    /**
     * Finds all Feedback entries associated with the specified Employee.
     *
     * @param optional An Optional containing the Employee instance for which to find feedback.
     * @return A list of Feedback entries linked to the specified Employee.
     */
    List<Feedback> findByFeedbackEmployee(Optional<Employee> optional);

    /**
     * Finds all Feedback entries associated with the specified Device.
     *
     * @param deviceFromDb An Optional containing the Device instance for which to find feedback.
     * @return A list of Feedback entries linked to the specified Device.
     */
    List<Feedback> findByFeedbackDevice(Optional<Device> deviceFromDb);

    /**
     * Finds all Feedback entries associated with the specified Merchant.
     *
     * @param merchantFromDb An Optional containing the Merchant instance for which to find feedback.
     * @return A list of Feedback entries linked to the specified Merchant.
     */
    List<Feedback> findByFeedbackMerchant(Optional<Merchant> merchantFromDb);

    /**
     * Finds all Feedback entries with the specified rating.
     *
     * @param rating The rating to filter Feedback entries.
     * @return A list of Feedback entries matching the specified rating.
     */
    List<Feedback> findByFeedbackRating(Integer rating);

    /**
     * Counts the number of feedbacks grouped by each Employee's ID.
     *
     * @return A list of Object arrays, where each array contains an employee ID and the corresponding feedback count.
     */
    @Query("SELECT f.feedbackEmployee.employeeId AS employeeId, COUNT(f) AS feedbackCount "
         + "FROM Feedback f GROUP BY f.feedbackEmployee.employeeId")
    List<Object[]> countFeedbacksByEmployee();

    /**
     * Calculates the average rating for feedback grouped by each Device's ID.
     *
     * @return A list of Object arrays, where each array contains a device ID and the corresponding average rating.
     */
    @Query("SELECT f.feedbackDevice.deviceId AS deviceId, AVG(f.feedbackRating) AS averageRating "
         + "FROM Feedback f GROUP BY f.feedbackDevice.deviceId")
    List<Object[]> avgRatingByDevice();

    /**
     * Counts the number of feedbacks grouped by each Device's ID.
     *
     * @return A list of Object arrays, where each array contains a device ID and the corresponding feedback count.
     */
    @Query("SELECT f.feedbackDevice.deviceId, COUNT(f) " +
           "FROM Feedback f " +
           "GROUP BY f.feedbackDevice.deviceId")
    List<Object[]> countFeedbacksByDevice();
}
