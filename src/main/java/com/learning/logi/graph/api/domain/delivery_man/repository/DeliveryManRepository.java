package com.learning.logi.graph.api.domain.delivery_man.repository;

import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {

    boolean existsByVehicleAndActiveTrue(final Vehicle vehicle);

}
