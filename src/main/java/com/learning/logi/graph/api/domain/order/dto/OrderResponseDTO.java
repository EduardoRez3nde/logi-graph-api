package com.learning.logi.graph.api.domain.order.dto;

import com.learning.logi.graph.api.domain.order.entities.Order;
import com.learning.logi.graph.api.domain.order.enums.OrderStatus;

import java.time.Instant;

public record OrderResponseDTO(
        Long id,
        String description,
        OrderStatus status,
        Double collectLon,
        Double collectLat,
        Double deliveryLon,
        Double deliveryLat,
        Instant createdAt,
        Long deliveryManId
) {
    public static OrderResponseDTO from(
            final Long id,
            final String description,
            final OrderStatus status,
            final Double collectLon,
            final Double collectLat,
            final Double deliveryLon,
            final Double deliveryLat,
            final Instant createdAt,
            final Long deliveryManId
    ) {
        return new OrderResponseDTO(
                id,
                description,
                status,
                collectLon,
                collectLat,
                deliveryLon,
                deliveryLat,
                createdAt,
                deliveryManId
        );
    }

    public static OrderResponseDTO of(final Order order) {
        return from(
                order.getId(),
                order.getDescription(),
                order.getOrderStatus(),
                order.getCollectionPoint().getX(),
                order.getCollectionPoint().getY(),
                order.getDeliveredPoint().getX(),
                order.getDeliveredPoint().getY(),
                order.getCreatedOn(),
                order.getDeliveryMan() != null ? order.getDeliveryMan().getId() : null
        );
    }
}
