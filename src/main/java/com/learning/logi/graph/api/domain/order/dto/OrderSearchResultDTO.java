package com.learning.logi.graph.api.domain.order.dto;

import com.learning.logi.graph.api.domain.order.document.OrderDocument;

import java.time.Instant;

public record OrderSearchResultDTO(
        Long id,
        String description,
        String status,
        Long deliveryManId,
        Instant createdOn
) {
    public static OrderSearchResultDTO from(final OrderDocument orderDocument) {
        return new OrderSearchResultDTO(
                orderDocument.getId(),
                orderDocument.getDescription(),
                orderDocument.getStatus(),
                orderDocument.getDeliveryManId(),
                orderDocument.getCreatedOn()
        );
    }
}
