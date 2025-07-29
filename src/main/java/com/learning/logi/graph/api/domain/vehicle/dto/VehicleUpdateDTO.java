package com.learning.logi.graph.api.domain.vehicle.dto;

import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;

public record VehicleUpdateDTO(
        String vehicleLicensePlate,
        String vehicleType,
        double maxCapacityKg
) {
    public static VehicleUpdateDTO from(
            final String vehicleLicensePlate,
            final String vehicleType,
            final double maxCapacityKg
    ) {
        return new VehicleUpdateDTO(vehicleLicensePlate, vehicleType, maxCapacityKg);
    }

    public static VehicleUpdateDTO of(final Vehicle vehicle) {
        return VehicleUpdateDTO.from(
                vehicle.getVehicleLicensePlate(),
                vehicle.getVehicleType().name(),
                vehicle.getMaxCapacityKg()
        );
    }
}
