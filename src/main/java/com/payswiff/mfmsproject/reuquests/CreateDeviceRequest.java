package com.payswiff.mfmsproject.reuquests;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import com.payswiff.mfmsproject.models.Device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a request to create a new Device.
 * This class holds the necessary information to create a Device entity.
 * <p>It contains fields for device model and manufacturer, which are used to create a new device.</p>
 * <p>Additionally, it includes a method to convert the request into a Device entity, 
 * with a randomly generated UUID for the device.</p>
 * 
 * @author Chatla Sarika
 * @version MFMS_0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class CreateDeviceRequest {

    /**
     * The model of the device.
     */
    private String deviceModel;

    /**
     * The manufacturer of the device.
     */
    private String deviceManufacturer;

    /**
     * Converts this CreateDeviceRequest to a Device entity.
     *
     * @return A Device entity populated with the data from this request,
     *         including a randomly generated UUID for the device.
     */
    public Device toDevice() {
        ModelMapper modelMapper = new ModelMapper();
        Device device = modelMapper.map(this, Device.class);
        device.setDeviceUuid(UUID.randomUUID().toString()); // Set UUID separately
        return device;
    }

    /**
     * Constructs a new CreateDeviceRequest with the specified device model
     * and manufacturer.
     *
     * @param deviceModel The model of the device.
     * @param deviceManufacturer The manufacturer of the device.
     */
    public CreateDeviceRequest(String deviceModel, String deviceManufacturer) {
        this.deviceModel = deviceModel;
        this.deviceManufacturer = deviceManufacturer;
    }
}
