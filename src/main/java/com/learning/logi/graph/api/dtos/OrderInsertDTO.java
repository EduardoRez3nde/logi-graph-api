package com.learning.logi.graph.api.dtos;

import com.learning.logi.graph.api.entities.Order;
import com.learning.logi.graph.api.entities.enums.OrderStatus;

public record OrderInsertDTO(
        String description,
        Double collectLon,
        Double collectLat,
        Double deliveryLon,
        Double deliveryLat
) {
    public static OrderInsertDTO from(
            final String description,
            final Double collectLon,
            final Double collectLat,
            final Double deliveryLon,
            final Double deliveryLat
    ) {
        return new OrderInsertDTO(description, collectLon, collectLat, deliveryLon, deliveryLat);
    }
}
