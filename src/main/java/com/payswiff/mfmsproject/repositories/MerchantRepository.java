package com.payswiff.mfmsproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.payswiff.mfmsproject.models.Merchant;

/**
 * Repository interface for managing {@link Merchant} entities.
 * This interface extends JpaRepository to provide basic CRUD operations 
 * and additional custom query methods for Merchant-related data access.
 * <p>Spring Data JPA will automatically implement this interface, providing methods for standard CRUD operations 
 * and custom queries specific to the Merchant entity.</p>
 * 
 * @author Ruchitha Guttikonda
 * @version MFMS_0.0.1
 */
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    /**
     * Finds a merchant by their email or phone number.
     *
     * @param email The email of the merchant to search for.
     * @param phone The phone number of the merchant to search for.
     * @return The Merchant object if found, or null if no merchant exists with the provided email or phone.
     */
    @Query("SELECT m FROM Merchant m WHERE m.merchantEmail = :email OR m.merchantPhone = :phone")
    Merchant findByMerchantEmailOrMerchantPhone(@Param("email") String email, @Param("phone") String phone);
}
