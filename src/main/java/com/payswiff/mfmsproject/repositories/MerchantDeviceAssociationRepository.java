package com.payswiff.mfmsproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.models.Merchant;
import com.payswiff.mfmsproject.models.MerchantDeviceAssociation;


/**
 * Repository interface for managing {@link MerchantDeviceAssociation} entities.
 * This interface extends JpaRepository to provide basic CRUD operations and additional custom query methods
 * for managing associations between merchants and devices.
 * <p>Spring Data JPA will automatically implement this interface, providing methods for standard CRUD operations
 * and custom queries specific to the MerchantDeviceAssociation entity.</p>
 * 
 * @author Ruchitha G
 * @version MFMS_0.0.1
 */
@Repository
public interface MerchantDeviceAssociationRepository extends JpaRepository<MerchantDeviceAssociation, Integer> {
    
    /**
     * You can define additional query methods here if needed.
     * For example, methods to find associations by merchant or device.
     */
    List<MerchantDeviceAssociation> findAllByMerchant(Merchant merchant);
    boolean existsByMerchantAndDevice(Merchant merchant, Device device);
    
    @Query("SELECT m.id AS merchantId, COUNT(md.device.id) AS deviceCount " +
            "FROM MerchantDeviceAssociation md " +
            "JOIN md.merchant m " +
            "GROUP BY m.id")
     List<Object[]> countDevicesByMerchant();
}
