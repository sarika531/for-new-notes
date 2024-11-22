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
 * Represents the association between a merchant and a device.
 * This class maps to the 'merchant_device_association' table in the database, 
 * linking a merchant with a device in the system. The class establishes 
 * a many-to-one relationship between the merchant and the device, where a merchant can have multiple devices.
 * 
 * <p>This class contains the following fields:</p>
 * <ul>
 *     <li><b>id</b>: A unique identifier for the association (auto-incremented).</li>
 *     <li><b>merchant</b>: A reference to the merchant associated with the device, 
 *         establishing a relationship with the Merchant entity.</li>
 *     <li><b>device</b>: A reference to the device associated with the merchant, 
 *         establishing a relationship with the Device entity.</li>
 * </ul>
 * 
 * <p>This class helps to manage the relationship between merchants and the devices they are linked to, 
 * enabling the system to associate each device with a specific merchant.</p>
 * 
 * @author Ruchitha Guttikonda
 * @version MFMS_0.0.1
 */
@Entity
@Table(name = "merchant_device_association")
//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@Builder
public class MerchantDeviceAssociation {

    /**
     * The unique identifier for the association.
     * This is automatically generated and used as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;  // Auto-increment ID for the association

    /**
     * The merchant associated with this device.
     * This establishes a many-to-one relationship with the Merchant entity.
     */
    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant; 

    /**
     * The device associated with this merchant.
     * This establishes a many-to-one relationship with the Device entity.
     */
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;  // Foreign key referencing the device

	/**
	 * @param id
	 * @param merchant
	 * @param device
	 */
	public MerchantDeviceAssociation(Integer id, Merchant merchant, Device device) {
		this.id = id;
		this.merchant = merchant;
		this.device = device;
	}

	public MerchantDeviceAssociation() {
		// TODO Auto-generated constructor stub
	}

	public MerchantDeviceAssociation(Merchant merchant2, Device device1) {
		// TODO Auto-generated constructor stub
		this.merchant=merchant2;
		this.device=device1;
	}

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
	 * @return the merchant
	 */
	public Merchant getMerchant() {
		return merchant;
	}

	/**
	 * @param merchant the merchant to set
	 */
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	/**
	 * @return the device
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(Device device) {
		this.device = device;
	}

	/**
	 * 
	 */
	
    
}
