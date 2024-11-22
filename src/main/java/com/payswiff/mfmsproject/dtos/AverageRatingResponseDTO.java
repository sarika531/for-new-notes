package com.payswiff.mfmsproject.dtos;

/**
 * AverageRatingResponseDTO is a Data Transfer Object that encapsulates 
 * the average rating of a device along with its identifier.
 * <p>
 * This class is used to transfer information about a device's average rating 
 * between different layers of the application, typically in API responses.
 * </p>
 * 
 * @version MFMS_0.0.1
 * @author Revanth K
 */
public class AverageRatingResponseDTO {
    private Long deviceId; // The unique identifier for the device
    private Double averageRating; // The average rating associated with the device

    /**
     * Constructs a new AverageRatingResponseDTO with specified device ID and average rating.
     * 
     * @param deviceId The unique identifier of the device.
     * @param averageRating The average rating of the device.
     */
    public AverageRatingResponseDTO(Long deviceId, Double averageRating) {
        this.deviceId = deviceId;
        this.averageRating = averageRating;
    }

    /**
     * Default constructor for AverageRatingResponseDTO.
     */
    public AverageRatingResponseDTO() {
    }

    /**
     * Gets the device ID.
     * 
     * @return the deviceId
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * Sets the device ID.
     * 
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * Gets the average rating.
     * 
     * @return the averageRating
     */
    public Double getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the average rating.
     * 
     * @param averageRating the averageRating to set
     */
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
