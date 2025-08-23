package com.learning.logi.graph.api.domain.vehicle.entities;


import com.learning.logi.graph.api.domain.vehicle.enums.VehicleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VehicleEntityTests {

    @Test
    @DisplayName("Deve criar um veículo corretamente com dados válidos")
    void shouldCreateVehicleCorrectlyWithValidData() {

        final String vehicleLicensePlate = "XYZ-1234";
        final VehicleType vehicleType = VehicleType.MOTORCYCLE;
        final Double maxCapacityKg = 20.0;

        final Vehicle vehicle = Vehicle.from(vehicleLicensePlate, vehicleType, maxCapacityKg);

        Assertions.assertNotNull(vehicle);
        Assertions.assertEquals(vehicleLicensePlate, vehicle.getVehicleLicensePlate());
        Assertions.assertEquals(vehicleType, vehicle.getVehicleType());
        Assertions.assertEquals(maxCapacityKg, vehicle.getMaxCapacityKg());
    }

    @Test
    @DisplayName("Deve criar uma cópia de um veículo corretamente")
    void shouldCreateVehicleCopyCorrectly() {

        final String vehicleLicensePlate = "XYZ-1234";
        final VehicleType vehicleType = VehicleType.MOTORCYCLE;
        final Double maxCapacityKg = 20.0;

        final Vehicle vehicle = Vehicle.from(vehicleLicensePlate, vehicleType, maxCapacityKg);
        final Vehicle vehicleTest = Vehicle.of(vehicle);

        Assertions.assertNotNull(vehicleTest);
        Assertions.assertEquals(vehicle.getVehicleLicensePlate(), vehicleTest.getVehicleLicensePlate());
        Assertions.assertEquals(vehicle.getVehicleType(), vehicleTest.getVehicleType());
        Assertions.assertEquals(vehicle.getMaxCapacityKg(), vehicleTest.getMaxCapacityKg());
        Assertions.assertNotSame(vehicle, vehicleTest);
    }
}