package com.learning.logi.graph.api.domain.vehicle.repository;

import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
