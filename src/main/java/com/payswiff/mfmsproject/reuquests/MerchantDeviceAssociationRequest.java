package com.payswiff.mfmsproject.reuquests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a request to associate a device with a merchant.
 * This class holds the necessary information required for the association.
 * @author Revanth K
 * @version MFMS_0.0.1
 */

@NoArgsConstructor
@Builder
@Data
public class MerchantDeviceAssociationRequest {
    
    /**
     * The ID of the merchant to which the device will be associated.
     */
    private Long merchantId; // ID of the merchant

    /**
     * The ID of the device to be associated with the merchant.
     */
    private Long deviceId;    // ID of the device

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @param merchantId
	 * @param deviceId
	 */
	public MerchantDeviceAssociationRequest(Long merchantId, Long deviceId) {
		
		this.merchantId = merchantId;
		this.deviceId = deviceId;
	}
	
}
