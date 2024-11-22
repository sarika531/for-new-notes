package com.payswiff.mfmsproject.reuquests;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payswiff.mfmsproject.models.Merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;


/**
 * Represents a request to create a new Merchant.
 * <p>This class holds the necessary information required to create a Merchant entity, 
 * such as the merchant's email, name, phone number, business name, and business type.</p>
 * 
 * @author Revanth K
 * @version MFMS_0.0.1
 */
@Data
@Getter
@Setter

//@AllArgsConstructor
//@NoArgsConstructor
public class CreateMerchantRequest {

    /**
     * The email address of the merchant.
     */
    private String merchantEmail;
    /**
     * The name of the merchant.
     */
    private String merchantName;

    /**
     * The phone number of the merchant.
     */
    private String merchantPhone;

    /**
     * The name of the merchant's business.
     */
    private String merchantBusinessName;

    /**
     * The type of the merchant's business.
     */
    private String merchantBusinessType;

    /**
     * Converts this CreateMerchantRequest to a Merchant entity.
     *
     * @return A new Merchant entity populated with the data from this request,
     *         including a randomly generated UUID for the merchant.
     */
    public Merchant toMerchant() {

    	ModelMapper modelMapper = new ModelMapper();
        Merchant merchant = modelMapper.map(this, Merchant.class);
        
        merchant.setMerchantUuid(UUID.randomUUID().toString()); // Set UUID separately
        return merchant;
    }

	/**
	 * @return the merchantName
	 */
	public String getmerchantName() {
		return merchantEmail;
	}

	/**
	 * @param merchantName the merchantEmail to set
	 */
	public void setmerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	/**
	 * @return the merchantEmail
	 */
	public String getMerchantEmail() {
		return merchantEmail;
	}

	/**
	 * @param merchantEmail the merchantEmail to set
	 */
	public void setMerchantEmail(String merchantEmail) {
		this.merchantEmail = merchantEmail;
	}

	/**
	 * @return the merchantPhone
	 */
	public String getMerchantPhone() {
		return merchantPhone;
	}

	/**
	 * @param merchantPhone the merchantPhone to set
	 */
	public void setMerchantPhone(String merchantPhone) {
		this.merchantPhone = merchantPhone;
	}

	/**
	 * @return the merchantBusinessName
	 */
	public String getMerchantBusinessName() {
		return merchantBusinessName;
	}

	/**
	 * @param merchantBusinessName the merchantBusinessName to set
	 */
	public void setMerchantBusinessName(String merchantBusinessName) {
		this.merchantBusinessName = merchantBusinessName;
	}

	/**
	 * @return the merchantBusinessType
	 */
	public String getMerchantBusinessType() {
		return merchantBusinessType;
	}

	/**
	 * @param merchantBusinessType the merchantBusinessType to set
	 */
	public void setMerchantBusinessType(String merchantBusinessType) {
		this.merchantBusinessType = merchantBusinessType;
	}

	
	/**
	 * 
	 */
	public CreateMerchantRequest() {
	}

	/**
	 * @param merchantEmail
	 * @param merchantName
	 * @param merchantPhone
	 * @param merchantBusinessName
	 * @param merchantBusinessType
	 */
	public CreateMerchantRequest(String merchantEmail, String merchantName, String merchantPhone,
			String merchantBusinessName, String merchantBusinessType) {
		this.merchantEmail = merchantEmail;
		this.merchantName = merchantName;
		this.merchantPhone = merchantPhone;
		this.merchantBusinessName = merchantBusinessName;
		this.merchantBusinessType = merchantBusinessType;
	}
    
}
