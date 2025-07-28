package com.learning.logi.graph.api.repositories;

import com.learning.logi.graph.api.entities.DeliveryMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {
}
