package com.payswiff.mfmsproject.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a merchant entity in the system.
 * This class is mapped to the 'merchant' table in the database and is used to store
 * information about a merchant, including their contact details, business information,
 * and timestamps for when the merchant was created or updated.
 * 
 * <p>This class contains the following fields:</p>
 * <ul>
 *     <li><b>merchantId</b>: A unique identifier for the merchant (auto-incremented).</li>
 *     <li><b>merchantUuid</b>: A unique UUID used to identify the merchant across systems.</li>
 *     <li><b>merchantName</b>: The name of the merchant (used for communication and identification).</li>
 *     <li><b>merchantEmail</b>: The email address of the merchant, which must be unique.</li>
 *     <li><b>merchantPhone</b>: The phone number of the merchant, which must be unique.</li>
 *     <li><b>merchantBusinessName</b>: The business name under which the merchant operates.</li>
 *     <li><b>merchantBusinessType</b>: The type of business the merchant is engaged in (used for categorization).</li>
 *     <li><b>merchantCreationTime</b>: The timestamp of when the merchant was created in the system.</li>
 *     <li><b>merchantUpdationTime</b>: The timestamp of when the merchant's details were last updated.</li>
 * </ul>
 * 
 * <p>This class ensures that the system can store and manage merchant information and supports features
 * such as unique identification via UUIDs and timestamps for creation and updates.</p>
 * 
 * @author Ruchitha Guttikonda
 * @version MFMS_0.0.1
 */
@Entity
@Table(name = "merchant")
@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class Merchant {

    /**
     * The unique identifier for the merchant.
     * This is automatically generated and used as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long merchantId;

    /**
     * The unique UUID of the merchant.
     * This is used to identify the merchant across different systems.
     */
    @Column(name = "merchant_uuid", nullable = false, unique = true, length = 36)
    private String merchantUuid;

    /**
     * The email address of the merchant.
     * This must be unique and is used for communication and identification.
     */
    @Column(name = "merchant_name", nullable = false, unique = false)
    private String merchantName;

    /**
     * The email address of the merchant.
     * This must be unique and is used for communication and identification.
     */
    @Column(name = "merchant_email", nullable = false, unique = true)
    private String merchantEmail;

    /**
     * The phone number of the merchant.
     * This must be unique and is used for communication purposes.
     */
    @Column(name = "merchant_phone", nullable = false, unique = true)
    private String merchantPhone;

    /**
     * The business name of the merchant.
     * This represents the name under which the merchant operates.
     */
    @Column(name = "merchant_business_name", nullable = false)
    private String merchantBusinessName;

    /**
     * The type of business the merchant is engaged in.
     * This can be used for categorization or reporting purposes.
     */
    @Column(name = "merchant_business_type", nullable = false)
    private String merchantBusinessType;
    
    /**
     * The timestamp when the merchant was created.
     * This is automatically set when the merchant is first created.
     */
    @Column(name = "merchant_creation_time")
    @CreationTimestamp
    private LocalDateTime merchantCreationTime;

    /**
     * The timestamp when the merchant was last updated.
     * This is automatically updated whenever the merchant's details are modified.
     */
    @UpdateTimestamp
    @Column(name = "merchant_updation_time")
    private LocalDateTime merchantUpdationTime;

	/**
	 * @return the merchantId
	 */
	public Long getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return the merchantUuid
	 */
	public String getMerchantUuid() {
		return merchantUuid;
	}

	/**
	 * @param merchantUuid the merchantUuid to set
	 */
	public void setMerchantUuid(String merchantUuid) {
		this.merchantUuid = merchantUuid;
	}

	/**
	 * @return the merchantEmail
	 */
	public String getmerchantName() {
		return merchantName;
	}

	/**
	 * @param merchantEmail the merchantEmail to set
	 */
	public void setmerchantName(String merchantName) {
		this.merchantName =merchantName;
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
	 * @return the merchantCreationTime
	 */
	public LocalDateTime getMerchantCreationTime() {
		return merchantCreationTime;
	}

	/**
	 * @param merchantCreationTime the merchantCreationTime to set
	 */
	public void setMerchantCreationTime(LocalDateTime merchantCreationTime) {
		this.merchantCreationTime = merchantCreationTime;
	}

	/**
	 * @return the merchantUpdationTime
	 */
	public LocalDateTime getMerchantUpdationTime() {
		return merchantUpdationTime;
	}

	/**
	 * @param merchantUpdationTime the merchantUpdationTime to set
	 */
	public void setMerchantUpdationTime(LocalDateTime merchantUpdationTime) {
		this.merchantUpdationTime = merchantUpdationTime;
	}


	/**
	 * @param merchantId
	 * @param merchantUuid
	 * @param merchantName
	 * @param merchantEmail
	 * @param merchantPhone
	 * @param merchantBusinessName
	 * @param merchantBusinessType
	 * @param merchantCreationTime
	 * @param merchantUpdationTime
	 */
	public Merchant(Long merchantId, String merchantUuid, String merchantName, String merchantEmail,
			String merchantPhone, String merchantBusinessName, String merchantBusinessType,
			LocalDateTime merchantCreationTime, LocalDateTime merchantUpdationTime) {
		this.merchantId = merchantId;
		this.merchantUuid = merchantUuid;
		this.merchantName = merchantName;
		this.merchantEmail = merchantEmail;
		this.merchantPhone = merchantPhone;
		this.merchantBusinessName = merchantBusinessName;
		this.merchantBusinessType = merchantBusinessType;
		this.merchantCreationTime = merchantCreationTime;
		this.merchantUpdationTime = merchantUpdationTime;
	}

	/**
	 * 
	 */
	public Merchant() {
	}

	public Merchant(long l, String string, String string2, String string3) {
		// TODO Auto-generated constructor stub
	}
    
    
}
