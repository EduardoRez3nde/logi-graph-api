package com.learning.logi.graph.api.domain.order.dto;

import com.learning.logi.graph.api.configuration.kafka.KafkaEvent;
import com.learning.logi.graph.api.configuration.kafka.KafkaTopic;
import com.learning.logi.graph.api.domain.order.entities.Order;
import com.learning.logi.graph.api.domain.order.enums.OrderStatus;

import java.time.Instant;

public record OrderUpdateEvent(
        Long id,
        String description,
        OrderStatus status,
        Double collectLon,
        Double collectLat,
        Double deliveryLon,
        Double deliveryLat,
        Instant createdAt,
        Long deliveryManId
) implements KafkaEvent {
    public static OrderUpdateEvent from(
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
        return new OrderUpdateEvent(
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

    public static OrderUpdateEvent of(final Order order) {
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

    @Override
    public KafkaTopic getTopic() {
        return KafkaTopic.ORDER_EVENTS;
    }

    @Override
    public String getKey() {
        return String.valueOf(this.id);
    }
}
