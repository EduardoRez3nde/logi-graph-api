package com.learning.logi.graph.api.domain.vehicle.dto;

import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import com.learning.logi.graph.api.domain.vehicle.enums.VehicleType;

public record VehicleResponseDTO(
        long id,
        String vehicleLicensePlate,
        VehicleType vehicleType,
        double maxCapacityKg
) {
    public static VehicleResponseDTO from(
            long id,
            final String vehicleLicensePlate,
            final VehicleType vehicleType,
            final double maxCapacityKg
    ) {
        return new VehicleResponseDTO(id, vehicleLicensePlate, vehicleType, maxCapacityKg);
    }

    public static VehicleResponseDTO of(final Vehicle vehicle) {
        return VehicleResponseDTO.from(
                vehicle.getId(),
                vehicle.getVehicleLicensePlate(),
                vehicle.getVehicleType(),
                vehicle.getMaxCapacityKg()
        );
    }
}
