package com.payswiff.mfmsproject.dtos;
/**
 * MerchantDeviceCountDTO is a Data Transfer Object that encapsulates
 * the number of devices associated with a specific merchant.
 * 
 * <p>This DTO is used to transfer the merchant's unique identifier and the count of 
 * devices associated with that merchant. It provides a structured way to share this 
 * data in API responses or requests.</p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */
public class MerchantDeviceCountDTO {
	
	private Long merchantId;  // Unique identifier for the merchant
    private Long deviceCount;  // Number of devices associated with the merchant

	/**
	 * Constructs a MerchantDeviceCountDTO with the specified merchant ID and device count.
	 *
	 * @param merchantId The unique identifier for the merchant.
	 * @param deviceCount The number of devices associated with the merchant.
	 */
	public MerchantDeviceCountDTO(Long merchantId, Long deviceCount) {
		this.merchantId = merchantId;
		this.deviceCount = deviceCount;
	}

	/**
	 * Gets the unique identifier for the merchant.
	 *
	 * @return The merchant ID as a Long.
	 */
	public Long getMerchantId() {
		return merchantId;
	}

	/**
	 * Sets the unique identifier for the merchant.
	 *
	 * @param merchantId The merchant ID to set.
	 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * Gets the number of devices associated with the merchant.
	 *
	 * @return The device count as a Long.
	 */
	public Long getDeviceCount() {
		return deviceCount;
	}

	/**
	 * Sets the number of devices associated with the merchant.
	 *
	 * @param deviceCount The device count to set.
	 */
	public void setDeviceCount(Long deviceCount) {
		this.deviceCount = deviceCount;
	}
}
