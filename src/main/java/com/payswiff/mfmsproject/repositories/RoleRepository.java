package com.payswiff.mfmsproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payswiff.mfmsproject.models.Role;
/**
 * Repository interface for managing {@link Role} entities.
 * This interface extends JpaRepository to provide basic CRUD operations
 * for handling role data within the application.
 * <p>Spring Data JPA will automatically implement this interface, 
 * providing methods for standard CRUD operations on the Role entity.</p>
 * 
 * @author Gopi Bapanapalli
 * @version MFMS_0.0.1
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Finds a Role entity by its name.
     *
     * @param name The name of the role to search for.
     * @return An Optional containing the Role if found, otherwise an empty Optional.
     */
    Optional<Role> findByName(String name);
}
