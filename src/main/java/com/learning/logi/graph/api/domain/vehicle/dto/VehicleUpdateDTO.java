package com.learning.logi.graph.api.domain.vehicle.dto;

import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import com.learning.logi.graph.api.domain.vehicle.enums.VehicleType;

public record VehicleUpdateDTO(
        String vehicleLicensePlate,
        VehicleType vehicleType,
        double maxCapacityKg
) {
    public static VehicleUpdateDTO from(
            final String vehicleLicensePlate,
            final VehicleType vehicleType,
            final double maxCapacityKg
    ) {
        return new VehicleUpdateDTO(vehicleLicensePlate, vehicleType, maxCapacityKg);
    }

    public static VehicleUpdateDTO of(final Vehicle vehicle) {
        return VehicleUpdateDTO.from(
                vehicle.getVehicleLicensePlate(),
                vehicle.getVehicleType(),
                vehicle.getMaxCapacityKg()
        );
    }
}
