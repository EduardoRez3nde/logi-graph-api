package com.learning.logi.graph.api.domain.order.repository;

import com.learning.logi.graph.api.domain.order.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM tb_order WHERE order_status = :status",
        countQuery = "SELECT (*) FROM tb_order WHERE order_status = :status"
    )
    Page<Order> findByStatus(@Param("status") final String status, final Pageable pageable);
}
