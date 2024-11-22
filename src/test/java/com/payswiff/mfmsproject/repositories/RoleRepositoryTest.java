package com.payswiff.mfmsproject.repositories;

import com.payswiff.mfmsproject.models.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RoleRepository} class.
 * These tests validate the repository's ability to find roles by their names,
 * ensuring that valid and invalid inputs are handled as expected.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Sets up the test environment before each test case.
     * Clears any existing data in the repository to prevent interference
     * from prior test runs.
     */
    @BeforeEach
    void setUp() {
        roleRepository.deleteAll(); // Clear any existing data
    }

    /**
     * Cleans up the test environment after each test case.
     * Ensures the repository is emptied out to maintain isolation between tests.
     */
    @AfterEach
    void tearDown() {
        roleRepository.deleteAll(); // Clean up after each test
    }

    /**
     * Positive test case for {@link RoleRepository#findByName(String)}.
     * This test verifies that a role with the name "ROLE_ADMIN" is successfully found
     * after being saved in the repository.
     */
    @Test
    void testFindByNameWhenRoleExists() {
        // Arrange: Create and save a role with the name "ROLE_ADMIN"
        Role role = new Role();
        role.setName("ROLE_admin");
        roleRepository.save(role);

        // Act: Attempt to retrieve the role by its name
        Optional<Role> foundRole = roleRepository.findByName("ROLE_admin");

        // Assert: Verify the role was found and has the expected name
        assertTrue(foundRole.isPresent(), "Role should be found in the repository.");
        assertEquals("ROLE_admin", foundRole.get().getName(), "Role name should match the saved value.");
    }

    /**
     * Negative test case for {@link RoleRepository#findByName(String)}.
     * This test verifies that attempting to find a role with the name "ROLE_USER"
     * returns an empty result if no such role exists in the repository.
     */
    @Test
    void testFindByNameWhenRoleDoesNotExist() {
        // Act: Attempt to retrieve a non-existent role with the name "ROLE_USER"
        Optional<Role> foundRole = roleRepository.findByName("ROLE_user");

        // Assert: Verify no role was found
        assertFalse(foundRole.isPresent(), "No role should be found for a non-existent role name.");
    }

    /**
     * Edge case test for {@link RoleRepository#findByName(String)}.
     * This test verifies that passing null as the name does not cause any exceptions
     * and returns an empty result.
     */
    @Test
    void testFindByNameWithNullName() {
        // Act: Attempt to retrieve a role using a null name
        Optional<Role> foundRole = roleRepository.findByName(null);

        // Assert: Verify no role was found
        assertFalse(foundRole.isPresent(), "Null input should return an empty result.");
    }

    /**
     * Edge case test for {@link RoleRepository#findByName(String)}.
     * This test verifies that passing an empty string as the name does not cause
     * any exceptions and returns an empty result.
     */
    @Test
    void testFindByNameWithEmptyString() {
        // Act: Attempt to retrieve a role using an empty string as the name
        Optional<Role> foundRole = roleRepository.findByName("");

        // Assert: Verify no role was found
        assertFalse(foundRole.isPresent(), "Empty string should return an empty result.");
    }

    /**
     * Case sensitivity test for {@link RoleRepository#findByName(String)}.
     * This test checks if the repository's search is case-sensitive.
     * By default, it should return an empty result if the case does not match.
     */
    @Test
    void testFindByNameWithDifferentCase() {
        // Arrange: Create and save a role with mixed-case name "ROLE_Admin"
        Role role = new Role();
        role.setName("ROLE_admin");
        roleRepository.save(role);

        // Act: Attempt to retrieve the role with all-uppercase name "ROLE_ADMIN"
        Optional<Role> foundRole = roleRepository.findByName("ROLE_ADMIN");

        // Assert: Verify that no role was found due to case mismatch
        assertFalse(foundRole.isPresent(), "The repository should be case-sensitive unless specified otherwise.");
    }
}
