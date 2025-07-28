package com.learning.logi.graph.api.dtos;

import java.time.Instant;

public record OrderResponseDTO(
        Long id,
        String description,
        String status,
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
            final String status,
            final Double collectLon,
            final Double collectLat,
            final Double deliveryLon,
            final Double deliveryLat,
            final Instant createdAt,
            final Long deliveryManId
    ) {
        return new OrderResponseDTO(id, description, status, collectLon, collectLat, deliveryLon, deliveryLat, createdAt, deliveryManId);
    }
}
