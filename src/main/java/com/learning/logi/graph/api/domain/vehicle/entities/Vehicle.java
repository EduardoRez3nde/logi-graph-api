package com.learning.logi.graph.api.domain.vehicle.entities;

import com.learning.logi.graph.api.domain.vehicle.enums.VehicleType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "tb_vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehicleLicensePlate;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(nullable = false)
    private Double maxCapacityKg;

    public Vehicle() { }

    private Vehicle(
            final String vehicleLicensePlate,
            final VehicleType vehicleType,
            final Double maxCapacityKg
    ) {
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.vehicleType = vehicleType;
        this.maxCapacityKg = maxCapacityKg;
    }

    public static Vehicle from(
            final String vehicleLicensePlate,
            final VehicleType vehicleType,
            final Double maxCapacityKg
    ) {
        return new Vehicle(vehicleLicensePlate, vehicleType, maxCapacityKg);
    }

    public static Vehicle of(final Vehicle vehicle) {
        return Vehicle.from(vehicle.getVehicleLicensePlate(), vehicle.getVehicleType(), vehicle.getMaxCapacityKg());
    }
}
