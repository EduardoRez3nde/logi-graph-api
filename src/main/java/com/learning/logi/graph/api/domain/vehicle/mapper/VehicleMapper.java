package com.learning.logi.graph.api.domain.vehicle.mapper;

import com.learning.logi.graph.api.domain.vehicle.dto.VehicleInsertDTO;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleUpdateDTO;
import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;

public class VehicleMapper {

    // Criar uma classe onde os dtos herdam dessa classe e criar um metodo generico

    public static Vehicle toEntity(final VehicleInsertDTO dto) {
        return Vehicle.from(
                dto.vehicleLicensePlate(),
                dto.vehicleType(),
                dto.maxCapacityKg()
        );
    }

    public static void toEntity(final Vehicle vehicle, final VehicleUpdateDTO dto) {
        vehicle.setVehicleType(dto.vehicleType());
        vehicle.setVehicleLicensePlate(dto.vehicleLicensePlate());
        vehicle.setMaxCapacityKg(dto.maxCapacityKg());
    }
}
