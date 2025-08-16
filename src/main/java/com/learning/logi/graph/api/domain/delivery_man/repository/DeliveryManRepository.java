package com.learning.logi.graph.api.domain.delivery_man.repository;

import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.delivery_man.enums.DeliveryManStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {

    boolean existsByVehicleIdAndDeliveryManStatus(final Long vehicleId, final DeliveryManStatus status);

    boolean existsByEmail(final String email);

    Page<DeliveryMan> findByDeliveryManStatus(final DeliveryManStatus status, final Pageable pageable);


    @Query(
            value = "SELECT * FROM tb_delivery_man dm " +
                    "WHERE dm.delivery_man_status = 'AVAILABLE' " +
                    "AND ST_DWithin(dm.current_location, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography, :radius)",
            nativeQuery = true
    )
    List<DeliveryMan> findAvailableDriversNearPoint(
            @Param("lat") final double latitude,
            @Param("lon") final double longitude,
            @Param("radius") final double radiusInMeters
    );
}
