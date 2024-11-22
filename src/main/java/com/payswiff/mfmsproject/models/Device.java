package com.payswiff.mfmsproject.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Represents a device entity in the system.
 * This class is mapped to the 'device' table in the database and holds information about
 * a specific device in the system. The device has attributes like unique UUID, model, 
 * manufacturer, creation time, and last updated time. The entity supports automatic 
 * timestamping for creation and updates.
 * <p>
 * This class uses Lombok annotations for reducing boilerplate code:
 * <ul>
 *     <li>@Getter and @Setter: Automatically generates getter and setter methods for all fields.</li>
 *     <li>@Builder: Provides a builder pattern to create instances of the class.</li>
 *     <li>@AllArgsConstructor: Generates a constructor with all fields as parameters.</li>
 *     <li>@NoArgsConstructor: Generates a no-arguments constructor.</li>
 * </ul>
 * </p>
 * 
 * @Entity Marks the class as a JPA entity to be mapped to a database table.
 * @Table(name = "device") Specifies the table name in the database.
 * @Id Denotes the primary key field of the entity.
 * @GeneratedValue(strategy = GenerationType.IDENTITY) Automatically generates the primary key value.
 * @Column Specifies the database column mapping for each field.
 * @CreationTimestamp Automatically sets the creation timestamp when the device is created.
 * @UpdateTimestamp Automatically updates the timestamp when the device is updated.
 * 
 * @author Ruchitha Guttikonda
 * @version MFMS_0.0.1
 */

@Entity
@Getter
@Setter
@Builder // Lombok annotation to provide a builder pattern for creating Device objects
//@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
//@NoArgsConstructor // Lombok annotation to generate a default constructor
@Table(name = "device") // Specifies the name of the table in the database
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing primary key
    @Column(name = "device_id") // Maps to the 'device_id' column in the database
    private Long deviceId;

    @Column(name = "device_uuid", nullable = false, unique = true) // Unique identifier for the device
    private String deviceUuid;

    @Column(name = "device_model", nullable = false, unique = true) // Model of the device, must be unique
    private String deviceModel;

    @Column(name = "device_manufacturer", nullable = false) // Manufacturer of the device
    private String deviceManufacturer;

    @Column(name = "device_creation_time") // Timestamp of when the device was created
    @CreationTimestamp // Automatically sets this field to the current timestamp upon creation
    private LocalDateTime deviceCreationTime;
    
    @UpdateTimestamp // Automatically updates this field to the current timestamp upon updates
    @Column(name = "device_updation_time") // Timestamp of the last update to the device
    private LocalDateTime deviceUpdationTime;

	/**
	 * @return the deviceId
	 */
	public Long getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceUuid
	 */
	public String getDeviceUuid() {
		return deviceUuid;
	}

	/**
	 * @param deviceUuid the deviceUuid to set
	 */
	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}

	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	/**
	 * @return the deviceManufacturer
	 */
	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}

	/**
	 * @param deviceManufacturer the deviceManufacturer to set
	 */
	public void setDeviceManufacturer(String deviceManufacturer) {
		this.deviceManufacturer = deviceManufacturer;
	}

	/**
	 * @return the deviceCreationTime
	 */
	public LocalDateTime getDeviceCreationTime() {
		return deviceCreationTime;
	}

	/**
	 * @param deviceCreationTime the deviceCreationTime to set
	 */
	public void setDeviceCreationTime(LocalDateTime deviceCreationTime) {
		this.deviceCreationTime = deviceCreationTime;
	}

	/**
	 * @return the deviceUpdationTime
	 */
	public LocalDateTime getDeviceUpdationTime() {
		return deviceUpdationTime;
	}

	/**
	 * @param deviceUpdationTime the deviceUpdationTime to set
	 */
	public void setDeviceUpdationTime(LocalDateTime deviceUpdationTime) {
		this.deviceUpdationTime = deviceUpdationTime;
	}

	/**
	 * @param deviceId
	 * @param deviceUuid
	 * @param deviceModel
	 * @param deviceManufacturer
	 * @param deviceCreationTime
	 * @param deviceUpdationTime
	 */
	public Device(Long deviceId, String deviceUuid, String deviceModel, String deviceManufacturer,
			LocalDateTime deviceCreationTime, LocalDateTime deviceUpdationTime) {
		this.deviceId = deviceId;
		this.deviceUuid = deviceUuid;
		this.deviceModel = deviceModel;
		this.deviceManufacturer = deviceManufacturer;
		this.deviceCreationTime = deviceCreationTime;
		this.deviceUpdationTime = deviceUpdationTime;
	}

	public Device() {
		
	}
    
}
