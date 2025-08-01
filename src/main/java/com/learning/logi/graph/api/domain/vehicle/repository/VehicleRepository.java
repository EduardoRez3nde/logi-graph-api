package com.learning.logi.graph.api.domain.vehicle.repository;

import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByPlateVehicle(String plateVehicle);
}
