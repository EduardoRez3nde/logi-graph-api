package com.learning.logi.graph.api.domain.delivery_man.repository;

import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.delivery_man.enums.DeliveryManStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {

    boolean existsByVehicleIdAndDeliveryManStatus(Long vehicleId, DeliveryManStatus status);

    boolean existsByEmail(final String email);

    Page<DeliveryMan> findByDeliveryManStatus(DeliveryManStatus status, Pageable pageable);
}
