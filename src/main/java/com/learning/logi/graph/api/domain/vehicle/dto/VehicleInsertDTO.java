package com.learning.logi.graph.api.domain.vehicle.dto;

import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import com.learning.logi.graph.api.domain.vehicle.enums.VehicleType;

public record VehicleInsertDTO(
        String vehicleLicensePlate,
        VehicleType vehicleType,
        double maxCapacityKg
) {
    public static VehicleInsertDTO from(
            final String vehicleLicensePlate,
            final VehicleType vehicleType,
            final double maxCapacityKg
    ) {
        return new VehicleInsertDTO(vehicleLicensePlate, vehicleType, maxCapacityKg);
    }

    public static VehicleInsertDTO of(final Vehicle vehicle) {
        return VehicleInsertDTO.from(
                vehicle.getVehicleLicensePlate(),
                vehicle.getVehicleType(),
                vehicle.getMaxCapacityKg()
        );
    }
}