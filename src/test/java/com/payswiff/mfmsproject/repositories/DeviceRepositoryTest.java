package com.payswiff.mfmsproject.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.payswiff.mfmsproject.models.Device;

/**
 * Test class for the DeviceRepository.
 * This class tests the methods in DeviceRepository
 * to ensure correct functionality and behavior.
 */
@DataJpaTest // Configures an in-memory database for testing JPA repositories
@Transactional // Ensures that each test runs in a transaction, rolled back after test completion
class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository; // Inject the DeviceRepository

    /**
     * Set up a test environment with multiple device models before each test.
     */
    @BeforeEach
    void setUp() {
        // Create test devices and save them to the repository
        Device posDevice = new Device();
        posDevice.setDeviceUuid(UUID.randomUUID().toString());
        posDevice.setDeviceModel("POS");
        posDevice.setDeviceManufacturer("NEWLAND");
        deviceRepository.save(posDevice);

        Device mposDevice = new Device();
        mposDevice.setDeviceModel("MPOS");
        mposDevice.setDeviceManufacturer("NEWLAND");
        mposDevice.setDeviceUuid(UUID.randomUUID().toString());
        deviceRepository.save(mposDevice);

        Device digitalPosDevice = new Device();
        digitalPosDevice.setDeviceModel("DIGITALPOS");
        digitalPosDevice.setDeviceManufacturer("NEXIGO");
        digitalPosDevice.setDeviceUuid(UUID.randomUUID().toString());
        deviceRepository.save(digitalPosDevice);

        Device androidDevice = new Device();
        androidDevice.setDeviceModel("ANDROID");
        androidDevice.setDeviceManufacturer("NEXIGO");
        androidDevice.setDeviceUuid(UUID.randomUUID().toString());
        deviceRepository.save(androidDevice);

        Device soundboxDevice = new Device();
        soundboxDevice.setDeviceModel("SOUNDBOX");
        soundboxDevice.setDeviceManufacturer("NEXIGO");
        soundboxDevice.setDeviceUuid(UUID.randomUUID().toString());
        deviceRepository.save(soundboxDevice);
    }

    /**
     * Clean up after each test.
     */
    @AfterEach
    void tearDown() {
        // Clear the repository after each test to maintain isolation
        deviceRepository.deleteAll();
    }

    /**
     * Positive test case: Test finding a device by a valid model (e.g., POS).
     */
    @Test
    void testFindByModel_ValidModel() {
        // Act: Find the device by the model
        Device foundDevice = deviceRepository.findByModel("POS");

        // Assert: Verify that the found device is not null and matches the expected model
        assertNotNull(foundDevice, "Device should be found by model");
        assertEquals("POS", foundDevice.getDeviceModel(), "The device model should match");
    }

    /**
     * Positive test case: Test finding another device by a valid model (e.g., ANDROID).
     */
    @Test
    void testFindByModel_ValidModel_Android() {
        // Act: Find the device by the model
        Device foundDevice = deviceRepository.findByModel("ANDROID");

        // Assert: Verify that the found device is not null and matches the expected model
        assertNotNull(foundDevice, "Device should be found by model");
        assertEquals("ANDROID", foundDevice.getDeviceModel(), "The device model should match");
    }

    /**
     * Negative test case: Test finding a device by a non-existent model.
     */
    @Test
    void testFindByModel_NonExistentModel() {
        // Act: Attempt to find a device by a non-existent model
        Device foundDevice = deviceRepository.findByModel("NON_EXISTENT_MODEL");

        // Assert: Verify that no device is found
        assertNull(foundDevice, "Device should not be found for a non-existent model");
    }

    /**
     * Negative test case: Test finding a device by a null model.
     */
    @Test
    void testFindByModel_NullModel() {
        // Act: Attempt to find a device by a null model
        Device foundDevice = deviceRepository.findByModel(null);

        // Assert: Verify that no device is found
        assertNull(foundDevice, "Device should not be found for a null model");
    }

    /**
     * Negative test case: Test finding a device by an empty string model.
     */
    @Test
    void testFindByModel_EmptyModel() {
        // Act: Attempt to find a device by an empty model
        Device foundDevice = deviceRepository.findByModel("");

        // Assert: Verify that no device is found
        assertNull(foundDevice, "Device should not be found for an empty model string");
    }
}
